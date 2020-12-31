package com.chat.tj.controller;

import com.alibaba.excel.EasyExcel;
import com.chat.tj.common.config.ExportConfig;
import com.chat.tj.model.entity.RoomEntity;
import com.chat.tj.model.entity.UserEntity;
import com.chat.tj.model.vo.ResponseVo;
import com.chat.tj.model.vo.req.RoomReqVO;
import com.chat.tj.model.vo.req.UserReqVO;
import com.chat.tj.model.vo.res.RoomMemberResVO;
import com.chat.tj.model.vo.res.UserResVO;
import com.chat.tj.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.List;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/20 10:46
 * @Version 1.0
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
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(reqVO,userEntity);
        return userService.login(userEntity);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册")
    public ResponseVo<Integer> register(@Valid @RequestBody UserReqVO reqVO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(reqVO,userEntity);
        return userService.register(userEntity);
    }

    @GetMapping("/findRoomList")
    @ApiOperation(value = "查找群组列表")
    public ResponseVo<List<RoomEntity>> findRoomList(String roomName) {
        return ResponseVo.content(userService.getRoomList(roomName));
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

    @PostMapping("/deleteRoom")
    @ApiOperation(value = "删除群聊")
    public ResponseVo<Integer> deleteRoom(@Valid @RequestBody RoomReqVO reqVO) {
        return userService.deleteRoom(reqVO);
    }

    @GetMapping("/findUserList")
    @ApiOperation(value = "查找用户列表")
    public ResponseVo<List<UserResVO>> findUserList(String userName) {
        return ResponseVo.content(userService.findUserList(userName));
    }

    @PostMapping("/makeFriend")
    @ApiOperation(value = "添加好友")
    public ResponseVo<Integer> makeFriend(Integer userId,Integer friendId) {
        return userService.makeFriend(userId,friendId);
    }

    @PostMapping("/deleteFriend")
    @ApiOperation(value = "删除好友")
    public ResponseVo<Integer> deleteFriend(Integer userId,Integer friendId) {
        return userService.deleteFriend(userId,friendId);
    }

    @PostMapping("/export")
    @ApiOperation("不用模板导出")
    public void exportData(String userName, HttpServletResponse response, HttpServletRequest request){
        List<UserResVO> list = userService.findUserList(userName);
        String filename = "聊天导出数据测试"+System.currentTimeMillis()+".xlsx";
        // 写法1
        EasyExcel.write(filename,UserResVO.class).sheet("测试").doWrite(list);
    }

    @PostMapping("/exportWithTemplate ")
    @ApiOperation("使用模板填充导出（带样式）")
    public void exportData2(String userName, HttpServletResponse response, HttpServletRequest request){
        List<UserResVO> list = userService.findUserList(userName);
        String template = ExportConfig.TEMPLATE_PATH+ File.separator+ExportConfig.TEMPLATE_NAME;
        String p1 = this.getClass().getResource("/").getPath()+template;
        System.out.println(p1);
        String filename = "聊天导出数据测试"+System.currentTimeMillis()+".xlsx";
        // 写法1
        EasyExcel.write(filename,UserResVO.class).withTemplate(p1).sheet().doFill(list);

    }


}
