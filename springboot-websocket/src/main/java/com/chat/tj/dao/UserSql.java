package com.chat.tj.dao;

import org.springframework.util.StringUtils;

/**
 * @author tangjing
 * @date 2020/12/29 14:10
 */
public class UserSql {
    public String getRoomList(String roomName){
        StringBuilder sql = new StringBuilder();
        sql.append("select a.room_id,a.room_name from websocket.room a where 1=1 ");
        if(!StringUtils.isEmpty(roomName)){
            sql.append("And room_name like '%").append(roomName).append("%'");
        }
        return sql.toString();
    }
}
