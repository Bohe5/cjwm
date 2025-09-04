package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 自定义注解，公共字段自动填充
 */
// 该注解只能应用于方法上
@Target(ElementType.METHOD)
// 注解在运行时仍然保留，可以通过反射获取
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
//    操作类型，用于指定自动填充的具体操作
    OperationType value();
}
