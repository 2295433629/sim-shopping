package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_order_item")
public class OrderItemDO extends BaseEntity {
    private String skuName;
    private BigDecimal subtotal;
    private BigDecimal price;
    private Long skuId;
    private Long productId;
    private String productImage;
    private Integer quantity;
    private String productName;
    private Long orderId;

    public String getSkuName() { return this.skuName; }
    public void setSkuName(String skuName) { this.skuName = skuName; }
    public BigDecimal getSubtotal() { return this.subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getPrice() { return this.price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Long getSkuId() { return this.skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductImage() { return this.productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    public Integer getQuantity() { return this.quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getProductName() { return this.productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}
