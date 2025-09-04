package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 密码修改数据传输对象（DTO）
 * 用于接收用户/员工修改密码时传递的参数，包含原密码与新密码，支撑密码更新流程
 * 实现Serializable接口，支持对象在网络传输或跨服务调用中的序列化
 */
// Lombok的@Data注解：自动生成所有字段的getter/setter、toString()、equals()、hashCode()方法，简化模板代码
@Data
public class PasswordEditDTO implements Serializable {

    //员工id
    private Long empId;

    //旧密码
    private String oldPassword;

    //新密码
    private String newPassword;

}
