package com.chat.tj.chat.socket;

import com.alibaba.fastjson.JSON;
import com.chat.tj.chat.model.SendMsg;
import com.chat.tj.chat.model.vo.res.RoomMemberResVO;
import com.chat.tj.chat.model.vo.res.RoomResVO;
import com.chat.tj.chat.model.vo.res.UserResVO;
import com.chat.tj.chat.service.UserService;
import com.chat.tj.common.constant.UserConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * @author TangJing
 * @date 2020/12/21 10:46
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{username}/{roomId}")
public class WebSocket {

    public static final String ALL_USER = "All";
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static final Map<String, Map<String, WebSocket>> CLIENTS = new ConcurrentHashMap<>(500);

    /**
     * 在某个群组中在线人数
     */
    private static final Map<String, Integer> ROOM_ONLINE_NUM = new ConcurrentHashMap<>(500);
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
     * @param session 会话
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, @PathParam("roomId") String roomId, Session session) {
        this.username = username;
        this.session = session;
        this.roomId = roomId;
        // 初始化聊天大厅
        if (CLIENTS.get(UserConstant.INIT_ROOM_ID) == null) {
            Map<String, WebSocket> initRoom = new ConcurrentHashMap<>(500);
            CLIENTS.put(UserConstant.INIT_ROOM_ID, initRoom);
            ROOM_ONLINE_NUM.put(UserConstant.INIT_ROOM_ID, 0);
        }
        // 如果连接用户不存在聊天大厅，所有用户的连接先统一先放置到聊天大厅
        if (CLIENTS.get(UserConstant.INIT_ROOM_ID).get(username) == null) {
            CLIENTS.get(UserConstant.INIT_ROOM_ID).put(username, this);
            ROOM_ONLINE_NUM.put(UserConstant.INIT_ROOM_ID, ROOM_ONLINE_NUM.get(UserConstant.INIT_ROOM_ID) + 1);
        }
        CLIENTS.get(UserConstant.INIT_ROOM_ID).putIfAbsent(username, this);
        log.info(username + "加入聊天大厅");
        // 如果连接的是群组
        if (!StringUtils.isEmpty(roomId) && !UserConstant.INIT_ROOM_ID.equals(roomId)) {
            Integer num = ROOM_ONLINE_NUM.get(roomId);
            num = (num == null) ? 0 : num;
            Map<String, WebSocket> temp;
            List<Integer> roomIds = getrooms();
            if (!CollectionUtils.isEmpty(roomIds)) {
                //查看群组是否存在,如果群组存在
                if (roomIds.contains(Integer.parseInt(roomId))) {
                    temp = CLIENTS.get(roomId);
                    if (CLIENTS.get(roomId) == null) {
                        temp = new ConcurrentHashMap<>(500);
                    }
                    //把自己的信息加入到map当中去
                    temp.put(username, this);
                    CLIENTS.put(roomId, temp);
                    ROOM_ONLINE_NUM.put(roomId, ++num);
                    log.info("现在来连接的用户名：" + username + "想连接的群组：" + roomId);
                    log.info("群组" + roomId + "有新连接加入！ 当前在线人数" + ROOM_ONLINE_NUM.get(roomId));
                }
            }

        }


    }

    @OnError
    public void onError(Throwable error) {
        log.info("服务端发生了错误" + error.getMessage());
        //error.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {

        log.info(this.username + "申请退出连接");
        if (!StringUtils.isEmpty(this.username)) {
            String userName = this.username;
            if (!CollectionUtils.isEmpty(CLIENTS.keySet())) {
                CLIENTS.keySet().forEach(id -> {
                    for (WebSocket item : CLIENTS.get(id).values()) {
                        if (item.username.equals(userName)) {
                            int num = ROOM_ONLINE_NUM.get(id);
                            ROOM_ONLINE_NUM.put(id, --num);
                            CLIENTS.get(id).remove(userName);
                            log.info("群组号：" + id + ",用户：" + userName + "已退出连接");
                        }
                    }
                });
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
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
            SendMsg msg = JSON.parseObject(message, SendMsg.class);
            String sender = msg.getSender();
            if (msg.getSender().contains("{")) {
                sender = msg.getSender().substring(msg.getSender().indexOf("{") + 1, msg.getSender().indexOf("}"));
                msg.setSender(sender);
            }
            if (ALL_USER.equals(msg.getReceiver())) {
                dealMessageToAll(msg, sender);
            } else {
                dealMessageToUser(msg, sender);
            }
        } catch (Exception e) {
            log.info("发生了错误了");
        }
    }

    public synchronized void sendMessageTo(SendMsg message) {
        int messageType = message.getMessageType();
        // 用户没上线，保存消息
        if (!StringUtils.isEmpty(message.getReceiver()) && CLIENTS.get(UserConstant.INIT_ROOM_ID).get(message.getReceiver()) == null) {
            // 不是系统消息，保存
            //messageType 1加好友 2加群 3代表系统消息  4代表普通消息 5图片消息 6文件消息
            if (UserConstant.SYSTEM_MESSAGE != messageType) {
                userService.saveRecord(message);
                message.setMessage("发送用户未上线，已将消息保存");
                message.setMessageType(UserConstant.SYSTEM_MESSAGE);
                CLIENTS.get(UserConstant.INIT_ROOM_ID).get(message.getSender()).session.getAsyncRemote().sendText(JSON.toJSONString(message));
                return;
            }
        }
        for (WebSocket item : CLIENTS.get(UserConstant.INIT_ROOM_ID).values()) {
            if (item.username.equals(message.getReceiver())) {
                if (!item.session.isOpen()) {
                    log.info(item.username + "连接已经被关闭");
                }
                item.session.getAsyncRemote().sendText(JSON.toJSONString(message));
                userService.saveRecord(message);
                break;
            }
        }

    }

    public synchronized void sendMessageAll(SendMsg msg) {
        for (WebSocket item : CLIENTS.get(roomId).values()) {
            if (item.username.equals(msg.getSender())) {
                continue;
            }
            item.session.getAsyncRemote().sendText(JSON.toJSONString(msg));
        }
    }

    public List<Integer> getrooms() {
        List<RoomResVO> rooms = userService.getRoomList(UserConstant.YES, "");
        if (CollectionUtils.isEmpty(rooms)) {
            return null;
        }
        return rooms.stream().map(RoomResVO::getRoomId).collect(Collectors.toList());
    }

    public Integer getUserId(String username) {
        return userService.getUserId(username);
    }

    private void dealMessageToAll(SendMsg msg, String sender) {
        msg.setReceiver("所有人");
        List<RoomMemberResVO> members = userService.getRoomMemberList(Integer.parseInt(msg.getRoomId()));
        //如果群组存在
        if (!CollectionUtils.isEmpty(members)) {
            UserResVO user = members.stream().filter(s -> s.getUserName().equals(msg.getSender())).findAny().orElse(null);
            //发送人在群组
            if (user != null) {
                userService.saveRecord(msg);
                sendMessageAll(msg);
                return;
            }
            //发送人不在群组
            msg.setMessage("您未加入该群组，无法发送信息，请先加入该群组！");
            msg.setReceiver(sender);
            msg.setMessageType(UserConstant.SYSTEM_MESSAGE);
            sendMessageTo(msg);
            return;
        }
        //群组不存在
        msg.setMessage("发送的群组不存在，信息发送错误");
        msg.setReceiver(sender);
        msg.setMessageType(UserConstant.SYSTEM_MESSAGE);
        sendMessageTo(msg);
    }

    private void dealMessageToUser(SendMsg msg, String sender) {
        int type = msg.getMessageType();
        //如果是普通消息或者图片文件消息
        if (type == UserConstant.GENERAL_MESSAGE || type == UserConstant.IMG_MESSAGE || type == UserConstant.FILE_MESSAGE) {
            List<UserResVO> friendList = userService.findFriendList(getUserId(msg.getSender()));
            UserResVO friend = friendList.stream().filter(s -> msg.getReceiver().equals(s.getUserName())).findAny().orElse(null);
            //想发送的用户不是好友
            if (friend == null) {
                msg.setMessage("您和对方不是好友，请先添加好友在发送信息");
                msg.setSender("系统");
                msg.setReceiver(sender);
                //messageType 1加好友 2加群 3代表系统消息  4代表普通消息 5图片消息 6文件消息
                msg.setMessageType(UserConstant.SYSTEM_MESSAGE);
                sendMessageTo(msg);
                return;
            }
            sendMessageTo(msg);
        }
        // 如果是加好友
        if (type == UserConstant.ADD_FRIEND_MESSAGE) {
            sendMessageTo(msg);
        }
        // 如果是加群组
        if (type == UserConstant.ADD_ROOM_MESSAGE) {
            List<RoomMemberResVO> members = userService.getRoomMemberList(Integer.parseInt(msg.getRoomId()));
            if (!CollectionUtils.isEmpty(members)) {
                RoomMemberResVO creator = members.stream().filter(s -> s.getType() == UserConstant.CREATER).findAny().orElse(null);
                // 群主不为空,将信息接受者设置为群主
                if (creator != null) {
                    msg.setMessage(msg.getReceiver());
                    msg.setReceiver(creator.getUserName());
                    sendMessageTo(msg);

                }
            }
        }
    }

}
