package com.sim.shopping.domain.event;

/**
 * NotificationNeededEvent
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return userId; }
    /**
     * 获取User Type
     * @return 返回结果
     */
    public String getUserType() { return userType; }
    /**
     * 获取Title
     * @return 返回结果
     */
    public String getTitle() { return title; }
    /**
     * 获取Content
     * @return 返回结果
     */
    public String getContent() { return content; }
    /**
     * 获取Reference Id
     * @return 返回结果
     */
    public Long getReferenceId() { return referenceId; }
    /**
     * 获取Reference Type
     * @return 返回结果
     */
    public String getReferenceType() { return referenceType; }
}
