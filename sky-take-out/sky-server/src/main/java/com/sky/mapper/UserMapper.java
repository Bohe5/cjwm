package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;
/**
 *用户数据访问接口
 * 负责用户信息的管理，像用户注册、登录验证、用户信息修改等操作的数据持久化。
 */
@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);
    /**
     * 插入数据
     * @param user
     * @return
     */
    void insert(User user);
    @Select("select * from user where id = #{id}")
    User getById(Long userId);
    /**
     *用户统计
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
