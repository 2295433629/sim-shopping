package com.sim.shopping.interfaces.dto.schedule;

import java.time.LocalDateTime;

/**
 * 定时任务执行日志响应DTO
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ScheduleLogResponse {

    private Long id;
    private Long jobId;
    private String jobName;
    private String status;
    private String statusText;
    private Long durationMs;
    private String errorMsg;
    private String result;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /** 获取Job Id */
    public Long getJobId() { return this.jobId; }
    /** set Job Id */
    public void setJobId(Long jobId) { this.jobId = jobId; }
    /** 获取Job Name */
    public String getJobName() { return this.jobName; }
    /** set Job Name */
    public void setJobName(String jobName) { this.jobName = jobName; }
    /** 获取Status */
    public String getStatus() { return this.status; }
    /** set Status */
    public void setStatus(String status) { this.status = status; }
    /** 获取Status Text */
    public String getStatusText() { return this.statusText; }
    /** set Status Text */
    public void setStatusText(String statusText) { this.statusText = statusText; }
    /** 获取Duration Ms */
    public Long getDurationMs() { return this.durationMs; }
    /** set Duration Ms */
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    /** 获取Error Msg */
    public String getErrorMsg() { return this.errorMsg; }
    /** set Error Msg */
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
    /** 获取Result */
    public String getResult() { return this.result; }
    /** set Result */
    public void setResult(String result) { this.result = result; }
    /** 获取Start Time */
    public LocalDateTime getStartTime() { return this.startTime; }
    /** set Start Time */
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    /** 获取End Time */
    public LocalDateTime getEndTime() { return this.endTime; }
    /** set End Time */
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
