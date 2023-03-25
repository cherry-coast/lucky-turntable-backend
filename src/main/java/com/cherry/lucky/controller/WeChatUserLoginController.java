package com.cherry.lucky.controller;

import com.cherry.lucky.common.annotate.WxInterfaceAnnotation;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.model.dto.WeChatUserInfoWebRequestDTO;
import com.cherry.lucky.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author cherry
 * @version 1.0.0
 * @ClassName WeChatUserLoginController
 * @Description
 * @createTime 2023年03月22日 16:06:00
 */
@RestController
@RequestMapping("/api/v1/wechat")
@Api(value = "微信用户登录api", tags = { "微信用户登录接口" })
public class WeChatUserLoginController {


    private UserService userServiceImpl;

    @Autowired
    public void setUserServiceImpl(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * 微信用户登录详情
     */
    @PostMapping("/login")
    @ApiOperation("微信用户登录")
    @WxInterfaceAnnotation("login")
    public CherryResponseEntity<String> login(@RequestBody WeChatUserInfoWebRequestDTO weChatUserInfoWebRequestDTO) {
        return userServiceImpl.login(weChatUserInfoWebRequestDTO);
    }

}
