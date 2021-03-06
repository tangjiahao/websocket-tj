package com.chat.tj.auth.interceptor;


import com.chat.tj.auth.constant.AuthConstant;
import com.chat.tj.chat.model.vo.ResponseVo;
import com.chat.tj.common.util.TokenCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token拦截
 *
 * @author tangjing
 * @date 2021/01/04 17:11
 */
@Slf4j
@Component
public class UserTokenInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1.获取token
        String token = TokenCache.getToken(request);
        System.out.println(request.toString());
        // 2.校验token
        // 参数token为空
        if (StringUtils.isEmpty(token)) {
            return writeAuthResponse(response, ResponseVo.STATUS_CODE.LOGIN_WITHOUT);
        }
        String userId = TokenCache.getKey(token);
        // 3.token 错误
        if (StringUtils.isEmpty(userId)) {
            return writeAuthResponse(response, ResponseVo.STATUS_CODE.LOGIN_TOKEN_ERROR);
        }
        String roldId = TokenCache.getKey(userId + AuthConstant.ROLE_ID);
        MDC.put(AuthConstant.USER_ID, userId);
        MDC.put(AuthConstant.ROLE_ID, roldId);
        return true;
    }

    /**
     * 返回权限验证结果
     *
     * @param response   res
     * @param authStatus 权限状态
     */
    private boolean writeAuthResponse(HttpServletResponse response, ResponseVo.STATUS_CODE authStatus) {
        ResponseVo<String> responseVo = new ResponseVo<>();
        responseVo.error(authStatus);
        return writeResponse(response, responseVo);
    }

    /**
     * 返回权限验证结果
     *
     * @param response   res
     * @param responseVo 返回结果
     */
    private boolean writeResponse(HttpServletResponse response, ResponseVo<String> responseVo) {
        response.reset();
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().print(responseVo.toString());
        } catch (IOException e) {
            log.error("结果返回异常！！！");
        }
        return false;
    }


}
