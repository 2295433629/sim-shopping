package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_shop")
public class ShopDO extends BaseEntity {
    private String status;
    private String shopLogo;
    private Long merchantId;
    private String shopName;
    private String description;

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getShopLogo() { return this.shopLogo; }
    public void setShopLogo(String shopLogo) { this.shopLogo = shopLogo; }
    public Long getMerchantId() { return this.merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public String getShopName() { return this.shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
}
