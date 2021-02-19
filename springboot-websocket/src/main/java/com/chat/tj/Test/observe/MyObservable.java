package com.chat.tj.Test.observe;

import com.chat.tj.chat.model.ObserveMsg;

import java.util.Observable;

/**
 * 被观察者类
 *
 * @author tangjing
 * @date 2021/02/19 14:27
 */
public class MyObservable extends Observable {
    public static void throwMessage(ObserveMsg code) {
        MyObservable myObservable = new MyObservable();
        myObservable.addObserver(new MyObserver());
        myObservable.addObserver(new MyObserverTest());
        // 将状态指示变量changed设为true，这样才能通知所有观察者
        myObservable.setChanged();
        // 通知所有观察者，并将状态指示变量changed设为false,注意这里的顺序是后进先出
        myObservable.notifyObservers(code);
    }

    public static void main(String[] args) {
        ObserveMsg msg = new ObserveMsg(2, "14");
        throwMessage(msg);
    }
}
