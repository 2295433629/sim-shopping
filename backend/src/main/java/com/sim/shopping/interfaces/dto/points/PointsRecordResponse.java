package com.sim.shopping.interfaces.dto.points;

import java.time.LocalDateTime;

/**
 * PointsRecord响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class PointsRecordResponse {

    private Long id;
    private Long userId;
    private Integer points;
    private String type;
    private String source;
    private String description;
    private Long relatedId;
    private LocalDateTime createdAt;

    /** 获取Id */
    public Long getId() {
        return id;
    }

    /** set Id */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取Points
     * @return 返回结果
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * set Points
     * @param points points
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    /** 获取Type */
    public String getType() {
        return type;
    }

    /** set Type */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取Source
     * @return 返回结果
     */
    public String getSource() {
        return source;
    }

    /**
     * set Source
     * @param source source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() {
        return description;
    }

    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取Related Id
     * @return 返回结果
     */
    public Long getRelatedId() {
        return relatedId;
    }

    /**
     * set Related Id
     * @param relatedId relatedId
     */
    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    /** 获取Created At */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
