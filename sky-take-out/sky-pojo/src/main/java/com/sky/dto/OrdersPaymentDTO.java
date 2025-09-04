package com.sky.dto;

import lombok.Data;
import java.io.Serializable;
/**
 * 订单支付数据传输对象（DTO）
 * 用于接收前端传递的订单支付相关参数，支撑支付流程的参数传递
 * 实现Serializable接口，支持对象在网络传输或跨服务调用中的序列化
 */
// Lombok的@Data注解：自动生成所有字段的getter/setter、toString()、equals()、hashCode()方法，简化模板代码
@Data
public class OrdersPaymentDTO implements Serializable {
    //订单号
    private String orderNumber;

    //付款方式
    private Integer payMethod;

}
