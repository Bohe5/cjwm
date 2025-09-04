package com.sky.exception;

/**
 * 密码错误异常
 */
// 声明公共类PasswordErrorException，继承自项目基础异常类BaseException
// 该类是专门用于表示密码错误场景的自定义异常
public class PasswordErrorException extends BaseException {

    // 无参构造方法
    // 调用父类BaseException的无参构造方法
    public PasswordErrorException() {
    }

    // 带异常消息的构造方法
    // 参数msg：异常的具体描述信息，用于说明密码错误的详细原因
    // 调用父类BaseException的带参构造方法，将异常信息传递给父类
    public PasswordErrorException(String msg) {
        super(msg);
    }

}
