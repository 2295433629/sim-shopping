package com.sim.shopping.interfaces.dto.cart;

public class UpdateCartItemRequest {
    private Integer quantity;
    private Integer selected;

    public Integer getQuantity() { return this.quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getSelected() { return this.selected; }
    public void setSelected(Integer selected) { this.selected = selected; }
}
