package com.sim.shopping.application.schedule;

import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.persistence.entity.ScheduleJobDO;
import com.sim.shopping.infrastructure.persistence.entity.ScheduleLogDO;
import com.sim.shopping.infrastructure.persistence.mapper.ScheduleJobMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ScheduleLogMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 动态定时任务管理器，负责在运行时注册、取消、执行定时任务并记录执行日志。
 * <p>
 * 核心职责：
 * <ul>
 *   <li>启动时从数据库加载所有 ACTIVE 任务并注册到 TaskScheduler</li>
 *   <li>添加任务：注册到 TaskScheduler</li>
 *   <li>暂停任务：取消对应的 ScheduledFuture</li>
 *   <li>恢复任务：重新注册</li>
 *   <li>更新任务：先取消再重新注册</li>
 *   <li>手动执行任务：通过反射调用 bean.method()</li>
 *   <li>记录执行日志到 t_schedule_log</li>
 * </ul>
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Component
public class ScheduleManager {

    private static final Logger log = LoggerFactory.getLogger(ScheduleManager.class);

    /** jobId -> ScheduledFuture 映射，用于取消任务 */
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    /** 正在执行中的 jobId 集合，用于禁止并发执行时跳过重复调度 */
    private final Set<Long> runningJobs = ConcurrentHashMap.newKeySet();

    private final TaskScheduler taskScheduler;
    private final ApplicationContext applicationContext;
    private final ScheduleJobMapper scheduleJobMapper;
    private final ScheduleLogMapper scheduleLogMapper;

    public ScheduleManager(TaskScheduler taskScheduler,
                           ApplicationContext applicationContext,
                           ScheduleJobMapper scheduleJobMapper,
                           ScheduleLogMapper scheduleLogMapper) {
        this.taskScheduler = taskScheduler;
        this.applicationContext = applicationContext;
        this.scheduleJobMapper = scheduleJobMapper;
        this.scheduleLogMapper = scheduleLogMapper;
    }

