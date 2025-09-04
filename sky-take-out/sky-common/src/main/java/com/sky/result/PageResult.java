package com.sky.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 */
// Lombok注解，自动生成getter、setter、toString、equals、hashCode等方法
@Data
// Lombok注解，生成包含所有参数的构造方法
@AllArgsConstructor
// Lombok注解，生成无参构造方法
@NoArgsConstructor
// 实现Serializable接口，支持对象序列化，便于网络传输或持久化
public class PageResult implements Serializable {

    private long total; // 总记录数：符合查询条件的所有记录总数

    private List records; //当前页数据集合：分页查询后当前页所包含的数据列表

}
