package com.cherry.lucky.constant;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName RedisConstant
 * @Description redis 常量
 * @createTime 2022年12月07日 14:47:00
 */
@SuppressWarnings("unused")
public class RedisConstant {

    /**
     * REDIS_FOR_NORMAL
     */
    public static final String REDIS_FOR_NORMAL = "redis://";

    /**
     * REDIS_FOR_SSL
     */
    public static final String REDIS_FOR_SSL = "rediss://";

    /**
     * REDIS_TOKEN_KEY
     */
//    public static final String REDIS_FOR_SSL = "TOKEN_://";

    /**
     * 限流key
     */
    public static final String REDIS_CHERRY_LUCKY_LIMIT = "CHERRY_LUCKY_LIMIT_";

}
