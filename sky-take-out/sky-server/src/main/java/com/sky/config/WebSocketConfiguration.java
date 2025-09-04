package com.sky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置类，用于注册WebSocket的Bean
 */
// 标识当前类为Spring配置类
@Configuration
public class WebSocketConfiguration {
    /**
     * 创建ServerEndpointExporter Bean
     *
     * ServerEndpointExporter是Spring提供的WebSocket服务器端点导出器，
     * 它的作用是自动注册使用@ServerEndpoint注解声明的WebSocket端点
     *
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
