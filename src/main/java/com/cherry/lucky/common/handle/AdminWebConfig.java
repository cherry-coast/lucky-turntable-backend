package com.cherry.lucky.common.handle;

import com.cherry.lucky.constant.UrlConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName WxInterfaceAnnotation
 * @Description 标识微信小程序接口
 * @createTime 2023年03月25日 15:33:00
 */
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
 
    /**
     * 配置拦截器
     * @param registry 相当于拦截器的注册中心
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(new LoginInterceptor())
                 .addPathPatterns()
                 .excludePathPatterns(UrlConstants.LOGIN_URL);
    }
}