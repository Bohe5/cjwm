package com.sky.exception;

// 声明公共类ShoppingCartBusinessException，继承自项目基础异常类BaseException
// 该类是购物车相关的自定义业务异常，专门用于处理购物车业务逻辑中的异常情况
public class ShoppingCartBusinessException extends BaseException {

    // 带异常消息的构造方法
    // 参数msg：异常的具体描述信息，用于说明购物车操作失败的原因
    // 调用父类BaseException的带参构造方法，将异常信息传递给父类
    public ShoppingCartBusinessException(String msg) {
        super(msg);
    }

}
