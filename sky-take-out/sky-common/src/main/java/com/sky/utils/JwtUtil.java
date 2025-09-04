package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import io.jsonwebtoken.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * JWT（JSON Web Token）工具类
 * 提供JWT令牌的生成（加密）和解析（解密）功能，基于HS256算法
 */
public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分。指定签名算法为HS256（HMAC-SHA256），对称加密算法，需服务端和客户端共享秘钥
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 计算令牌过期时间：当前时间 + 过期毫秒数
        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body，构建JWT令牌
        JwtBuilder builder = Jwts.builder()
                // 设置自定义负载信息（会包含在JWT的payload部分）
                // 注意：需先设置自定义声明，再设置标准声明（避免覆盖）
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);
        // 压缩为最终的JWT字符串（Header.Payload.Signature格式）
        return builder.compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // 解析JWT令牌
        // 得到DefaultJwtParser
        Claims claims = Jwts.parser()
                // 设置签名的秘钥（需与生成时一致，否则验证失败）
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt，并获取其负载部分（Body）
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
