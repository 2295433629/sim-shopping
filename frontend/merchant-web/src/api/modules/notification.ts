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

export function getNotifications(params: { page?: number; size?: number; type?: string; isRead?: number }) {
  return request.get('/notifications', { params: { pageNum: params.page || 1, pageSize: params.size || 10, type: params.type, isRead: params.isRead } })
}

export function getUnreadCount() {
  return request.get('/notifications/unread-count')
}

export function markAsRead(notificationId: number) {
  return request.put(`/notifications/${notificationId}/read`)
}

export function markAllAsRead() {
  return request.put('/notifications/read-all')
}

export function deleteNotification(notificationId: number) {
  return request.delete(`/notifications/${notificationId}`)
}
