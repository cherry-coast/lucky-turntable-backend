package com.cherry.lucky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.entity.User;
import com.cherry.lucky.model.dto.WeChatUserInfoWebRequestDTO;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName UserService
 * @Description
 * @createTime 2023年03月22日 16:43:00
 */

public interface UserService extends IService<User> {

    /**
     *
     * 登录接口
     * @param weChatUserInfoWebRequestDTO 微信用户信息
     * @return 统一返回
     */
    CherryResponseEntity<String> login(WeChatUserInfoWebRequestDTO weChatUserInfoWebRequestDTO);

}
