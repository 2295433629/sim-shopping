import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 积分商品状态 */
export type PointsProductStatus = 'ACTIVE' | 'INACTIVE'

/** 积分商品 */
export interface PointsProduct {
  productId: number
  name: string
  description?: string
  imageUrl?: string
  pointsRequired: number
  stock: number
  status: PointsProductStatus
  sort: number
  createdAt: string
  updatedAt: string
}

/** 积分商品表单数据 */
export interface PointsProductFormData {
  name: string
  description?: string
  imageUrl?: string
  pointsRequired: number
  stock: number
  status: PointsProductStatus
  sort: number
}

/** 积分记录类型 */
export type PointsRecordType = 'EARN' | 'DEDUCT'

/** 积分记录 */
export interface PointsRecord {
  recordId: number
  userId: number
  username?: string
  points: number
  type: PointsRecordType
  source: string
  description?: string
  orderNo?: string
  createdAt: string
}

/** 积分统计 */
export interface PointsStatistics {
  totalUsers: number
  totalEarned: number
  totalDeducted: number
  totalExchanged: number
}

/** 查询参数 */
export interface PointsProductQueryParams {
  page: number
  size: number
  status?: string
  keyword?: string
}

/** 积分记录查询参数 */
export interface PointsRecordQueryParams {
  page: number
  size: number
  userId?: number
  type?: string
  source?: string
}

/** 获取积分商品列表 */
export function getAdminProducts(params: PointsProductQueryParams) {
  return request.get<unknown, PageResponse<PointsProduct>>('/admin/points/products', { params })
}

/** 创建积分商品 */
export function createAdminProduct(data: PointsProductFormData) {
  return request.post<unknown, PointsProduct>('/admin/points/products', data)
}

/** 更新积分商品 */
export function updateAdminProduct(id: number, data: PointsProductFormData) {
  return request.put<unknown, PointsProduct>(`/admin/points/products/${id}`, data)
}

/** 删除积分商品 */
export function deleteAdminProduct(id: number) {
  return request.delete<unknown, void>(`/admin/points/products/${id}`)
}

/** 获取积分流水列表 */
export function getAdminRecords(params: PointsRecordQueryParams) {
  return request.get<unknown, PageResponse<PointsRecord>>('/admin/points/records', { params })
}

/** 获取积分统计 */
export function getPointsStatistics() {
  return request.get<unknown, PointsStatistics>('/admin/points/statistics')
}
