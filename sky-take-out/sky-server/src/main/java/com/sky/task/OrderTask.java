package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时处理订单
 */
@Component
@Slf4j
public class OrderTask {
    // 自动注入订单数据访问接口，用于操作订单数据库
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 定时处理超时订单
     */

    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单:{}", LocalDateTime.now());
        // 计算"超时时间点"：当前时间往前推15分钟（即15分钟前的时间）
        // 逻辑：订单创建后15分钟内未支付，则判定为超时
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        // 调用mapper查询：状态为"待支付"（Orders.PENDING_PAYMENT）且订单创建时间早于超时时间的订单
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,time);
        // 若存在超时订单，批量处理状态更新
        if(orderList != null && orderList.size() > 0){
            for(Orders orders : orderList){
                // 1. 将订单状态改为"已取消"（Orders.CANCELLED）
                orders.setStatus(Orders.CANCELLED);
                // 2. 设置取消原因（用于前端展示和后台记录）
                orders.setCancelReason("订单超时，自动取消");
                // 3. 更新订单取消时间
                orders.setCancelTime(LocalDateTime.now());
                // 4. 调用mapper更新订单信息到数据库
                orderMapper.update(orders);
            }
        }
    }
    /**
     * 定时处理处于派送中的订单
     */
   @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理处于派送中的订单:{}",LocalDateTime.now());
       // 计算"超时派送时间点"：当前时间往前推60分钟（即1小时前的时间）
       // 逻辑：订单进入"派送中"状态后1小时未确认完成，则自动标记为完成
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,time);
       // 若存在超时派送订单，批量处理状态更新
        if(ordersList != null && ordersList.size() > 0){
            for(Orders orders : ordersList){
                // 1. 将订单状态改为"已完成"（Orders.COMPLETED）
                orders.setStatus(Orders.COMPLETED);
                // 2. 调用mapper更新订单信息到数据库
                orderMapper.update(orders);
            }
        }
    }
}
