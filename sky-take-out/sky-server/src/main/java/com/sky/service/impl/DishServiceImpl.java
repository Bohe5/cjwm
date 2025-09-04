package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
/**
 *菜品业务层
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    // 注入菜品数据访问对象，用于操作菜品表
    @Autowired
    private DishMapper dishMapper;
    // 注入菜品口味数据访问对象，用于操作菜品口味表
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    // 注入套餐菜品关联表数据访问对象，用于操作套餐与菜品的关联关系
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    // 注入套餐数据访问对象，用于操作套餐表
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @Transactional // 声明事务，确保菜品和口味信息同时保存成功或失败
    public void saveWithFlavor(DishDTO dishDTO){
        // 创建菜品实体对象
        Dish dish=new Dish();
        // 将DTO中的属性拷贝到实体对象
        BeanUtils.copyProperties(dishDTO,dish);
        // 保存菜品基本信息到数据库
        dishMapper.insert(dish);
        // 获取刚插入的菜品ID（由数据库自动生成的主键）
        Long dishId=dish.getId();//获取insert语句生成的主键值
        // 获取DTO中的口味列表
        List<DishFlavor> flavors= dishDTO.getFlavors();
        // 判断口味列表是否不为空
        if(flavors!=null && flavors.size()>0){
            // 为每个口味设置对应的菜品ID
            flavors.forEach(dishflavor->{
                dishflavor.setDishId(dishId);
            });
            // 批量保存菜品口味信息
            dishFlavorMapper.insertBatch(flavors);
        }
    }
    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO){
        // 开启分页插件，设置当前页码和每页显示条数
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        // 执行分页查询，返回分页对象
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        // 封装分页结果并返回
        return new PageResult(page.getTotal(),page.getResult());
    }
    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @Transactional// 声明事务，确保批量删除操作的原子性
    public void delectBatch(List<Long> ids){
        // 遍历所有要删除的菜品ID
        for(Long id:ids){
            // 查询菜品信息
            Dish dish=dishMapper.getById(id);
            // 判断菜品是否处于启售状态
            if(dish.getStatus() == StatusConstant.ENABLE){
                // 若启售中，抛出异常阻止删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 查询这些菜品是否关联了套餐
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        // 若有关联的套餐，抛出异常阻止删除
        if(setmealIds!=null && setmealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        /*方法一：批量删除
        for(Long id:ids){
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        }*/
        //方法二：（更优）
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }
    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id){
        // 查询菜品基本信息
        Dish dish=dishMapper.getById(id);
        // 查询该菜品对应的口味列表
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 创建VO对象用于封装返回结果
        DishVO dishVO=new DishVO();
        // 拷贝菜品基本信息到VO
        BeanUtils.copyProperties(dish,dishVO);
        // 设置口味列表
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }
    /**
     * 修改菜品
     * @param dishDTO
     *
     */

    public void updateWithFlavor(DishDTO dishDTO) {
        // 创建菜品实体对象
        Dish dish=new Dish();
        // 拷贝DTO中的属性到实体
        BeanUtils.copyProperties(dishDTO,dish);
        // 更新菜品基本信息
        dishMapper.update(dish);
        // 先删除原有的口味信息
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        // 获取新的口味列表
        List<DishFlavor> flavors= dishDTO.getFlavors();
        // 若口味列表不为空
        if(flavors!=null && flavors.size()>0){
            // 为每个口味设置菜品ID
            flavors.forEach(dishflavor->{
                dishflavor.setDishId(dishDTO.getId());
            });
            // 批量保存新的口味信息
            dishFlavorMapper.insertBatch(flavors);
        }
    }
    /**
     * 菜品起售停售
     *
     * @param status
     * @param id
     */
    @Transactional // 声明事务，确保状态更新的一致性
    public void startOrStop(Integer status, Long id) {
        // 构建菜品对象，仅设置ID和状态
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        // 更新菜品状态
        dishMapper.update(dish);
        // 若为停售操作
        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // 查询包含该菜品的所有套餐ID
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            // 若存在关联的套餐
            if (setmealIds != null && setmealIds.size() > 0) {
                // 遍历所有关联的套餐，将其状态改为停售
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        // 构建查询条件：指定分类ID且状态为启售
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        // 执行查询并返回结果
        return dishMapper.list(dish);
    }
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        // 根据条件查询菜品列表
        List<Dish> dishList = dishMapper.list(dish);
        // 创建VO列表用于封装结果
        List<DishVO> dishVOList = new ArrayList<>();
        // 遍历菜品列表
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            // 拷贝菜品基本信息到VO
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());
            // 设置口味列表
            dishVO.setFlavors(flavors);
            // 添加到VO列表
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }


}
