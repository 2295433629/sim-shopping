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

/** 积分记录 */
export interface PointsRecord {
  recordId: number
  userId: number
  points: number
  type: PointsRecordType
  source: string
  description?: string
  orderNo?: string
  createdAt: string
}

/** 获取当前积分余额 */
export function getPointsBalance() {
  return request.get<unknown, { balance: number }>('/user/points')
}

/** 获取积分明细记录 */
export function getPointsRecords(params: {
  type?: string
  page?: number
  size?: number
}) {
  return request.get<unknown, PageResponse<PointsRecord>>('/user/points/records', { params })
}

/** 获取积分商品列表（公开） */
export function getPointsProducts() {
  return request.get<unknown, PointsProduct[]>('/public/points/products')
}

/** 兑换积分商品 */
export function exchangeProduct(productId: number, quantity: number = 1) {
  return request.post<unknown, void>(`/user/points/products/${productId}/exchange`, { quantity })
}
