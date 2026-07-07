package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_shipment")
public class ShipmentDO extends BaseEntity {
    private Long orderId;
    private String logisticsCompany;
    private String orderNo;
    private LocalDateTime shippedAt;
    private Long shopId;
    private String shipmentNo;
    private String status;

    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getLogisticsCompany() { return this.logisticsCompany; }
    public void setLogisticsCompany(String logisticsCompany) { this.logisticsCompany = logisticsCompany; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public LocalDateTime getShippedAt() { return this.shippedAt; }
    public void setShippedAt(LocalDateTime shippedAt) { this.shippedAt = shippedAt; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public String getShipmentNo() { return this.shipmentNo; }
    public void setShipmentNo(String shipmentNo) { this.shipmentNo = shipmentNo; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
