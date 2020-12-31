package com.chat.tj.model.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tangjing
 * @date 2020/12/30 09:54
 */

@Data
@ApiModel(value ="UserReqVO",description = "用户登录和注册请求")
public class UserReqVO {

    @ApiModelProperty("用户名")
    @NotBlank
    private String userName;

    @ApiModelProperty("密码")
    @NotBlank
    private String pwd;
}
