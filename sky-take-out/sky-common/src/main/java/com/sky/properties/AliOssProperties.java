package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS（对象存储服务）的配置属性类
 * 用于封装从配置文件中读取的OSS相关配置信息
 */
// 标识该类为Spring组件，使其能被Spring容器管理
@Component
// 指定配置文件中属性的前缀，用于绑定配置项
@ConfigurationProperties(prefix = "sky.alioss")
// Lombok的注解，自动生成getter、setter、toString等方法
@Data
public class AliOssProperties {

    // OSS服务的访问端点（Endpoint）
    private String endpoint;

    // 访问OSS的AccessKey ID，用于身份验证
    private String accessKeyId;

    // 访问OSS的AccessKey Secret，用于身份验证
    private String accessKeySecret;

    // OSS存储桶（Bucket）的名称，用于指定操作的存储容器
    private String bucketName;

}
