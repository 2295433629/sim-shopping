package com.sim.shopping.interfaces.dto.settlement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SettlementRecord响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class SettlementRecordResponse {

    private Long id;
    private String orderNo;
    private BigDecimal amount;
    private String type;
    private String status;
    private String description;
    private LocalDateTime createdAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
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
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
