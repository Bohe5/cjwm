package com.sky.dto;

import com.sky.entity.SetmealDish;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 套餐数据传输对象（DTO）
 * 用于套餐信息的新增、修改、查询等操作的数据传递
 * 实现Serializable接口接口支持对象序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class SetmealDTO implements Serializable {
    // 套餐ID，新增时可不传，修改和查询时使用
    private Long id;

    //分类id
    private Long categoryId;

    //套餐名称
    private String name;

    //套餐价格
    private BigDecimal price;

    //状态 0:停用 1:启用
    private Integer status;

    //描述信息
    private String description;

    //图片
    private String image;

    //套餐菜品关系
    private List<SetmealDish> setmealDishes = new ArrayList<>();

}
