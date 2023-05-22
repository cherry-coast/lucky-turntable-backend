package com.cherry.lucky.common.aop;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.cherry.lucky.common.annotate.WebMonitorAnnotation;
import com.cherry.lucky.common.auth.UserInfo;
import com.cherry.lucky.common.exception.CherryException;
import com.cherry.lucky.constant.ErrorCodeConstants;
import com.cherry.lucky.constant.StringConstant;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.domain.InterfaceLog;
import com.cherry.lucky.service.InterfaceLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : ganxiongwen
 * Date: 2022/4/21 15:40
 * Description: 接口日志切面
 * ClassName: InterfaceLogAspect
 * Package: com.cherry.flow.common.aop
 * Copyright (c) 2022,All Rights Reserved.
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class InterfaceLogAspect {


    private UserInfo userInfo;

    private InterfaceLogService interfaceLogServiceImpl;

    @Autowired
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Autowired
    public void setInterfaceLogServiceImpl(InterfaceLogService interfaceLogServiceImpl) {
        this.interfaceLogServiceImpl = interfaceLogServiceImpl;
    }

    @Pointcut("execution( * com.cherry.lucky.controller.*.*(..))")
    public void interfaceLogAspect() {}

    @Around(value = "interfaceLogAspect()")
    public Object recordWebLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result;
        // 创建计时器
        StopWatch stopWatch = new StopWatch();
        // 开始计时器
        stopWatch.start();
        // 不需要我们自己处理这个异常
        result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        // 记时结束
        stopWatch.stop();

        // 获取请求的上下文
        ServletRequestAttributes requestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null) {
            throw new CherryException(ErrorCodeConstants.GET_REQUEST_ERROR, "日志切面获取请求的上下文为空 !!! ");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取方法
        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 获取方法上的ApiOperation注解
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        // 获取目标对象的类型名称
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        // 获取请求的url 地址
        String requestUrl = request.getRequestURL().toString();
        // 用户信息
        String token = request.getHeader("token");
        String username = "";
        if(StringUtils.isEmpty(token) && method.getAnnotation(WebMonitorAnnotation.class) == null) {
            if(result instanceof CherryResponseEntity && ((CherryResponseEntity<?>) result).isSuccess())  {
                token = ((CherryResponseEntity<String>) result).getData();
                username = userInfo.getUserInfoByRedis(token).getUsername();
            }
        }
        InterfaceLog webLog = InterfaceLog
                .builder()
                .username(username)
                .basePath(StrUtil.removeSuffix(requestUrl, URLUtil.url(requestUrl).getPath()))
                .description(annotation == null ? "no desc" : annotation.value())
                .ip(request.getRemoteAddr())
                .parameter(getMethodParameter(method, proceedingJoinPoint.getArgs()))
                .method(className + "." + method.getName())
                .result(result)
                .recodeTime(System.currentTimeMillis())
                .spendTime(stopWatch.getTotalTimeMillis())
                .uri(request.getRequestURI())
                .url(request.getRequestURL().toString())
                .build();
        if (method.getAnnotation(WebMonitorAnnotation.class) != null) {
            log.debug("monitor request !!! ");
        } else {
            interfaceLogServiceImpl.saveLog(webLog);
        }
        return result;
    }

    /**
     * 获取方法的执行参数
     * 
     * @param method 方法
     * @param args 参数
     * @return {"key_参数的名称":"value_参数的值"}
     */
    private Object getMethodParameter(Method method, Object[] args) {
        Map<String, Object> methodParametersWithValues = new HashMap<>(16);
        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer =
            new LocalVariableTableParameterNameDiscoverer();
        // 方法的形参名称
        String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            if (parameterNames[i].equals(StringConstant.PASSWORD) || parameterNames[i].equals(StringConstant.FILE)) {
                methodParametersWithValues.put(parameterNames[i], "受限的支持类型");
            } else {
                methodParametersWithValues.put(parameterNames[i], args[i]);
            }
        }
        return methodParametersWithValues;
    }
}