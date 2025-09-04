package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 订单取消数据传输对象（DTO）
 * 用于接收订单取消操作所需的参数
 * 实现Serializable接口支持对象的序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class OrdersCancelDTO implements Serializable {
    // 订单ID，唯一标识需要取消的订单
    // 取消订单时必须指定该ID，用于定位具体要取消的订单记录
    private Long id;
    //订单取消原因
    private String cancelReason;

}
