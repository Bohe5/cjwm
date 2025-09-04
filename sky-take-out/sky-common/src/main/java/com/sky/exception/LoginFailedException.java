package com.sky.exception;

/**
 * 登录失败
 */
// 1. 声明公共类LoginFailedException，继承自项目统一的基础异常类BaseException
// 作用：专门定义“登录失败”场景的自定义业务异常，使异常类型与业务场景强绑定
public class LoginFailedException extends BaseException{

    // 2. 带异常消息的构造方法
    // 参数msg：字符串类型的异常详情消息，用于描述登录失败的具体原因（如“账号不存在”“密码错误”等）
    // 核心逻辑：调用父类BaseException的带参构造方法，将异常消息传递给父类，统一维护异常信息
    public LoginFailedException(String msg){
        super(msg);
    }
}
