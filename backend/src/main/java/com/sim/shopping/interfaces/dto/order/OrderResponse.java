package com.sim.shopping.interfaces.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单响应DTO，包含订单基本信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class OrderResponse {
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
}
