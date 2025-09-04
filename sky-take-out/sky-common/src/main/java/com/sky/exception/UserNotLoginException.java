package com.sky.exception;

// 声明公共类UserNotLoginException，继承自项目基础异常类BaseException
// 该类是专门用于表示用户未登录场景的自定义异常
public class UserNotLoginException extends BaseException {

    // 无参构造方法
    // 调用父类BaseException的无参构造方法
    // 适用于只需标识"用户未登录"状态，无需详细描述的场景
    public UserNotLoginException() {
    }

    // 带异常消息的构造方法
    // 参数msg：异常的具体描述信息，用于说明未登录的原因或相关提示
    // 调用父类BaseException的带参构造方法，将异常信息传递给父类
    public UserNotLoginException(String msg) {
        super(msg);
    }

}
