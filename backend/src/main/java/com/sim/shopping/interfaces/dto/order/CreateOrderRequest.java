package com.sim.shopping.interfaces.dto.order;

import java.util.List;

/**
 * 创建订单请求DTO，包含收货地址ID、备注和购物车项ID列表
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CreateOrderRequest {
    private Long addressId;
    private String remark;
    private List<Long> cartItemIds;

    /**
     * 获取Address Id
     * @return 返回结果
     */
    public Long getAddressId() { return addressId; }
    /**
     * set Address Id
     * @param addressId addressId
     */
    public void setAddressId(Long addressId) { this.addressId = addressId; }
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
     * 获取Cart Item Ids
     * @return 返回结果
     */
    public List<Long> getCartItemIds() { return cartItemIds; }
    /**
     * set Cart Item Ids
     * @param cartItemIds cartItemIds
     */
    public void setCartItemIds(List<Long> cartItemIds) { this.cartItemIds = cartItemIds; }
}
