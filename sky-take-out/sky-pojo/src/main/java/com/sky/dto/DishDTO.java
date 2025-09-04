package com.sky.dto;

import com.sky.entity.DishFlavor;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 菜品数据传输对象（DTO）
 * 用于菜品信息的新增、修改、查询等操作的数据传递
 * 实现Serializable接口支持对象序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class DishDTO implements Serializable {
    // 菜品ID，新增时可不传，修改时必传
    private Long id;
    //菜品名称
    private String name;
    //菜品分类id
    private Long categoryId;
    //菜品价格
    private BigDecimal price;
    //图片
    private String image;
    //描述信息
    private String description;
    //0 停售 1 起售
    private Integer status;
    //口味
    private List<DishFlavor> flavors = new ArrayList<>();

}
