package com.cherry.lucky.constant;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName ErrorCodeConstants
 * @Description
 * @createTime 2023年03月25日 13:55:00
 */
@SuppressWarnings("unused")
public class ErrorCodeConstants {

    /**
     * 接口切面错误
     */
    public static final int INTERFACE_POINT_CUT_ERROR = 50014;

    /**
     * swagger 解析字段为空
     */
    public static final int SWAGGER_FILED_IS_NULL = 50015;


    /**
     * redis 通过token获取用户信息为空
     */
    public static final int REDIS_GET_USERINFO_BY_TOKEN_IS_NULL = 50016;

    /**
     * token不存在
     */
    public static final int TOKEN_IS_NOT_EXISTENT = 50017;

    /**
     * token有误
     */
    public static final int TOKEN_IS_ERROR = 50018;

    /**
     * token claims 为空
     */
    public static final int CLAIMS_IS_NULL_ERROR = 50018;

    /**
     * 获取请求对象失败
     */
    public static final int GET_REQUEST_ERROR = 50019;


    /**
     * 请求过于频繁
     */
    public static final int REQUEST_FREQUENTLY = 50020;

    /**
     *用户已存在
     */
    public static final int USER_IS_EXIST = 50021;

    /**
     *用户不存在
     */
    public static final int USER_IS_NOT_EXIST = 50021;

}
