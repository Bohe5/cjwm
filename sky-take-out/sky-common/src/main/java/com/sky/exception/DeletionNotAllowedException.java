package com.sky.exception;

// 声明一个公共类DeletionNotAllowedException，继承自BaseException
// 这是一个自定义异常类，专门用于表示删除操作不被允许的异常情况
public class DeletionNotAllowedException extends BaseException {

    // 带消息参数的构造方法
    // 参数msg：异常信息描述，用于说明删除操作不被允许的具体原因
    // 调用父类BaseException的带消息参数构造方法，实现异常信息的传递
    public DeletionNotAllowedException(String msg) {
        super(msg);
    }

}
