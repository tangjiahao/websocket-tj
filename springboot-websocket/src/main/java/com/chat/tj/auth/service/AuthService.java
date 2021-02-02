package com.chat.tj.auth.service;

import com.chat.tj.auth.vo.AuthResVO;
import com.chat.tj.chat.model.vo.ResponseVo;
import com.chat.tj.chat.model.vo.req.UserReqVO;

public interface AuthService {

    /**
     * 获取token
     *
     * @param accessKeyId AK
     * @param reqVO       用户请求
     * @return token
     */
    ResponseVo<AuthResVO> getToken(String accessKeyId, UserReqVO reqVO);
}
