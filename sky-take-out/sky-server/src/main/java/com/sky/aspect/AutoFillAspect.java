package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义注解，公共字段自动填充
 */
// 标识当前类为切面类
@Aspect
// 将当前类注册为Spring的组件
@Component
// 提供日志功能
@Slf4j
public class AutoFillAspect {
    /**
     * 定义切入点
     * 匹配com.sky.mapper包下所有类的所有方法，且方法上标注了@AutoFill注解
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}
    /**
     * 前置通知：在目标方法执行前执行自动填充操作
     * @param joinPoint 连接点对象，包含目标方法的信息
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获得数据库操作类型
        // 获取目标方法的参数（假设实体对象是第一个参数）
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;// 没有参数则无需处理
        }
        // 实体对象（如User、Order等）
        Object entity = args[0];
        // 准备填充的数据
        LocalDateTime now = LocalDateTime.now();// 当前时间
        Long currentId = BaseContext.getCurrentId();// 当前登录用户ID（从ThreadLocal中获取）
        // 根据操作类型执行不同的填充逻辑
        if (operationType == OperationType.INSERT) {
            // 新增操作：需要填充创建时间、创建人、更新时间、更新人
            try {
                // 通过反射获取实体类中对应的setter方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                // 调用setter方法设置值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }catch (Exception e) {
                e.printStackTrace(); // 处理反射调用异常
            }
        }else if (operationType == OperationType.UPDATE) {
            // 更新操作：只需要填充更新时间、更新人
            try {
                // 通过反射获取实体类中对应的setter方法
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                // 调用setter方法设置值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }catch (Exception e) {
                e.printStackTrace();// 处理反射调用异常
            }

        }
    }
}
