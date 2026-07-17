package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 物流实体，对应 t_shipment 表，存储物流信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_shipment")
public class ShipmentDO extends BaseEntity {
    private Long orderId;
    private String logisticsCompany;
    private String orderNo;
    private LocalDateTime shippedAt;
    private Long shopId;
    private String shipmentNo;
    private String status;

    /**
     * 获取Order Id
     * @return 返回结果
     */
    public Long getOrderId() { return this.orderId; }
    /**
     * set Order Id
     * @param orderId orderId
     */
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    /**
     * 获取Logistics Company
     * @return 返回结果
     */
    public String getLogisticsCompany() { return this.logisticsCompany; }
    /**
     * set Logistics Company
     * @param logisticsCompany logisticsCompany
     */
    public void setLogisticsCompany(String logisticsCompany) { this.logisticsCompany = logisticsCompany; }
    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return this.orderNo; }
    /**
     * set Order No
     * @param orderNo orderNo
     */
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    /**
     * 获取Shipped At
     * @return 返回结果
     */
    public LocalDateTime getShippedAt() { return this.shippedAt; }
    /**
     * set Shipped At
     * @param shippedAt shippedAt
     */
    public void setShippedAt(LocalDateTime shippedAt) { this.shippedAt = shippedAt; }
    /**
     * 获取Shop Id
     * @return 返回结果
     */
    public Long getShopId() { return this.shopId; }
    /**
     * set Shop Id
     * @param shopId shopId
     */
    public void setShopId(Long shopId) { this.shopId = shopId; }
    /**
     * 获取Shipment No
     * @return 返回结果
     */
    public String getShipmentNo() { return this.shipmentNo; }
    /**
     * set Shipment No
     * @param shipmentNo shipmentNo
     */
    public void setShipmentNo(String shipmentNo) { this.shipmentNo = shipmentNo; }
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
}
