package com.sim.shopping.interfaces.notification;

import com.sim.shopping.application.notification.NotificationService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.notification.NotificationResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息通知控制器，处理系统消息和用户通知
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 查询通知列表
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<NotificationResponse>> getNotifications(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(notificationService.getNotifications(userId, type, isRead, pageNum, pageSize));
    }

    /**
     * 查询未读通知数量
     * @return 返回结果
     */
    @GetMapping("/unread-count")
    public ApiResponse<Map<String, Long>> getUnreadCount() {
        Long userId = SecurityUtils.getCurrentUserId();
        long count = notificationService.getUnreadCount(userId);
        Map<String, Long> result = new HashMap<>();
        result.put("unreadCount", count);
        return ApiResponse.success(result);
    }

    /**
     * mark Read
     * @param notificationId notificationId
     * @return 返回结果
     */
    @PutMapping("/{notificationId}/read")
    public ApiResponse<Void> markRead(@PathVariable Long notificationId) {
        Long userId = SecurityUtils.getCurrentUserId();
        notificationService.markRead(userId, notificationId);
        return ApiResponse.success();
    }

    /**
     * mark All Read
     * @return 返回结果
     */
    @PutMapping("/read-all")
    public ApiResponse<Void> markAllRead() {
        Long userId = SecurityUtils.getCurrentUserId();
        notificationService.markAllRead(userId);
        return ApiResponse.success();
    }

    /**
     * 删除通知
     * @param notificationId notificationId
     * @return 返回结果
     */
    @DeleteMapping("/{notificationId}")
    public ApiResponse<Void> deleteNotification(@PathVariable Long notificationId) {
        Long userId = SecurityUtils.getCurrentUserId();
        notificationService.deleteNotification(userId, notificationId);
        return ApiResponse.success();
    }
}
