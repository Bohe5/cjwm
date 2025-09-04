package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 数据概览查询DTO
 * 用于接收前端传递的数据统计时间范围参数
 * 实现Serializable接口支持对象在网络中传输或持久化
 */
// Lombok注解：@Data生成getter、setter、toString等方法；
@Data
// @Builder提供建造者模式支持，方便对象创建；
@Builder
// @NoArgsConstructor生成无参构造方法；
@NoArgsConstructor
// @AllArgsConstructor生成全参构造方法
@AllArgsConstructor
public class DataOverViewQueryDTO implements Serializable {
    // 查询开始时间
    private LocalDateTime begin;

    // 查询结束时间
    private LocalDateTime end;
}
