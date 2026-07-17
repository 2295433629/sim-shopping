package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

/**
 * SettlementRecord数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取Shop Id
     * @return 返回结果
     */
    public Long getShopId() { return this.shopId; }
    /**
     * set Shop Id
     * @param shopId shopId
     */
    public void setShopId(Long shopId) { this.shopId = shopId; }
    /**
     * 获取Order Id
     * @return 返回结果
     */
    public Long getOrderId() { return this.orderId; }
    /**
     * set Order Id
     * @param orderId orderId
     */
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return this.orderNo; }
    /**
     * set Order No
     * @param orderNo orderNo
     */
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    /**
     * 获取Amount
     * @return 返回结果
     */
    public BigDecimal getAmount() { return this.amount; }
    /**
     * set Amount
     * @param amount amount
     */
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    /** 获取Type */
    public String getType() { return this.type; }
    /** set Type */
    public void setType(String type) { this.type = type; }
    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() { return this.description; }
    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) { this.description = description; }
}
