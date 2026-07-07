package com.sim.shopping.interfaces.notification;

import com.sim.shopping.application.notification.NotificationService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.notification.NotificationResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ApiResponse<PageResponse<NotificationResponse>> getNotifications(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(notificationService.getNotifications(userId, type, isRead, pageNum, pageSize));
    }

    @GetMapping("/unread-count")
    public ApiResponse<Map<String, Long>> getUnreadCount() {
        Long userId = SecurityUtils.getCurrentUserId();
        long count = notificationService.getUnreadCount(userId);
        Map<String, Long> result = new HashMap<>();
        result.put("unreadCount", count);
        return ApiResponse.success(result);
    }

    @PutMapping("/{notificationId}/read")
    public ApiResponse<Void> markRead(@PathVariable Long notificationId) {
        Long userId = SecurityUtils.getCurrentUserId();
        notificationService.markRead(userId, notificationId);
        return ApiResponse.success();
    }

    @PutMapping("/read-all")
    public ApiResponse<Void> markAllRead() {
        Long userId = SecurityUtils.getCurrentUserId();
        notificationService.markAllRead(userId);
        return ApiResponse.success();
    }

    @DeleteMapping("/{notificationId}")
    public ApiResponse<Void> deleteNotification(@PathVariable Long notificationId) {
        Long userId = SecurityUtils.getCurrentUserId();
        notificationService.deleteNotification(userId, notificationId);
        return ApiResponse.success();
    }
}
