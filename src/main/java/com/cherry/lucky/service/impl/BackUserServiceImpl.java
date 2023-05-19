package com.cherry.lucky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cherry.lucky.constant.ErrorCodeConstants;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.entity.BackUser;
import com.cherry.lucky.mapper.BackUserMapper;
import com.cherry.lucky.model.dto.BackUserLoginDTO;
import com.cherry.lucky.model.dto.RedisUserInfo;
import com.cherry.lucky.service.BackUserService;
import com.cherry.lucky.utils.CherryCollectionUtil;
import com.cherry.lucky.utils.TokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName BackUserServiceImpl
 * @author cherry
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月22日 16:43:00
 */
@Slf4j
@Service
public class BackUserServiceImpl extends ServiceImpl<BackUserMapper, BackUser> implements BackUserService {

    private ObjectMapper objectMapper;

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public CherryResponseEntity<String> register(BackUserLoginDTO backUserLoginDTO) throws JsonProcessingException {
        Long count = this.baseMapper.selectCount(
                new QueryWrapper<BackUser>()
                        .lambda()
                        .eq(BackUser::getName, backUserLoginDTO.getName())
                        .eq(BackUser::getDel, false)
        );
        if(count > 0) {
            return CherryResponseEntity.fail(ErrorCodeConstants.USER_IS_EXIST, "此用户已存在");
        }
        BackUser backUser = new BackUser();
        BeanUtils.copyProperties(backUserLoginDTO, backUser);
        int insertResult = this.baseMapper.insert(backUser);
        if (insertResult <= 0) {
            return CherryResponseEntity.fail("注册失败!!!!");
        }
        String token = TokenUtils.getToken(backUserLoginDTO.getName(), String.valueOf(System.currentTimeMillis()));

        String userInfo = objectMapper.writeValueAsString(
                RedisUserInfo.builder()
                        .username(backUserLoginDTO.getName())
                        .openId(backUserLoginDTO.getName())
                        .gender(0)
                        .build()
        );

        redisTemplate.opsForValue().set(
                token,
                userInfo,
                24, TimeUnit.HOURS
        );
        return CherryResponseEntity.success(token);
    }

    @Override
    public CherryResponseEntity<String> login(BackUserLoginDTO backUserLoginDTO) throws JsonProcessingException {
        List<BackUser> backUsers = this.baseMapper.selectList(
                new QueryWrapper<BackUser>()
                        .lambda()
                        .eq(BackUser::getName, backUserLoginDTO.getName())
                        .eq(BackUser::getPassword, backUserLoginDTO.getPassword())
                        .eq(BackUser::getDel, false)
        );
        if (CherryCollectionUtil.listIsEmpty(backUsers)) {
            return CherryResponseEntity.fail(ErrorCodeConstants.USER_IS_NOT_EXIST,"没有此用户!!!!");
        }
        String token = TokenUtils.getToken(backUserLoginDTO.getName(), String.valueOf(System.currentTimeMillis()));

        String userInfo = objectMapper.writeValueAsString(
                RedisUserInfo.builder()
                        .username(backUserLoginDTO.getName())
                        .openId(backUserLoginDTO.getName())
                        .gender(0)
                        .build()
        );

        redisTemplate.opsForValue().set(
                token,
                userInfo,
                24, TimeUnit.HOURS
        );
        return CherryResponseEntity.success(token);
    }
}
