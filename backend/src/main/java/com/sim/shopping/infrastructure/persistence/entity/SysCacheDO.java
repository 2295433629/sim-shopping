package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 缓存实体，对应 sys_cache 表，存储数据库降级缓存数据
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("sys_cache")
public class SysCacheDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String cacheKey;

    private String cacheValue;

    private LocalDateTime expiredAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /** 获取Id */
    public Long getId() { return id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }

    /**
     * 获取Cache Key
     * @return 返回结果
     */
    public String getCacheKey() { return cacheKey; }
    /**
     * set Cache Key
     * @param cacheKey cacheKey
     */
    public void setCacheKey(String cacheKey) { this.cacheKey = cacheKey; }

    /**
     * 获取Cache Value
     * @return 返回结果
     */
    public String getCacheValue() { return cacheValue; }
    /**
     * set Cache Value
     * @param cacheValue cacheValue
     */
    public void setCacheValue(String cacheValue) { this.cacheValue = cacheValue; }

    /**
     * 获取Expired At
     * @return 返回结果
     */
    public LocalDateTime getExpiredAt() { return expiredAt; }
    /**
     * set Expired At
     * @param expiredAt expiredAt
     */
    public void setExpiredAt(LocalDateTime expiredAt) { this.expiredAt = expiredAt; }

    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /** 获取Updated At */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /** set Updated At */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
