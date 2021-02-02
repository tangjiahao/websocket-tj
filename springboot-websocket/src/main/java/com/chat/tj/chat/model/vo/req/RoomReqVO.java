package com.chat.tj.chat.model.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tangjing
 * @date 2020/12/30 13:33
 */
@Data
@ApiModel(value = "RoomReqVO", description = "用户创建群组和加入群组的请求")
public class RoomReqVO {

    @ApiModelProperty("用户id")
    @NotNull(message = "用户id为空")
    private Integer userId;

    @ApiModelProperty("群名称")
    @NotBlank(message = "群名称不能为空")
    private String roomName;

    @ApiModelProperty(value = "前端不用传，群id", hidden = true)
    private Integer roomId;

    @ApiModelProperty(value = "前端不用传，群成员类型，1群主（创建者）2管理员 3普通群员", hidden = true)
    private Integer type;
}
