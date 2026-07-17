package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 积分流水实体，对应 t_points_record 表，存储积分变动明细
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_points_record")
public class PointsRecordDO extends BaseEntity {

    @TableField("user_id")
    private Long userId;

    @TableField("points")
    private Integer points;

    @TableField("type")
    private String type;

    @TableField("source")
    private String source;

    @TableField("description")
    private String description;

    @TableField("related_id")
    private Long relatedId;

    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取Points
     * @return 返回结果
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * set Points
     * @param points points
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    /** 获取Type */
    public String getType() {
        return type;
    }

    /** set Type */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取Source
     * @return 返回结果
     */
    public String getSource() {
        return source;
    }

    /**
     * set Source
     * @param source source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() {
        return description;
    }

    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取Related Id
     * @return 返回结果
     */
    public Long getRelatedId() {
        return relatedId;
    }

    /**
     * set Related Id
     * @param relatedId relatedId
     */
    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }
}
