package com.chat.tj.chat.model;

import lombok.Data;

/**
 * 观察者模式传递信息用实体
 *
 * @author tangjing
 * @date 2021/02/19 15:00
 */
@Data
public class ObserveMsg {
    /**
     * 状态码
     */
    Integer code;

    /**
     * 附加信息
     */
    String msg;

    public ObserveMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ObserveMsg(Integer code) {
        this.code = code;
        this.msg = "";
    }
}
