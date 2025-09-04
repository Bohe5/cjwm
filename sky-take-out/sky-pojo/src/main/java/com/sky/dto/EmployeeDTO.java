package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 员工数据传输对象（DTO）
 * 用于在不同层之间传递员工信息，如前端与后端之间的数据交互
 * 实现Serializable接口支持对象的序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString、equals、hashCode等方法
@Data
public class EmployeeDTO implements Serializable {
    // 员工ID，唯一标识一个员工
    private Long id;

    // 用户名，用于员工登录系统
    private String username;

    // 员工姓名，显示用的真实姓名
    private String name;

    // 员工联系电话
    private String phone;

    // 员工性别，通常用"男"、"女"或"未知"表示
    private String sex;

    // 身份证号码，用于员工身份验证和信息登记
    private String idNumber;
}
