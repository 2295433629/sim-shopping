package com.sim.shopping.interfaces.dto.shipment;

import java.time.LocalDateTime;

public class ShipmentResponse {
    private Long shipmentId;
    private String orderNo;
    private String shipmentNo;
    private String logisticsCompany;
    private String trackingNo;
    private String status;
    private LocalDateTime shippedAt;

    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getShipmentNo() { return shipmentNo; }
    public void setShipmentNo(String shipmentNo) { this.shipmentNo = shipmentNo; }
    public String getLogisticsCompany() { return logisticsCompany; }
    public void setLogisticsCompany(String logisticsCompany) { this.logisticsCompany = logisticsCompany; }
    public String getTrackingNo() { return trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getShippedAt() { return shippedAt; }
    public void setShippedAt(LocalDateTime shippedAt) { this.shippedAt = shippedAt; }
}
