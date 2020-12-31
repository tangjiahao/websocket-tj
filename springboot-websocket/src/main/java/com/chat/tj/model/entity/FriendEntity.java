package com.chat.tj.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/20 10:58
 * @Version 1.0
 */
@Data
public class FriendEntity {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("好友id")
    private String friendId;

    @ApiModelProperty("创建时间")
    private String createTime;

}
