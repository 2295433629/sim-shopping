package com.sim.shopping.application.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.persistence.entity.ScheduleJobDO;
import com.sim.shopping.infrastructure.persistence.entity.ScheduleLogDO;
import com.sim.shopping.infrastructure.persistence.mapper.ScheduleJobMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ScheduleLogMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.schedule.ScheduleJobRequest;
import com.sim.shopping.interfaces.dto.schedule.ScheduleJobResponse;
import com.sim.shopping.interfaces.dto.schedule.ScheduleLogResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时任务服务层，提供任务的增删改查、启停、手动执行及历史日志查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class ScheduleJobService {

    private static final Map<String, String> STATUS_TEXT_MAP = new HashMap<>();
    private static final Map<String, String> LOG_STATUS_TEXT_MAP = new HashMap<>();

    static {
        STATUS_TEXT_MAP.put(SystemConstants.SCHEDULE_JOB_STATUS_ACTIVE, "运行中");
        STATUS_TEXT_MAP.put(SystemConstants.SCHEDULE_JOB_STATUS_RUNNING, "执行中");
        STATUS_TEXT_MAP.put(SystemConstants.SCHEDULE_JOB_STATUS_PAUSED, "已暂停");
        STATUS_TEXT_MAP.put(SystemConstants.SCHEDULE_JOB_STATUS_ERROR, "异常");

        LOG_STATUS_TEXT_MAP.put("SUCCESS", "成功");
        LOG_STATUS_TEXT_MAP.put("FAIL", "失败");
    }

    private final ScheduleJobMapper scheduleJobMapper;
    private final ScheduleLogMapper scheduleLogMapper;
    private final ScheduleManager scheduleManager;

    public ScheduleJobService(ScheduleJobMapper scheduleJobMapper,
                              ScheduleLogMapper scheduleLogMapper,
                              ScheduleManager scheduleManager) {
        this.scheduleJobMapper = scheduleJobMapper;
        this.scheduleLogMapper = scheduleLogMapper;
        this.scheduleManager = scheduleManager;
    }

    /**
     * 分页查询任务列表
     *
     * @param page    页码
     * @param size    每页条数
     * @param keyword 关键词（任务名称模糊匹配）
     * @param group   任务分组
     * @return 分页结果
     */
    public PageResponse<ScheduleJobResponse> getJobList(int page, int size, String keyword, String group) {
        Page<ScheduleJobDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ScheduleJobDO> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ScheduleJobDO::getJobName, keyword);
        }
        if (group != null && !group.isEmpty()) {
            wrapper.eq(ScheduleJobDO::getJobGroup, group);
        }
        wrapper.orderByDesc(ScheduleJobDO::getCreatedAt);

        Page<ScheduleJobDO> result = scheduleJobMapper.selectPage(pageObj, wrapper);

        List<ScheduleJobResponse> list = result.getRecords().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 获取任务详情
     *
     * @param jobId 任务ID
     * @return 任务详情
     */
    public ScheduleJobResponse getJobDetail(Long jobId) {
        ScheduleJobDO job = scheduleJobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(404, "任务不存在: " + jobId);
        }
        return toResponse(job);
    }

    /**
     * 创建新任务
     *
     * @param request 任务创建请求
     * @return 创建后的任务详情
     */
    @Transactional
    public ScheduleJobResponse createJob(ScheduleJobRequest request) {
        validateScheduleConfig(request);

        ScheduleJobDO job = new ScheduleJobDO();
        job.setJobName(request.getJobName());
        job.setJobGroup(request.getJobGroup() != null ? request.getJobGroup() : "default");
        job.setBeanName(request.getBeanName());
        job.setMethodName(request.getMethodName());
        job.setParams(request.getParams());
        job.setCronExpression(request.getCronExpression());
        job.setFixedDelay(request.getFixedDelay());
        job.setFixedRate(request.getFixedRate());
        job.setDescription(request.getDescription());
        job.setStatus(SystemConstants.SCHEDULE_JOB_STATUS_ACTIVE);
        job.setConcurrent(request.getConcurrent() != null ? request.getConcurrent() : 0);

        scheduleJobMapper.insert(job);

        // 注册到调度器
        scheduleManager.schedule(job.getId());

        return toResponse(job);
    }

    /**
     * 更新任务
     *
     * @param jobId   任务ID
     * @param request 任务更新请求
     * @return 更新后的任务详情
     */
    @Transactional
    public ScheduleJobResponse updateJob(Long jobId, ScheduleJobRequest request) {
        validateScheduleConfig(request);

        ScheduleJobDO job = scheduleJobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(404, "任务不存在: " + jobId);
        }

        // 先取消旧调度
        scheduleManager.cancel(jobId);

        // 更新字段
        job.setJobName(request.getJobName());
        job.setJobGroup(request.getJobGroup() != null ? request.getJobGroup() : "default");
        job.setBeanName(request.getBeanName());
        job.setMethodName(request.getMethodName());
        job.setParams(request.getParams());
        job.setCronExpression(request.getCronExpression());
        job.setFixedDelay(request.getFixedDelay());
        job.setFixedRate(request.getFixedRate());
        job.setDescription(request.getDescription());
        job.setConcurrent(request.getConcurrent() != null ? request.getConcurrent() : 0);

        scheduleJobMapper.updateById(job);

        // 如果任务状态为ACTIVE，重新注册调度
        if (SystemConstants.SCHEDULE_JOB_STATUS_ACTIVE.equals(job.getStatus())) {
            scheduleManager.schedule(jobId);
        }

        return toResponse(job);
    }

    /**
     * 删除任务（取消调度 + 软删除）
     *
     * @param jobId 任务ID
     */
    @Transactional
    public void deleteJob(Long jobId) {
        ScheduleJobDO job = scheduleJobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(404, "任务不存在: " + jobId);
        }

        // 取消调度
        scheduleManager.cancel(jobId);

        // 软删除
        scheduleJobMapper.deleteById(jobId);
    }

    /**
     * 暂停任务
     *
     * @param jobId 任务ID
     */
    @Transactional
    public void pauseJob(Long jobId) {
        ScheduleJobDO job = scheduleJobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(404, "任务不存在: " + jobId);
        }

        // 取消调度
        scheduleManager.cancel(jobId);

        job.setStatus(SystemConstants.SCHEDULE_JOB_STATUS_PAUSED);
        scheduleJobMapper.updateById(job);
    }

    /**
     * 恢复任务
     *
     * @param jobId 任务ID
     */
    @Transactional
    public void resumeJob(Long jobId) {
        ScheduleJobDO job = scheduleJobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(404, "任务不存在: " + jobId);
        }

        job.setStatus(SystemConstants.SCHEDULE_JOB_STATUS_ACTIVE);
        scheduleJobMapper.updateById(job);

        // 重新注册调度
        scheduleManager.schedule(jobId);
    }

    /**
     * 手动执行一次任务
     *
     * @param jobId 任务ID
     */
    public void executeJob(Long jobId) {
        scheduleManager.execute(jobId);
    }

    /**
     * 查询任务执行历史
     *
     * @param jobId 任务ID
     * @param page  页码
     * @param size  每页条数
     * @return 分页日志结果
     */
    public PageResponse<ScheduleLogResponse> getJobLogs(Long jobId, int page, int size) {
        Page<ScheduleLogDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ScheduleLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduleLogDO::getJobId, jobId);
        wrapper.orderByDesc(ScheduleLogDO::getStartTime);

        Page<ScheduleLogDO> result = scheduleLogMapper.selectPage(pageObj, wrapper);

        List<ScheduleLogResponse> list = result.getRecords().stream()
                .map(this::toLogResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 获取所有任务分组
     *
     * @return 分组列表
     */
    public List<String> getJobGroups() {
        List<ScheduleJobDO> jobs = scheduleJobMapper.selectList(null);

        return jobs.stream()
                .map(ScheduleJobDO::getJobGroup)
                .filter(g -> g != null && !g.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    // ==================== 私有方法 ====================

    /**
     * 校验调度配置：cron/fixedDelay/fixedRate 至少配置一个
     */
    private void validateScheduleConfig(ScheduleJobRequest request) {
        boolean hasCron = request.getCronExpression() != null && !request.getCronExpression().isEmpty();
        boolean hasFixedDelay = request.getFixedDelay() != null;
        boolean hasFixedRate = request.getFixedRate() != null;

        if (!hasCron && !hasFixedDelay && !hasFixedRate) {
            throw new BusinessException(400, "必须配置调度策略：cronExpression、fixedDelay、fixedRate 至少配置一个");
        }
    }

    /**
     * 实体转响应DTO
     */
    private ScheduleJobResponse toResponse(ScheduleJobDO job) {
        ScheduleJobResponse resp = new ScheduleJobResponse();
        resp.setId(job.getId());
        resp.setJobName(job.getJobName());
        resp.setJobGroup(job.getJobGroup());
        resp.setBeanName(job.getBeanName());
        resp.setMethodName(job.getMethodName());
        resp.setParams(job.getParams());
        resp.setCronExpression(job.getCronExpression());
        resp.setFixedDelay(job.getFixedDelay());
        resp.setFixedRate(job.getFixedRate());
        resp.setDescription(job.getDescription());
        resp.setStatus(job.getStatus());
        resp.setStatusText(STATUS_TEXT_MAP.getOrDefault(job.getStatus(), job.getStatus()));
        resp.setConcurrent(job.getConcurrent());
        resp.setLastRunTime(job.getLastRunTime());
        resp.setNextRunTime(job.getNextRunTime());
        resp.setLastRunStatus(job.getLastRunStatus());
        resp.setLastErrorMsg(job.getLastErrorMsg());
        resp.setCreatedAt(job.getCreatedAt());
        resp.setUpdatedAt(job.getUpdatedAt());
        return resp;
    }

    /**
     * 日志实体转响应DTO
     */
    private ScheduleLogResponse toLogResponse(ScheduleLogDO logDO) {
        ScheduleLogResponse resp = new ScheduleLogResponse();
        resp.setId(logDO.getId());
        resp.setJobId(logDO.getJobId());
        resp.setJobName(logDO.getJobName());
        resp.setStatus(logDO.getStatus());
        resp.setStatusText(LOG_STATUS_TEXT_MAP.getOrDefault(logDO.getStatus(), logDO.getStatus()));
        resp.setDurationMs(logDO.getDurationMs());
        resp.setErrorMsg(logDO.getErrorMsg());
        resp.setResult(logDO.getResult());
        resp.setStartTime(logDO.getStartTime());
        resp.setEndTime(logDO.getEndTime());
        resp.setCreatedAt(logDO.getCreatedAt());
        return resp;
    }
}
