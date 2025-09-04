package com.sky.vo;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;
/**
 * 订单扩展视图对象（VO）
 * 继承自Orders订单实体类，在其基础上补充前端展示或业务处理所需的关联数据
 * 用于完整呈现订单信息（含订单主数据+关联的菜品/详情数据）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    //订单菜品信息
    private String orderDishes;

    //订单详情
    private List<OrderDetail> orderDetailList;

}
