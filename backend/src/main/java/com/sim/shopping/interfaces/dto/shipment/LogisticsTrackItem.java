package com.sim.shopping.interfaces.dto.shipment;

import java.time.LocalDateTime;

/**
 * LogisticsTrackItem
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class LogisticsTrackItem {
    private String status;
    private String description;
    private String location;
    private LocalDateTime occurredAt;

    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() { return description; }
    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * 获取Location
     * @return 返回结果
     */
    public String getLocation() { return location; }
    /**
     * set Location
     * @param location location
     */
    public void setLocation(String location) { this.location = location; }
    /**
     * 获取Occurred At
     * @return 返回结果
     */
    public LocalDateTime getOccurredAt() { return occurredAt; }
    /**
     * set Occurred At
     * @param occurredAt occurredAt
     */
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
