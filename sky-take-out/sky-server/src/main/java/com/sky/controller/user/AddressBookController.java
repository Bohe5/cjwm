/**
 * 控制器，功能模块的初始调用程序
 * 处理用户端（微信小程序）
 */
package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 用户地址簿控制器
 * 处理当前登录用户的地址管理需求，包括地址的增删改查、默认地址设置与查询
 */
@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端地址簿接口")
public class AddressBookController {
    // 注入地址簿服务层对象，委托地址相关业务逻辑处理
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有地址信息
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        // 1. 构建查询条件：指定当前登录用户ID（从ThreadLocal中获取，避免前端传递）
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId()); // BaseContext：存储当前登录用户ID的工具类
        // 2. 调用服务层查询方法，按用户ID筛选地址
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook); //调用服务层方法
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id); //调用服务层方法
        return Result.success(addressBook);
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook); //调用服务层方法
        return Result.success();
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook); //调用服务层方法
        return Result.success();
    }

    /**
     * 根据id删除地址
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id); //调用服务层方法
        return Result.success();
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        // 1. 构建查询条件：当前用户ID + 地址类型为“默认”（is_default=1）
        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1); // 1=默认地址，0=非默认地址（数据库字段约定）
        addressBook.setUserId(BaseContext.getCurrentId());
        // 2. 查询符合条件的地址（理论上最多1条，因设置默认时已确保唯一性）
        List<AddressBook> list = addressBookService.list(addressBook);
        // 3. 处理查询结果：有默认地址则返回，无则提示错误
        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认地址");
    }

}
