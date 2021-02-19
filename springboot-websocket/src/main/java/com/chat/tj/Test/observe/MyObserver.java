package com.chat.tj.Test.observe;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者
 *
 * @author tangjing
 * @date 2021/02/19 14:31
 */
public class MyObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("观察对象为:" + o + ";传递参数为：" + arg);
    }
}
