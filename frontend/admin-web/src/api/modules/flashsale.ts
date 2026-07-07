import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 秒杀活动 */
export interface FlashSale {
  saleId: number
  productId: number
  productName: string
  productImage: string
  originalPrice: number
  flashPrice: number
  stock: number
  sold: number
  limitPerUser: number
  startTime: string
  endTime: string
  status: string
}

/** 秒杀活动表单数据 */
export interface FlashSaleFormData {
  productId: number
  originalPrice: number
  flashPrice: number
  stock: number
  limitPerUser: number
  startTime: string
  endTime: string
}

/** 秒杀查询参数 */
export interface FlashSaleQueryParams {
  page?: number
  size?: number
  status?: string
  keyword?: string
}

/** 获取秒杀活动列表 */
export function getAdminFlashSales(params?: FlashSaleQueryParams) {
  return request.get<unknown, PageResponse<FlashSale>>('/admin/flash-sales', { params })
}

/** 创建秒杀活动 */
export function createAdminFlashSale(data: FlashSaleFormData) {
  return request.post<unknown, FlashSale>('/admin/flash-sales', data)
}

/** 更新秒杀活动 */
export function updateAdminFlashSale(id: number, data: FlashSaleFormData) {
  return request.put<unknown, FlashSale>(`/admin/flash-sales/${id}`, data)
}

/** 删除秒杀活动 */
export function deleteAdminFlashSale(id: number) {
  return request.delete<unknown, void>(`/admin/flash-sales/${id}`)
}
