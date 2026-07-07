package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public String getUserType() { return this.userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public String getNotificationType() { return this.notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    public Long getReferenceId() { return this.referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }
    public String getReferenceType() { return this.referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getIsRead() { return this.isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
    public LocalDateTime getReadAt() { return this.readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
