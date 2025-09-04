package com.sky.dto;

import com.sky.entity.OrderDetail;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * 订单数据传输对象（DTO）
 * 用于订单信息的新增、修改、查询等操作的数据传递
 * 实现Serializable接口支持对象序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class OrdersDTO implements Serializable {
    // 订单ID，新增时可不传，修改和查询时使用
    private Long id;

    //订单号
    private String number;

    //订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
    private Integer status;

    //下单用户id
    private Long userId;

    //地址id
    private Long addressBookId;

    //下单时间
    private LocalDateTime orderTime;

    //结账时间
    private LocalDateTime checkoutTime;

    //支付方式 1微信，2支付宝
    private Integer payMethod;

    //实收金额
    private BigDecimal amount;

    //备注
    private String remark;

    //用户名
    private String userName;

    //手机号
    private String phone;

    //地址
    private String address;

    //收货人
    private String consignee;
    // 订单包含的菜品列表
    private List<OrderDetail> orderDetails;

}
