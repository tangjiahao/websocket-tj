package com.chat.tj.common.observe;

import com.chat.tj.chat.model.ObserveMsg;

import java.util.Observable;

/**
 * 被观察者（redis）
 *
 * @author tangjing
 * @date 2021/02/19 15:29
 */
public class RedisObservable extends Observable {
    public static void throwMessage(ObserveMsg code) {
        RedisObservable myObservable = new RedisObservable();
        myObservable.addObserver(new RedisObserver());
        // 将状态指示变量changed设为true，这样才能通知所有观察者
        myObservable.setChanged();
        // 通知所有观察者，并将状态指示变量changed设为false,注意这里的顺序是后进先出
        myObservable.notifyObservers(code);
    }
}
