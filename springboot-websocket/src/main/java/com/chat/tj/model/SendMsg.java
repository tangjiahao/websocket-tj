package com.chat.tj.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息对象
 *
 * @author tangjing
 * @date 2021/01/21 13:43
 */
@Data
public class SendMsg {

    @ApiModelProperty("消息文本")
    private String message;

    @ApiModelProperty("发送者")
    private String sender;

    @ApiModelProperty("接收者")
    private String receiver;

    @ApiModelProperty("消息类型，1 上线，2 下线 3在线名单 4 普通消息 5图片消息 6文件消息")
    private Integer messageType;

    @ApiModelProperty("频道号")
    private String roomId;

    @ApiModelProperty("传图片和文件消息的时候可能用到,用于返回给前端文件名")
    private String fileName;

    @ApiModelProperty(value = "消息创建时间", hidden = true)
    private String createTime;
}
