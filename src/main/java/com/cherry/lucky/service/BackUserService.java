package com.cherry.lucky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.entity.BackUser;
import com.cherry.lucky.model.dto.BackUserLoginDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName BackUserService
 * @Description
 * @createTime 2023年03月22日 16:43:00
 */

public interface BackUserService extends IService<BackUser> {

    /**
     * 用户注册
     *
     * @param backUserLoginDTO 用户信息
     * @return 用户token
     * @throws JsonProcessingException json转换异常
     */
    CherryResponseEntity<String> register(BackUserLoginDTO backUserLoginDTO) throws JsonProcessingException;

    /**
     * 用户登录
     *
     * @param backUserLoginDTO 用户信息
     * @return 用户token
     * @throws JsonProcessingException json转换异常
     */
    CherryResponseEntity<String> login(BackUserLoginDTO backUserLoginDTO) throws JsonProcessingException;
}
