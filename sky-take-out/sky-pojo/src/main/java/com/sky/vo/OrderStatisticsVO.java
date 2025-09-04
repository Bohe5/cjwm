package com.sky.vo;

import lombok.Data;
import java.io.Serializable;
/**
 * 订单统计视图对象（VO）
 * 用于封装订单相关的核心统计数据，通常用于后台管理系统的概览页面
 * 展示关键指标的汇总信息，方便快速了解解订单整体情况
 */
@Data
public class OrderStatisticsVO implements Serializable {
    //待接单数量
    private Integer toBeConfirmed;

    //待派送数量
    private Integer confirmed;

    //派送中数量
    private Integer deliveryInProgress;
}
