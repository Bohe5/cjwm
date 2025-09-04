package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 套餐套餐分页查询数据传输对象（DTO）
 * 用于接收前端传递的套餐分页查询条件参数，支持分页和名称筛选
 * 实现SerializableSerializableSerializable接口支持对象序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class SetmealPageQueryDTO implements Serializable {
    // 页码，从1开始，用于指定查询第几页的数据
    private int page;
    // 每页显示的记录数，用于指定每页展示多少条套餐数据
    private int pageSize;
    // 套餐名称，用于根据名称进行模糊查询（可选参数）
    private String name;

    //分类id
    private Integer categoryId;

    //状态 0表示禁用 1表示启用
    private Integer status;

}
