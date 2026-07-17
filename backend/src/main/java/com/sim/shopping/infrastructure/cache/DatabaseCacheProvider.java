package com.sim.shopping.infrastructure.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sim.shopping.infrastructure.persistence.entity.SysCacheDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysCacheMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * DatabaseCache提供者
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Component
public class DatabaseCacheProvider implements CacheProvider {

    private static final Logger log = LoggerFactory.getLogger(DatabaseCacheProvider.class);

    private final SysCacheMapper cacheMapper;
    private final ObjectMapper objectMapper;

    public DatabaseCacheProvider(SysCacheMapper cacheMapper) {
        this.cacheMapper = cacheMapper;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    /**
     * set
     * @param key key
     * @param value value
     * @param ttl ttl
     */
    @Override
    public void set(String key, Object value, Duration ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            LocalDateTime expiredAt = LocalDateTime.now().plus(ttl);

            LambdaQueryWrapper<SysCacheDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysCacheDO::getCacheKey, key);
            SysCacheDO existing = cacheMapper.selectOne(wrapper);

            if (existing != null) {
                existing.setCacheValue(jsonValue);
                existing.setExpiredAt(expiredAt);
                existing.setUpdatedAt(LocalDateTime.now());
                cacheMapper.updateById(existing);
            } else {
                SysCacheDO cache = new SysCacheDO();
                cache.setCacheKey(key);
                cache.setCacheValue(jsonValue);
                cache.setExpiredAt(expiredAt);
                cache.setCreatedAt(LocalDateTime.now());
                cache.setUpdatedAt(LocalDateTime.now());
                cacheMapper.insert(cache);
            }
        } catch (JsonProcessingException e) {
            log.error("Cache set failed for key={}, error={}", key, e.getMessage());
        }
    }

    /**
     * get
     * @param key key
     * @return 返回结果
     */
    @Override
    public Object get(String key) {
        try {
            LambdaQueryWrapper<SysCacheDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysCacheDO::getCacheKey, key);
            wrapper.gt(SysCacheDO::getExpiredAt, LocalDateTime.now());
            SysCacheDO cache = cacheMapper.selectOne(wrapper);

            if (cache != null) {
                return cache.getCacheValue();
            }
        } catch (Exception e) {
            log.error("Cache get failed for key={}, error={}", key, e.getMessage());
        }
        return null;
    }

    /**
     * 删除
     * @param key key
     */
    @Override
    public void delete(String key) {
        try {
            LambdaQueryWrapper<SysCacheDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysCacheDO::getCacheKey, key);
            cacheMapper.delete(wrapper);
        } catch (Exception e) {
            log.error("Cache delete failed for key={}, error={}", key, e.getMessage());
        }
    }

    /**
     * has Key
     * @param key key
     * @return 返回结果
     */
    @Override
    public boolean hasKey(String key) {
        try {
            LambdaQueryWrapper<SysCacheDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysCacheDO::getCacheKey, key);
            wrapper.gt(SysCacheDO::getExpiredAt, LocalDateTime.now());
            return cacheMapper.selectCount(wrapper) > 0;
        } catch (Exception e) {
            log.error("Cache hasKey failed for key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 从数据库缓存中获取并反序列化为指定类型
     */
    public <T> T get(String key, Class<T> clazz) {
        String json = (String) get(key);
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Cache deserialization failed for key={}, error={}", key, e.getMessage());
            return null;
        }
    }
}
