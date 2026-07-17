package com.sim.shopping.interfaces.activity;

import com.sim.shopping.application.activity.ActivityAdminService;
import com.sim.shopping.infrastructure.persistence.entity.ActivityDO;
import com.sim.shopping.interfaces.dto.activity.ActivityResponse;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专题活动管理控制器，处理专题活动的增删改查
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/activities")
@PreAuthorize("hasRole('ADMIN')")
public class ActivityAdminController {

    private final ActivityAdminService activityAdminService;

    public ActivityAdminController(ActivityAdminService activityAdminService) {
        this.activityAdminService = activityAdminService;
    }

    /**
     * 获取Activity List
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<ActivityResponse>> getActivityList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(activityAdminService.getActivityList(page, size, status, keyword));
    }

    /**
     * 创建专题活动
     * @param request request
     * @return 返回结果
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ActivityDO> createActivity(@RequestBody ActivityCreateRequest request) {
        ActivityDO activity = new ActivityDO();
        activity.setActivityName(request.getName());
        activity.setBannerImage(request.getBannerImage());
        activity.setDescription(request.getDescription());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setSortOrder(request.getSortOrder());
        activity.setStatus(request.getStatus());
        return ApiResponse.success(activityAdminService.createActivity(activity, request.getProductIds()));
    }

    /**
     * 更新专题活动
     * @param activityId activityId
     * @param request request
     * @return 返回结果
     */
    @PutMapping("/{activityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ActivityDO> updateActivity(@PathVariable Long activityId, @RequestBody ActivityCreateRequest request) {
        ActivityDO activity = new ActivityDO();
        activity.setActivityName(request.getName());
        activity.setBannerImage(request.getBannerImage());
        activity.setDescription(request.getDescription());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setSortOrder(request.getSortOrder());
        activity.setStatus(request.getStatus());
        return ApiResponse.success(activityAdminService.updateActivity(activityId, activity, request.getProductIds()));
    }

    /**
     * 删除专题活动
     * @param activityId activityId
     * @return 返回结果
     */
    @DeleteMapping("/{activityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteActivity(@PathVariable Long activityId) {
        activityAdminService.deleteActivity(activityId);
        return ApiResponse.success();
    }

    public static class ActivityCreateRequest {
        private String name;
        private String bannerImage;
        private String description;
        private java.time.LocalDateTime startTime;
        private java.time.LocalDateTime endTime;
        private Integer sortOrder;
        private String status;
        private List<Long> productIds;

        /** 获取Name */
        public String getName() {
            return this.name;
        }

        /** set Name */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 获取Banner Image
         * @return 返回结果
         */
        public String getBannerImage() {
            return this.bannerImage;
        }

        /**
         * set Banner Image
         * @param bannerImage bannerImage
         */
        public void setBannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
        }

        /**
         * 获取Description
         * @return 返回结果
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * set Description
         * @param description description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * 获取Start Time
         * @return 返回结果
         */
        public java.time.LocalDateTime getStartTime() {
            return this.startTime;
        }

        /**
         * set Start Time
         * @param startTime startTime
         */
        public void setStartTime(java.time.LocalDateTime startTime) {
            this.startTime = startTime;
        }

        /**
         * 获取End Time
         * @return 返回结果
         */
        public java.time.LocalDateTime getEndTime() {
            return this.endTime;
        }

        /**
         * set End Time
         * @param endTime endTime
         */
        public void setEndTime(java.time.LocalDateTime endTime) {
            this.endTime = endTime;
        }

        /**
         * 获取Sort Order
         * @return 返回结果
         */
        public Integer getSortOrder() {
            return this.sortOrder;
        }

        /**
         * set Sort Order
         * @param sortOrder sortOrder
         */
        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }

        /**
         * 获取Status
         * @return 返回结果
         */
        public String getStatus() {
            return this.status;
        }

        /**
         * set Status
         * @param status status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * 获取Product Ids
         * @return 返回结果
         */
        public List<Long> getProductIds() {
            return this.productIds;
        }

        /**
         * set Product Ids
         * @param productIds productIds
         */
        public void setProductIds(List<Long> productIds) {
            this.productIds = productIds;
        }
    }
}
