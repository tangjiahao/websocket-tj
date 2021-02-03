package com.chat.tj.Test;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

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

    // 反射获取arraylist的capacity
    public static int getCapacityOfArrayList(ArrayList arrayList) {

        try {
            // 测试获取属性值
            // Field defaultCapacity = ArrayList.class.getDeclaredField("DEFAULT_CAPACITY");
            // defaultCapacity.setAccessible(true);
            // System.out.println(defaultCapacity.get(arrayList));
            Field elementData = ArrayList.class.getDeclaredField("elementData");
            elementData.setAccessible(true);
            return ((Object[]) elementData.get(arrayList)).length;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 获取vector的capacity
    public static int getCapacityOfVector(Vector vector) {
        try {
            Field elementData = Vector.class.getDeclaredField("elementData");
            elementData.setAccessible(true);
            return ((Object[]) elementData.get(vector)).length;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        // testFieldAndMethod(new TestResVO());
        // getInstance(new TestResVO());
        ArrayList<Object> s = new ArrayList<>();
        System.out.println("默认s-capacity:" + getCapacityOfArrayList(s));
        s.add(1);
        s.add("ace");
        System.out.println("添加元素后，s-size:" + s.size() + " s-capacity:" + getCapacityOfArrayList(s));
        for (int i = 0; i < 10; i++) {
            s.add(i);
        }
        System.out.println("超过旧的capacity大小后：");
        System.out.println("s-size:" + s.size() + " s-capacity:" + getCapacityOfArrayList(s));

        Vector<Object> v = new Vector<>();
        System.out.println("默认v-capacity:" + getCapacityOfVector(v));
        v.add(1);
        v.add("ace");
        System.out.println("添加元素后，v-size:" + v.size() + " v-capacity:" + getCapacityOfVector(v));
        for (int i = 0; i < 11; i++) {
            v.add(i);
        }
        System.out.println("超过旧的capacity大小后：");
        System.out.println("v-size:" + v.size() + " v-capacity:" + getCapacityOfVector(v));

    }

}
