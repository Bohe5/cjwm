package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
/**
 *  用户业务实现
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    // 微信登录接口地址常量
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    // 微信配置属性（注入）
    @Autowired
    private  WeChatProperties weChatProperties;
    // 用户数据访问接口（注入）
    @Autowired
    private  UserMapper userMapper;
    // 构造方法，用于依赖注入
    public UserServiceImpl(WeChatProperties weChatProperties, UserMapper userMapper) {
        this.weChatProperties = weChatProperties;
        this.userMapper = userMapper;
    }

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 1. 通过登录凭证code获取微信用户的openid
        String openid = getOpenid(userLoginDTO.getCode());
        // 2. 如果获取openid失败，抛出登录失败异常
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 3. 根据openid查询数据库，判断用户是否已存在
        User user = userMapper.getByOpenid(openid);
        // 4. 如果用户不存在，则创建新用户并保存到数据库
        if(user==null){
            user = User.builder()
                    .openid(openid)// 设置微信唯一标识
                    .createTime(LocalDateTime.now()) // 设置创建时间为当前时间
                    .build();
            userMapper.insert(user);// 插入新用户记录
        }
        // 5. 返回用户信息（已存在的用户或新创建的用户）
        return user;
    }
    /**
     * 调用微信接口服务，获取用户openid
     * @param code
     * @return
     */
    private String getOpenid(String code){
        // 1. 构建请求参数
        Map<String,String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());// 微信小程序appid
        map.put("secret",weChatProperties.getSecret()); // 微信小程序密钥
        map.put("grant_type","authorization_code");// 固定值
        map.put("js_code",code); // 前端传来的code
        // 2. 发送GET请求调用微信接口
        String json = HttpClientUtil.doGet(WX_LOGIN,map);
        // 3. 解析返回的JSON数据
        JSONObject jsonObject = JSONObject.parseObject(json);
        // 4. 提取并返回openid（微信用户唯一标识）
        String openid = jsonObject.getString("openid");
        return openid;
    }


}
