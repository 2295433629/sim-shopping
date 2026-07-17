package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 订单实体，对应 t_order 表，存储订单主信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_order")
public class OrderDO extends BaseEntity {
    private Long userId;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;
    private BigDecimal payAmount;
    private String remark;
    private Integer autoConfirm;
    private BigDecimal discountAmount;
    private String status;
    private String receiverAddress;
    private LocalDateTime cancelledAt;
    private String orderNo;
    private String receiverName;
    private Long shopId;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private LocalDateTime deliveredAt;
    private String receiverPhone;

    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return this.userId; }
    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) { this.userId = userId; }
    /**
     * 获取Paid At
     * @return 返回结果
     */
    public LocalDateTime getPaidAt() { return this.paidAt; }
    /**
     * set Paid At
     * @param paidAt paidAt
     */
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
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
     * 获取Completed At
     * @return 返回结果
     */
    public LocalDateTime getCompletedAt() { return this.completedAt; }
    /**
     * set Completed At
     * @param completedAt completedAt
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    /**
     * 获取Pay Amount
     * @return 返回结果
     */
    public BigDecimal getPayAmount() { return this.payAmount; }
    /**
     * set Pay Amount
     * @param payAmount payAmount
     */
    public void setPayAmount(BigDecimal payAmount) { this.payAmount = payAmount; }
    /**
     * 获取Remark
     * @return 返回结果
     */
    public String getRemark() { return this.remark; }
    /**
     * set Remark
     * @param remark remark
     */
    public void setRemark(String remark) { this.remark = remark; }
    /**
     * 获取Auto Confirm
     * @return 返回结果
     */
    public Integer getAutoConfirm() { return this.autoConfirm; }
    /**
     * set Auto Confirm
     * @param autoConfirm autoConfirm
     */
    public void setAutoConfirm(Integer autoConfirm) { this.autoConfirm = autoConfirm; }
    /**
     * 获取Discount Amount
     * @return 返回结果
     */
    public BigDecimal getDiscountAmount() { return this.discountAmount; }
    /**
     * set Discount Amount
     * @param discountAmount discountAmount
     */
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
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
     * 获取Cancelled At
     * @return 返回结果
     */
    public LocalDateTime getCancelledAt() { return this.cancelledAt; }
    /**
     * set Cancelled At
     * @param cancelledAt cancelledAt
     */
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
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
     * 获取Receiver Name
     * @return 返回结果
     */
    public String getReceiverName() { return this.receiverName; }
    /**
     * set Receiver Name
     * @param receiverName receiverName
     */
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
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
     * 获取Shipping Fee
     * @return 返回结果
     */
    public BigDecimal getShippingFee() { return this.shippingFee; }
    /**
     * set Shipping Fee
     * @param shippingFee shippingFee
     */
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }
    /**
     * 获取Total Amount
     * @return 返回结果
     */
    public BigDecimal getTotalAmount() { return this.totalAmount; }
    /**
     * set Total Amount
     * @param totalAmount totalAmount
     */
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
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
     * 获取Receiver Phone
     * @return 返回结果
     */
    public String getReceiverPhone() { return this.receiverPhone; }
    /**
     * set Receiver Phone
     * @param receiverPhone receiverPhone
     */
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
}
