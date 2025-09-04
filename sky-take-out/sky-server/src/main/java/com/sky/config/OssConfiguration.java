package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 配置类，用于创建阿里云对象
 */
// 标识当前类为配置类，相当于传统的XML配置文件
@Configuration
// 提供日志输出功能
@Slf4j
public class OssConfiguration {
    /**
     * 创建阿里云OSS工具类对象并注册为Spring Bean
     * @ConditionalOnMissingBean 条件注解：当Spring容器中不存在AliOssUtil类型的Bean时，才会创建该Bean
     * 避免重复创建，提高配置灵活性
     */
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        // 输出日志，记录创建工具类对象的信息及配置属性
        log.info("开始创建阿里云文件上传工具类对象：{}", aliOssProperties);
        // 通过配置属性实例化AliOssUtil并返回
        return new AliOssUtil(aliOssProperties.getEndpoint(),// OSS服务端点
                aliOssProperties.getAccessKeyId(),// 访问密钥ID
                aliOssProperties.getAccessKeySecret(),// 访问密钥密钥
                aliOssProperties.getBucketName());// 存储空间名称
    }
}
