package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 员工登录返回视图对象（VO）
 * 用于封装员工登录成功后返回给前端的数据，包含身份信息和认证令牌
 */
// Lombok注解：自动生成getter、setter、toString、equals、hashCode等方法
@Data
// Lombok注解：提供建造者模式，支持链式调用创建对象
@Builder
// Lombok注解：生成无参构造方法（反序列化和框架要求）
@NoArgsConstructor
// Lombok注解：生成全参构造方法（配合Builder使用）
@AllArgsConstructor
// Swagger注解：描述该类的作用，用于API文档生成
@ApiModel(description = "员工登录返回的数据格式")
// 实现Serializable接口，支持对象序列化传输（如网络传输、缓存存储）
public class EmployeeLoginVO implements Serializable {
    // Swagger注解：描述字段含义，用于API文档展示
    @ApiModelProperty("主键值")
    private Long id; // 员工唯一标识ID，用于后续业务操作（如查询个人信息）

    @ApiModelProperty("用户名")
    private String userName; // 员工登录账号，用于系统内身份识别

    @ApiModelProperty("姓名")
    private String name; // 员工真实姓名，用于前端页面友好展示

    @ApiModelProperty("jwt令牌")
    private String token; // JWT认证令牌，前端需携带此令牌进行后续请求的身份验证
}
