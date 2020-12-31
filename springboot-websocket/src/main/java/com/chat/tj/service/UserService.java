package com.chat.tj.service;

import com.chat.tj.model.entity.RoomEntity;
import com.chat.tj.model.entity.UserEntity;
import com.chat.tj.model.vo.ResponseVo;
import com.chat.tj.model.vo.req.RoomReqVO;
import com.chat.tj.model.vo.res.RoomMemberResVO;
import com.chat.tj.model.vo.res.UserResVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/20 10:50
 * @Version 1.0
 */
public interface UserService {

    List<RoomEntity> findRoomList(Integer userId);

    List<UserResVO> findFriendList(Integer userId);

    List<RoomMemberResVO> getRoomMemberList(Integer roomId);

    ResponseVo<UserResVO> login(UserEntity userEntity);

    ResponseVo<Integer> register(UserEntity userEntity);

    List<RoomEntity> getRoomList(String roomName);

    Integer getUserId(String userName);

    ResponseVo<RoomEntity> createRoom(RoomReqVO reqVO);

    ResponseVo<Integer> joinRoom(RoomReqVO reqVO);

    ResponseVo<Integer> deleteRoom(RoomReqVO reqVO);

    List<UserResVO> findUserList(String userName);

    ResponseVo<Integer> makeFriend(Integer userId,Integer friendId);

    ResponseVo<Integer> deleteFriend(Integer userId,Integer friendId);

}
