import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 专题活动 */
export interface Activity {
  activityId: number
  name: string
  description: string
  bannerImage: string
  startTime: string
  endTime: string
  status: string
  productCount: number
  createdAt: string
  updatedAt: string
}

/** 专题活动表单数据 */
export interface ActivityFormData {
  name: string
  description: string
  bannerImage: string
  startTime: string
  endTime: string
  productIds: number[]
}

/** 专题活动查询参数 */
export interface ActivityQueryParams {
  page?: number
  size?: number
  status?: string
  keyword?: string
}

/** 获取专题活动列表 */
export function getAdminActivities(params?: ActivityQueryParams) {
  return request.get<unknown, PageResponse<Activity>>('/admin/activities', { params })
}

/** 创建专题活动 */
export function createAdminActivity(data: ActivityFormData) {
  return request.post<unknown, Activity>('/admin/activities', data)
}

/** 更新专题活动 */
export function updateAdminActivity(id: number, data: ActivityFormData) {
  return request.put<unknown, Activity>(`/admin/activities/${id}`, data)
}

/** 删除专题活动 */
export function deleteAdminActivity(id: number) {
  return request.delete<unknown, void>(`/admin/activities/${id}`)
}
