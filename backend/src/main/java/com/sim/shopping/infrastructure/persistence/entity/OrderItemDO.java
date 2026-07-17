package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 订单项实体，对应 t_order_item 表，存储订单商品明细
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取Sku Name
     * @return 返回结果
     */
    public String getSkuName() { return this.skuName; }
    /**
     * set Sku Name
     * @param skuName skuName
     */
    public void setSkuName(String skuName) { this.skuName = skuName; }
    /**
     * 获取Subtotal
     * @return 返回结果
     */
    public BigDecimal getSubtotal() { return this.subtotal; }
    /**
     * set Subtotal
     * @param subtotal subtotal
     */
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    /**
     * 获取Price
     * @return 返回结果
     */
    public BigDecimal getPrice() { return this.price; }
    /**
     * set Price
     * @param price price
     */
    public void setPrice(BigDecimal price) { this.price = price; }
    /**
     * 获取Sku Id
     * @return 返回结果
     */
    public Long getSkuId() { return this.skuId; }
    /**
     * set Sku Id
     * @param skuId skuId
     */
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() { return this.productId; }
    /**
     * set Product Id
     * @param productId productId
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * 获取Product Image
     * @return 返回结果
     */
    public String getProductImage() { return this.productImage; }
    /**
     * set Product Image
     * @param productImage productImage
     */
    public void setProductImage(String productImage) { this.productImage = productImage; }
    /**
     * 获取Quantity
     * @return 返回结果
     */
    public Integer getQuantity() { return this.quantity; }
    /**
     * set Quantity
     * @param quantity quantity
     */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    /**
     * 获取Product Name
     * @return 返回结果
     */
    public String getProductName() { return this.productName; }
    /**
     * set Product Name
     * @param productName productName
     */
    public void setProductName(String productName) { this.productName = productName; }
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
}
