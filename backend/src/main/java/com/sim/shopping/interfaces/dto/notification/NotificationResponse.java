package com.sim.shopping.interfaces.dto.notification;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private String notificationType;
    private String title;
    private String content;
    private Integer isRead;
    private Long referenceId;
    private String referenceType;
    private LocalDateTime createdAt;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public String getNotificationType() { return this.notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }
    public Integer getIsRead() { return this.isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
    public Long getReferenceId() { return this.referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getReferenceType() { return this.referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
