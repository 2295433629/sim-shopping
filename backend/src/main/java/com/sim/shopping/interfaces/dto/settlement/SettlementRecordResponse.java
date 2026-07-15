package com.sim.shopping.interfaces.dto.settlement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SettlementRecordResponse {

    private Long id;
    private String orderNo;
    private BigDecimal amount;
    private String type;
    private String status;
    private String description;
    private LocalDateTime createdAt;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
