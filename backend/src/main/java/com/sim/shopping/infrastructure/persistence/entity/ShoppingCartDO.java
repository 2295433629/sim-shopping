package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_shopping_cart")
public class ShoppingCartDO extends BaseEntity {
    private Long userId;
    private Long shopId;

    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
}
