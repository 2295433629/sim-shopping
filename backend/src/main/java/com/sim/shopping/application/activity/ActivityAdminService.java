package com.sim.shopping.application.activity;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.ActivityDO;
import com.sim.shopping.infrastructure.persistence.entity.ActivityProductDO;
import com.sim.shopping.infrastructure.persistence.mapper.ActivityMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ActivityProductMapper;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.activity.ActivityResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ActivityAdmin服务，处理相关业务逻辑
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class ActivityAdminService {

    private final ActivityMapper activityMapper;
    private final ActivityProductMapper activityProductMapper;

    public ActivityAdminService(ActivityMapper activityMapper, ActivityProductMapper activityProductMapper) {
        this.activityMapper = activityMapper;
        this.activityProductMapper = activityProductMapper;
    }

    /**
     * 获取Activity List
     * @param page page
     * @param size size
     * @param status status
     * @param keyword keyword
     * @return 返回结果
     */
    public PageResponse<ActivityResponse> getActivityList(int page, int size, String status, String keyword) {
        Page<ActivityResponse> pageParam = new Page<>(page, size);
        Page<ActivityResponse> result = activityMapper.selectActivityPage(pageParam, status, keyword);
        return PageResponse.of(result.getRecords(), result.getTotal(), (int) result.getCurrent(), (int) result.getSize());
    }

    /**
     * 创建专题活动
     * @param activity activity
     * @param productIds productIds
     * @return 返回结果
     */
    @Transactional
    public ActivityDO createActivity(ActivityDO activity, List<Long> productIds) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = SecurityUtils.getCurrentUserId();

        activity.setCreatedAt(now);
        activity.setUpdatedAt(now);
        activity.setCreatedBy(userId);
        activity.setUpdatedBy(userId);

        activityMapper.insert(activity);

        if (!CollectionUtils.isEmpty(productIds)) {
            insertActivityProducts(activity.getId(), productIds, userId, now);
        }

        return activity;
    }

    /**
     * 更新专题活动
     * @param id id
     * @param activity activity
     * @param productIds productIds
     * @return 返回结果
     */
    @Transactional
    public ActivityDO updateActivity(Long id, ActivityDO activity, List<Long> productIds) {
        ActivityDO existing = activityMapper.selectById(id);
        if (existing == null || existing.getDeleted() != null && existing.getDeleted() == 1) {
            throw new BusinessException(404, "活动不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        Long userId = SecurityUtils.getCurrentUserId();

        activity.setId(id);
        activity.setUpdatedAt(now);
        activity.setUpdatedBy(userId);

        activityMapper.updateById(activity);

        // 重新关联商品：先删除旧关联，再插入新关联
        deleteActivityProductsByActivityId(id, userId, now);
        if (!CollectionUtils.isEmpty(productIds)) {
            insertActivityProducts(id, productIds, userId, now);
        }

        return activityMapper.selectById(id);
    }

    /**
     * 删除专题活动
     * @param id id
     */
    @Transactional
    public void deleteActivity(Long id) {
        ActivityDO existing = activityMapper.selectById(id);
        if (existing == null || existing.getDeleted() != null && existing.getDeleted() == 1) {
            throw new BusinessException(404, "活动不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        Long userId = SecurityUtils.getCurrentUserId();

        // 删除关联商品（逻辑删除）
        deleteActivityProductsByActivityId(id, userId, now);

        // 删除活动（逻辑删除由MyBatis-Plus自动处理）
        activityMapper.deleteById(id);
    }

    private void insertActivityProducts(Long activityId, List<Long> productIds, Long userId, LocalDateTime now) {
        List<ActivityProductDO> list = productIds.stream().distinct().map(productId -> {
            ActivityProductDO ap = new ActivityProductDO();
            ap.setActivityId(activityId);
            ap.setProductId(productId);
            ap.setSortOrder(0);
            ap.setCreatedAt(now);
            ap.setUpdatedAt(now);
            ap.setCreatedBy(userId);
            ap.setUpdatedBy(userId);
            return ap;
        }).collect(Collectors.toList());

        for (ActivityProductDO ap : list) {
            activityProductMapper.insert(ap);
        }
    }

    private void deleteActivityProductsByActivityId(Long activityId, Long userId, LocalDateTime now) {
        LambdaQueryWrapper<ActivityProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityProductDO::getActivityId, activityId);
        List<ActivityProductDO> existingList = activityProductMapper.selectList(wrapper);
        for (ActivityProductDO ap : existingList) {
            ap.setUpdatedAt(now);
            ap.setUpdatedBy(userId);
            activityProductMapper.updateById(ap);
            activityProductMapper.deleteById(ap.getId());
        }
    }
}
