package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * Sky项目的Spring Boot启动类：负责初始化Spring容器、扫描组件、启用项目核心功能，是项目入口
 */
// 核心注解：整合3个关键注解，简化启动类配置
// 1. @SpringBootConfiguration：标记当前类为配置类，允许使用@Bean定义Bean
// 2. @EnableAutoConfiguration：开启Spring Boot自动配置（根据依赖jar包自动配置Bean，如数据源、Web容器等）
// 3. @ComponentScan：默认扫描当前类所在包及其子包下的@Component、@Service、@Controller等组件
@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j// Lombok注解：自动生成log日志对象（无需手动new Logger），用于打印项目日志
@EnableCaching// 启用Spring缓存功能：使@Cacheable、@CachePut、@CacheEvict等缓存注解生效，提升查询性能
@EnableScheduling// 启用Spring定时任务功能：使@Scheduled注解生效，支持 cron 表达式、固定延迟等定时任务
public class SkyApplication {
    public static void main(String[] args) {
        // 启动Spring Boot应用
        SpringApplication.run(SkyApplication.class, args);
        // 启动成功后打印日志
        log.info("server started");
    }
}
