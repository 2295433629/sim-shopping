package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_logistics_track")
public class LogisticsTrackDO extends BaseEntity {
    private String status;
    private String description;
    private LocalDateTime occurredAt;
    private String location;
    private Long logisticsId;

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getOccurredAt() { return this.occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location; }
    public Long getLogisticsId() { return this.logisticsId; }
    public void setLogisticsId(Long logisticsId) { this.logisticsId = logisticsId; }
}
