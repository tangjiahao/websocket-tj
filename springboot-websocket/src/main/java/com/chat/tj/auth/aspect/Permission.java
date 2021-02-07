package com.chat.tj.auth.aspect;

import com.chat.tj.auth.annotation.RolePermission;
import com.chat.tj.auth.constant.AuthConstant;
import com.chat.tj.chat.model.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aop 角色权限
 *
 * @author tangjing
 * @date 2021/02/07 11:08
 */
@Aspect
@Component
@Slf4j
public class Permission {

    @Pointcut("@annotation(com.chat.tj.auth.annotation.RolePermission)")
    private void rolePermission() {
    }

    /**
     * 角色权限控制
     *
     * @param joinPoint 切
     * @return 权限结果
     * @throws Throwable 异常
     */
    @Around("rolePermission()")
    public Object rolePermissionAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取角色id
        String roleId = MDC.get(AuthConstant.ROLE_ID);
        // 获取注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RolePermission annotation = methodSignature.getMethod().getAnnotation(RolePermission.class);
        String[] permissionRoleId = annotation.roleId();
        String permissionRoleIds = Arrays.toString(permissionRoleId);
        // 1.角色限制
        if (!permissionRoleIds.contains(roleId)) {
            return ResponseVo.failed("权限不足！");
        }
        return joinPoint.proceed();
    }

}
