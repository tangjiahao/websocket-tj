package com.chat.tj.dao;

import com.chat.tj.model.vo.req.MessageRecordReqVO;
import org.springframework.util.StringUtils;

/**
 * @author tangjing
 * @date 2020/12/29 14:10
 */
public class UserSql {

    public static final String ALL = "All";
    public static final String Hall = "-1";

    public String getRoomList(String roomName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.room_id,a.room_name from websocket.room a where 1=1 ");
        // 群组名不为空
        if (!StringUtils.isEmpty(roomName)) {
            sql.append("And room_name like '%").append(roomName).append("%'");
        }
        return sql.toString();
    }

    public String getUserList(String userName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.user_id,a.user_name from websocket.user a where 1=1 ");
        // 群组名不为空
        if (!StringUtils.isEmpty(userName)) {
            sql.append("And user_name like '%").append(userName).append("%'");
        }
        return sql.toString();
    }

    public String getRecordList(MessageRecordReqVO reqVO) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM websocket.chat_record a where 1=1 ");
        // 查看群组消息
        if (ALL.equals(reqVO.getNameTwo()) && !Hall.equals(reqVO.getRoomId())) {
            sql.append("and a.room_id=#{roomId} ");
        }
        if (Hall.equals(reqVO.getRoomId())) {
            sql.append("and (a.sender =#{nameOne} and a.receiver = #{nameTwo}) or (a.sender =#{nameTwo} and a.receiver = #{nameOne}) ");
        }
        sql.append("order by a.create_time desc ");
        if (reqVO.getRecordNum() == null) {
            sql.append("limit 10");
            return sql.toString();
        }
        return sql.toString();

    }

}
