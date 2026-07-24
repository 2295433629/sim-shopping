package com.sim.shopping.application.activity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.persistence.entity.ActivityDO;
import com.sim.shopping.infrastructure.persistence.entity.ActivityProductQueryResult;
import com.sim.shopping.infrastructure.persistence.entity.ActivityQueryResult;
import com.sim.shopping.infrastructure.persistence.mapper.ActivityMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ActivityProductMapper;
import com.sim.shopping.interfaces.dto.activity.ActivityDetailResponse;
import com.sim.shopping.interfaces.dto.activity.ActivityProductResponse;
import com.sim.shopping.interfaces.dto.activity.ActivityResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 专题活动服务，处理专题活动的增删改查和商品关联
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class ActivityService {

    private final ActivityMapper activityMapper;
    private final ActivityProductMapper activityProductMapper;



    public ActivityService(ActivityMapper activityMapper, ActivityProductMapper activityProductMapper) {
        this.activityMapper = activityMapper;
        this.activityProductMapper = activityProductMapper;
    }

    /**
     * 查询进行中的活动列表
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<ActivityResponse> getActiveActivities(int page, int size) {
        Page<ActivityQueryResult> pageParam = new Page<>(page, size);
        LocalDateTime now = LocalDateTime.now();
        Page<ActivityQueryResult> result = activityMapper.selectActiveActivityPage(pageParam, SystemConstants.ACTIVITY_STATUS_ACTIVE, now);
        List<ActivityResponse> records = result.getRecords().stream()
                .map(this::toActivityResponse)
                .collect(Collectors.toList());
        return PageResponse.of(records, result.getTotal(), (int) result.getCurrent(), (int) result.getSize());
    }

    /**
     * ActivityQueryResult -> ActivityResponse 转换
     * @param qr qr
     * @return 返回结果
     */
    private ActivityResponse toActivityResponse(ActivityQueryResult qr) {
        ActivityResponse resp = new ActivityResponse();
        resp.setId(qr.getId());
        resp.setActivityName(qr.getActivityName());
        resp.setBannerImage(qr.getBannerImage());
        resp.setDescription(qr.getDescription());
        resp.setStartTime(qr.getStartTime());
        resp.setEndTime(qr.getEndTime());
        resp.setSortOrder(qr.getSortOrder());
        resp.setStatus(qr.getStatus());
        resp.setProductCount(qr.getProductCount());
        return resp;
    }

    /**
     * ActivityProductQueryResult -> ActivityProductResponse 转换
     * @param qr qr
     * @return 返回结果
     */
    private ActivityProductResponse toActivityProductResponse(ActivityProductQueryResult qr) {
        ActivityProductResponse resp = new ActivityProductResponse();
        resp.setProductId(qr.getProductId());
        resp.setProductName(qr.getProductName());
        resp.setProductImage(qr.getProductImage());
        resp.setPrice(qr.getPrice());
        resp.setSortOrder(qr.getSortOrder());
        return resp;
    }

    /**
     * 查询活动详情
     * @param activityId activityId
     * @return 返回结果
     */
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

        List<ActivityProductQueryResult> products = activityProductMapper.selectProductsByActivityId(activityId);
        response.setProducts(products.stream()
                .map(this::toActivityProductResponse)
                .collect(Collectors.toList()));

        return response;
    }
}
