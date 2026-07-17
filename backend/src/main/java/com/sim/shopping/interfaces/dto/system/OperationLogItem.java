package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

/**
 * OperationLogItem
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /**
     * 获取Operator Id
     * @return 返回结果
     */
    public Long getOperatorId() { return this.operatorId; }
    /**
     * set Operator Id
     * @param operatorId operatorId
     */
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
    /**
     * 获取Operator Name
     * @return 返回结果
     */
    public String getOperatorName() { return this.operatorName; }
    /**
     * set Operator Name
     * @param operatorName operatorName
     */
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    /**
     * 获取Operator Type
     * @return 返回结果
     */
    public String getOperatorType() { return this.operatorType; }
    /**
     * set Operator Type
     * @param operatorType operatorType
     */
    public void setOperatorType(String operatorType) { this.operatorType = operatorType; }
    /**
     * 获取Module
     * @return 返回结果
     */
    public String getModule() { return this.module; }
    /**
     * set Module
     * @param module module
     */
    public void setModule(String module) { this.module = module; }
    /**
     * 获取Operation Type
     * @return 返回结果
     */
    public String getOperationType() { return this.operationType; }
    /**
     * set Operation Type
     * @param operationType operationType
     */
    public void setOperationType(String operationType) { this.operationType = operationType; }
    /**
     * 获取Method
     * @return 返回结果
     */
    public String getMethod() { return this.method; }
    /**
     * set Method
     * @param method method
     */
    public void setMethod(String method) { this.method = method; }
    /**
     * 获取Request Url
     * @return 返回结果
     */
    public String getRequestUrl() { return this.requestUrl; }
    /**
     * set Request Url
     * @param requestUrl requestUrl
     */
    public void setRequestUrl(String requestUrl) { this.requestUrl = requestUrl; }
    /**
     * 获取Ip
     * @return 返回结果
     */
    public String getIp() { return this.ip; }
    /**
     * set Ip
     * @param ip ip
     */
    public void setIp(String ip) { this.ip = ip; }
    /**
     * 获取Cost Time
     * @return 返回结果
     */
    public Long getCostTime() { return this.costTime; }
    /**
     * set Cost Time
     * @param costTime costTime
     */
    public void setCostTime(Long costTime) { this.costTime = costTime; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
