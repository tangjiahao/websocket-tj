package com.chat.tj.chat.dao;

import com.chat.tj.chat.model.SendMsg;
import com.chat.tj.chat.model.entity.RoomEntity;
import com.chat.tj.chat.model.entity.UserEntity;
import com.chat.tj.chat.model.vo.req.MessageRecordReqVO;
import com.chat.tj.chat.model.vo.req.RoomReqVO;
import com.chat.tj.chat.model.vo.req.UpdateRoomRoleReqVO;
import com.chat.tj.chat.model.vo.req.UserReqVO;
import com.chat.tj.chat.model.vo.res.RoomMemberResVO;
import com.chat.tj.chat.model.vo.res.UserResVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author tangjing
 * @date 2021/01/25 10:48
 */
@Mapper
public interface UserMapper {
    /**
     * 查询用户所在群组列表
     *
     * @param userId 用户id
     * @return 结果集
     */
    @Select("SELECT a.room_id,a.room_name FROM websocket.room a left join websocket.room_user b on a.room_id =b.room_id where b.user_id=#{userId}")
    List<RoomEntity> getRoomListByUserId(Integer userId);

    /**
     * 查询好友列表
     *
     * @param userId 用户id
     * @return 结果集
     */
    @Select("SELECT a.* FROM  websocket.user a where a.user_id in (select user_id as userId from websocket.friend_relation b " +
            "where b.friend_id = #{userId} union all select friend_id as userId from websocket.friend_relation c where c.user_id =#{userId})")
    List<UserResVO> getFriendList(Integer userId);

    /**
     * 查询群组内所有成员
     *
     * @param roomId 群组
     * @return 结果集
     */
    @Select("SELECT b.*,a.type FROM websocket.room_user a left join websocket.user b on a.user_id = b.user_id where " +
            "a.room_id =#{roomId} order by a.type ")
    List<RoomMemberResVO> getRoomMemberList(Integer roomId);

    /**
     * 注册用户
     *
     * @param userEntity 用户信息
     */
    @Insert("INSERT into websocket.user(user_name,pwd,create_time) values(#{userName},#{pwd},now())")
    @Options(useGeneratedKeys = true, keyProperty = "user_id", keyColumn = "report_id")
    void saveUser(UserEntity userEntity);

    /**
     * 登录
     *
     * @param userEntity 用户信息
     * @return 结果集
     */
    @Select("SELECT * FROM websocket.user where user_name=#{userName} and pwd=#{pwd} limit 1")
    UserResVO selectUser(UserReqVO userEntity);

    /**
     * 得到群组列表
     *
     * @param roomName 群组名称
     * @return 结果集
     */
    @SelectProvider(type = UserSql.class, method = "getRoomList")
    List<RoomEntity> getRoomList(String roomName);

    /**
     * 通过用户名查找用户id
     *
     * @param userName 用户名
     * @return 用户id
     */
    @Select("SELECT user_id FROM websocket.user WHERE user_name =#{userName} limit 1")
    Integer findUserId(String userName);

    /**
     * 创建群组
     *
     * @param reqVO 请求
     */
    @Insert("INSERT into websocket.room(room_name) values(#{roomName})")
    @Options(useGeneratedKeys = true, keyProperty = "roomId", keyColumn = "room_id")
    void saveRoom(RoomReqVO reqVO);

    /**
     * 加入群
     *
     * @param reqVO 请求
     */
    @Insert("INSERT into websocket.room_user(room_id,user_id,type) values(#{roomId},#{userId},#{type})")
    void addRoomUser(RoomReqVO reqVO);

    /**
     * 查找群组
     *
     * @param roomName 群名
     * @return 结果
     */
    @Select("SELECT * FROM websocket.room WHERE room_name=#{roomName} limit 1")
    RoomEntity selectRoom(String roomName);

    /**
     * 删除群组
     *
     * @param roomId 群id
     */
    @Delete("DELETE FROM websocket.room where room_id=#{roomId}")
    void deleteRoom(Integer roomId);

    /**
     * 删除群成员
     *
     * @param roomId 群id
     * @param userId 用户id
     */
    @DeleteProvider(type = UserSql.class, method = "deleteRoomUser")
    void deleteRoomUser(Integer roomId, Integer userId);

    /**
     * 判断是否为群主
     *
     * @param reqVO 请求
     * @return 结果
     */
    @Select("SELECT count(1) FROM websocket.room_user where room_id=#{roomId} and user_id=#{userId} and type=1")
    Integer isRoomCreater(RoomReqVO reqVO);


    /**
     * 获取用户列表，传了名字表示指定查询
     *
     * @param userName 用户名
     * @return 结果集
     */
    @SelectProvider(type = UserSql.class, method = "getUserList")
    List<UserResVO> getUserList(String userName);

    /**
     * 添加好友
     *
     * @param userId   用户id
     * @param friendId 好友id
     */
    @Insert("INSERT INTO websocket.friend_relation VALUES(#{userId},#{friendId},now())")
    void makeFriend(Integer userId, Integer friendId);

    /**
     * 删除好友
     *
     * @param userId   用户id
     * @param friendId 好友id
     */
    @Delete("delete from websocket.friend_relation where (user_id=#{userId} and friend_id=#{friendId}) or (user_id=#{friendId} and friend_id=#{userId})")
    void deleteFriend(Integer userId, Integer friendId);

    /**
     * 保存记录
     *
     * @param sendMsg 消息记录
     */
    @Insert("INSERT into websocket.chat_record(sender,receiver,create_time,message,message_type,room_id,file_name) " +
            "values(#{sender},#{receiver},now(),#{message},#{messageType},#{roomId},#{fileName})")
    void saveChatRecord(SendMsg sendMsg);

    /**
     * 获得用户消息记录
     *
     * @param reqVO 获得消息记录请求
     * @return 结果集
     */
    @SelectProvider(type = UserSql.class, method = "getRecordList")
    List<SendMsg> getRecordList(MessageRecordReqVO reqVO);

    /**
     * 删除记录
     */
    @Delete("delete from websocket.chat_record")
    void deleteRecord();

    @Update("update websocket.chat_record set is_delete=1 where sender=#{sender} and receiver=#{receiver} and message_type=" +
            "#{messageType} and create_time=#{createTime}")
    void deleteRecordLogic(SendMsg sendMsg);

    @Update("update websocket.room_user set type=#{type} where room_id=#{roomId} and user_id=#{userId} ")
    void updateRoomRole(UpdateRoomRoleReqVO roleReqVO);

}
