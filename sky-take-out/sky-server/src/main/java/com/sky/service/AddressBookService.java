/**
 * 服务接口
 * 根据不同的功能模块，对应不同的服务接口
 */
package com.sky.service;

import com.sky.entity.AddressBook;
import java.util.List;
/**
 * 定义地址簿服务接口
 * 声明地址簿相关的业务方法
 */
public interface AddressBookService {
    // 根据地址簿对象条件查询地址簿列表
    List<AddressBook> list(AddressBook addressBook);
    // 保存地址簿信息
    void save(AddressBook addressBook);
    // 根据ID查询地址簿详情
    AddressBook getById(Long id);
    // 更新地址簿信息
    void update(AddressBook addressBook);
    // 设置默认地址簿
    void setDefault(AddressBook addressBook);
    // 根据ID删除地址簿
    void deleteById(Long id);
}
