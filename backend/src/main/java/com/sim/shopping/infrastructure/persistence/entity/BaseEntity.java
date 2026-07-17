package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
/**
 * 基础实体抽象类，提供所有业务表共有的审计字段（id、创建时间、更新时间、删除标记等）
 *
 * @author Sim Team
 * @since 1.0.0
 */
public abstract class BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** 获取Updated At */
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    /** set Updated At */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    /** 获取Created By */
    public Long getCreatedBy() { return this.createdBy; }
    /** set Created By */
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    /** 获取Updated By */
    public Long getUpdatedBy() { return this.updatedBy; }
    /** set Updated By */
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    /** 获取Deleted */
    public Integer getDeleted() { return this.deleted; }
    /** set Deleted */
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
