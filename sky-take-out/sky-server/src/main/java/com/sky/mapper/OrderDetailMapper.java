package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 *订单详情数据访问接口
 * 处理订单详情相关的数据，比如订单中具体菜品的信息、数量等在数据库层面的操作。
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入
     * @param orderDetailList
     * @return
     */
    void insertBatch(List<OrderDetail> orderDetailList);
    /**
     * 根据订单id查询订单明细
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}
