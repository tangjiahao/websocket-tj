package com.chat.tj.chat.dao.db1;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Mapper1 {
    /**
     * 获取用户列表，传了名字表示指定查询
     *
     * @return 结果集
     */
    @Select("select user_name from user")
    List<String> getUserList();
}
