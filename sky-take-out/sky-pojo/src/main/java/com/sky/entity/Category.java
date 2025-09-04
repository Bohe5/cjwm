package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 分类实体类
 * 用于存储系统中的各类分类信息，如菜品分类、套餐分类等
 * 实现Serializable接口支持对象的序列化传输
 */
// Lombok注解组合：
// @Data：自动生成getter、setter、toString、equals、hashCode等方法
@Data
// @Builder：提供建造者模式，支持链式创建对象
@Builder
// @NoArgsConstructor：生成无参构造方法
@NoArgsConstructor
// @AllArgsConstructor：生成全参构造方法
@AllArgsConstructor
public class Category implements Serializable {
    // 序列化版本号，确保对象序列化和反序列化的兼容性
    private static final long serialVersionUID = 1L;
    // 分类ID，唯一标识一个分类
    private Long id;

    //类型: 1菜品分类 2套餐分类
    private Integer type;

    //分类名称
    private String name;

    //顺序
    private Integer sort;

    //分类状态 0标识禁用 1表示启用
    private Integer status;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;

    //创建人
    private Long createUser;

    //修改人
    private Long updateUser;
}
