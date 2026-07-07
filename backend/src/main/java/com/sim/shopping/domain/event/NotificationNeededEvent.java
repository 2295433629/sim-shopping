package com.sim.shopping.domain.event;

public class NotificationNeededEvent {
    private final Long userId;
    private final String userType;
    private final String title;
    private final String content;
    private final Long referenceId;
    private final String referenceType;

    public NotificationNeededEvent(Long userId, String userType, String title, String content, Long referenceId, String referenceType) {
        this.userId = userId;
        this.userType = userType;
        this.title = title;
        this.content = content;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
    }

    public Long getUserId() { return userId; }
    public String getUserType() { return userType; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Long getReferenceId() { return referenceId; }
    public String getReferenceType() { return referenceType; }
}
