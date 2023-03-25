package com.cherry.lucky.common.handle;

import cn.hutool.json.JSON;
import com.cherry.lucky.common.annotate.WxInterfaceAnnotation;
import com.cherry.lucky.constant.HttpCodeConstants;
import com.cherry.lucky.constant.UrlConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StreamUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

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
        if(handler instanceof HandlerMethod handlerMethod) {
            if(handlerMethod.getMethodAnnotation(WxInterfaceAnnotation.class) != null) {
                if(StringUtils.isEmpty(request.getHeader("token"))) {
                    response.setStatus(HttpCodeConstants.UN_AUTH);
                    return false;
                }
            }
        }
        return true;
    }
 

}