package com.chat.tj.common.constant;

import javax.servlet.http.HttpServletRequest;


/**
 * @author wujing
 * @date: 2019/02/20 16:19
 */
public class CommonCodes {

    /**
     * 获取
     *
     * @param request
     * @return
     */
    public static String getUserId(HttpServletRequest request) {
        return request.getHeader("userId");
    }

    public static String getRoleId(HttpServletRequest request) {
        String roleId = request.getHeader("roleId");
        return roleId == null ? "-1" : roleId;
    }

}
