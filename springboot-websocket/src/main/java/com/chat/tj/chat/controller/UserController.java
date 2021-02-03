package com.chat.tj.chat.controller;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * @author tangjing
 * @date 2021/12/04 15:31
 */
@RestController
@RequestMapping("api/user")
@Api("用户相关接口")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/getUserRooms")
    @ApiOperation(value = "获得用户加入的群组列表")
    public ResponseVo<List<RoomEntity>> getUserRooms(Integer userId) {
        return ResponseVo.content(userService.findRoomList(userId));
    }

    @GetMapping("/getFriendList")
    @ApiOperation(value = "获得用户的好友列表")
    public ResponseVo<List<UserResVO>> findFriendList(Integer userId) {
        return ResponseVo.content(userService.findFriendList(userId));
    }

    @GetMapping("/getRoomMemberList")
    @ApiOperation(value = "获得群组内的成员列表")
    public ResponseVo<List<RoomMemberResVO>> getRoomMemberList(Integer roomId) {
        return ResponseVo.content(userService.getRoomMemberList(roomId));
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public ResponseVo<UserResVO> login(@RequestBody UserReqVO reqVO) {
        return userService.login(reqVO);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册")
    public ResponseVo<Integer> register(@Valid @RequestBody UserReqVO reqVO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(reqVO, userEntity);
        return userService.register(userEntity);
    }

    @GetMapping("/findRoomList")
    @ApiOperation(value = "查找群组列表")
    public ResponseVo<List<RoomResVO>> findRoomList(Integer userId, String roomName) {
        return ResponseVo.content(userService.getRoomList(userId, roomName));
    }

    @PostMapping("/joinRoom")
    @ApiOperation(value = "加入群聊")
    public ResponseVo<Integer> joinRoom(@Valid @RequestBody RoomReqVO reqVO) {
        return userService.joinRoom(reqVO);
    }

    @PostMapping("/createRoom")
    @ApiOperation(value = "创建群聊")
    public ResponseVo<RoomEntity> createRoom(@Valid @RequestBody RoomReqVO reqVO) {
        return userService.createRoom(reqVO);
    }

    @GetMapping("/deleteRoomMember")
    @ApiOperation(value = "删除群成员")
    public ResponseVo<String> deleteRoomMember(Integer roomId, Integer userId) {
        return userService.deleteRoomMember(roomId, userId);
    }

    @PostMapping("/deleteRoom")
    @ApiOperation(value = "删除群聊")
    public ResponseVo<Integer> deleteRoom(@Valid @RequestBody RoomReqVO reqVO) {
        return userService.deleteRoom(reqVO);
    }


    @GetMapping("/findUserList")
    @ApiOperation(value = "查找用户列表")
    public ResponseVo<List<UserResVO>> findUserList(Integer userId, String serchName) {
        return ResponseVo.content(userService.findUserList(userId, serchName));
    }

    @GetMapping("/findUserId")
    @ApiOperation(value = "查找用户Id")
    public ResponseVo<Integer> findUserId(String userName) {
        return ResponseVo.content(userService.getUserId(userName));
    }


    @GetMapping("/makeFriend")
    @ApiOperation(value = "添加好友")
    public ResponseVo<Integer> makeFriend(Integer userId, Integer friendId) {
        return userService.makeFriend(userId, friendId);
    }

    @GetMapping("/deleteFriend")
    @ApiOperation(value = "删除好友")
    public ResponseVo<Integer> deleteFriend(Integer userId, Integer friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @PostMapping("/saveRecord")
    // @ApiIgnore
    @ApiOperation(value = "保存消息")
    public ResponseVo<Integer> saveRecord(@RequestBody SendMsg sendMsg) {
        return userService.saveRecord(sendMsg);
    }

    @PostMapping("/getRecordList")
    @ApiOperation(value = "查询消息记录")
    public ResponseVo<List<SendMsg>> getRecordList(@Valid @RequestBody MessageRecordReqVO reqVO) {
        return userService.getRecordList(reqVO);
    }

    @PostMapping("/deleteRecord")
    @ApiIgnore
    @ApiOperation(value = "删除所有记录")
    public ResponseVo<Integer> deleteRecord() {
        return userService.deleteRecord();
    }

    @PostMapping("/deleteRecordLogic")
    @ApiOperation(value = "逻辑删除记录")
    public ResponseVo<Integer> deleteRecordLogic(@Valid @RequestBody SendMsg sendMsg) {
        return userService.deleteRecordLogic(sendMsg);
    }

    @PostMapping("/updateRoomRole")
    @ApiOperation(value = "更改群成员角色")
    public ResponseVo<String> updateRole(@Valid @RequestBody UpdateRoomRoleReqVO roomRoleReqVO) {
        return userService.setRoomRole(roomRoleReqVO);
    }


}
