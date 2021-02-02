package com.chat.tj.common.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tangjing
 * @date 2020/12/31 17:47
 */
public class ExcelUtil {

    /**
     * 设置导出excel 的响应
     *
     * @param response response
     * @param fileName 文件名称
     */
    public static void setExcelResponse(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.reset();
        if (request.getHeader("User-Agent").toUpperCase().contains("MSIE")) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        }
        response.setContentType("application/x-excel");
        response.setHeader("Content-disposition", "attachment; fileName=\"" + fileName + ".xlsx\"");
    }

    /**
     * 深拷贝list
     *
     * @param object   源list对象
     * @param destclas 目标对象class
     * @param <T>      目标对象类型T
     * @return 目标对象list
     */
    public static <T> List<T> deepCloneList(List<?> object, Class<T> destclas) {
        if (object == null) {
            return new ArrayList<>();
        }
        String json = JSON.toJSONString(object);
        return JSON.parseArray(json, destclas);
    }

    /**
     * 获得时间
     *
     * @return 当前时间
     */
    public static String getNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

}
