package com.chat.tj.Test.observe;

import java.util.Observable;
import java.util.Observer;

/**
 * @author tangjing
 * @date 2021/02/19 14:42
 */
public class MyObserverTest implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("test,观察对象为:" + o + ";传递参数为：" + arg);
    }
}
