package com.sim.shopping.interfaces.dto.schedule;

import jakarta.validation.constraints.NotBlank;

/**
 * 定时任务创建/更新请求DTO
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ScheduleJobRequest {

    @NotBlank(message = "任务名称不能为空")
    private String jobName;

    private String jobGroup;

    @NotBlank(message = "执行Bean名称不能为空")
    private String beanName;

    @NotBlank(message = "执行方法名不能为空")
    private String methodName;

    private String params;

    private String cronExpression;

    private Long fixedDelay;

    private Long fixedRate;

    private String description;

    private Integer concurrent;

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
    /** 获取Concurrent */
    public Integer getConcurrent() { return this.concurrent; }
    /** set Concurrent */
    public void setConcurrent(Integer concurrent) { this.concurrent = concurrent; }
}
