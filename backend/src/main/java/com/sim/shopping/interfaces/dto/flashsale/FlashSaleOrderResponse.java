package com.sim.shopping.interfaces.dto.flashsale;

import java.math.BigDecimal;

/**
 * FlashSaleOrder响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class FlashSaleOrderResponse {

    private Long orderId;
    private String orderNo;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal payAmount;
    private Long saleId;
    private BigDecimal flashPrice;

    /**
     * 获取Order Id
     * @return 返回结果
     */
    public Long getOrderId() {
        return this.orderId;
    }

    /**
     * set Order Id
     * @param orderId orderId
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() {
        return this.orderNo;
    }

    /**
     * set Order No
     * @param orderNo orderNo
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取Total Amount
     * @return 返回结果
     */
    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    /**
     * set Total Amount
     * @param totalAmount totalAmount
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * 获取Shipping Fee
     * @return 返回结果
     */
    public BigDecimal getShippingFee() {
        return this.shippingFee;
    }

    /**
     * set Shipping Fee
     * @param shippingFee shippingFee
     */
    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    /**
     * 获取Discount Amount
     * @return 返回结果
     */
    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    /**
     * set Discount Amount
     * @param discountAmount discountAmount
     */
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * 获取Pay Amount
     * @return 返回结果
     */
    public BigDecimal getPayAmount() {
        return this.payAmount;
    }

    /**
     * set Pay Amount
     * @param payAmount payAmount
     */
    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * 获取Sale Id
     * @return 返回结果
     */
    public Long getSaleId() {
        return this.saleId;
    }

    /**
     * set Sale Id
     * @param saleId saleId
     */
    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    /**
     * 获取Flash Price
     * @return 返回结果
     */
    public BigDecimal getFlashPrice() {
        return this.flashPrice;
    }

    /**
     * set Flash Price
     * @param flashPrice flashPrice
     */
    public void setFlashPrice(BigDecimal flashPrice) {
        this.flashPrice = flashPrice;
    }
}
