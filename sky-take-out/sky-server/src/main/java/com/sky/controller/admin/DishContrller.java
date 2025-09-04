package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关管理")
@Slf4j
public class DishContrller {
    // 注入菜品服务层对象，委托业务逻辑处理
    @Autowired
    private DishService dishService;
    // 注入RedisTemplate，用于操作Redis缓存
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}",dishDTO);
        // 调用服务层方法，保存菜品及关联口味（事务性操作）
        dishService.saveWithFlavor(dishDTO);
        //清除缓存数据，缓存key格式：dish_分类ID
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }
    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        // 调用服务层分页查询方法，返回分页结果
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除：{}",ids);
        // 调用服务层批量删除方法（含关联数据清理，如口味、套餐关联）
        dishService.delectBatch(ids);
        // 缓存清理：批量删除可能涉及多个分类的菜品，直接清除所有dish前缀的缓存
        cleanCache("dish_*");
        return Result.success();
    }
    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品:{}",id);
        // 调用服务层方法，查询菜品及关联口味
        DishVO dishVO=dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }
    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}",dishDTO);
        // 调用服务层方法，更新菜品及关联口味（先删旧口味，再插新口味）
        dishService.updateWithFlavor(dishDTO);
        // 缓存清理：修改菜品可能影响原分类/新分类的列表，直接清除所有dish前缀缓存
        cleanCache("dish_*");
        return Result.success();
    }
    /**
     * 菜品起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status,Long id){
        // 调用服务层方法，更新菜品状态
        dishService.startOrStop(status,id);
        // 缓存清理：状态变更会影响分类下的菜品列表，清除所有dish前缀缓存
        cleanCache("dish_*");
        return Result.success();
    }
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        // 调用服务层方法，查询指定分类下的菜品
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }
    /**
     * //清除缓存数据
     * @param pattern
     * @return
     */
    private void cleanCache(String pattern) {
        // 根据匹配模式查询所有符合条件的缓存key
        Set keys = redisTemplate.keys(pattern);
        // 批量删除匹配的缓存key
        redisTemplate.delete(keys);
    }
}
