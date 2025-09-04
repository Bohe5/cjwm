package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
/**
 * JWT（JSON Web Token）相关配置属性类
 * 用于封装生成和验证JWT令牌的配置信息，区分管理端和用户端的不同配置
 */
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    // 管理端JWT令牌加密的密钥
    private String adminSecretKey;
    // 管理端JWT令牌的有效期（单位：毫秒）
    private long adminTtl;
    // 管理端在请求头中携带JWT令牌的参数名称
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    // 用户端JWT令牌加密的密钥
    private String userSecretKey;
    // 用户端JWT令牌的有效期（单位：毫秒）
    private long userTtl;
    // 用户端在请求头中携带JWT令牌的参数名称
    private String userTokenName;

}
