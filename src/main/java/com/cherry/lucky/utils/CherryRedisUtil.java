package com.cherry.lucky.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName CherryRedisUtil
 * @Description
 * @createTime 2023年02月24日 11:47:00
 */
@SuppressWarnings("unused")
public class CherryRedisUtil {

    public static RedissonClient redissonClient;

    /**
     * 尝试获取锁
     *
     * @param lockKey   锁 KEY
     * @param waitTime  最多等待时间
     * @param leaseTime 超时时间，上锁后自动释放锁时间
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey   锁 KEY
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 超时时间，上锁后自动释放锁时间
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁 KEY
     */
    public static void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        // 锁存在，则释放锁
        if(lock.isLocked()){
            lock.unlock();
        }
    }

    /**
     * 查询锁
     *
     * @param lockKey 锁 KEY
     */
    public static boolean isLocked(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.isLocked();
    }

}
