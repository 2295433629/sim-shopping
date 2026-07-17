package com.sim.shopping.infrastructure.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * RedisCache提供者
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Component
public class RedisCacheProvider implements CacheProvider {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheProvider(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * set
     * @param key key
     * @param value value
     * @param ttl ttl
     */
    @Override
    public void set(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    /**
     * get
     * @param key key
     * @return 返回结果
     */
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除
     * @param key key
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * has Key
     * @param key key
     * @return 返回结果
     */
    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