    /**
     * 初始化：加载所有 ACTIVE 任务并注册到调度器
     */
    @PostConstruct
    public void init() {
        try {
            List<ScheduleJobDO> jobs = scheduleJobMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ScheduleJobDO>()
                            .eq(ScheduleJobDO::getStatus, SystemConstants.SCHEDULE_JOB_STATUS_ACTIVE));

            int successCount = 0;
            for (ScheduleJobDO job : jobs) {
                try {
                    schedule(job.getId());
                    successCount++;
                    log.info("定时任务已注册: jobId={}, jobName={}, bean={}, method={}",
                            job.getId(), job.getJobName(), job.getBeanName(), job.getMethodName());
                } catch (Exception e) {
                    log.error("定时任务注册失败: jobId={}, jobName={}, error={}",
                            job.getId(), job.getJobName(), e.getMessage());
                }
            }

            log.info("定时任务初始化完成，共注册 {}/{} 个任务", successCount, jobs.size());
        } catch (Exception e) {
            log.error("定时任务初始化异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 注册任务到调度器
     *
     * @param jobId 任务ID
     */
    public void schedule(Long jobId) {
        // 先取消已存在的调度
        cancel(jobId);

        ScheduleJobDO job = scheduleJobMapper.selectById(jobId);
        if (job == null) {
            log.warn("任务不存在，无法调度: jobId={}", jobId);
            return;
        }

        if (!SystemConstants.SCHEDULE_JOB_STATUS_ACTIVE.equals(job.getStatus())) {
            log.info("任务状态非ACTIVE，跳过调度: jobId={}, status={}", jobId, job.getStatus());
            return;
        }

        Runnable runnable = () -> {
            try {
                // 每次执行前重新加载最新的任务配置
                ScheduleJobDO latestJob = scheduleJobMapper.selectById(jobId);
                if (latestJob != null && SystemConstants.SCHEDULE_JOB_STATUS_ACTIVE.equals(latestJob.getStatus())) {
                    executeJob(latestJob);
                }
            } catch (Exception e) {
                log.error("定时任务执行异常: jobId={}, error={}", jobId, e.getMessage(), e);
            }
        };

        ScheduledFuture<?> future;
        try {
            if (job.getCronExpression() != null && !job.getCronExpression().isEmpty()) {
                future = taskScheduler.schedule(runnable, new CronTrigger(job.getCronExpression()));
            } else if (job.getFixedDelay() != null) {
                future = taskScheduler.scheduleWithFixedDelay(runnable, Duration.ofMillis(job.getFixedDelay()));
            } else if (job.getFixedRate() != null) {
                future = taskScheduler.scheduleAtFixedRate(runnable, Duration.ofMillis(job.getFixedRate()));
            } else {
                log.error("任务未配置调度策略（cron/fixedDelay/fixedRate均为空）: jobId={}", jobId);
                throw new BusinessException(400, "任务未配置调度策略（cron/fixedDelay/fixedRate均为空）");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("任务调度注册失败: jobId={}, error={}", jobId, e.getMessage());
            job.setStatus("ERROR");
            job.setLastErrorMsg("调度注册失败: " + e.getMessage());
            scheduleJobMapper.updateById(job);
            throw new BusinessException(500, "任务调度注册失败: " + e.getMessage());
        }

        scheduledTasks.put(jobId, future);
    }

    /**
     * 取消任务调度
     *
     * @param jobId 任务ID
     */
    public void cancel(Long jobId) {
        ScheduledFuture<?> future = scheduledTasks.remove(jobId);
        if (future != null) {
            future.cancel(false);
            log.info("定时任务已取消调度: jobId={}", jobId);
        }
    }

    /**
     * 手动执行一次任务
     *
     * @param jobId 任务ID
     */
    public void execute(Long jobId) {
        ScheduleJobDO job = scheduleJobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(404, "任务不存在: " + jobId);
        }
        executeJob(job);
    }

    /**
     * 反射执行任务并记录日志
     *
     * @param job 任务配置
     */
    private void executeJob(ScheduleJobDO job) {
        boolean allowConcurrent = job.getConcurrent() != null && job.getConcurrent() == 1;
        boolean addedToRunning = false;

        if (!allowConcurrent) {
            if (!runningJobs.add(job.getId())) {
                log.info("任务正在执行中，跳过本次调度: jobId={}, jobName={}", job.getId(), job.getJobName());
                return;
            }
            addedToRunning = true;
        }

        LocalDateTime startTime = LocalDateTime.now();
        long startNano = System.nanoTime();

        ScheduleLogDO logDO = new ScheduleLogDO();
        logDO.setJobId(job.getId());
        logDO.setJobName(job.getJobName());
        logDO.setStartTime(startTime);

        try {
            Object bean = applicationContext.getBean(job.getBeanName());
            Method method = bean.getClass().getMethod(job.getMethodName());
            Object result = method.invoke(bean);

            long durationMs = (System.nanoTime() - startNano) / 1_000_000;
            LocalDateTime endTime = LocalDateTime.now();

            logDO.setStatus("SUCCESS");
            logDO.setDurationMs(durationMs);
            logDO.setEndTime(endTime);
            logDO.setResult(result != null ? result.toString() : "执行成功");

            job.setLastRunTime(startTime);
            job.setLastRunStatus("SUCCESS");
            job.setLastErrorMsg(null);
            job.setNextRunTime(calculateNextRunTime(job));
            scheduleJobMapper.updateById(job);

            log.info("定时任务执行成功: jobId={}, jobName={}, 耗时={}ms", job.getId(), job.getJobName(), durationMs);

        } catch (InvocationTargetException e) {
            // 目标方法抛出的异常
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            long durationMs = (System.nanoTime() - startNano) / 1_000_000;
            LocalDateTime endTime = LocalDateTime.now();

            String errorMsg = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
            logDO.setStatus("FAIL");
            logDO.setDurationMs(durationMs);
            logDO.setEndTime(endTime);
            logDO.setErrorMsg(errorMsg);

            job.setLastRunTime(startTime);
            job.setLastRunStatus("FAIL");
            job.setLastErrorMsg(errorMsg);
            job.setNextRunTime(calculateNextRunTime(job));
            scheduleJobMapper.updateById(job);

            log.error("定时任务执行失败: jobId={}, jobName={}, error={}", job.getId(), job.getJobName(), errorMsg, cause);

        } catch (NoSuchMethodException | IllegalAccessException e) {
            // 方法不存在或不可访问 —— 配置错误，标记为ERROR并取消调度
            long durationMs = (System.nanoTime() - startNano) / 1_000_000;
            LocalDateTime endTime = LocalDateTime.now();

            String errorMsg = "方法不存在或不可访问: " + job.getMethodName() + ", error=" + e.getMessage();
            logDO.setStatus("FAIL");
            logDO.setDurationMs(durationMs);
            logDO.setEndTime(endTime);
            logDO.setErrorMsg(errorMsg);

            job.setStatus("ERROR");
            job.setLastRunTime(startTime);
            job.setLastRunStatus("FAIL");
            job.setLastErrorMsg(errorMsg);
            scheduleJobMapper.updateById(job);

            // 取消调度，避免反复失败
            cancel(job.getId());

            log.error("定时任务配置错误（方法不存在）: jobId={}, jobName={}, error={}", job.getId(), job.getJobName(), errorMsg);

        } catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) {
            // Bean不存在 —— 配置错误，标记为ERROR并取消调度
            long durationMs = (System.nanoTime() - startNano) / 1_000_000;
            LocalDateTime endTime = LocalDateTime.now();

            String errorMsg = "Bean不存在: " + job.getBeanName();
            logDO.setStatus("FAIL");
            logDO.setDurationMs(durationMs);
            logDO.setEndTime(endTime);
            logDO.setErrorMsg(errorMsg);

            job.setStatus("ERROR");
            job.setLastRunTime(startTime);
            job.setLastRunStatus("FAIL");
            job.setLastErrorMsg(errorMsg);
            scheduleJobMapper.updateById(job);

            cancel(job.getId());

            log.error("定时任务配置错误（Bean不存在）: jobId={}, jobName={}, bean={}", job.getId(), job.getJobName(), job.getBeanName());

        } catch (Exception e) {
            // 其他异常
            long durationMs = (System.nanoTime() - startNano) / 1_000_000;
            LocalDateTime endTime = LocalDateTime.now();

            String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
            logDO.setStatus("FAIL");
            logDO.setDurationMs(durationMs);
            logDO.setEndTime(endTime);
            logDO.setErrorMsg(errorMsg);

            job.setLastRunTime(startTime);
            job.setLastRunStatus("FAIL");
            job.setLastErrorMsg(errorMsg);
            job.setNextRunTime(calculateNextRunTime(job));
            scheduleJobMapper.updateById(job);

            log.error("定时任务执行异常: jobId={}, jobName={}, error={}", job.getId(), job.getJobName(), errorMsg, e);

        } finally {
            try {
                scheduleLogMapper.insert(logDO);
            } catch (Exception logEx) {
                log.error("定时任务日志保存失败: jobId={}, error={}", job.getId(), logEx.getMessage());
            }
            if (addedToRunning) {
                runningJobs.remove(job.getId());
            }
        }
    }

    /**
     * 根据调度策略计算下次执行时间
     *
     * @param job 任务配置
     * @return 下次执行时间，若无法计算则返回null
     */
    private LocalDateTime calculateNextRunTime(ScheduleJobDO job) {
        try {
            if (job.getCronExpression() != null && !job.getCronExpression().isEmpty()) {
                CronTrigger trigger = new CronTrigger(job.getCronExpression());
                SimpleTriggerContext context = new SimpleTriggerContext();
                Instant next = trigger.nextExecution(context);
                return next != null ? LocalDateTime.ofInstant(next, ZoneId.systemDefault()) : null;
            } else if (job.getFixedDelay() != null) {
                return LocalDateTime.now().plusNanos(job.getFixedDelay() * 1_000_000L);
            } else if (job.getFixedRate() != null) {
                return LocalDateTime.now().plusNanos(job.getFixedRate() * 1_000_000L);
            }
        } catch (Exception e) {
            log.warn("计算下次执行时间失败: jobId={}, error={}", job.getId(), e.getMessage());
        }
        return null;
    }

    /**
     * 检查任务是否正在调度中
     *
     * @param jobId 任务ID
     * @return true表示已注册调度
     */
    public boolean isScheduled(Long jobId) {
        return scheduledTasks.containsKey(jobId);
    }
}
