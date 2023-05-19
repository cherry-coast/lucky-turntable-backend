package com.cherry.lucky.common.exception;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.cherry.lucky.constant.ErrorCodeConstants;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.domain.InterfaceLog;
import com.cherry.lucky.service.InterfaceLogService;
import com.cherry.lucky.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ganxiongwen
 * Date: 2022/4/20 16:30
 * Description: 全局异常
 * ClassName: GlobalException
 * Package: com.cherry.flow.common.exception
 * Copyright (c) 2022,All Rights Reserved.
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    private InterfaceLogService interfaceLogServiceImpl;

    @Autowired
    public void setInterfaceLogServiceImpl(InterfaceLogService interfaceLogServiceImpl) {
        this.interfaceLogServiceImpl = interfaceLogServiceImpl;
    }

    @ExceptionHandler(value = CherryException.class)
    public CherryResponseEntity<String> handleCherryException(CherryException e) {
        log.error("module error !!! error message: {}", e.getMessage());
        generateInterfaceLog(e);
        return CherryResponseEntity.fail(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CherryResponseEntity<String> handleValidException(MethodArgumentNotValidException e) {
        return getFormatException(e);
    }

    @ExceptionHandler(value = BindException.class)
    public CherryResponseEntity<String> handleValidException(BindException e) {
        return getFormatException(e);
    }

    @ExceptionHandler(value = Exception.class)
    public CherryResponseEntity<String> handleValidException(Exception e) {
        generateInterfaceLog(e);
        log.error(e.toString());
        return CherryResponseEntity.fail(e.getMessage());
    }

    public CherryResponseEntity<String> getFormatException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        generateInterfaceLog(e);
        log.error(message);
        return CherryResponseEntity.fail(message);
    }

    private void generateInterfaceLog(Exception e) {
        // 获取请求的上下文
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null) {
            throw new CherryException(ErrorCodeConstants.GET_REQUEST_ERROR, "获取请求的上下文为空 !!! ");
        }

        HttpServletRequest request = requestAttributes.getRequest();
        // 获取请求的url 地址
        String requestUrl = request.getRequestURL().toString();
        // 用户信息
        String token = request.getHeader("token");
        String username = "";
        if(StringUtils.isNotBlank(token)) {
            username = TokenUtils.parseTokenToUserName(token);
        }

        InterfaceLog webLog = InterfaceLog
                .builder()
                .username(username)
                .basePath(StrUtil.removeSuffix(requestUrl, URLUtil.url(requestUrl).getPath()))
                .description(e.getMessage())
                .ip(request.getRemoteAddr())
                .parameter(null)
                .method("")
                .result("request error error message : " + getError(e) + "\n" + e.getMessage())
                .recodeTime(System.currentTimeMillis())
                .spendTime(0L)
                .uri(request.getRequestURI())
                .url(request.getRequestURL().toString())
                .build();
        interfaceLogServiceImpl.saveLog(webLog);
    }

    private String getError(Exception e) {
        StringBuilder errorMsg = new StringBuilder();
        if(e != null && e.getStackTrace() != null) {
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                errorMsg.append(stackTraceElement.toString()).append("\n");
            }
        }
        return errorMsg.toString();
    }
}