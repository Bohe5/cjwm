package com.sky.exception;

/**
 * 业务异常
 */
// 声明一个公共的BaseException类，继承自RuntimeException
// 这是一个基础异常类，作为项目中所有自定义异常的父类
// 继承RuntimeException表示这是一个非受检异常（unchecked exception）
public class BaseException extends RuntimeException {

    // 无参构造方法
    // 调用父类RuntimeException的无参构造方法
    public BaseException() {
    }

    // 带消息参数的构造方法
    // 参数msg：异常的详细信息，描述异常发生的原因
    // 调用父类RuntimeException的带消息参数构造方法，将异常信息传递给父类
    public BaseException(String msg) {
        super(msg);
    }

}
