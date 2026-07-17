package com.sim.shopping.interfaces.dto.flashsale;

/**
 * FlashSaleOrder请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class FlashSaleOrderRequest {

    private Long addressId;
    private Integer quantity;

    /**
     * 获取Address Id
     * @return 返回结果
     */
    public Long getAddressId() {
        return this.addressId;
    }

    /**
     * set Address Id
     * @param addressId addressId
     */
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    /**
     * 获取Quantity
     * @return 返回结果
     */
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * set Quantity
     * @param quantity quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
