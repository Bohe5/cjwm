package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 地址簿服务实现类
 * 实现AddressBookService接口，处理地址相关的业务逻辑
 */
// 标记为Spring服务组件，交由Spring容器管理
@Service
// 引入日志功能，通过log对象输出日志
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {
    // 自动注入地址簿数据访问层对象，用于数据库操作
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 条件查询
     *
     * @param addressBook
     * @return
     */
    public List<AddressBook> list(AddressBook addressBook) {
    // 调用数据访问层的list方法执行查询，返回满足条件的地址列表
    // 实际查询逻辑由MyBatis映射文件或注解定义，根据addressBook中的非空字段构建动态查询条件
        return addressBookMapper.list(addressBook);
    }

    /**
     * 新增地址
     *
     * @param addressBook
     */
    public void save(AddressBook addressBook) {
        // 设置地址所属用户ID（从上下文获取当前登录用户ID）
        addressBook.setUserId(BaseContext.getCurrentId());
        // 新增地址默认为非默认状态（0表示非默认，1表示默认）
        addressBook.setIsDefault(0);
        // 调用Mapper层插入数据
        addressBookMapper.insert(addressBook);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public AddressBook getById(Long id) {
        // 调用Mapper层根据ID查询
        AddressBook addressBook = addressBookMapper.getById(id);
        return addressBook;
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     */
    public void update(AddressBook addressBook) {
        // 调用数据访问层的update方法执行更新操作
        // 实际更新逻辑由MyBatis映射文件或注解定义，根据ID定位记录并更新非空字段
        addressBookMapper.update(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     */
    // 标记为事务方法，保证操作的原子性
    @Transactional
    public void setDefault(AddressBook addressBook) {
        //1、将当前用户的所有地址修改为非默认地址 update address_book set is_default = ? where user_id = ?
        // 新增地址默认为非默认状态（0表示非默认，1表示默认）
        addressBook.setIsDefault(0);
        // 设置地址所属用户ID（从上下文获取当前登录用户ID）
        addressBook.setUserId(BaseContext.getCurrentId());
        // 调用Mapper层修改数据
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        //2、将当前地址改为默认地址 update address_book set is_default = ? where id = ?
        addressBook.setIsDefault(1); // 设置状态为默认（1-默认）
        addressBookMapper.update(addressBook);// 根据ID更新该地址的状态
    }

    /**
     * 根据id删除地址
     *
     * @param id
     */
    public void deleteById(Long id) {
        // 调用数据访问层的deleteById方法执行删除操作
        // 通过地址ID定位并删除对应的数据库记录
        addressBookMapper.deleteById(id);
    }

}
