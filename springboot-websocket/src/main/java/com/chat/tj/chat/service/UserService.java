package com.chat.tj.chat.service;

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

    List<RoomResVO> getRoomList(Integer userId, String roomName);

    Integer getUserId(String userName);

    ResponseVo<RoomEntity> createRoom(RoomReqVO reqVO);

    ResponseVo<Integer> joinRoom(RoomReqVO reqVO);

    ResponseVo<Integer> deleteRoom(RoomReqVO reqVO);

    ResponseVo<String> deleteRoomMember(Integer roomId, Integer userId);

    List<UserResVO> findUserList(Integer userId, String serchName);

    ResponseVo<Integer> makeFriend(Integer userId, Integer friendId);

    ResponseVo<Integer> deleteFriend(Integer userId, Integer friendId);

    ResponseVo<Integer> saveRecord(SendMsg sendMsg);

    ResponseVo<List<SendMsg>> getRecordList(MessageRecordReqVO reqVO);

    ResponseVo<Integer> deleteRecord();

    ResponseVo<Integer> deleteRecordLogic(SendMsg sendMsg);

    ResponseVo<String> setRoomRole(UpdateRoomRoleReqVO roomRoleReqVO);

}
