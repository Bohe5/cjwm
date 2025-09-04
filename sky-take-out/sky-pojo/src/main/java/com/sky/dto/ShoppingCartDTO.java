package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 购物车数据传输对象（DTO）
 * 用于接收用户添加到购物车的商品信息，支持菜品和套餐两种类型
 * 实现Serializable接口支持对象的序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class ShoppingCartDTO implements Serializable {
    // 菜品ID，添加菜品到购物车时使用（与setmealId二选一）
    private Long dishId;

    // 套餐ID，添加套餐到购物车时使用（与dishId二选一）
    private Long setmealId;

    // 菜品口味，仅当添加菜品时有效，用于记录用户选择的菜品口味组合
    // 例如："少辣,加麻" 或 JSON格式的口味描述
    private String dishFlavor;

    // 可根据实际业务需求添加更多字段，例如：
    // private Integer number; // 商品数量，默认为1，支持手动指定数量
}
