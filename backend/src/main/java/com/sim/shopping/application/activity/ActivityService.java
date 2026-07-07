package com.sim.shopping.application.activity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.ActivityDO;
import com.sim.shopping.infrastructure.persistence.mapper.ActivityMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ActivityProductMapper;
import com.sim.shopping.interfaces.dto.activity.ActivityDetailResponse;
import com.sim.shopping.interfaces.dto.activity.ActivityProductResponse;
import com.sim.shopping.interfaces.dto.activity.ActivityResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityMapper activityMapper;
    private final ActivityProductMapper activityProductMapper;

    private static final String ACTIVITY_STATUS_ACTIVE = "ACTIVE";

    public ActivityService(ActivityMapper activityMapper, ActivityProductMapper activityProductMapper) {
        this.activityMapper = activityMapper;
        this.activityProductMapper = activityProductMapper;
    }

    public PageResponse<ActivityResponse> getActiveActivities(int page, int size) {
        Page<ActivityResponse> pageParam = new Page<>(page, size);
        LocalDateTime now = LocalDateTime.now();
        Page<ActivityResponse> result = activityMapper.selectActiveActivityPage(pageParam, ACTIVITY_STATUS_ACTIVE, now);
        return PageResponse.of(result.getRecords(), result.getTotal(), (int) result.getCurrent(), (int) result.getSize());
    }

    public ActivityDetailResponse getActivityDetail(Long activityId) {
        ActivityDO activity = activityMapper.selectById(activityId);
        if (activity == null || activity.getDeleted() != null && activity.getDeleted() == 1) {
            throw new BusinessException(404, "活动不存在");
        }

        ActivityDetailResponse response = new ActivityDetailResponse();
        response.setId(activity.getId());
        response.setActivityName(activity.getActivityName());
        response.setBannerImage(activity.getBannerImage());
        response.setDescription(activity.getDescription());
        response.setStartTime(activity.getStartTime());
        response.setEndTime(activity.getEndTime());
        response.setSortOrder(activity.getSortOrder());
        response.setStatus(activity.getStatus());

        Long productCount = activityProductMapper.countByActivityId(activityId);
        response.setProductCount(productCount);

        List<ActivityProductResponse> products = activityProductMapper.selectProductsByActivityId(activityId);
        response.setProducts(products);

        return response;
    }
}
