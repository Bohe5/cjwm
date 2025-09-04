package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 商品销售数据DTO
 * 用于封装商品销售相关的统计数据，如销量、销售额等信息
 * 实现Serializable接口支持对象的序列化传输
 */
// Lombok注解组合：
// @Data：生成getter、setter、toString等方法
@Data
// @AllArgsConstructor：生成全参构造方法
@AllArgsConstructor
// @NoArgsConstructor：生成无参构造方法
@NoArgsConstructor
// @Builder：提供建造者模式，方便对象创建
@Builder
public class GoodsSalesDTO implements Serializable {
    //商品名称
    private String name;

    //销量
    private Integer number;
}
