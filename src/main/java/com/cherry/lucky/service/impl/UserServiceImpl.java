package com.cherry.lucky.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.model.dto.RedisUserInfo;
import com.cherry.lucky.model.dto.SessionKeyOpenIdDTO;
import com.cherry.lucky.model.dto.WeChatUserInfoDTO;
import com.cherry.lucky.model.dto.WeChatUserInfoWebRequestDTO;
import com.cherry.lucky.utils.TokenUtils;
import com.cherry.lucky.utils.WeChatUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidParameterSpecException;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cherry.lucky.mapper.UserMapper;
import com.cherry.lucky.entity.User;
import com.cherry.lucky.service.UserService;
/**
 * @ClassName UserServiceImpl
 * @author cherry
 * @version 1.0.0
 * @Description 
 * @createTime 2023年03月22日 16:43:00
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

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
    public CherryResponseEntity<String> login(WeChatUserInfoWebRequestDTO weChatUserInfoWebRequestDTO) {
        try {
            SessionKeyOpenIdDTO sessionKeyOrOpenId = WeChatUtil.getSessionKeyOrOpenId(weChatUserInfoWebRequestDTO.getCode());

//            JSONObject rawDataJson = JSONUtil.parseObj(weChatUserInfoWebRequestDTO.getRawData());

            WeChatUserInfoDTO weChatUserInfoDTO = WeChatUtil.getUserInfo(
                    weChatUserInfoWebRequestDTO.getEncryptedData(),
                    sessionKeyOrOpenId.getSessionKey(),
                    weChatUserInfoWebRequestDTO.getIv()
            );

            if(weChatUserInfoDTO == null) {
                return CherryResponseEntity.fail();
            }

            long userCount = this.count(new QueryWrapper<User>().lambda().eq(User::getOpenId, sessionKeyOrOpenId.getOpenId()));

            if (userCount <= 0) {
                // 用户信息入库
                User user = new User();
                BeanUtils.copyProperties(weChatUserInfoDTO, user);
                user.setGender(Integer.parseInt(weChatUserInfoDTO.getGender()));
                user.setName(weChatUserInfoDTO.getNickName());
                user.setOpenId(sessionKeyOrOpenId.getOpenId());
                this.baseMapper.insert(user);
            }
            // 此处token仅用于生成唯一值
            String token = TokenUtils.getToken(weChatUserInfoDTO.getNickName(), sessionKeyOrOpenId.getOpenId());
            String userInfo = objectMapper.writeValueAsString(
                    RedisUserInfo.builder()
                            .username(weChatUserInfoDTO.getNickName())
                            .openId(sessionKeyOrOpenId.getOpenId())
                            .gender(Integer.parseInt(weChatUserInfoDTO.getGender()))
                            .build()
            );

            redisTemplate.opsForValue().set(
                    token,
                    userInfo,
                    24, TimeUnit.HOURS
            );
            return CherryResponseEntity.success(token);
        } catch (
                InvalidAlgorithmParameterException |
                NoSuchPaddingException |
                IllegalBlockSizeException |
                UnsupportedEncodingException |
                NoSuchAlgorithmException |
                BadPaddingException |
                InvalidKeyException |
                NoSuchProviderException |
                JsonProcessingException |
                InvalidParameterSpecException e
        ) {
            log.info("login error msg : {}", e.toString());
            return CherryResponseEntity.fail();
        }
    }
}
