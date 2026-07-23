package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
/**
 * 定时任务实体，对应 t_schedule_job 表，存储定时任务配置信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_schedule_job")
public class ScheduleJobDO extends BaseEntity {

    @TableField("job_name")
    private String jobName;

    @TableField("job_group")
    private String jobGroup;

    @TableField("bean_name")
    private String beanName;

    @TableField("method_name")
    private String methodName;

    private String params;

    @TableField("cron_expression")
    private String cronExpression;

    @TableField("fixed_delay")
    private Long fixedDelay;

    @TableField("fixed_rate")
    private Long fixedRate;

    private String description;

    private String status;

    private Integer concurrent;

    @TableField("last_run_time")
    private LocalDateTime lastRunTime;

    @TableField("next_run_time")
    private LocalDateTime nextRunTime;

    @TableField("last_run_status")
    private String lastRunStatus;

    @TableField("last_error_msg")
    private String lastErrorMsg;

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
}
