package com.sim.shopping.interfaces.activity;

import com.sim.shopping.application.activity.ActivityService;
import com.sim.shopping.interfaces.dto.activity.ActivityDetailResponse;
import com.sim.shopping.interfaces.dto.activity.ActivityResponse;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.web.bind.annotation.*;

/**
 * 专题活动公开控制器，提供活动列表和详情查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/public/activities")
public class PublicActivityController {

    private final ActivityService activityService;

    public PublicActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * 查询进行中的活动列表
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<ActivityResponse>> getActiveActivities(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(activityService.getActiveActivities(page, size));
    }

    /**
     * 查询活动详情
     * @param activityId activityId
     * @return 返回结果
     */
    @GetMapping("/{activityId}")
    public ApiResponse<ActivityDetailResponse> getActivityDetail(@PathVariable Long activityId) {
        return ApiResponse.success(activityService.getActivityDetail(activityId));
    }
}
