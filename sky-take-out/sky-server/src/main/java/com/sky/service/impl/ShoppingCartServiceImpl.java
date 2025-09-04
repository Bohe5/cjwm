package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
/**
 * 购物车业务实现
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    // 自动注入购物车数据访问接口
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    // 自动注入菜品数据访问接口
    @Autowired
    private DishMapper dishMapper;
    // 自动注入套餐数据访问接口
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 创建购物车实体对象
        ShoppingCart shoppingCart = new ShoppingCart();
        // 将DTO中的属性复制到实体对象
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        // 获取当前登录用户ID（从ThreadLocal中获取）
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        // 根据用户ID和商品ID查询购物车中是否已有该商品
        List<ShoppingCart> list=shoppingCartMapper.list(shoppingCart);
        // 如果购物车中已有该商品
        if(list !=null && list.size()>0){
            ShoppingCart cart = list.get(0);
            // 将商品数量加1
            cart.setNumber(cart.getNumber()+1);
            // 更新购物车中该商品的数量
            shoppingCartMapper.updateNumberById(cart);
        }else {
            // 购物车中没有该商品，需要新增记录
            // 判断是菜品还是套餐
            Long dishId = shoppingCart.getDishId();
            if(dishId != null){
                // 如果是菜品，查询菜品详情
                Dish dish = dishMapper.getById(dishId);
                // 设置商品名称、图片和单价
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else {
                // 如果是套餐，查询套餐详情
                Long setmealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                // 设置商品名称、图片和单价
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            // 设置初始数量为1
            shoppingCart.setNumber(1);
            // 设置创建时间为当前时间
            shoppingCart.setCreateTime(LocalDateTime.now());
            // 将新商品添加到购物车
            shoppingCartMapper.insert(shoppingCart);
        }
    }
    /**
     * 查看购物车
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        // 获取当前登录用户ID
        Long userId = BaseContext.getCurrentId();
        // 构建查询条件（仅查询当前用户的购物车）
        ShoppingCart shoppingCart = ShoppingCart.builder()
                        .userId(userId)
                        .build();
        // 查询并返回当前用户的购物车列表
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     * @return
     */
    public void cleanShoppingCart() {
        // 获取当前登录用户ID
        Long userId = BaseContext.getCurrentId();
        // 根据用户ID删除购物车中所有商品
        shoppingCartMapper.deleteByUserId(userId);
    }
    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 创建购物车实体对象
        ShoppingCart shoppingCart = new ShoppingCart();
        // 复制DTO属性到实体
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //设置查询条件，查询当前登录用户的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 查询购物车中该用户的对应商品
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        // 如果存在该商品
        if(list != null && list.size() > 0){
            shoppingCart = list.get(0);
            // 获取当前商品数量
            Integer number = shoppingCart.getNumber();
            if(number == 1){
                //当前商品在购物车中的份数为1，直接删除当前记录
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else {
                //当前商品在购物车中的份数不为1，修改份数即可
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }
}
