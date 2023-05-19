package com.cherry.lucky.controller;

import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.model.dto.BackUserLoginDTO;
import com.cherry.lucky.service.BackUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
 * @ClassName UserController
 * @Description
 * @createTime 2023年04月27日 16:47:00
 */
@RestController
@RequestMapping("/api/v1/user")
@Api(value = "用户api", tags = { "用户接口" })
public class UserController {

    private BackUserService backUserServiceImpl;

    @Autowired
    public void setBackUserServiceImpl(BackUserService backUserServiceImpl) {
        this.backUserServiceImpl = backUserServiceImpl;
    }

    /**
     * 用户登录详情
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public CherryResponseEntity<String> login(@RequestBody BackUserLoginDTO backUserLoginDTO) throws JsonProcessingException {
        return backUserServiceImpl.login(backUserLoginDTO);
    }

    /**
     * 用户注册详情
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public CherryResponseEntity<String> register(@RequestBody BackUserLoginDTO backUserLoginDTO) throws JsonProcessingException {
        return backUserServiceImpl.register(backUserLoginDTO);
    }

}
