package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 用户登录返回视图对象（VO）
 * 专门用于封装用户（通常是移动端用户，如微信小程序用户）登录成功后返回给前端的数据
 * 包含用户身份标识和认证令牌，支撑后续业务交互
 */
// Lombok注解：自动生成getter、setter、toString、equals、hashCode方法，简化模板代码
@Data
// Lombok注解：提供建造者模式，支持链式调用创建对象（如 UserLoginVO.builder().id(1L).build()）
@Builder
// Lombok注解：生成无参构造方法，满足反序列化、框架依赖注入等场景需求
@NoArgsConstructor
// Lombok注解：生成全参构造方法，便于批量赋值或配合建造者模式使用
@AllArgsConstructor
// 实现Serializable接口：支持对象序列化，确保可在网络传输（如前后端交互）、缓存存储中正常处理
public class UserLoginVO implements Serializable {
    // 用户唯一标识ID（系统内部主键）
    // 用于后端定位具体用户，支撑个人信息查询、订单关联、权限校验等后续业务操作
    private Long id;

    // 用户第三方平台标识（如微信openid）
    // 通常用于移动端第三方登录场景（如微信小程序登录），关联用户在第三方平台的唯一身份
    // 避免用户重复注册，同时作为第三方登录的核心身份凭证
    private String openid;

    // 身份认证令牌（如JWT令牌）
    // 前端登录成功后需存储该令牌，后续发起请求时携带在请求头中
    // 后端通过令牌验证用户身份合法性，确保接口访问安全
    private String token;
}
