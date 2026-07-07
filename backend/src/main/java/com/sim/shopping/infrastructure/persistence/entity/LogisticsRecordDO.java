package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_logistics_record")
public class LogisticsRecordDO extends BaseEntity {
    private Long orderId;
    private String trackingNo;
    private Long shipmentId;
    private String orderNo;
    private LocalDateTime deliveredAt;
    private String logisticsCompany;
    private String status;

    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getTrackingNo() { return this.trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    public Long getShipmentId() { return this.shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public LocalDateTime getDeliveredAt() { return this.deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    public String getLogisticsCompany() { return this.logisticsCompany; }
    public void setLogisticsCompany(String logisticsCompany) { this.logisticsCompany = logisticsCompany; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
