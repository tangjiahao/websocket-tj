package com.chat.tj.chat.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/19 18:47
 * @Version 1.0
 */
@Data
public class UserEntity {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String pwd;

    @ApiModelProperty("创建时间")
    private String createTime;
}
