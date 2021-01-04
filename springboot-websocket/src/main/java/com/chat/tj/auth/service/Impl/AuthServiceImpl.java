package com.chat.tj.auth.service.Impl;

import com.chat.tj.auth.constant.SecretKey;
import com.chat.tj.auth.service.AuthService;
import com.chat.tj.auth.vo.AuthResVO;
import com.chat.tj.common.util.MD5Util;
import com.chat.tj.common.util.TokenCache;
import com.chat.tj.model.vo.ResponseVo;
import com.chat.tj.model.vo.req.UserReqVO;
import com.chat.tj.model.vo.res.UserResVO;
import com.chat.tj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangjing
 * @date 2021/01/04 14:53
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseVo<AuthResVO> getToken(String accessKeyId, UserReqVO reqVO) {
        if (!accessKeyId.equals(SecretKey.SECRET_KEY_AK)) {
            return ResponseVo.failed("访问秘钥错误");
        }
        UserResVO resVO = userService.login(reqVO).getContent();
        if (resVO == null) {
            return ResponseVo.failed("账号密码错误，鉴权失败");
        }
        AuthResVO authResVO = new AuthResVO();
        authResVO.setUserResVO(resVO);
        //加密得到token
        String encodeString = accessKeyId + reqVO.getUserName() + reqVO.getPwd();
        String token = MD5Util.encode(encodeString);
        authResVO.setToken(token);
        TokenCache.setKey(token, reqVO.getUserName());
        return ResponseVo.content(authResVO);

    }
}
