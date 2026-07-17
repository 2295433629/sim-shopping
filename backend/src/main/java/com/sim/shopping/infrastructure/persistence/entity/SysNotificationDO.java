package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysNotification数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_notification")
public class SysNotificationDO extends BaseEntity {
    private String userType;
    private String notificationType;
    private Long referenceId;
    private String content;
    private String referenceType;
    private String title;
    private Integer isRead;
    private LocalDateTime readAt;
    private Long userId;

    /**
     * 获取User Type
     * @return 返回结果
     */
    public String getUserType() { return this.userType; }
    /**
     * set User Type
     * @param userType userType
     */
    public void setUserType(String userType) { this.userType = userType; }
    /**
     * 获取Notification Type
     * @return 返回结果
     */
    public String getNotificationType() { return this.notificationType; }
    /**
     * set Notification Type
     * @param notificationType notificationType
     */
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    /**
     * 获取Reference Id
     * @return 返回结果
     */
    public Long getReferenceId() { return this.referenceId; }
    /**
     * set Reference Id
     * @param referenceId referenceId
     */
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    /**
     * 获取Content
     * @return 返回结果
     */
    public String getContent() { return this.content; }
    /**
     * set Content
     * @param content content
     */
    public void setContent(String content) { this.content = content; }
    /**
     * 获取Reference Type
     * @return 返回结果
     */
    public String getReferenceType() { return this.referenceType; }
    /**
     * set Reference Type
     * @param referenceType referenceType
     */
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    /**
     * 获取Title
     * @return 返回结果
     */
    public String getTitle() { return this.title; }
    /**
     * set Title
     * @param title title
     */
    public void setTitle(String title) { this.title = title; }
    /**
     * 获取Is Read
     * @return 返回结果
     */
    public Integer getIsRead() { return this.isRead; }
    /**
     * set Is Read
     * @param isRead isRead
     */
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
    /**
     * 获取Read At
     * @return 返回结果
     */
    public LocalDateTime getReadAt() { return this.readAt; }
    /**
     * set Read At
     * @param readAt readAt
     */
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return this.userId; }
    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) { this.userId = userId; }
}
