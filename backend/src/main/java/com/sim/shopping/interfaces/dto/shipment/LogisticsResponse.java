package com.sim.shopping.interfaces.dto.shipment;

import java.time.LocalDateTime;
import java.util.List;

public class LogisticsResponse {
    private Long id;
    private String orderNo;
    private String trackingNo;
    private String logisticsCompany;
    private String status;
    private LocalDateTime deliveredAt;
    private List<LogisticsTrackItem> tracks;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getTrackingNo() { return trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    public String getLogisticsCompany() { return logisticsCompany; }
    public void setLogisticsCompany(String logisticsCompany) { this.logisticsCompany = logisticsCompany; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    public List<LogisticsTrackItem> getTracks() { return tracks; }
    public void setTracks(List<LogisticsTrackItem> tracks) { this.tracks = tracks; }
}
