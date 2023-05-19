package com.cherry.lucky.common.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName WebMonitorAnnotation
 * @Description 标识微信小程序接口
 * @createTime 2023年03月25日 15:33:00
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface WebMonitorAnnotation {

    String value() default "";

}
