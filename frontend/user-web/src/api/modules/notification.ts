import request from '@/api/request'

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

/** 消息列表 */
export function getNotifications(params: { page: number; size: number }) {
  return request.get('/notifications', { params })
}

/** 未读消息数量 */
export function getUnreadCount() {
  return request.get('/notifications/unread-count')
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
