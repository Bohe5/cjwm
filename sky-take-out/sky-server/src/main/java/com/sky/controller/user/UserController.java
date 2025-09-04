package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
/**
 * 用户端用户控制器
 * 处理用户微信登录相关操作，实现微信小程序授权登录功能
 */
@RestController
@RequestMapping("/user/user")
@Api(tags = "c端用户相关接口")
@Slf4j
public class UserController {
    // 注入用户服务层，处理登录业务逻辑
    @Autowired
    private UserService userService;
    // 注入JWT配置属性，包含密钥和有效期
    @Autowired
    private JwtProperties jwtProperties;
    // 注入用户数据访问层（注：实际应通过Service调用，此处可能为冗余依赖）
    @Autowired
    private UserMapper userMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("微信用户登录：{}", userLoginDTO);
        // 1. 调用服务层处理微信登录逻辑
        // 核心流程：通过code调用微信接口获取openid → 判断用户是否已注册 → 未注册则自动创建用户 → 返回用户信息
        User user = userService.wxLogin(userLoginDTO);
        // 2. 生成JWT令牌
        // 构建令牌声明（claims），存储用户ID（用于后续身份识别）
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        // 使用JWT工具类生成令牌，参数：密钥、有效期、声明信息
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);
        // 3. 构建登录响应VO（视图对象）
        UserLoginVO userLoginVO=UserLoginVO.builder()
                .id(user.getId())// 用户ID（系统内部唯一标识）
                .openid(user.getOpenid()) // 微信用户唯一标识（用于后续微信相关操作）
                .token(token)// JWT令牌（用于后续接口身份验证）
                .build();
        // 4. 返回登录结果
        return Result.success(userLoginVO);
    }
}
