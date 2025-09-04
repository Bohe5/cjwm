package com.sky.exception;

/**
 * 套餐启用失败异常
 */
// 声明公共类SetmealEnableFailedException，继承自项目基础异常类BaseException
// 该类是专门用于表示套餐启用失败场景的自定义业务异常
public class SetmealEnableFailedException extends BaseException {

    // 无参构造方法
    // 调用父类BaseException的无参构造方法，适用于不需要详细错误信息的场景
    public SetmealEnableFailedException(){}

    // 带异常消息的构造方法
    // 参数msg：异常的具体描述信息，用于说明套餐启用失败的详细原因
    // 调用父类BaseException的带参构造方法，将异常信息传递给父类
    public SetmealEnableFailedException(String msg){
        super(msg);
    }
}
