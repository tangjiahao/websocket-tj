package com.chat.tj.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 消息对象
 *
 * @author tangjing
 * @date 2021/01/21 13:43
 */
@Data
public class SendMsg {

    @ApiModelProperty("消息文本")
    @NotBlank(message = "消息不能为空")
    private String message;

    @ApiModelProperty("发送者")
    @NotBlank(message = "发送者不能为空")
    private String sender;

    @ApiModelProperty("接收者")
    @NotBlank(message = "接受者不能为空")
    private String receiver;

    @ApiModelProperty("消息类型，1加好友 2加群 3代表系统消息  4代表普通消息 5图片消息 6文件消息")
    @NotNull(message = "消息类型不能为空")
    private Integer messageType;

    @ApiModelProperty("频道号")
    @NotBlank(message = "频道号不能为空")
    private String roomId;

    @ApiModelProperty("传图片和文件消息的时候可能用到,用于返回给前端文件名")
    private String fileName;

    @ApiModelProperty(value = "消息创建时间", hidden = true)
    private String createTime;
}
