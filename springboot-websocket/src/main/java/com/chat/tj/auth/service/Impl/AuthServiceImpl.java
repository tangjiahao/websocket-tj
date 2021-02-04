package com.chat.tj.auth.service.Impl;

import com.chat.tj.auth.constant.SecretKey;
import com.chat.tj.auth.service.AuthService;
import com.chat.tj.auth.vo.AuthResVO;
import com.chat.tj.chat.model.vo.ResponseVo;
import com.chat.tj.chat.model.vo.req.UserReqVO;
import com.chat.tj.chat.model.vo.res.UserResVO;
import com.chat.tj.chat.service.UserService;
import com.chat.tj.common.util.ExcelUtil;
import com.chat.tj.common.util.MD5Util;
import com.chat.tj.common.util.TokenCache;
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
            return ResponseVo.failed("账号密码错误，登录失败");
        }
        AuthResVO authResVO = new AuthResVO();
        authResVO.setUserResVO(resVO);
        // 使用公钥秘钥账号密码，当前时间加密得到token，加入当前时间可以保证token每次都不同，用于token过期或者token泄密后重新获取
        String encodeString = accessKeyId + reqVO.getUserName() + SecretKey.SECRET_KEY_SK + reqVO.getPwd() + ExcelUtil.getNowTime();
        String token = MD5Util.encode(encodeString);
        authResVO.setToken(token);
        // 当前用户在guava的缓存中存在token
        if (TokenCache.getKey(reqVO.getUserName()) != null) {
            String romoveToken = TokenCache.getKey(reqVO.getUserName());
            TokenCache.remove(romoveToken);
            TokenCache.remove(reqVO.getUserName());
        }
        TokenCache.setKey(reqVO.getUserName(), token);
        TokenCache.setKey(token, reqVO.getUserName());
        return ResponseVo.content(authResVO);

    }
}
