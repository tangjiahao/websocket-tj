package com.chat.tj.service.impl;

import com.chat.tj.common.constant.UserConstant;
import com.chat.tj.controller.UserController;
import com.chat.tj.dao.UserMapper;
import com.chat.tj.model.entity.RoomEntity;
import com.chat.tj.model.entity.UserEntity;
import com.chat.tj.model.vo.ResponseVo;
import com.chat.tj.model.vo.req.RoomReqVO;
import com.chat.tj.model.vo.res.RoomMemberResVO;
import com.chat.tj.model.vo.res.UserResVO;
import com.chat.tj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.ws.Response;
import java.util.List;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/20 10:46
 * @Version 1.0
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
    public ResponseVo<UserResVO> login(UserEntity userEntity) {
        UserResVO user = userMapper.selectUser(userEntity);
        if (user != null) {
            return ResponseVo.content(user);
        }
        return ResponseVo.failed("密码账号错误，登录失败");
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
    public List<RoomEntity> getRoomList(String roomName) {
        return userMapper.getRoomList(roomName);
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
        if(entities.size()>=UserConstant.ROOM_LIMIT){
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
        List<RoomMemberResVO> members = userMapper.getRoomMemberList(Integer.parseInt(entity.getRoomId()));
        int flag=(int)members.stream().filter(s->s.getUserId().equals(reqVO.getUserId())).count();
        if(flag>UserConstant.NO){
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
        int flag=userMapper.isRoomCreater(reqVO);
        if(flag==0){
            return ResponseVo.failed("用户不是群主，无权删除群聊！");
        }
        //删除群组里的用户
        userMapper.deleteRoomUser(entity.getRoomId());
        //删除群组
        userMapper.deleteRoom(entity.getRoomId());
        return ResponseVo.success();
    }
}
