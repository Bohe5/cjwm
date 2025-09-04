package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * 用户端分类控制器
 * 提供分类查询功能，支撑用户端菜品/套餐的分类展示（如首页分类导航、筛选功能）
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C端-分类接口")
public class CategoryController {
    // 注入分类服务层对象，委托分类查询的业务逻辑处理
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询分类")
    public Result<List<Category>> list(Integer type) {
        // 调用服务层方法，按类型筛选分类（Service层会处理“未传type时返回所有类型”或“仅返回启用状态分类”的逻辑）
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
