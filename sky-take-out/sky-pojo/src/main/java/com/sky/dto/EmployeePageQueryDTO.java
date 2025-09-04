package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 员工分页查询数据传输对象（DTO）
 * 用于接收前端传递的员工分页查询条件参数
 * 实现Serializable接口支持对象序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString、equals、hashCode等方法
@Data
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    private String name;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;

}
