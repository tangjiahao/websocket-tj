package com.chat.tj.Test.datasource.dao.db2;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Mapper2 {
    @Select("select url from api_interface_info")
    List<String> getinterfaceName();
}
