package com.chat.tj.chat.model.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author tangjing
 * @date 2021/02/02 13:42
 */
@Data
public class UpdateRoomRoleReqVO {

    @ApiModelProperty("群组id")
    @NotNull(message = "群组id不能为空")
    private Integer roomId;

    @ApiModelProperty("用户id")
    @NotNull(message = "用户id不能为空")
    private Integer userId;

    @ApiModelProperty("成员角色：2管理员 3：普通成员")
    @NotNull(message = "成员角色不能为空")
    @Max(value = 3, message = "最大值为3")
    @Min(value = 2, message = "最小值为2")
    private Integer type;
}
