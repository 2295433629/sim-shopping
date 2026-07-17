package com.sim.shopping.interfaces.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情视图对象，包含订单完整信息和商品明细
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class OrderDetailVO {
    private Long orderId;
    private String orderNo;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal payAmount;
    private Long shopId;
    private String shopName;
    private List<OrderItemVO> items;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String remark;
    private PaymentInfoVO payment;
    private LogisticsInfoVO logistics;
    private com.sim.shopping.interfaces.dto.refund.RefundVO refund;

    /**
     * 获取Order Id
     * @return 返回结果
     */
    public Long getOrderId() { return orderId; }
    /**
     * set Order Id
     * @param orderId orderId
     */
    public void setOrderId(Long orderId) { this.orderId = orderId; }
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
     * 获取Total Amount
     * @return 返回结果
     */
    public BigDecimal getTotalAmount() { return totalAmount; }
    /**
     * set Total Amount
     * @param totalAmount totalAmount
     */
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    /**
     * 获取Shipping Fee
     * @return 返回结果
     */
    public BigDecimal getShippingFee() { return shippingFee; }
    /**
     * set Shipping Fee
     * @param shippingFee shippingFee
     */
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }
    /**
     * 获取Discount Amount
     * @return 返回结果
     */
    public BigDecimal getDiscountAmount() { return discountAmount; }
    /**
     * set Discount Amount
     * @param discountAmount discountAmount
     */
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    /**
     * 获取Pay Amount
     * @return 返回结果
     */
    public BigDecimal getPayAmount() { return payAmount; }
    /**
     * set Pay Amount
     * @param payAmount payAmount
     */
    public void setPayAmount(BigDecimal payAmount) { this.payAmount = payAmount; }
    /**
     * 获取Shop Id
     * @return 返回结果
     */
    public Long getShopId() { return shopId; }
    /**
     * set Shop Id
     * @param shopId shopId
     */
    public void setShopId(Long shopId) { this.shopId = shopId; }
    /**
     * 获取Shop Name
     * @return 返回结果
     */
    public String getShopName() { return shopName; }
    /**
     * set Shop Name
     * @param shopName shopName
     */
    public void setShopName(String shopName) { this.shopName = shopName; }
    /**
     * 获取Items
     * @return 返回结果
     */
    public List<OrderItemVO> getItems() { return items; }
    /**
     * set Items
     * @param items items
     */
    public void setItems(List<OrderItemVO> items) { this.items = items; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * 获取Paid At
     * @return 返回结果
     */
    public LocalDateTime getPaidAt() { return paidAt; }
    /**
     * set Paid At
     * @param paidAt paidAt
     */
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
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
     * 获取Completed At
     * @return 返回结果
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * set Completed At
     * @param completedAt completedAt
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    /**
     * 获取Cancelled At
     * @return 返回结果
     */
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    /**
     * set Cancelled At
     * @param cancelledAt cancelledAt
     */
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
    /**
     * 获取Receiver Name
     * @return 返回结果
     */
    public String getReceiverName() { return receiverName; }
    /**
     * set Receiver Name
     * @param receiverName receiverName
     */
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    /**
     * 获取Receiver Phone
     * @return 返回结果
     */
    public String getReceiverPhone() { return receiverPhone; }
    /**
     * set Receiver Phone
     * @param receiverPhone receiverPhone
     */
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    /**
     * 获取Receiver Address
     * @return 返回结果
     */
    public String getReceiverAddress() { return receiverAddress; }
    /**
     * set Receiver Address
     * @param receiverAddress receiverAddress
     */
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }
    /**
     * 获取Remark
     * @return 返回结果
     */
    public String getRemark() { return remark; }
    /**
     * set Remark
     * @param remark remark
     */
    public void setRemark(String remark) { this.remark = remark; }
    /**
     * 获取Payment
     * @return 返回结果
     */
    public PaymentInfoVO getPayment() { return payment; }
    /**
     * set Payment
     * @param payment payment
     */
    public void setPayment(PaymentInfoVO payment) { this.payment = payment; }
    /**
     * 获取Logistics
     * @return 返回结果
     */
    public LogisticsInfoVO getLogistics() { return logistics; }
    /**
     * set Logistics
     * @param logistics logistics
     */
    public void setLogistics(LogisticsInfoVO logistics) { this.logistics = logistics; }
    /**
     * 获取Refund
     * @return 返回结果
     */
    public com.sim.shopping.interfaces.dto.refund.RefundVO getRefund() { return refund; }
    /**
     * set Refund
     * @param refund refund
     */
    public void setRefund(com.sim.shopping.interfaces.dto.refund.RefundVO refund) { this.refund = refund; }
}
