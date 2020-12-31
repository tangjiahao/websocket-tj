package com.chat.tj.model.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/20 12:43
 * @Version 1.0
 */
@Data
public class UserResVO {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

}
