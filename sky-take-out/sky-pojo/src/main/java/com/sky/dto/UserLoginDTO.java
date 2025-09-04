package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
// Lombok的@Data注解：自动生成getter、setter、toString等方法
@Data
public class UserLoginDTO implements Serializable {
    // 验证码，用户登录时输入的短信验证码或图形验证码
    // 后端会校验该验证码的有效性（是否存在、是否过期、是否与发送的一致）
    private String code;

}
