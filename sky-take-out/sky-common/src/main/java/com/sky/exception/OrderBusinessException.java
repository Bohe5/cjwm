package com.sky.exception;

// 声明公共类OrderBusinessException，继承自项目基础异常类BaseException
// 该类是订单相关的自定义业务异常，专门用于处理订单业务逻辑中出现的异常情况
public class OrderBusinessException extends BaseException {

    // 带异常消息的构造方法
    // 参数msg：异常的具体描述信息，用于说明订单业务异常的原因
    // 调用父类BaseException的带参构造方法，将异常消息传递给父类
    public OrderBusinessException(String msg) {
        super(msg);
    }

}
