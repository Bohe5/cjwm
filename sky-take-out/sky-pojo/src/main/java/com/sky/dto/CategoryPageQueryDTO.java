package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 分类分页查询数据传输对象（DTO）
 * 用于接收前端传递的分类分页查询条件参数
 * 实现Serializable接口支持对象的序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString、equals、hashCode等方法
@Data
public class CategoryPageQueryDTO implements Serializable {

    //页码
    private int page;

    //每页记录数
    private int pageSize;

    //分类名称
    private String name;

    //分类类型 1菜品分类  2套餐分类
    private Integer type;

}
