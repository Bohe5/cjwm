package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
// 组合注解：@ControllerAdvice + @ResponseBody，用于全局异常处理并返回JSON
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){// 指定捕获BaseException及其子类异常
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage()); // 直接使用异常中携带的错误信息返回（业务异常信息通常已适配前端展示）
    }
    /**
     * 处理sql异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage(); // 获取数据库返回的原始错误信息
        // 处理"唯一键重复"异常（如用户名、手机号重复）
        if(message.contains("Duplicate entry")){
            // 解析错误信息：MySQL的Duplicate entry格式通常为"Duplicate entry 'xxx' for key 'xxx'"
            String[] split = message.split(" ");// 按空格分割字符串
            String username = split[2];// 提取重复的值（如重复的手机号）
            // 组合成用户友好的提示（如"13800138000已存在"）
            String msg=username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else{
            // 其他未明确处理的SQL约束异常，返回通用错误提示
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
