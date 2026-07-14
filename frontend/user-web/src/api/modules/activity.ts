import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 活动商品 — 对齐后端 ActivityProductResponse */
export interface ActivityProduct {
  productId: number
  productName: string
  productImage: string
  price: number
  sortOrder: number
}

/** 专题活动 — 对齐后端 ActivityResponse */
export interface Activity {
  id: number
  activityName: string
  description: string
  bannerImage: string
  startTime: string
  endTime: string
  status: string
  productCount: number
}

/** 专题活动详情 — 对齐后端 ActivityDetailResponse */
export interface ActivityDetail extends Activity {
  products: ActivityProduct[]
}

/** 专题活动查询参数 */
export interface ActivityQueryParams {
  page?: number
  size?: number
}

/** 获取进行中的专题活动列表 */
export function getActiveActivities(params?: ActivityQueryParams) {
  return request.get<unknown, PageResponse<Activity>>('/public/activities', { params })
}

/** 获取专题活动详情 */
export function getActivityDetail(activityId: number) {
  return request.get<unknown, ActivityDetail>(`/public/activities/${activityId}`)
}
