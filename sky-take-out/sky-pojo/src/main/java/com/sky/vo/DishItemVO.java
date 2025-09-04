package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 菜品项视图对象（VO）
 * 用于向前端展示菜品的详细信息，包含菜品基本属性和可选口味等
 * 实现Serializable接口支持对象的序列化传输
 */
// Lombok注解组合：
// @Data：自动生成getter、setter、toString等方法
@Data
// @Builder：提供建造者模式，支持链式创建对象
@Builder
// @NoArgsConstructor：生成无参构造方法
@NoArgsConstructor
// @AllArgsConstructor：生成全参构造方法
@AllArgsConstructor
public class DishItemVO implements Serializable {

    //菜品名称
    private String name;

    //份数
    private Integer copies;

    //菜品图片
    private String image;

    //菜品描述
    private String description;
}
