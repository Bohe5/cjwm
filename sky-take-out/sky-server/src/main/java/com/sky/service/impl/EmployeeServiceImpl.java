package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
/**
 *员工业务层
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    // 自动注入员工数据访问对象（Mapper），用于与数据库交互操作员工表
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        // 从登录DTO中提取用户名和密码
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());//对前端传的密码进行md5加密处理
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {
        // 打印当前处理线程ID（用于调试多线程场景，如分布式环境下的线程上下文问题）
        System.out.println("当前线程的id："+ Thread.currentThread().getId());
        // 创建员工实体对象（与数据库表映射）
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);//对象属性拷贝
        employee.setStatus(StatusConstant.DISABLE);//设置账号状态
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));//设置密码
//        //设置当前记录的创建时间和修改时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        //设置当前记录的创建id和修改id
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        // 调用Mapper将新增员工数据插入数据库
        employeeMapper.insert(employee);
    }
    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 1、开启MyBatis分页插件，设置当前页码和每页显示条数
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        // 2、执行分页查询：MyBatis会自动为后续SQL添加LIMIT语句，返回分页对象Page
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        // 3、从分页对象中提取总记录数和当前页数据
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        // 4、封装为通用分页结果对象返回
        return new PageResult(total,records);
    }

    /**
     * 启用、禁用
     * @param status
     * @param id
     * @return
     */
    public void startOrStop(Integer status, Long id) {
        /*写法一：
        Employee employee=new Employee();
        employee.setStatus(status);// 设置目标状态
        employee.setId(id);// 设置员工ID（用于定位数据库记录）
        */
        // 写法二：使用建造者模式（Builder）构建对象，代码更简洁
        Employee employee=Employee.builder()
                .status(status) // 链式设置状态
                .id(id)         // 链式设置员工ID
                .build();       // 构建员工实体
        // 调用Mapper更新员工状态（仅更新status和id对应的记录）
        employeeMapper.update(employee);
    }
    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    public Employee getById(Long id) {
        // 调用Mapper根据ID查询员工完整信息
        Employee employee=employeeMapper.getById(id);
        // 密码脱敏：将数据库中的加密密码替换为"****"，避免前端泄露敏感信息
        employee.setPassword("****");
        return employee;
    }
    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    public void update(EmployeeDTO employeeDTO) {
        // 创建员工实体对象
        Employee employee=new Employee();
        // 将DTO中的更新信息拷贝到实体对象（ID会一并拷贝，用于定位待更新记录）
        BeanUtils.copyProperties(employeeDTO,employee);
//        employee.setUpdateTime(LocalDateTime.now());// 更新修改时间为当前时间
//        employee.setUpdateUser(BaseContext.getCurrentId());// 更新修改人为当前登录管理员ID
        // 调用Mapper执行更新操作（根据ID更新员工信息）
        employeeMapper.update(employee);
    }
}
