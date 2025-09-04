package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 分类数据传输对象（DTO）
 * 用于在不同层之间传输分类相关的数据
 * 实现 Serializable 接口支持对象的序列化
 */
// Lombok 的 @Data 注解，自动生成 getter、setter、toString、equals、hashCode 等方法
@Data
public class CategoryDTO implements Serializable {

    //主键
    private Long id;

    //类型 1 菜品分类 2 套餐分类
    private Integer type;

    //分类名称
    private String name;

    //排序
    private Integer sort;

}
