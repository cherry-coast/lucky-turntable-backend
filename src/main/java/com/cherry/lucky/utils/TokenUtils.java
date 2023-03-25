package com.cherry.lucky.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName TokenUtils
 * @Description
 * @createTime 2023年03月22日 16:02:00
 */
public class TokenUtils {

    private final static String KEY = "cherry-lucky";


    public static String getToken(String name, String openId) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(openId)
                // 用户名
                .setSubject(name)
                // 登录时间
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, KEY)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000 * 2));
        System.out.println(jwtBuilder.compact());
        return jwtBuilder.compact();
    }

    public static String parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody();
        System.out.println("用户id:" + claims.getId());
        System.out.println("用户名:" + claims.getSubject());
        System.out.println("用户时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(claims.getIssuedAt()));
        System.out.println("过期时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(claims.getExpiration()));
        return claims.getId();
    }


}