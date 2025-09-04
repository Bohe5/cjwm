package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * 菜品口味数据访问层接口
 * 管理菜品与口味的关联关系，支持批量操作，适配多口味场景
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 插入口味数据
     * @param flavors
     *
     */
    void insertBatch(List<DishFlavor> flavors);
    /**
     * 删除口味数据
     * @param dishId
     *
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);
    /**
     * 批量删除口味数据
     * @param dishIds
     *
     */
    void deleteByDishIds(List<Long> dishIds);
    /**
     * 根据id查询菜品
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
