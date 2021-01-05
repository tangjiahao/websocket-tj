package com.chat.tj.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.chat.tj.common.config.ExportConfig;
import com.chat.tj.common.excel.ExcelStyleStrategy;
import com.chat.tj.common.util.ExcelUtil;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseVo<Integer> makeFriend(Integer userId, Integer friendId) {
        return userService.makeFriend(userId, friendId);
    }

    @PostMapping("/deleteFriend")
    @ApiOperation(value = "删除好友")
    public ResponseVo<Integer> deleteFriend(Integer userId, Integer friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @PostMapping("/export")
    @ApiIgnore
    @ApiOperation("不用模板导出")
    public void exportData(String userName, HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<UserResVO> list = userService.findUserList(userName);
        String fileName = "聊天导出数据测试" + System.currentTimeMillis();
        // 设置导出excel响应头样式
        ExcelUtil.setExcelResponse(request, response, fileName);
        // 写法1,只有一个sheet页面导出
        // EasyExcel.write(response.getOutputStream(), UserResVO.class).sheet("测试").doWrite(list);

        // 写法2，多sheet页面导出
        ExcelWriter writer = null;
        // 主要设置excel导出使用默认样式
        writer = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new ExcelStyleStrategy()).build();
        WriteSheet sheet1 = EasyExcel.writerSheet(0, "导出数据1").head(UserResVO.class).build();
        writer.write(list, sheet1);
        WriteSheet sheet2 = EasyExcel.writerSheet(1, "导出数据2").head(UserResVO.class).build();
        writer.write(list, sheet2);
        writer.finish();
    }

    @PostMapping("/exportWithTemplate")
    @ApiIgnore
    @ApiOperation("使用模板填充导出(带样式)")
    public void exportData2(String userName, HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<UserResVO> list = userService.findUserList(userName);

        String fileName = "chatUserExport";
        // 设置导出excel表头样式
        ExcelUtil.setExcelResponse(request, response, fileName);
        // 获取模板
        String template = ExportConfig.TEMPLATE_PATH + ExportConfig.TEMPLATE_NAME;
        InputStream templateInputStream = this.getClass().getResourceAsStream(template);
        // 写法1,只有一个sheet页面导出
        // EasyExcel.write(response.getOutputStream(), UserResVO.class).withTemplate(templateInputStream).sheet().doFill(list);

        // 写法2，多sheet页面导出
        ExcelWriter writer = null;
        writer = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
        WriteSheet sheet1 = EasyExcel.writerSheet(0, "导出数据1").build();
        Map<String, Object> map = new HashMap<>();
        map.put("date", ExcelUtil.getNowTime());
        // 填充一些注释的特殊数据
        writer.fill(map, sheet1);
        writer.fill(list, sheet1);

        WriteSheet sheet2 = EasyExcel.writerSheet(1, "导出数据2").build();
        writer.fill(list, sheet2);
        writer.finish();


    }


}
