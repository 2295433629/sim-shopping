package com.sim.shopping.interfaces.dto.shipment;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Logistics响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class LogisticsResponse {
    private Long id;
    private String orderNo;
    private String trackingNo;
    private String logisticsCompany;
    private String status;
    private LocalDateTime deliveredAt;
    private List<LogisticsTrackItem> tracks;

    /** 获取Id */
    public Long getId() { return id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return orderNo; }
    /**
     * set Order No
     * @param orderNo orderNo
     */
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    /**
     * 获取Tracking No
     * @return 返回结果
     */
    public String getTrackingNo() { return trackingNo; }
    /**
     * set Tracking No
     * @param trackingNo trackingNo
     */
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    /**
     * 获取Logistics Company
     * @return 返回结果
     */
    public String getLogisticsCompany() { return logisticsCompany; }
    /**
     * set Logistics Company
     * @param logisticsCompany logisticsCompany
     */
    public void setLogisticsCompany(String logisticsCompany) { this.logisticsCompany = logisticsCompany; }
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
     * 获取Delivered At
     * @return 返回结果
     */
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    /**
     * set Delivered At
     * @param deliveredAt deliveredAt
     */
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    /**
     * 获取Tracks
     * @return 返回结果
     */
    public List<LogisticsTrackItem> getTracks() { return tracks; }
    /**
     * set Tracks
     * @param tracks tracks
     */
    public void setTracks(List<LogisticsTrackItem> tracks) { this.tracks = tracks; }
}
