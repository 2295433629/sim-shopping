package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * LogisticsRecord数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_logistics_record")
public class LogisticsRecordDO extends BaseEntity {
    private Long orderId;
    private String trackingNo;
    private Long shipmentId;
    private String orderNo;
    private LocalDateTime deliveredAt;
    private String logisticsCompany;
    private String status;

    @TableField("sender_address")
    private String senderAddress;

    @TableField("receiver_address")
    private String receiverAddress;

    @TableField("sender_city")
    private String senderCity;

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
     * 获取Tracking No
     * @return 返回结果
     */
    public String getTrackingNo() { return this.trackingNo; }
    /**
     * set Tracking No
     * @param trackingNo trackingNo
     */
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    /**
     * 获取Shipment Id
     * @return 返回结果
     */
    public Long getShipmentId() { return this.shipmentId; }
    /**
     * set Shipment Id
     * @param shipmentId shipmentId
     */
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
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
     * 获取Delivered At
     * @return 返回结果
     */
    public LocalDateTime getDeliveredAt() { return this.deliveredAt; }
    /**
     * set Delivered At
     * @param deliveredAt deliveredAt
     */
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
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
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * 获取Sender Address
     * @return 返回结果
     */
    public String getSenderAddress() { return this.senderAddress; }
    /**
     * set Sender Address
     * @param senderAddress senderAddress
     */
    public void setSenderAddress(String senderAddress) { this.senderAddress = senderAddress; }
    /**
     * 获取Receiver Address
     * @return 返回结果
     */
    public String getReceiverAddress() { return this.receiverAddress; }
    /**
     * set Receiver Address
     * @param receiverAddress receiverAddress
     */
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }
    /**
     * 获取Sender City
     * @return 返回结果
     */
    public String getSenderCity() { return this.senderCity; }
    /**
     * set Sender City
     * @param senderCity senderCity
     */
    public void setSenderCity(String senderCity) { this.senderCity = senderCity; }
}
