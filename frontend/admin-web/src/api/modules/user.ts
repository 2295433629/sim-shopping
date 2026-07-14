import request from '../request'
import type { PageResponse } from '@/types/common'

export interface UserItem {
  userId: number
  username: string
  nickname?: string
  phone?: string
  email?: string
  role?: string
  status: number
  createdAt?: string
}

export function getUsers(params: { page: number; size: number; keyword?: string; status?: number }) {
  return request.get<unknown, PageResponse<UserItem>>('/admin/users', { params })
}

export function getUserDetail(id: number) {
  return request.get<unknown, UserItem>(`/admin/users/${id}`)
}

export function disableUser(id: number) {
  return request.patch<unknown, void>(`/admin/users/${id}/disable`)
}

export function enableUser(id: number) {
  return request.patch<unknown, void>(`/admin/users/${id}/enable`)
}
