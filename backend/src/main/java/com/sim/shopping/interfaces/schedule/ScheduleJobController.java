package com.sim.shopping.interfaces.schedule;

import com.sim.shopping.application.schedule.ScheduleJobService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.schedule.ScheduleJobRequest;
import com.sim.shopping.interfaces.dto.schedule.ScheduleJobResponse;
import com.sim.shopping.interfaces.dto.schedule.ScheduleLogResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * 定时任务管理控制器，提供任务的查看、修改、启停、手动执行及历史记录查询接口
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/schedule/jobs")
@PreAuthorize("hasRole('ADMIN')")
public class ScheduleJobController {

    private final ScheduleJobService scheduleJobService;

    public ScheduleJobController(ScheduleJobService scheduleJobService) {
        this.scheduleJobService = scheduleJobService;
    }

    /**
     * 获取任务列表（分页，支持keyword/group筛选）
     *
     * @param page    页码
     * @param size    每页条数
     * @param keyword 关键词
     * @param group   任务分组
     * @return 分页任务列表
     */
    @GetMapping
    public ApiResponse<PageResponse<ScheduleJobResponse>> getJobList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String group) {
        return ApiResponse.success(scheduleJobService.getJobList(page, size, keyword, group));
    }

    /**
     * 获取任务分组列表
     *
     * @return 分组列表
     */
    @GetMapping("/groups")
    public ApiResponse<List<String>> getJobGroups() {
        return ApiResponse.success(scheduleJobService.getJobGroups());
    }

    /**
     * 获取任务详情
     *
     * @param jobId 任务ID
     * @return 任务详情
     */
    @GetMapping("/{jobId}")
    public ApiResponse<ScheduleJobResponse> getJobDetail(@PathVariable Long jobId) {
        return ApiResponse.success(scheduleJobService.getJobDetail(jobId));
    }

    /**
     * 创建任务
     *
     * @param request 任务创建请求
     * @return 创建后的任务详情
     */
    @PostMapping
    @Log(module = "系统", type = "新增")
    public ApiResponse<ScheduleJobResponse> createJob(@Valid @RequestBody ScheduleJobRequest request) {
        return ApiResponse.success(scheduleJobService.createJob(request));
    }

    /**
     * 更新任务
     *
     * @param jobId   任务ID
     * @param request 任务更新请求
     * @return 更新后的任务详情
     */
    @PutMapping("/{jobId}")
    @Log(module = "系统", type = "修改")
    public ApiResponse<ScheduleJobResponse> updateJob(@PathVariable Long jobId,
                                                       @Valid @RequestBody ScheduleJobRequest request) {
        return ApiResponse.success(scheduleJobService.updateJob(jobId, request));
    }

    /**
     * 删除任务
     *
     * @param jobId 任务ID
     * @return 操作结果
     */
    @DeleteMapping("/{jobId}")
    @Log(module = "系统", type = "删除")
    public ApiResponse<Void> deleteJob(@PathVariable Long jobId) {
        scheduleJobService.deleteJob(jobId);
        return ApiResponse.success();
    }

    /**
     * 暂停任务
     *
     * @param jobId 任务ID
     * @return 操作结果
     */
    @PutMapping("/{jobId}/pause")
    @Log(module = "系统", type = "修改")
    public ApiResponse<Void> pauseJob(@PathVariable Long jobId) {
        scheduleJobService.pauseJob(jobId);
        return ApiResponse.success();
    }

    /**
     * 恢复任务
     *
     * @param jobId 任务ID
     * @return 操作结果
     */
    @PutMapping("/{jobId}/resume")
    @Log(module = "系统", type = "修改")
    public ApiResponse<Void> resumeJob(@PathVariable Long jobId) {
        scheduleJobService.resumeJob(jobId);
        return ApiResponse.success();
    }

    /**
     * 手动执行一次任务
     *
     * @param jobId 任务ID
     * @return 操作结果
     */
    @PostMapping("/{jobId}/execute")
    @Log(module = "系统", type = "执行")
    public ApiResponse<Void> executeJob(@PathVariable Long jobId) {
        scheduleJobService.executeJob(jobId);
        return ApiResponse.success();
    }

    /**
     * 查询任务执行历史（分页）
     *
     * @param jobId 任务ID
     * @param page  页码
     * @param size  每页条数
     * @return 分页日志列表
     */
    @GetMapping("/{jobId}/logs")
    public ApiResponse<PageResponse<ScheduleLogResponse>> getJobLogs(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(scheduleJobService.getJobLogs(jobId, page, size));
    }
}
