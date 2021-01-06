package com.chat.tj.socket;

import com.alibaba.fastjson.JSON;
import com.chat.tj.common.constant.UserConstant;
import com.chat.tj.model.SendMsg;
import com.chat.tj.model.entity.RoomEntity;
import com.chat.tj.model.vo.res.RoomMemberResVO;
import com.chat.tj.model.vo.res.UserResVO;
import com.chat.tj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@ServerEndpoint("/websocket/{username}/{roomId}")
public class WebSocket {

    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static Map<String, Map<String, WebSocket>> clients = new ConcurrentHashMap<>(100);

    /**
     * 在某个群组中在线人数
     */
    private static Map<String, Integer> roomOnlineNum = new ConcurrentHashMap<>();


    /**
     * 会话
     */
    private Session session;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 聊天频道
     */
    private String roomId;

    //解决socket中无法使用@autowired注入对象
    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        WebSocket.userService = userService;
    }


    /**
     * OnOpen 表示有浏览器链接过来的时候被调用
     * OnClose 表示浏览器发出关闭请求的时候被调用
     * OnMessage 表示浏览器发消息的时候被调用
     * OnError 表示有错误发生，比如网络断开了等等
     */

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, @PathParam("roomId") String roomId, Session session) {
        this.username = username;
        this.session = session;
        this.roomId = roomId;
        if (clients.get(UserConstant.INIT_ROOM_ID) == null) {
            Map<String, WebSocket> initRoom = new ConcurrentHashMap<>(40);
            clients.put(UserConstant.INIT_ROOM_ID, initRoom);
        }
        // 将所有用户的连接都统一先放置到聊天大厅
        clients.get(UserConstant.INIT_ROOM_ID).putIfAbsent(username, this);

        if (!StringUtils.isEmpty(roomId) && !UserConstant.INIT_ROOM_ID.equals(roomId)) {
            Integer num = roomOnlineNum.get(roomId);
            if (num == null) {
                num = 0;
            }
            Map<String, WebSocket> temp = null;
            List<String> roomIds = getrooms();
            if (roomIds != null) {
                //查看群组是否存在,如果群组存在
                if (roomIds.contains(roomId)) {
                    temp = clients.get(roomId);
                    if (clients.get(roomId) == null) {
                        temp = new ConcurrentHashMap<>(40);
                    }
                    //把自己的信息加入到map当中去
                    temp.put(username, this);
                    clients.put(roomId, temp);
                    roomOnlineNum.put(roomId, ++num);
                    log.info("现在来连接的用户名：" + username + "想连接的群组：" + roomId);
                    log.info("群组" + roomId + "有新连接加入！ 当前在线人数" + roomOnlineNum.get(roomId));
                }
            }

        }


    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("服务端发生了错误" + error.getMessage());
        //error.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(@PathParam("userName") String userName) {

        log.info(userName + "申请退出连接");
        //webSockets.remove(this);

        if (!CollectionUtils.isEmpty(clients.keySet())) {
            clients.keySet().forEach(id -> {
                for (WebSocket item : clients.get(id).values()) {
                    if (item.username.equals(userName)) {
                        int num = roomOnlineNum.get(id);
                        roomOnlineNum.put(id, --num);
                        clients.get(id).remove(userName);
                        log.info("群组号：" + id + ",用户：" + userName + "已退出连接");
                    }
                }
            });
        }
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话0.0
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("来自客户端消息：" + message + "客户端的id是：" + session.getId());
            // JSONObject jsonObject = JSON.parseObject(message);
            SendMsg msg = JSON.parseObject(message, SendMsg.class);
            String sender = msg.getSender();
            if (msg.getSender().contains("{")) {
                sender = msg.getSender().substring(msg.getSender().indexOf("{") + 1, msg.getSender().indexOf("}"));
                msg.setSender(sender);
            }
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            msg.setMessageType(4);
            if ("All".equals(msg.getReceiver())) {
                msg.setReceiver("所有人");
                List<RoomMemberResVO> members = userService.getRoomMemberList(Integer.parseInt(msg.getRoomId()));
                //如果群组存在
                if (!CollectionUtils.isEmpty(members)) {
                    UserResVO user = members.stream().filter(s -> s.getUserName().equals(msg.getSender())).findAny().orElse(null);
                    //发送人在群组
                    if (user != null) {
                        sendMessageAll(msg);
                    }
                    //发送人不在群组
                    msg.setMessage("您未加入该群组，无法发送信息，请先加入该群组！");
                    msg.setReceiver(sender);
                    sendMessageTo(msg);
                    return;
                }
                //群组不存在
                msg.setMessage("发送的群组不存在，信息发送错误");
                msg.setReceiver(sender);
                sendMessageTo(msg);

            } else {
                msg.setRoomId(UserConstant.INIT_ROOM_ID);
                List<UserResVO> userResVOS = userService.findFriendList(getUserId(msg.getSender()));
                UserResVO friend = userResVOS.stream().filter(s -> msg.getReceiver().equals(s.getUserName())).findAny().orElse(null);
                //想发送的用户不是好友
                if (friend == null) {
                    msg.setMessage("您和对方不是好友，请先添加好友在发送信息");
                    msg.setSender("系统");
                    msg.setReceiver(sender);
                    sendMessageTo(msg);
                    return;
                }
                sendMessageTo(msg);
            }
        } catch (Exception e) {
            log.info("发生了错误了");
        }
    }

    public synchronized void sendMessageTo(SendMsg message) throws IOException {
        if (!StringUtils.isEmpty(message.getReceiver()) && clients.get(UserConstant.INIT_ROOM_ID).get(message.getReceiver()) == null) {
            message.setMessage("发送用户未上线，发送失败");
            clients.get(UserConstant.INIT_ROOM_ID).get(message.getSender()).session.getAsyncRemote().sendText(JSON.toJSONString(message));
            return;
        }
        for (WebSocket item : clients.get(roomId).values()) {
            if (item.username.equals(message.getReceiver())) {
                item.session.getAsyncRemote().sendText(JSON.toJSONString(message));
                System.out.println(message);
                break;
            }
        }
    }

    public synchronized void sendMessageAll(SendMsg msg) throws IOException {

        for (WebSocket item : clients.get(roomId).values()) {
            if (item.username.equals(msg.getSender())) {
                continue;
            }
            item.session.getAsyncRemote().sendText(JSON.toJSONString(msg));
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

    /**
     * 建立连接
     *
     * @param roomId   频道号 0 表示默认频道号，所有用户一开始都会加入该频道（用于私聊）,1之后的频道号代表群组号
     * @param username 用户名
     */
    public synchronized void addConnection(String roomId, String username, Session session) {
        this.roomId = roomId;
        this.username = username;
        this.session = session;
        clients.get(roomId).put(username, this);
    }

    public boolean findUserByRoomId(String username, String roomId) {
        if (clients.get(roomId) == null) {
            return false;
        }
        return clients.get(roomId).get(username) != null;
    }

    public List<String> getrooms() {
        List<RoomEntity> rooms = userService.getRoomList("");
        if (CollectionUtils.isEmpty(rooms)) {
            return null;
        }
        return rooms.stream().map(RoomEntity::getRoomId).collect(Collectors.toList());
    }

    public Integer getUserId(String username) {
        return userService.getUserId(username);
    }

}
