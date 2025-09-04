package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 员工实体类
 * 用于存储系统中员工的基本信息、登录凭证及操作审计记录
 * 实现Serializable接口支持对象在网络传输或持久化过程中的序列化
 */
// Lombok注解组合：
// @Data：自动生成getter、setter、toString、equals、hashCode等方法
@Data
// @Builder：提供建造者模式，支持链式调用创建对象
@Builder
// @NoArgsConstructor：生成无参构造方法（反序列化和ORM框架要求）
@NoArgsConstructor
// @AllArgsConstructor：生成全参构造方法（配合Builder使用）
@AllArgsConstructor
public class Employee implements Serializable {
    // 序列化版本号，确保对象序列化后即使类结构微调仍能正确反序列化
    private static final long serialVersionUID = 1L;

    // 员工ID，唯一标识（数据库自增或分布式ID）
    private Long id;

    // 登录用户名，系统内唯一，用于员工登录认证
    private String username;

    // 员工姓名，用于显示和业务称呼
    private String name;

    // 登录密码，存储时需加密（如BCrypt哈希），不存储明文
    private String password;

    // 联系电话，用于工作沟通和账号安全验证
    private String phone;

    // 性别，如"男"、"女"（可使用枚举约束取值范围）
    private String sex;

    // 身份证号，用于员工身份核实（敏感信息，需加密存储）
    private String idNumber;

    // 员工状态：1-正常，0-禁用（控制账号是否可登录系统）
    private Integer status;
    // 记录创建时间，用于数据审计和追踪
    //方法一：注释
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    // 记录最后更新时间，用于数据变更追踪
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    // 创建人ID，关联操作用户（通常为管理员ID）
    private Long createUser;
    // 最后更新人ID，记录最后修改该员工信息的用户
    private Long updateUser;

}
