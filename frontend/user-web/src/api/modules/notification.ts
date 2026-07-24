import request from '@/api/request'
import type { PageResponse } from '@/types/common'

export interface NotificationItem {
  id: number
  userId: number
  userType: string
  notificationType: string
  title: string
  content: string
  referenceId?: number
  referenceType?: string
  isRead: number
  readAt?: string
  createdAt: string
}

/** 未读消息数量响应 */
export interface UnreadCountResponse {
  unreadCount: number
}

/** 消息列表 */
export function getNotifications(params: { pageNum: number; pageSize: number }) {
  return request.get<unknown, PageResponse<NotificationItem>>('/notifications', { params })
}

/** 未读消息数量 */
export function getUnreadCount() {
  return request.get<unknown, UnreadCountResponse>('/notifications/unread-count')
}

/** 标记已读 */
export function markAsRead(notificationId: number) {
  return request.put(`/notifications/${notificationId}/read`)
}

/** 全部标记已读 */
export function markAllAsRead() {
  return request.put('/notifications/read-all')
}

/** 删除消息 */
export function deleteNotification(notificationId: number) {
  return request.delete(`/notifications/${notificationId}`)
}
