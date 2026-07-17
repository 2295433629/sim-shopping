package com.sim.shopping.interfaces.dto.notification;

import java.time.LocalDateTime;

/**
 * Notification响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class NotificationResponse {

    private Long id;
    private String notificationType;
    private String title;
    private String content;
    private Integer isRead;
    private Long referenceId;
    private String referenceType;
    private LocalDateTime createdAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
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
     * 获取Reference Type
     * @return 返回结果
     */
    public String getReferenceType() { return this.referenceType; }
    /**
     * set Reference Type
     * @param referenceType referenceType
     */
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
