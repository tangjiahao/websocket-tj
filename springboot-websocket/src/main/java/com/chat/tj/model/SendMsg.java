package com.chat.tj.model;

import lombok.Data;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/19 18:46
 * @Version 1.0
 */
@Data
public class SendMsg {

    //消息文本
    private String message;

    //发送者
    private String sender;

    //接收者
    private String receiver;

    //消息类型，1 上线，2 下线 3在线名单 4 普通类型
    private Integer messageType;

    //频道号
    private String roomId;
}
