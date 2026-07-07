package com.sim.shopping.interfaces.dto.order;

import java.util.List;

public class CreateOrderRequest {
    private Long addressId;
    private String remark;
    private List<Long> cartItemIds;

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public List<Long> getCartItemIds() { return cartItemIds; }
    public void setCartItemIds(List<Long> cartItemIds) { this.cartItemIds = cartItemIds; }
}
