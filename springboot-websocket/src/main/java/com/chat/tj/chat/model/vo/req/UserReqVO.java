package com.chat.tj.chat.model.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author tangjing
 * @date 2020/12/30 09:54
 */

@Data
@ApiModel(value = "UserReqVO", description = "用户登录和注册请求")
public class UserReqVO {

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String pwd;

    @ApiModelProperty("角色id,管理员创建用户时使用,1后台管理员,2普通用户")
    @Max(value = 2, message = "roleId最大值为2")
    @Min(value = 1, message = "roleId最小值为1")
    private Integer roleId;


}
