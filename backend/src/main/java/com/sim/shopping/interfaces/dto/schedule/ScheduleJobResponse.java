package com.sim.shopping.interfaces.dto.schedule;

import java.time.LocalDateTime;

/**
 * 定时任务响应DTO，包含任务全量字段及状态中文映射
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ScheduleJobResponse {

    private Long id;
    private String jobName;
    private String jobGroup;
    private String beanName;
    private String methodName;
    private String params;
    private String cronExpression;
    private Long fixedDelay;
    private Long fixedRate;
    private String description;
    private String status;
    private String statusText;
    private Integer concurrent;
    private LocalDateTime lastRunTime;
    private LocalDateTime nextRunTime;
    private String lastRunStatus;
    private String lastErrorMsg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /** 获取Job Name */
    public String getJobName() { return this.jobName; }
    /** set Job Name */
    public void setJobName(String jobName) { this.jobName = jobName; }
    /** 获取Job Group */
    public String getJobGroup() { return this.jobGroup; }
    /** set Job Group */
    public void setJobGroup(String jobGroup) { this.jobGroup = jobGroup; }
    /** 获取Bean Name */
    public String getBeanName() { return this.beanName; }
    /** set Bean Name */
    public void setBeanName(String beanName) { this.beanName = beanName; }
    /** 获取Method Name */
    public String getMethodName() { return this.methodName; }
    /** set Method Name */
    public void setMethodName(String methodName) { this.methodName = methodName; }
    /** 获取Params */
    public String getParams() { return this.params; }
    /** set Params */
    public void setParams(String params) { this.params = params; }
    /** 获取Cron Expression */
    public String getCronExpression() { return this.cronExpression; }
    /** set Cron Expression */
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    /** 获取Fixed Delay */
    public Long getFixedDelay() { return this.fixedDelay; }
    /** set Fixed Delay */
    public void setFixedDelay(Long fixedDelay) { this.fixedDelay = fixedDelay; }
    /** 获取Fixed Rate */
    public Long getFixedRate() { return this.fixedRate; }
    /** set Fixed Rate */
    public void setFixedRate(Long fixedRate) { this.fixedRate = fixedRate; }
    /** 获取Description */
    public String getDescription() { return this.description; }
    /** set Description */
    public void setDescription(String description) { this.description = description; }
    /** 获取Status */
    public String getStatus() { return this.status; }
    /** set Status */
    public void setStatus(String status) { this.status = status; }
    /** 获取Status Text */
    public String getStatusText() { return this.statusText; }
    /** set Status Text */
    public void setStatusText(String statusText) { this.statusText = statusText; }
    /** 获取Concurrent */
    public Integer getConcurrent() { return this.concurrent; }
    /** set Concurrent */
    public void setConcurrent(Integer concurrent) { this.concurrent = concurrent; }
    /** 获取Last Run Time */
    public LocalDateTime getLastRunTime() { return this.lastRunTime; }
    /** set Last Run Time */
    public void setLastRunTime(LocalDateTime lastRunTime) { this.lastRunTime = lastRunTime; }
    /** 获取Next Run Time */
    public LocalDateTime getNextRunTime() { return this.nextRunTime; }
    /** set Next Run Time */
    public void setNextRunTime(LocalDateTime nextRunTime) { this.nextRunTime = nextRunTime; }
    /** 获取Last Run Status */
    public String getLastRunStatus() { return this.lastRunStatus; }
    /** set Last Run Status */
    public void setLastRunStatus(String lastRunStatus) { this.lastRunStatus = lastRunStatus; }
    /** 获取Last Error Msg */
    public String getLastErrorMsg() { return this.lastErrorMsg; }
    /** set Last Error Msg */
    public void setLastErrorMsg(String lastErrorMsg) { this.lastErrorMsg = lastErrorMsg; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** 获取Updated At */
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    /** set Updated At */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
