package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
// 标识当前类为配置类，Spring会自动扫描并加载
@Configuration
// 提供日志输出功能
@Slf4j
public class RedisConfiguration {
    /**
     * 创建 RedisTemplate bean
     * 配置并创建RedisTemplate实例，用于操作Redis
     *
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建redis模拟对象...");
        // 创建RedisTemplate实例
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置Redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置key的序列化器为StringRedisSerializer
        // 避免key在Redis中出现乱码问题
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 返回配置好的RedisTemplate实例
        return redisTemplate;
    }
}
