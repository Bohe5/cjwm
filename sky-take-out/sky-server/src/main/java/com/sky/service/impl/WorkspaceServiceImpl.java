package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
/**
 *  工作区业务实现
 */
@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {
    // 注入订单数据访问接口
    @Autowired
    private OrderMapper orderMapper;
    // 注入用户数据访问接口
    @Autowired
    private UserMapper userMapper;
    // 注入菜品数据访问接口
    @Autowired
    private DishMapper dishMapper;
    // 注入套餐数据访问接口
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据时间段统计营业数据
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        /**
         * 营业额：当日已完成订单的总金额
         * 有效订单：当日已完成订单的数量
         * 订单完成率：有效订单数 / 总订单数
         * 平均客单价：营业额 / 有效订单数
         * 新增用户：当日新增用户的数量
         */
        // 创建查询参数Map
        Map map = new HashMap();
        map.put("begin",begin);// 开始时间
        map.put("end",end);// 结束时间

        //查询总订单数
        Integer totalOrderCount = orderMapper.countByMap(map);
        // 查询营业额（仅统计已完成状态的订单）
        map.put("status", Orders.COMPLETED);// 订单状态：已完成
        //营业额
        Double turnover = orderMapper.sumByMap(map);// 求和订单金额
        turnover = turnover == null? 0.0 : turnover;// 为空时设为0

        //有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);
        // 计算平均客单价和订单完成率
        Double unitPrice = 0.0;// 平均客单价
        Double orderCompletionRate = 0.0;// 订单完成率
        if(totalOrderCount != 0 && validOrderCount != 0){
            //订单完成率 = 有效订单数/总订单数
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //平均客单价= 营业额/有效订单数
            unitPrice = turnover / validOrderCount;
        }

        //新增用户数
        Integer newUsers = userMapper.countByMap(map);
        // 构建并返回营业数据视图对象
        return BusinessDataVO.builder()
                .turnover(turnover) // 营业额
                .validOrderCount(validOrderCount)// 有效订单数
                .orderCompletionRate(orderCompletionRate)// 订单完成率
                .unitPrice(unitPrice)// 平均客单价
                .newUsers(newUsers)// 新增用户数
                .build();
    }


    /**
     * 查询订单管理数据
     *
     * @return
     */
    public OrderOverViewVO getOrderOverView() {
        Map map = new HashMap();
        // 设置查询时间为当天（从00:00:00开始）
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        // 待接单数量（订单状态：待确认）
        map.put("status", Orders.TO_BE_CONFIRMED);
        //待接单
        Integer waitingOrders = orderMapper.countByMap(map);
        // 待派送数量（订单状态：已确认）
        //待派送
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);

        //已完成
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);

        //已取消
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);

        //全部订单
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);
        // 构建并返回订单总览视图对象
        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)// 待接单
                .deliveredOrders(deliveredOrders)// 待派送
                .completedOrders(completedOrders)// 已完成
                .cancelledOrders(cancelledOrders)// 已取消
                .allOrders(allOrders)// 全部订单
                .build();
    }

    /**
     * 查询菜品总览
     *
     * @return
     */
    public DishOverViewVO getDishOverView() {
        Map map = new HashMap();
        // 1. 在售菜品数量（状态：启用）
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);
        // 2. 停售菜品数量（状态：禁用）
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);
        // 构建并返回菜品总览视图对象
        return DishOverViewVO.builder()
                .sold(sold)// 在售数量
                .discontinued(discontinued)// 停售数量
                .build();
    }

    /**
     * 查询套餐总览
     *
     * @return
     */
    public SetmealOverViewVO getSetmealOverView() {
        Map map = new HashMap();
        // 1. 在售套餐数量（状态：启用）
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);
        // 2. 停售套餐数量（状态：禁用）
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);
        // 构建并返回套餐总览视图对象
        return SetmealOverViewVO.builder()
                .sold(sold)// 在售数量
                .discontinued(discontinued)// 停售数量
                .build();
    }
}
