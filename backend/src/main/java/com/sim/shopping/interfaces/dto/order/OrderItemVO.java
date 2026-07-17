package com.sim.shopping.interfaces.dto.order;

import java.math.BigDecimal;

/**
 * 订单商品项视图对象，包含商品信息和购买数量
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class OrderItemVO {
    private Long productId;
    private String productName;
    private String productImage;
    private Long skuId;
    private String skuName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;

    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() { return productId; }
    /**
     * set Product Id
     * @param productId productId
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * 获取Product Name
     * @return 返回结果
     */
    public String getProductName() { return productName; }
    /**
     * set Product Name
     * @param productName productName
     */
    public void setProductName(String productName) { this.productName = productName; }
    /**
     * 获取Product Image
     * @return 返回结果
     */
    public String getProductImage() { return productImage; }
    /**
     * set Product Image
     * @param productImage productImage
     */
    public void setProductImage(String productImage) { this.productImage = productImage; }
    /**
     * 获取Sku Id
     * @return 返回结果
     */
    public Long getSkuId() { return skuId; }
    /**
     * set Sku Id
     * @param skuId skuId
     */
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    /**
     * 获取Sku Name
     * @return 返回结果
     */
    public String getSkuName() { return skuName; }
    /**
     * set Sku Name
     * @param skuName skuName
     */
    public void setSkuName(String skuName) { this.skuName = skuName; }
    /**
     * 获取Price
     * @return 返回结果
     */
    public BigDecimal getPrice() { return price; }
    /**
     * set Price
     * @param price price
     */
    public void setPrice(BigDecimal price) { this.price = price; }
    /**
     * 获取Quantity
     * @return 返回结果
     */
    public Integer getQuantity() { return quantity; }
    /**
     * set Quantity
     * @param quantity quantity
     */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    /**
     * 获取Subtotal
     * @return 返回结果
     */
    public BigDecimal getSubtotal() { return subtotal; }
    /**
     * set Subtotal
     * @param subtotal subtotal
     */
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
