package com.cherry.lucky.config.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author cherry
 */
@Configuration
// This SuppressWarnings is used to suppress "application context that are not configured for this file" warnings
@SuppressWarnings(value = "all")
public class RedisConfig {

    public RedisTemplate<String, Object> redisTemplate;

    /**
     * RedisTemplate
     */
    @Bean
    @ConditionalOnMissingBean(name = {"redisTemplate"})
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // redis key的序列化
        StringRedisSerializer keyRedisSerializer = new StringRedisSerializer();
        // redis value的序列化
        GenericJackson2JsonRedisSerializer valueRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(keyRedisSerializer);
        redisTemplate.setValueSerializer(valueRedisSerializer);
        redisTemplate.setHashKeySerializer(keyRedisSerializer);
        redisTemplate.setHashValueSerializer(valueRedisSerializer);
        return redisTemplate;
    }
}
