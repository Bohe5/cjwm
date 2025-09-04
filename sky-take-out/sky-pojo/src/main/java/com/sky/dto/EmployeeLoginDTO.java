package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * 员工登录数据传输对象（DTO）
 * 用于接收前端传递的员工登录信息（用户名和密码）
 * 实现Serializable接口支持对象在网络中的序列化传输
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
// Swagger的@ApiModel注解：用于描述该DTO的作用，生成API文档时使用
@ApiModel(description = "员工登录时传递的数据模型")
public class EmployeeLoginDTO implements Serializable {
    // Swagger的@ApiModelProperty注解：描述该字段的含义，生成API文档时显示
    @ApiModelProperty("用户名")
    private String username; // 员工登录用户名

    @ApiModelProperty("密码")
    private String password; // 员工登录密码
}
