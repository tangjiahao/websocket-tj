package com.chat.tj.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标签角色权限注解
 *
 * @author tangjing
 * @date 2021/02/07 11:03
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolePermission {
    String[] roleId() default "-1";
}
