package com.cherry.lucky.common.aop;

import cn.hutool.json.JSON;
import com.cherry.lucky.common.exception.CherryException;
import com.cherry.lucky.constant.ErrorCodeConstants;
import com.cherry.lucky.constant.RedisConstant;
import com.cherry.lucky.utils.CherryRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName InterfaceRequestHandlerAspect
 * @Description
 * @createTime 2023年03月31日 15:00:00
 */
//@Aspect
//@Component
//@Order(10)
//@Slf4j
public class InterfaceRequestHandlerAspect {

    private static final Integer REQUEST_NUM = 10;

    private RedisTemplate<String, String> redisTemplate;

//    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

//    @Pointcut("execution( * com.cherry.lucky.controller.*.*(..))")
    public void interfaceRequestHandlerAspect() {}

//    @Around("interfaceRequestHandlerAspect()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

        if (servletRequestAttributes == null) {
            return pjp.proceed();
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getHeader("token");
        if (StringUtils.isNotBlank(token)) {
            slidingWindow(token);
        }
        // result的值就是被拦截方法的返回值
        return pjp.proceed();
    }

    /**
     * every time we get a request,
     * we make a decision to provide serve it or not provide;
     * hence we check the request number made in last 10 seconds.
     * So this process of checking for a fixed window of 10 seconds on every request,
     * makes this approach a sliding window where the fixed window of size 10 seconds is moving forward with each request.
     * The entire approach could be visualized as follows
     *
     */
    public void slidingWindow(String token) {
        long currentTime = System.currentTimeMillis();
        String redisKey = RedisConstant.REDIS_CHERRY_LUCKY_LIMIT.concat(token);
        // flow limit window size
        long intervalTime = 10000L;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            Set<String> requestInfo = redisTemplate
                    .opsForZSet()
                    .rangeByScore(redisKey, currentTime - intervalTime, currentTime);
            // If the number of requests served on configuration redis key in the last 10 seconds is more than 10 number requests then discard,
            // else the request goes through while we update the counter.
            if (requestInfo != null && requestInfo.size() > REQUEST_NUM) {
                throw new CherryException(ErrorCodeConstants.REQUEST_FREQUENTLY, "访问频率过高!!!");
            }
        }
        // save this request
        redisTemplate.opsForZSet().add(redisKey, token, currentTime);
        redisTemplate.expire(redisKey, intervalTime, TimeUnit.MICROSECONDS);
    }

}
