package com.sim.shopping.interfaces.dto.cart;

import java.math.BigDecimal;

/**
 * 购物车项视图对象，用于购物车页面展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CartItemVO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private String productImage;
    private Long skuId;
    private String skuName;
    private BigDecimal price;
    private Integer quantity;
    private Integer stock;
    private Integer selected;
    private Boolean available;

    /**
     * 获取Cart Item Id
     * @return 返回结果
     */
    public Long getCartItemId() { return this.cartItemId; }
    /**
     * set Cart Item Id
     * @param cartItemId cartItemId
     */
    public void setCartItemId(Long cartItemId) { this.cartItemId = cartItemId; }
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
     * 获取Stock
     * @return 返回结果
     */
    public Integer getStock() { return this.stock; }
    /**
     * set Stock
     * @param stock stock
     */
    public void setStock(Integer stock) { this.stock = stock; }
    /**
     * 获取Selected
     * @return 返回结果
     */
    public Integer getSelected() { return this.selected; }
    /**
     * set Selected
     * @param selected selected
     */
    public void setSelected(Integer selected) { this.selected = selected; }
    /**
     * 获取Available
     * @return 返回结果
     */
    public Boolean getAvailable() { return this.available; }
    /**
     * set Available
     * @param available available
     */
    public void setAvailable(Boolean available) { this.available = available; }
}
