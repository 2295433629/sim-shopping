package com.sim.shopping.interfaces.dto.shipment;

import java.time.LocalDateTime;

public class LogisticsTrackItem {
    private String status;
    private String description;
    private String location;
    private LocalDateTime occurredAt;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
