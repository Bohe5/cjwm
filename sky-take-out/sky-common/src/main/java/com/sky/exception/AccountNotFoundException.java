package com.sky.exception;

/**
 * 账号不存在异常
 */
// 声明一个公共类AccountNotFoundException，继承自BaseException
// 这是一个自定义异常类，用于表示账户未找到的异常情况
public class AccountNotFoundException extends BaseException {
    // 无参构造方法
    // 调用父类BaseException的无参构造方法
    public AccountNotFoundException() {
    }
    // 带消息参数的构造方法
    // 接收一个字符串类型的异常消息
    // 调用父类BaseException的带消息参数的构造方法，将异常消息传递给父类
    public AccountNotFoundException(String msg) {
        super(msg);
    }

}
