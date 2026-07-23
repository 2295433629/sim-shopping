package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
/**
 * 定时任务执行日志实体，对应 t_schedule_log 表，记录每次任务执行的结果
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_schedule_log")
public class ScheduleLogDO extends BaseEntity {

    @TableField("job_id")
    private Long jobId;

    @TableField("job_name")
    private String jobName;

    private String status;

    @TableField("duration_ms")
    private Long durationMs;

    @TableField("error_msg")
    private String errorMsg;

    private String result;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

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
}
