package com.sky.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
// Lombok注解，自动生成getter、setter、toString、equals、hashCode等方法
@Data
/**
 * 通用接口返回结果封装类
 * 用于统一所有API接口的返回格式，支持泛型数据
 */
public class Result<T> implements Serializable {

    private Integer code; //状态编码：1成功，0和其它数字为失败
    private String msg; //错误信息：当code不为1时，返回具体的错误描述
    private T data; //数据：接口成功时返回的具体数据，类型由泛型T指定
    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;// 设置成功状态码
        return result;
    }
    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object; // 设置返回数据
        result.code = 1;// 设置成功状态码
        return result;
    }
    /**
     * 失败返回（带错误信息）
     */
    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;// 设置错误信息
        result.code = 0;// 设置失败状态码
        return result;
    }

}
