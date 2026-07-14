import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 积分商品 */
export interface PointsProduct {
  productId: number
  name: string
  description?: string
  imageUrl?: string
  pointsRequired: number
  stock: number
  status: string
  sort?: number
  createdAt?: string
  updatedAt?: string
}

/** 积分记录类型 */
export type PointsRecordType = 'EARN' | 'DEDUCT'

/** 积分记录 — 对齐后端 PointsRecordResponse */
export interface PointsRecord {
  id: number
  userId: number
  points: number
  type: PointsRecordType
  source: string
  description?: string
  relatedId?: number
  createdAt: string
}

/** 获取当前积分余额 — 对齐后端 PointsBalanceVO */
export function getPointsBalance() {
  return request.get<unknown, { currentPoints: number; totalEarned: number; totalSpent: number }>('/user/points')
}

/** 获取积分明细记录 */
export function getPointsRecords(params: {
  type?: string
  page?: number
  size?: number
}) {
  return request.get<unknown, PageResponse<PointsRecord>>('/user/points/records', { params })
}

/** 获取积分商品列表（公开，分页） */
export function getPointsProducts() {
  return request.get<unknown, { list: PointsProduct[]; total: number }>('/public/points/products')
}

/** 兑换积分商品 — quantity 通过 query parameter 传递 */
export function exchangeProduct(productId: number, quantity: number = 1) {
  return request.post<unknown, void>(`/user/points/products/${productId}/exchange`, null, {
    params: { quantity },
  })
}
