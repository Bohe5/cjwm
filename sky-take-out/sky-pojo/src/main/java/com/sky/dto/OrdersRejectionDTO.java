package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 订单拒绝数据传输对象（DTO）
 * 用于接收订单拒绝操作所需的参数，如订单ID和拒绝原因等
 * 实现Serializable接口支持对象的序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class OrdersRejectionDTO implements Serializable {
    // 订单ID，唯一标识需要拒绝的订单
    // 拒绝订单时必须指定该ID，用于定位具体要拒绝的订单记录
    private Long id;

    //订单拒绝原因
    private String rejectionReason;

}
