package com.cherry.lucky.utils;


import java.util.Date;

import com.cherry.lucky.common.exception.CherryException;
import com.cherry.lucky.constant.ErrorCodeConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName TokenUtils
 * @Description
 * @createTime 2023年03月22日 16:02:00
 */
@Slf4j
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
        return jwtBuilder.compact();
    }

    public static String parseTokenToOpenId(String token) {
        Claims claims = getClaims(token);
        if(claims == null) {
            throw new CherryException(ErrorCodeConstants.CLAIMS_IS_NULL_ERROR, "parse token error claims is null !!!");
        }
        return claims.getId();
    }

    public static String parseTokenToUserName(String token) {
        Claims claims = getClaims(token);
        if(claims == null) {
            throw new CherryException(ErrorCodeConstants.CLAIMS_IS_NULL_ERROR, "parse token error claims is null !!!");
        }
        return claims.getSubject();
    }

    private static Claims getClaims(String token) {
        if (StringUtils.isEmpty(token)) {
            log.info("get claims error token is null !!!! ");
            return null;
        }
        return Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}