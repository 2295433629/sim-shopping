package com.sim.shopping.application.notification;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.SysNotificationDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysNotificationMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.notification.NotificationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final SysNotificationMapper sysNotificationMapper;

    public NotificationService(SysNotificationMapper sysNotificationMapper) {
        this.sysNotificationMapper = sysNotificationMapper;
    }

    public PageResponse<NotificationResponse> getNotifications(Long userId, String type, Integer isRead, int page, int size) {
        LambdaQueryWrapper<SysNotificationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysNotificationDO::getUserId, userId);
        if (type != null && !type.isEmpty()) {
            wrapper.eq(SysNotificationDO::getNotificationType, type);
        }
        if (isRead != null) {
            wrapper.eq(SysNotificationDO::getIsRead, isRead);
        }
        wrapper.orderByDesc(SysNotificationDO::getCreatedAt);

        Page<SysNotificationDO> pageResult = sysNotificationMapper.selectPage(new Page<>(page, size), wrapper);
        List<NotificationResponse> list = pageResult.getRecords().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PageResponse.of(list, pageResult.getTotal(), page, size);
    }

    public long getUnreadCount(Long userId) {
        LambdaQueryWrapper<SysNotificationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysNotificationDO::getUserId, userId)
               .eq(SysNotificationDO::getIsRead, 0);
        return sysNotificationMapper.selectCount(wrapper);
    }

    @Transactional
    public void markRead(Long userId, Long notificationId) {
        SysNotificationDO notification = sysNotificationMapper.selectById(notificationId);
        if (notification == null || !notification.getUserId().equals(userId)) {
            throw new BusinessException(404, "通知不存在");
        }
        notification.setIsRead(1);
        notification.setReadAt(LocalDateTime.now());
        sysNotificationMapper.updateById(notification);
    }

    @Transactional
    public void markAllRead(Long userId) {
        LambdaUpdateWrapper<SysNotificationDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysNotificationDO::getUserId, userId)
                     .eq(SysNotificationDO::getIsRead, 0)
                     .set(SysNotificationDO::getIsRead, 1)
                     .set(SysNotificationDO::getReadAt, LocalDateTime.now());
        sysNotificationMapper.update(null, updateWrapper);
    }

    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        SysNotificationDO notification = sysNotificationMapper.selectById(notificationId);
        if (notification == null || !notification.getUserId().equals(userId)) {
            throw new BusinessException(404, "通知不存在");
        }
        sysNotificationMapper.deleteById(notificationId);
    }

    private NotificationResponse toResponse(SysNotificationDO notif) {
        NotificationResponse resp = new NotificationResponse();
        resp.setId(notif.getId());
        resp.setNotificationType(notif.getNotificationType());
        resp.setTitle(notif.getTitle());
        resp.setContent(notif.getContent());
        resp.setIsRead(notif.getIsRead());
        resp.setReferenceId(notif.getReferenceId());
        resp.setReferenceType(notif.getReferenceType());
        resp.setCreatedAt(notif.getCreatedAt());
        return resp;
    }
}
