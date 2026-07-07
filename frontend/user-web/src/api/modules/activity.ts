import request from '@/api/request'
import type { PageResponse } from '@/types/common'
import type { ProductCardVO } from '@/types/product'

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
}

/** 专题活动详情 */
export interface ActivityDetail extends Activity {
  products: ProductCardVO[]
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
