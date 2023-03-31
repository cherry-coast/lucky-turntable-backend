package com.cherry.lucky.config.redis;


import com.cherry.lucky.constant.RedisConstant;
import com.cherry.lucky.utils.CherryRedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

/**
 * @author cherry
 */
@Configuration
@ConditionalOnMissingBean(RedissonClient.class)
@Import(RedissonClientConfig.RedissonConfig.class)
// This SuppressWarnings is used to suppress "application context that are not configured for this file" warnings
@SuppressWarnings(value = "all")
public class RedissonClientConfig {
 
    private final Config config;
 
    RedissonClientConfig(Config config) {
        this.config = config;
    }
 
    @Bean
    public RedissonClient redissonClient() {
        return Redisson.create(config);
    }
 
    @ConditionalOnMissingBean(Config.class)
    @EnableConfigurationProperties({RedisProperties.class})
    static class RedissonConfig {

        @Resource
        private RedisProperties redisProperties;

        @Bean
        public Config redissonConfig() {
            Config config = new Config();
            
            // sentinel
            if (redisProperties.getSentinel() != null) {
                SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
                RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
                sentinelServersConfig.setMasterName(sentinel.getMaster());
                sentinelServersConfig.addSentinelAddress(sentinel.getNodes().toArray(new String[0]));
                sentinelServersConfig.setDatabase(redisProperties.getDatabase());
                baseConfig(sentinelServersConfig, redisProperties);

            // cluster
            } else if (redisProperties.getCluster() != null) {
                ClusterServersConfig clusterServersConfig = config.useClusterServers();
                RedisProperties.Cluster cluster = redisProperties.getCluster();
                clusterServersConfig.addNodeAddress(cluster.getNodes().toArray(new String[0]));
                clusterServersConfig.setFailedSlaveReconnectionInterval(cluster.getMaxRedirects());
                baseConfig(clusterServersConfig, redisProperties);

            // single server
            } else {
                SingleServerConfig singleServerConfig = config.useSingleServer();
                String schema = redisProperties.isSsl() ? RedisConstant.REDIS_FOR_SSL : RedisConstant.REDIS_FOR_NORMAL;
                singleServerConfig.setAddress(schema + redisProperties.getHost() + ":" + redisProperties.getPort());
                singleServerConfig.setDatabase(redisProperties.getDatabase());
                baseConfig(singleServerConfig, redisProperties);
            }
            CherryRedisUtil.redissonClient = Redisson.create(config);
            config.setCodec(new JsonJacksonCodec());
            return config;
        }
 
        private <T extends BaseConfig<T>> void baseConfig(BaseConfig<T> config, RedisProperties properties) {
            if (!StringUtils.isBlank(properties.getPassword())) {
                config.setPassword(properties.getPassword());
            }
            if (properties.getTimeout() != null) {
                config.setTimeout(Long.valueOf(properties.getTimeout().getSeconds() * 1000).intValue());
            }
            if (!StringUtils.isBlank(properties.getClientName())) {
                config.setClientName(properties.getClientName());
            }
        }
    }
 
}