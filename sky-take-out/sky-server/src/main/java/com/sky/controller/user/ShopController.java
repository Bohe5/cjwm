package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
/**
 * 店铺营业状态控制器（通用/用户端）
 * 提供店铺当前营业状态的查询功能，基于Redis存储状态数据，支撑前端业务逻辑判断
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    // 定义Redis中存储店铺状态的Key（常量），统一Key管理，避免硬编码错误
    public static final String KEY = "SHOP_STATUS";
    // 注入RedisTemplate，用于从Redis中读取店铺营业状态（Redis存储实现高频读取性能优化）
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取店铺的营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus() {
        // 从Redis中读取状态：通过固定Key获取Value，强转为Integer类型（状态码）
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺的营业状态:{}", status==1 ? "营业中" : "打烊中");
        // 返回状态结果：前端可根据状态码做业务逻辑处理（如营业中显示下单入口，打烊中提示）
        return Result.success(status);
    }
}
