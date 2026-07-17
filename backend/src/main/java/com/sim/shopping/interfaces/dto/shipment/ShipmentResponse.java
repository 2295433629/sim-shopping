package com.sim.shopping.interfaces.dto.shipment;

import java.time.LocalDateTime;

/**
 * Shipment响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ShipmentResponse {
    private Long shipmentId;
    private String orderNo;
    private String shipmentNo;
    private String logisticsCompany;
    private String trackingNo;
    private String status;
    private LocalDateTime shippedAt;

    /**
     * 获取Shipment Id
     * @return 返回结果
     */
    public Long getShipmentId() { return shipmentId; }
    /**
     * set Shipment Id
     * @param shipmentId shipmentId
     */
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
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
     * 获取Shipment No
     * @return 返回结果
     */
    public String getShipmentNo() { return shipmentNo; }
    /**
     * set Shipment No
     * @param shipmentNo shipmentNo
     */
    public void setShipmentNo(String shipmentNo) { this.shipmentNo = shipmentNo; }
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
     * 获取Shipped At
     * @return 返回结果
     */
    public LocalDateTime getShippedAt() { return shippedAt; }
    /**
     * set Shipped At
     * @param shippedAt shippedAt
     */
    public void setShippedAt(LocalDateTime shippedAt) { this.shippedAt = shippedAt; }
}
