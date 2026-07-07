package com.sim.shopping.interfaces.activity;

import com.sim.shopping.application.activity.ActivityAdminService;
import com.sim.shopping.infrastructure.persistence.entity.ActivityDO;
import com.sim.shopping.interfaces.dto.activity.ActivityResponse;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/activities")
@PreAuthorize("hasRole('ADMIN')")
public class ActivityAdminController {

    private final ActivityAdminService activityAdminService;

    public ActivityAdminController(ActivityAdminService activityAdminService) {
        this.activityAdminService = activityAdminService;
    }

    @GetMapping
    public ApiResponse<PageResponse<ActivityResponse>> getActivityList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(activityAdminService.getActivityList(page, size, status, keyword));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ActivityDO> createActivity(@RequestBody ActivityCreateRequest request) {
        ActivityDO activity = new ActivityDO();
        activity.setActivityName(request.getActivityName());
        activity.setBannerImage(request.getBannerImage());
        activity.setDescription(request.getDescription());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setSortOrder(request.getSortOrder());
        activity.setStatus(request.getStatus());
        return ApiResponse.success(activityAdminService.createActivity(activity, request.getProductIds()));
    }

    @PutMapping("/{activityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ActivityDO> updateActivity(@PathVariable Long activityId, @RequestBody ActivityCreateRequest request) {
        ActivityDO activity = new ActivityDO();
        activity.setActivityName(request.getActivityName());
        activity.setBannerImage(request.getBannerImage());
        activity.setDescription(request.getDescription());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setSortOrder(request.getSortOrder());
        activity.setStatus(request.getStatus());
        return ApiResponse.success(activityAdminService.updateActivity(activityId, activity, request.getProductIds()));
    }

    @DeleteMapping("/{activityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteActivity(@PathVariable Long activityId) {
        activityAdminService.deleteActivity(activityId);
        return ApiResponse.success();
    }

    public static class ActivityCreateRequest {
        private String activityName;
        private String bannerImage;
        private String description;
        private java.time.LocalDateTime startTime;
        private java.time.LocalDateTime endTime;
        private Integer sortOrder;
        private String status;
        private List<Long> productIds;

        public String getActivityName() {
            return this.activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public String getBannerImage() {
            return this.bannerImage;
        }

        public void setBannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public java.time.LocalDateTime getStartTime() {
            return this.startTime;
        }

        public void setStartTime(java.time.LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public java.time.LocalDateTime getEndTime() {
            return this.endTime;
        }

        public void setEndTime(java.time.LocalDateTime endTime) {
            this.endTime = endTime;
        }

        public Integer getSortOrder() {
            return this.sortOrder;
        }

        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<Long> getProductIds() {
            return this.productIds;
        }

        public void setProductIds(List<Long> productIds) {
            this.productIds = productIds;
        }
    }
}
