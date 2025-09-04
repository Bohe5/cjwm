package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    // 自动注入依赖注入，自动套餐数据访问接口
    @Autowired
    private SetmealMapper setmealMapper;
    // 自动注入套餐菜品关联关系数据访问接口
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    // 自动注入入菜品数据访问接口
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    @Transactional// 声明事务，确保套餐和关联关系要么同时保存，要么都不保存
    public void saveWithDish(SetmealDTO setmealDTO) {
        // 创建套餐实体对象
        Setmeal setmeal = new Setmeal();
        // 将DTO中的属性复制到实体对象中（属性名需一致）
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //向套餐表插入数据
        setmealMapper.insert(setmeal);

        //获取生成的套餐id
        Long setmealId = setmeal.getId();
        // 获取DTO中的套餐菜品关联列表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 为每个关联对象设置套餐id（建立关联关系）
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        //保存套餐和菜品的关联关系
        setmealDishMapper.insertBatch(setmealDishes);
    }
    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 获取页码和每页条数
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        // 使用PageHelper分页插件设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        // 执行分页查询，返回分页对象
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        // 封装并返回分页结果（总记录数和当前页数据列表）
        return new PageResult(page.getTotal(), page.getResult());
    }
    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional// 声明事务，确保套餐和关联关系要么同时删除，要么都不删除
    public void deleteBatch(List<Long> ids) {
        // 遍历每个要删除的套餐id
        ids.forEach(id -> {
            // 根据id查询套餐信息
            Setmeal setmeal = setmealMapper.getById(id);
            // 如果套餐处于起售状态（1），则不允许删除
            if(StatusConstant.ENABLE == setmeal.getStatus()){
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        // 遍历删除每个套餐
        ids.forEach(setmealId -> {
            //删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            //删除套餐菜品关系表中的数据
            setmealDishMapper.deleteBySetmealId(setmealId);
        });
    }
    /**
     * 根据id查询套餐和套餐菜品关系
     *
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {
        // 根据id查询套餐基本信息
        Setmeal setmeal = setmealMapper.getById(id);
        // 根据套餐id查询关联的菜品列表
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        // 创建套餐VO对象（用于前端展示）
        SetmealVO setmealVO = new SetmealVO();
        // 复制套餐基本属性到VO
        BeanUtils.copyProperties(setmeal, setmealVO);
        // 设置VO中的关联菜品列表
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    @Transactional// 声明事务，确保所有更新操作要么同时成功，要么同时失败
    public void update(SetmealDTO setmealDTO) {
        // 创建套餐实体对象
        Setmeal setmeal = new Setmeal();
        // 复制DTO属性到实体
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //1、修改套餐表，执行update
        setmealMapper.update(setmeal);

        //套餐id
        Long setmealId = setmealDTO.getId();

        //2、删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
        setmealDishMapper.deleteBySetmealId(setmealId);
        // 获取新的套餐菜品关联列表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 为每个关联对象设置套餐id
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        //3、重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert
        setmealDishMapper.insertBatch(setmealDishes);
    }
    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        if(status == StatusConstant.ENABLE){
            // 查询该套餐包含的所有菜品
            //select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = ?
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            // 检查是否有停售的菜品
            if(dishList != null && dishList.size() > 0){
                dishList.forEach(dish -> {
                    // 若有菜品处于停售状态（0），则抛出异常阻止起售
                    if(StatusConstant.DISABLE == dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        // 构建要更新的套餐对象（仅包含id和状态）
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        // 执行更新操作
        setmealMapper.update(setmeal);
    }
    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        // 调用mapper执行条件查询
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        // 调用mapper查询套餐包含的菜品选项
        return setmealMapper.getDishItemBySetmealId(id);
    }

}
