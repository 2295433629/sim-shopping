package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

public class OperationLogItem {
    private Long id;
    private Long operatorId;
    private String operatorName;
    private String operatorType;
    private String module;
    private String operationType;
    private String method;
    private String requestUrl;
    private String ip;
    private Long costTime;
    private LocalDateTime createdAt;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public Long getOperatorId() { return this.operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
    public String getOperatorName() { return this.operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getOperatorType() { return this.operatorType; }
    public void setOperatorType(String operatorType) { this.operatorType = operatorType; }
    public String getModule() { return this.module; }
    public void setModule(String module) { this.module = module; }
    public String getOperationType() { return this.operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getMethod() { return this.method; }
    public void setMethod(String method) { this.method = method; }
    public String getRequestUrl() { return this.requestUrl; }
    public void setRequestUrl(String requestUrl) { this.requestUrl = requestUrl; }
    public String getIp() { return this.ip; }
    public void setIp(String ip) { this.ip = ip; }
    public Long getCostTime() { return this.costTime; }
    public void setCostTime(Long costTime) { this.costTime = costTime; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
