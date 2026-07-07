package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_operation_log")
public class SysOperationLogDO extends BaseEntity {
    private String operationType;
    private Long costTime;
    private String method;
    private String requestUrl;
    private String requestParams;
    private String operatorName;
    private String responseResult;
    private String module;
    private String ip;
    private Long operatorId;
    private String operatorType;

    public String getOperationType() { return this.operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public Long getCostTime() { return this.costTime; }
    public void setCostTime(Long costTime) { this.costTime = costTime; }
    public String getMethod() { return this.method; }
    public void setMethod(String method) { this.method = method; }
    public String getRequestUrl() { return this.requestUrl; }
    public void setRequestUrl(String requestUrl) { this.requestUrl = requestUrl; }
    public String getRequestParams() { return this.requestParams; }
    public void setRequestParams(String requestParams) { this.requestParams = requestParams; }
    public String getOperatorName() { return this.operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getResponseResult() { return this.responseResult; }
    public void setResponseResult(String responseResult) { this.responseResult = responseResult; }
    public String getModule() { return this.module; }
    public void setModule(String module) { this.module = module; }
    public String getIp() { return this.ip; }
    public void setIp(String ip) { this.ip = ip; }
    public Long getOperatorId() { return this.operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
    public String getOperatorType() { return this.operatorType; }
    public void setOperatorType(String operatorType) { this.operatorType = operatorType; }
}
