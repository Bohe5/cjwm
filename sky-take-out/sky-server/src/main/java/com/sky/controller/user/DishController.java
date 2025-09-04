package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * 用户端菜品浏览控制器
 * 处理用户端“根据分类查询菜品”的请求，集成Redis缓存优化高频查询性能，仅返回启用状态的菜品
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    // 注入菜品服务层对象，委托菜品查询及关联口味的业务逻辑
    @Autowired
    private DishService dishService;
    // 注入RedisTemplate，用于操作Redis缓存（存储分类对应的菜品列表，减少数据库查询）
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        // 1. 构建Redis缓存Key（格式：dish_分类ID，如dish_1）
        // 设计目的：按分类缓存菜品列表，确保不同分类的菜品缓存隔离
        String key = "dish_" + categoryId;
        // 2. 从Redis查询缓存：若命中，直接返回缓存数据，无需查数据库
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(list!=null && list.size()>0){
            return Result.success(list);
        }
        // 3. 缓存未命中：构建查询条件（分类ID+启用状态），查询数据库
        Dish dish = new Dish();
        dish.setCategoryId(categoryId); // 按分类ID筛选
        dish.setStatus(StatusConstant.ENABLE);// 仅查询“启用”状态的菜品（StatusConstant.ENABLE通常为1）
        // 调用服务层方法，查询菜品及关联的口味信息（DishVO包含菜品基本信息+flavors列表）
        list = dishService.listWithFlavor(dish);
        // 4. 将数据库查询结果存入Redis，后续查询可直接命中缓存
        redisTemplate.opsForValue().set(key,list);
        // 5. 返回查询结果
        return Result.success(list);
    }

}
