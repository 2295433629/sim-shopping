package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 活动商品实体，对应 t_activity_product 表，存储活动商品关联
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_activity_product")
public class ActivityProductDO extends BaseEntity {

    private Long activityId;
    private Long productId;
    private Integer sortOrder;

    /**
     * 获取Activity Id
     * @return 返回结果
     */
    public Long getActivityId() {
        return this.activityId;
    }

    /**
     * set Activity Id
     * @param activityId activityId
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() {
        return this.productId;
    }

    /**
     * set Product Id
     * @param productId productId
     */
    public void setProductId(Long productId) {
        this.productId = productId;
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
}
