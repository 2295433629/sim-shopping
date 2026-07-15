package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
@TableName("t_shop")
public class ShopDO extends BaseEntity {
    private String status;
    private String shopLogo;
    private Long merchantId;
    private String shopName;
    private String description;

    @TableField("balance")
    private BigDecimal balance;

    @TableField("total_income")
    private BigDecimal totalIncome;

    @TableField("total_settled")
    private BigDecimal totalSettled;

    @TableField("frozen_amount")
    private BigDecimal frozenAmount;

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
    public BigDecimal getBalance() { return this.balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal getTotalIncome() { return this.totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    public BigDecimal getTotalSettled() { return this.totalSettled; }
    public void setTotalSettled(BigDecimal totalSettled) { this.totalSettled = totalSettled; }
    public BigDecimal getFrozenAmount() { return this.frozenAmount; }
    public void setFrozenAmount(BigDecimal frozenAmount) { this.frozenAmount = frozenAmount; }
}
