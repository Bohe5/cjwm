/**
 * 控制器，功能模块的初始调用程序
 * 处理管理员端
 */
package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 分类管理
 * 处理管理员端关于商品分类的各种操作请求
 */
// 标识为REST风格控制器，自动返回JSON格式数据
@RestController
// 基础请求路径，所有接口都以该路径开头
@RequestMapping("/admin/category")
// Swagger文档注解，标识该类的接口标签
@Api(tags = "分类相关接口")
// 提供日志功能
@Slf4j
public class CategoryController {
    // 注入分类服务层对象，用于处理业务逻辑
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping// 处理POST请求，完整路径为/admin/category
    @ApiOperation("新增分类")// Swagger文档注解，描述接口功能
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类：{}", categoryDTO);// 记录日志
        categoryService.save(categoryDTO);// 调用服务层方法执行新增操作
        return Result.success();// 返回成功响应
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")// 处理GET请求，完整路径为/admin/category/page
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);// 调用分页查询方法
        return Result.success(pageResult);// 返回包含分页结果的响应
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping// 处理DELETE请求，完整路径为/admin/category
    @ApiOperation("删除分类")
    public Result<String> deleteById(Long id){
        log.info("删除分类：{}", id);
        categoryService.deleteById(id);// 调用服务层删除方法
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);// 调用服务层更新方法
        return Result.success();
    }

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")// 处理POST请求，路径中的{status}为动态参数
    @ApiOperation("启用禁用分类")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        categoryService.startOrStop(status,id);// 调用服务层状态修改方法
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type); // 调用服务层查询方法
        return Result.success(list);// 返回包含分类列表的响应
    }
}
