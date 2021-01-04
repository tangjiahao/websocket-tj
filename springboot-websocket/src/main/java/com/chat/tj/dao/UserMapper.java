package com.chat.tj.dao;

import com.chat.tj.model.entity.RoomEntity;
import com.chat.tj.model.entity.UserEntity;
import com.chat.tj.model.vo.req.RoomReqVO;
import com.chat.tj.model.vo.req.UserReqVO;
import com.chat.tj.model.vo.res.RoomMemberResVO;
import com.chat.tj.model.vo.res.UserResVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/20 10:47
 * @Version 1.0
 */
@Mapper
public interface UserMapper {

    @Select("SELECT a.room_id,a.room_name FROM websocket.room a left join websocket.room_user b on a.room_id =b.room_id where b.user_id=#{userId}")
    List<RoomEntity> getRoomListByUserId(Integer userId);

    @Select("SELECT a.* FROM  websocket.user a where a.user_id in (select user_id as userId from websocket.friend_relation b " +
            "where b.friend_id = #{userId} union all select friend_id as userId from websocket.friend_relation c where c.user_id =#{userId})")
    List<UserResVO> getFriendList(Integer userId);

    @Select("SELECT b.*,a.type FROM websocket.room_user a left join websocket.user b on a.user_id = b.user_id where a.room_id =#{roomId}")
    List<RoomMemberResVO> getRoomMemberList(Integer roomId);

    @Insert("INSERT into websocket.user(user_name,pwd,create_time) values(#{userName},#{pwd},now())")
    @Options(useGeneratedKeys = true, keyProperty = "user_id", keyColumn = "report_id")
    void saveUser(UserEntity userEntity);

    @Select("SELECT * FROM websocket.user where user_name=#{userName} and pwd=#{pwd} limit 1")
    UserResVO selectUser(UserReqVO userEntity);

    @SelectProvider(type = UserSql.class, method = "getRoomList")
    List<RoomEntity> getRoomList(String roomName);

    @Select("SELECT user_id FROM websocket.user WHERE user_name =#{userName} limit 1")
    Integer findUserId(String userName);

    @Insert("INSERT into websocket.room(room_name) values(#{roomName})")
    @Options(useGeneratedKeys = true, keyProperty = "roomId", keyColumn = "room_id")
    void saveRoom(RoomReqVO reqVO);

    @Insert("INSERT into websocket.room_user(room_id,user_id,type) values(#{roomId},#{userId},#{type})")
    void addRoomUser(RoomReqVO reqVO);

    @Select("SELECT * FROM websocket.room WHERE room_name=#{roomName} limit 1")
    RoomEntity selectRoom(String roomName);

    @Delete("DELETE FROM websocket.room where room_id=#{roomId}")
    Integer deleteRoom(String roomId);

    @Delete("DELETE FROM websocket.room_user where room_id=#{roomId}")
    Integer deleteRoomUser(String roomId);

    @Select("SELECT count(1) FROM websocket.room_user where room_id=#{roomId} and user_id=#{userId} and type=1")
    Integer isRoomCreater(RoomReqVO reqVO);

    @SelectProvider(type = UserSql.class, method = "getUserList")
    List<UserResVO> getUserList(String userName);

    @Insert("INSERT INTO websocket.friend_relation VALUES(#{userId},#{friendId},now())")
    void makeFriend(Integer userId,Integer friendId);

    @Delete("delete from websocket.friend_relation where (user_id=#{userId} and friend_id=#{friendId}) or (user_id=#{friendId} and friend_id=#{userId})")
    void deleteFriend(Integer userId,Integer friendId);



}
