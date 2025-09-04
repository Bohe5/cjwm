package com.sky.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 订单分页查询分页查询数据传输对象（DTO）
 * 用于接收前端传递的订单分页查询条件参数，支持多条件组合查询
 * 实现Serializable接口支持对象序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class OrdersPageQueryDTO implements Serializable {
    // 页码，从1开始，用于指定查询第几页的数据
    private int page;

    // 每页显示的记录数，用于指定每页展示多少条订单数据
    private int pageSize;

    // 订单编号，用于根据精确查询特定订单
    private String number;

    // 联系电话，用于根据手机号查询相关订单
    private String phone;

    // 订单状态，用于按状态筛选订单（如：1-待付款，2-待接单等）
    private Integer status;

    // 开始时间，用于按时间范围查询（配合@endTime使用）
    // @DateTimeFormat指定前端传递的日期时间格式，便于参数绑定
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    // 结束时间，用于按时间范围查询（配合@beginTime使用）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    // 用户ID，用于查询指定用户的订单
    private Long userId;
}
