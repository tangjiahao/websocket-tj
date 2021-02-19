package com.chat.tj.common.observe;

import com.chat.tj.chat.model.ObserveMsg;
import com.chat.tj.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;

/**
 * 观察者
 *
 * @author tangjing
 * @date 2021/02/19 15:30
 */
@Component
public class RedisObserver implements Observer {
    public static RedisObserver redisObserver;
    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        redisObserver = this;
        redisObserver.redisUtil = this.redisUtil;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            return;
        }
        ObserveMsg obj = (ObserveMsg) arg;
        System.out.println("观察对象为:" + o + ";传递参数为：" + obj.getMsg());
        switch (obj.getCode()) {
            case ObserveCode.ROOM_MEMBER_CHANGE: {
                if (!CollectionUtils.isEmpty(redisObserver.redisUtil.lGet(obj.getMsg(), 0, -1))) {
                    redisObserver.redisUtil.del(obj.getMsg());
                }
            }
            ;
            break;
            default:
                System.out.println("非观察处理对象");
        }
    }
}
