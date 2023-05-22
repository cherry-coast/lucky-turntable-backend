package com.cherry.lucky.common.handle;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.cherry.lucky.common.annotate.WebMonitorAnnotation;
import com.cherry.lucky.constant.HttpCodeConstants;
import com.cherry.lucky.domain.InterfaceLog;
import com.cherry.lucky.service.impl.InterfaceLogServiceImpl;
import com.cherry.lucky.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录检查
 * 1.配置到拦截器要拦截哪些请求
 * 2.把这些配置放在容器中
 * <p>
 * 实现HandlerInterceptor接口
 * @author cherry
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 目标方法执行之前
     * 登录检查写在这里，如果没有登录，就不执行目标方法
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler target method
     * @return Interception Result
     */
    @Override
    public boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler
    ) {
        String requestUrl = request.getRequestURL().toString();
        if(handler instanceof HandlerMethod handlerMethod) {
            if(handlerMethod.getMethodAnnotation(WebMonitorAnnotation.class) == null) {
                String token = request.getHeader("token");
                ApiOperation annotation = handlerMethod.getMethodAnnotation(ApiOperation.class);
                if(StringUtils.isEmpty(token)) {
                    InterfaceLog webLog = InterfaceLog
                            .builder()
                            .basePath(StrUtil.removeSuffix(requestUrl, URLUtil.url(requestUrl).getPath()))
                            .description(annotation == null ? "no desc" : annotation.value())
                            .ip(request.getRemoteAddr())
                            .parameter(null)
                            .method(handlerMethod.getMethod().getName())
                            .result("token is null !!! ")
                            .recodeTime(System.currentTimeMillis())
                            .spendTime(0L)
                            .uri(request.getRequestURI())
                            .url(request.getRequestURL().toString())
                            .build();
                    InterfaceLogServiceImpl interfaceLogService = new InterfaceLogServiceImpl();
                    interfaceLogService.saveLogAsJdbc(webLog);
                    response.setStatus(HttpCodeConstants.UN_AUTH);
                    return false;
                } else if (TokenUtils.isTokenExpired(token)) {
                    InterfaceLog webLog = InterfaceLog
                            .builder()
                            .basePath(StrUtil.removeSuffix(requestUrl, URLUtil.url(requestUrl).getPath()))
                            .description(annotation == null ? "no desc" : annotation.value())
                            .ip(request.getRemoteAddr())
                            .parameter(null)
                            .method(handlerMethod.getMethod().getName())
                            .result("token expired !!! ")
                            .recodeTime(System.currentTimeMillis())
                            .spendTime(0L)
                            .uri(request.getRequestURI())
                            .url(request.getRequestURL().toString())
                            .build();
                    InterfaceLogServiceImpl interfaceLogService = new InterfaceLogServiceImpl();
                    interfaceLogService.saveLogAsJdbc(webLog);
                    response.setStatus(HttpCodeConstants.UN_AUTH);
                    return false;
                }
            }
        }
        return true;
    }
 

}