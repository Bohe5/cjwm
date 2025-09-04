package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
// 为控制器指定别名，避免与其他店铺控制器（如用户端）冲突
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    // 定义Redis中存储店铺状态的Key（常量），避免硬编码，便于维护
    public static final String KEY = "SHOP_STATUS";
    // 注入RedisTemplate，用于操作Redis缓存（存储店铺营业状态）
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 设置店铺的营业状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺的营业状态:{}", status);
        // 将店铺状态存入Redis：Key为常量KEY，Value为传入的status
        // 采用String类型存储（opsForValue()），便于快速读写
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }
    /**
     * 获取店铺的营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus() {
        // 从Redis中读取店铺状态：根据Key获取Value，强转为Integer类型
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺的营业状态:{}", status==1 ? "营业中" : "打烊中");
        // 返回包含状态的成功响应，前端可根据状态值做不同逻辑（如显示“营业中”按钮/“打烊”提示）
        return Result.success(status);
    }
}
