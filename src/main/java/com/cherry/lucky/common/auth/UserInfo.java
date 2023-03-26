package com.cherry.lucky.common.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cherry.lucky.common.exception.CherryException;
import com.cherry.lucky.constant.ErrorCodeConstants;
import com.cherry.lucky.constant.TokenConstants;
import com.cherry.lucky.entity.User;
import com.cherry.lucky.model.dto.RedisUserInfo;
import com.cherry.lucky.service.UserService;
import com.cherry.lucky.utils.TokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName UserInfo
 * @Description
 * @createTime 2023年03月25日 15:08:00
 */
@Slf4j
@Component("UserInfo")
public class UserInfo {

    private RedisTemplate<String, String> redisTemplate;

    private ObjectMapper objectMapper;

    private UserService userServiceImpl;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setUserServiceImpl(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    public RedisUserInfo getUserInfoByRedis(String token) throws JsonProcessingException {
        String userInfo = redisTemplate.opsForValue().get(token);
        if(userInfo == null || StringUtils.isEmpty(userInfo)) {
            if (StringUtils.isBlank(token)) {
                throw new CherryException(ErrorCodeConstants.TOKEN_IS_NOT_EXISTENT, "token 不存在 !!!");
            } else {
                String openId = TokenUtils.parseTokenToOpenId(token);
                if (StringUtils.isBlank(openId)) {
                    throw new CherryException(ErrorCodeConstants.TOKEN_IS_ERROR, "token 有误 !!!");
                }
                User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("OPEN_ID", openId));
                if(user == null) {
                    throw new CherryException(ErrorCodeConstants.TOKEN_IS_ERROR, "token 有误 !!!");
                }
                // token 续期，不生成新token 只对当前token延时，对于使用旧token重复提交已做拦截处理
                String newUserInfo = objectMapper.writeValueAsString(
                        RedisUserInfo.builder()
                                .username(user.getName())
                                .openId(user.getOpenId())
                                .gender(user.getGender())
                                .build()
                );
                redisTemplate.opsForValue().set(
                        token,
                        newUserInfo,
                        24 * 2, TimeUnit.HOURS
                );
            }

        }

        RedisUserInfo redisUserInfo = objectMapper.readValue(userInfo, new TypeReference<>() {
        });
        if(redisUserInfo == null) {
            log.info("redis get userinfo by token is null, token: {}", token);
            throw new CherryException(
                    ErrorCodeConstants.REDIS_GET_USERINFO_BY_TOKEN_IS_NULL,
                    String.format("redis get userinfo by token is null, token: %s", token)
            );
        }
        return redisUserInfo;
    }


    public String getOpenIdByRedis(String token) throws JsonProcessingException {
        RedisUserInfo userInfoByRedis = getUserInfoByRedis(token);
        String openId = userInfoByRedis.getOpenId();
        if(StringUtils.isEmpty(openId)) {
            log.info("redis get userinfo`openId by token is null, token: {}", token);
            openId = TokenConstants.DEFAULT_OPEN_ID;
        }
        return openId;
    }

}
