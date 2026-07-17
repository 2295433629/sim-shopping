package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * LogisticsTrack数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_logistics_track")
public class LogisticsTrackDO extends BaseEntity {
    private String status;
    private String description;
    private LocalDateTime occurredAt;
    private String location;
    private Long logisticsId;

    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() { return this.description; }
    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * 获取Occurred At
     * @return 返回结果
     */
    public LocalDateTime getOccurredAt() { return this.occurredAt; }
    /**
     * set Occurred At
     * @param occurredAt occurredAt
     */
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
    /**
     * 获取Location
     * @return 返回结果
     */
    public String getLocation() { return this.location; }
    /**
     * set Location
     * @param location location
     */
    public void setLocation(String location) { this.location = location; }
    /**
     * 获取Logistics Id
     * @return 返回结果
     */
    public Long getLogisticsId() { return this.logisticsId; }
    /**
     * set Logistics Id
     * @param logisticsId logisticsId
     */
    public void setLogisticsId(Long logisticsId) { this.logisticsId = logisticsId; }
}
