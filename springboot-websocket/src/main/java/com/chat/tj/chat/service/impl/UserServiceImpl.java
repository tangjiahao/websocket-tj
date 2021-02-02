package com.chat.tj.chat.service.impl;

import com.chat.tj.chat.dao.UserMapper;
import com.chat.tj.chat.model.SendMsg;
import com.chat.tj.chat.model.entity.RoomEntity;
import com.chat.tj.chat.model.entity.UserEntity;
import com.chat.tj.chat.model.vo.ResponseVo;
import com.chat.tj.chat.model.vo.req.MessageRecordReqVO;
import com.chat.tj.chat.model.vo.req.RoomReqVO;
import com.chat.tj.chat.model.vo.req.UpdateRoomRoleReqVO;
import com.chat.tj.chat.model.vo.req.UserReqVO;
import com.chat.tj.chat.model.vo.res.RoomMemberResVO;
import com.chat.tj.chat.model.vo.res.RoomResVO;
import com.chat.tj.chat.model.vo.res.UserResVO;
import com.chat.tj.chat.service.UserService;
import com.chat.tj.common.constant.UserConstant;
import com.chat.tj.common.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TangJing
 * @version 1.0
 * @description :用户业务层
 * @date 2020/12/20 10:46
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<RoomEntity> findRoomList(Integer userId) {
        List<RoomEntity> result = userMapper.getRoomListByUserId(userId);
        return result;
    }

    @Override
    public List<UserResVO> findFriendList(Integer userId) {
        return userMapper.getFriendList(userId);
    }

    @Override
    public List<RoomMemberResVO> getRoomMemberList(Integer roomId) {
        return userMapper.getRoomMemberList(roomId);
    }

    @Override
    public ResponseVo<UserResVO> login(UserReqVO reqVO) {
        UserResVO user = userMapper.selectUser(reqVO);
        if (user != null) {
            return ResponseVo.content(user);
        }
        return ResponseVo.failed("密码账号错误,登录失败");
    }

    @Override
    public ResponseVo<Integer> register(UserEntity userEntity) {
        Integer flag = userMapper.findUserId(userEntity.getUserName());
        if (flag == null) {
            userMapper.saveUser(userEntity);
            return ResponseVo.content(flag);
        }
        return ResponseVo.failed("用户昵称已被占用，请重新注册");
    }

    @Override
    public List<RoomResVO> getRoomList(Integer userId, String roomName) {
        List<RoomEntity> allRooms = userMapper.getRoomList(roomName);
        List<RoomEntity> joinRooms = userMapper.getRoomListByUserId(userId);
        List<Integer> ids = joinRooms.stream().map(RoomEntity::getRoomId).collect(Collectors.toList());
        List<RoomResVO> result = ExcelUtil.deepCloneList(allRooms, RoomResVO.class);
        result.stream().filter(s -> ids.contains(s.getRoomId())).forEach(s -> s.setMemeber(true));
        return result;
    }

    @Override
    public Integer getUserId(String userName) {
        return userMapper.findUserId(userName);
    }

    @Override
    public ResponseVo<RoomEntity> createRoom(RoomReqVO reqVO) {
        RoomEntity entity = userMapper.selectRoom(reqVO.getRoomName());
        if (entity != null) {
            return ResponseVo.failed("群名称已被占用，创建失败!");
        }
        List<RoomEntity> entities = userMapper.getRoomListByUserId(reqVO.getUserId());
        if (entities.size() >= UserConstant.ROOM_LIMIT) {
            return ResponseVo.failed("每个用户能创建的群组数量最多为3，创建失败");
        }
        //创建群
        userMapper.saveRoom(reqVO);
        //增加群创建者
        reqVO.setRoomId(reqVO.getRoomId());
        reqVO.setType(UserConstant.CREATER);
        userMapper.addRoomUser(reqVO);
        return ResponseVo.content(entity);
    }

    @Override
    public ResponseVo<Integer> joinRoom(RoomReqVO reqVO) {
        RoomEntity entity = userMapper.selectRoom(reqVO.getRoomName());
        if (entity == null) {
            return ResponseVo.failed("加入的群聊不存在,请检查群聊名称");
        }
        List<RoomMemberResVO> members = userMapper.getRoomMemberList(entity.getRoomId());
        int flag = (int) members.stream().filter(s -> s.getUserId().equals(reqVO.getUserId())).count();
        if (flag > UserConstant.NO) {
            return ResponseVo.failed("用户已加入该群聊，无需重复加入");
        }
        reqVO.setRoomId(entity.getRoomId());
        reqVO.setType(UserConstant.GENERAL);
        userMapper.addRoomUser(reqVO);
        return ResponseVo.content(UserConstant.YES);
    }

    @Override
    public ResponseVo<Integer> deleteRoom(RoomReqVO reqVO) {
        RoomEntity entity = userMapper.selectRoom(reqVO.getRoomName());
        if (entity == null) {
            return ResponseVo.failed("删除的群聊不存在,请检查群聊名称");
        }
        reqVO.setRoomId(entity.getRoomId());
        //用户加入的群聊为空
        int flag = userMapper.isRoomCreater(reqVO);
        if (flag == 0) {
            return ResponseVo.failed("用户不是群主，无权删除群聊！");
        }
        //删除群组里的用户
        userMapper.deleteRoomUser(entity.getRoomId(), null);
        //删除群组
        userMapper.deleteRoom(entity.getRoomId());
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<String> deleteRoomMember(Integer roomId, Integer userId) {
        //删除群组里的用户
        userMapper.deleteRoomUser(roomId, userId);
        return ResponseVo.success();
    }

    @Override
    public List<UserResVO> findUserList(Integer userId, String serchName) {
        if (userId == null) {
            return null;
        }
        List<UserResVO> friendList = userMapper.getFriendList(userId);
        // 获得好友id列表
        List<Integer> ids = friendList.stream().map(UserResVO::getUserId).collect(Collectors.toList());
        List<UserResVO> allUser = userMapper.getUserList(serchName);
        // 标记好友和自己
        allUser.stream().filter(s -> ids.contains(s.getUserId()) || s.getUserId().equals(userId)).forEach(s -> s.setFriend(true));
        return allUser;
    }

    @Override
    public ResponseVo<Integer> makeFriend(Integer userId, Integer friendId) {
        List<UserResVO> friends = userMapper.getFriendList(userId);
        if (!CollectionUtils.isEmpty(friends)) {
            UserResVO resVO = friends.stream().filter(s -> s.getUserId().equals(friendId)).findAny().orElse(null);
            if (resVO != null) {
                return ResponseVo.failed("您想加为好友关系的用户已是好友，无需重复添加");
            }
        }
        userMapper.makeFriend(userId, friendId);
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<Integer> deleteFriend(Integer userId, Integer friendId) {
        List<UserResVO> friends = userMapper.getFriendList(userId);
        // 没有好友无需删除
        if (CollectionUtils.isEmpty(friends)) {
            return ResponseVo.success();
        }
        userMapper.deleteFriend(userId, friendId);
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<Integer> saveRecord(SendMsg sendMsg) {
        userMapper.saveChatRecord(sendMsg);
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<List<SendMsg>> getRecordList(MessageRecordReqVO reqVO) {
        return ResponseVo.content(userMapper.getRecordList(reqVO));
    }

    @Override
    public ResponseVo<Integer> deleteRecord() {
        userMapper.deleteRecord();
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<Integer> deleteRecordLogic(SendMsg sendMsg) {
        userMapper.deleteRecordLogic(sendMsg);
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<String> setRoomRole(UpdateRoomRoleReqVO roomRoleReqVO) {
        userMapper.updateRoomRole(roomRoleReqVO);
        return ResponseVo.success();
    }
}
