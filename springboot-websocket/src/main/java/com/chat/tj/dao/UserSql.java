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
        // 群组名不为空
        if(!StringUtils.isEmpty(roomName)){
            sql.append("And room_name like '%").append(roomName).append("%'");
        }
        return sql.toString();
    }

    public String getUserList(String userName){
        StringBuilder sql = new StringBuilder();
        sql.append("select a.user_id,a.user_name from websocket.user a where 1=1 ");
        // 群组名不为空
        if(!StringUtils.isEmpty(userName)){
            sql.append("And user_name like '%").append(userName).append("%'");
        }
        return sql.toString();
    }
}
