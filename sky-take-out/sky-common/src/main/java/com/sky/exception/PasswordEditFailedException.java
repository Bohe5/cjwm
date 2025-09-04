package com.sky.exception;

/**
 * 密码修改失败异常
 */
// 声明公共类PasswordEditFailedException，继承自项目基础异常类BaseException
// 这是一个专门用于表示密码修改失败场景的自定义业务异常
public class PasswordEditFailedException extends BaseException{

    // 带异常消息的构造方法
    // 参数msg：异常的具体描述信息，用于说明密码修改失败的原因
    // 调用父类BaseException的带参构造方法，将异常信息传递给父类
    public PasswordEditFailedException(String msg){
        super(msg);
    }

}
