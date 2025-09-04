package com.sky.exception;

// 声明一个公共类AddressBookBusinessException，继承自BaseException
// 这是一个地址簿相关的自定义业务异常类，用于处理地址簿业务逻辑中出现的异常情况
public class AddressBookBusinessException extends BaseException {

    // 带消息参数的构造方法
    // 参数msg：异常信息描述，用于说明具体的业务异常原因
    // 调用父类BaseException的带消息参数构造方法，实现异常信息的传递
    public AddressBookBusinessException(String msg) {
        super(msg);
    }

}
