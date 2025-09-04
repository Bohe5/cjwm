package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 订单确认数据传输对象（DTO）
 * 用于接收订单确认操作所需的参数
 * 实现Serializable接口支持对象的序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class OrdersConfirmDTO implements Serializable {
    // 订单ID，唯一标识需要确认的订单
    private Long id;
    //订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款
    private Integer status;

}
