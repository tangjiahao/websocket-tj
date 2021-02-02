package com.chat.tj.auth.controller;

import com.chat.tj.auth.service.AuthService;
import com.chat.tj.auth.vo.AuthReqVO;
import com.chat.tj.auth.vo.AuthResVO;
import com.chat.tj.chat.model.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author tangjing
 * @date 2021/01/04 15:31
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@Api("鉴权")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/token")
    @ApiOperation("获取token")
    public ResponseVo<AuthResVO> getToken(@Valid @RequestBody AuthReqVO reqVO) {
        return authService.getToken(reqVO.getAccessKeyId(), reqVO.getReqVO());
    }
}
