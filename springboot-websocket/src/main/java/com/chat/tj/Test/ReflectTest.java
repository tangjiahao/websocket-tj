package com.chat.tj.Test;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射机制的测试文件
 *
 * @author tangjing
 * @date 2021/01/19 15:07
 */
@Data
public class ReflectTest {


    public static void testFieldAndMethod(Object ace) {
        if (ace == null) {
            return;
        }
        // 获取对象的class
        Class<?> class0 = ace.getClass();
        // 获取class对象的摘要信息
        System.out.println(class0.getName() + "是否是基础类型:" + class0.isPrimitive());
        System.out.println(class0.getName() + "是否是集合:" + class0.isArray());
        System.out.println(class0.getName() + "是否是枚举类:" + class0.isEnum());

        // 获取class对象的属性和方法
        // 获取所有属性
        Field[] allFileds = class0.getDeclaredFields();
        // 获取public属性
        Field[] publicFields = class0.getFields();
        System.out.println("所有属性：");
        for (Field field : allFileds) {
            System.out.println(field.getName() + "详细：" + field.toString());
        }
        System.out.println("public属性：");
        for (Field field : publicFields) {
            System.out.println(field.getName() + "详细：" + field.toString());
        }
        // 获取对象所有方法
        Method[] methods = class0.getDeclaredMethods();
        // 获取包含父类在内的所有方法
        Method[] allMethods = class0.getMethods();
        System.out.println("所有方法：");
        for (Method method : methods) {
            System.out.println(method.getName() + "返回类型：" + method.getReturnType());
        }
        System.out.println("包含父类在内的所有方法");
        for (Method method : allMethods) {
            System.out.println(method.getName() + "详细：" + method.toString());
        }
    }

    // Java反射得到一个类的私有方法
    public static void getInstance(Object obj) {
        if (obj == null) {
            return;
        }
        Class<?> class1 = obj.getClass();
        try {
            Method met = class1.getDeclaredMethod("testReflect", int.class);
            met.setAccessible(true);
            met.invoke(class1.newInstance(), 5);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        // testFieldAndMethod(new TestResVO());
        getInstance(new TestResVO());

    }

}
