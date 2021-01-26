package com.chat.tj.service;

import com.chat.tj.model.SendMsg;
import com.chat.tj.model.entity.RoomEntity;
import com.chat.tj.model.entity.UserEntity;
import com.chat.tj.model.vo.ResponseVo;
import com.chat.tj.model.vo.req.MessageRecordReqVO;
import com.chat.tj.model.vo.req.RoomReqVO;
import com.chat.tj.model.vo.req.UserReqVO;
import com.chat.tj.model.vo.res.RoomMemberResVO;
import com.chat.tj.model.vo.res.UserResVO;

import java.util.List;


/**
 * @author tangjing
 * @date 2021/01/25 09:48
 */
public interface UserService {

    List<RoomEntity> findRoomList(Integer userId);

    List<UserResVO> findFriendList(Integer userId);

    List<RoomMemberResVO> getRoomMemberList(Integer roomId);

    ResponseVo<UserResVO> login(UserReqVO reqVO);

    ResponseVo<Integer> register(UserEntity userEntity);

    List<RoomEntity> getRoomList(String roomName);

    Integer getUserId(String userName);

    ResponseVo<RoomEntity> createRoom(RoomReqVO reqVO);

    ResponseVo<Integer> joinRoom(RoomReqVO reqVO);

    ResponseVo<Integer> deleteRoom(RoomReqVO reqVO);

    List<UserResVO> findUserList(Integer userId, String serchName);

    ResponseVo<Integer> makeFriend(Integer userId, Integer friendId);

    ResponseVo<Integer> deleteFriend(Integer userId, Integer friendId);

    ResponseVo<Integer> saveRecord(SendMsg sendMsg);

    ResponseVo<List<SendMsg>> getRecordList(MessageRecordReqVO reqVO);

    ResponseVo<Integer> deleteRecord();

}
