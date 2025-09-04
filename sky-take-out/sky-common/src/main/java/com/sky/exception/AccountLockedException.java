package com.sky.exception;

/**
 * 账号被锁定异常
 */
// 定义账户锁定异常类，继承自基础异常类BaseException，用于表示账户被锁定的异常情况
public class AccountLockedException extends BaseException {
    // 无参构造方法，调用父类BaseException的无参构造方法
    public AccountLockedException() {
    }
    // 有参构造方法，接收一个字符串类型的异常信息msg，调用父类BaseException的有参构造方法并传入该信息
    //super是一个关键字，主要用于访问父类的成员（属性、方法、构造器）
    // super(msg)表示调用父类BaseException中带有一个 String 类型参数的构造方法，并将当前构造方法接收的msg参数传递给父类构造方法。
    public AccountLockedException(String msg) {
        super(msg);
    }

}
