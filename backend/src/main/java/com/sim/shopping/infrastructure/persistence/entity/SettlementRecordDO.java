package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName("t_settlement_record")
public class SettlementRecordDO extends BaseEntity {

    @TableField("shop_id")
    private Long shopId;

    @TableField("order_id")
    private Long orderId;

    @TableField("order_no")
    private String orderNo;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("type")
    private String type;

    @TableField("status")
    private String status;

    @TableField("description")
    private String description;

    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public BigDecimal getAmount() { return this.amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getType() { return this.type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
}
