package com.sim.shopping.interfaces.activity;

import com.sim.shopping.application.activity.ActivityService;
import com.sim.shopping.interfaces.dto.activity.ActivityDetailResponse;
import com.sim.shopping.interfaces.dto.activity.ActivityResponse;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/activities")
public class PublicActivityController {

    private final ActivityService activityService;

    public PublicActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ApiResponse<PageResponse<ActivityResponse>> getActiveActivities(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(activityService.getActiveActivities(page, size));
    }

    @GetMapping("/{activityId}")
    public ApiResponse<ActivityDetailResponse> getActivityDetail(@PathVariable Long activityId) {
        return ApiResponse.success(activityService.getActivityDetail(activityId));
    }
}
