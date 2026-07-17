package com.sim.shopping.interfaces.dto.cart;

/**
 * AddToCart请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class AddToCartRequest {
    private Long productId;
    private Long skuId;
    private Integer quantity;

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
     * 获取Quantity
     * @return 返回结果
     */
    public Integer getQuantity() { return this.quantity; }
    /**
     * set Quantity
     * @param quantity quantity
     */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
