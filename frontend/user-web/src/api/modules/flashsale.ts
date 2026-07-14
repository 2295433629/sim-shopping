import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 秒杀活动 — 对齐后端 FlashSaleResponse */
export interface FlashSale {
  saleId: number
  productId: number
  productName: string
  productImage: string
  originalPrice: number
  flashPrice: number
  stock: number
  soldCount: number
  limitPerUser: number
  startTime: string
  endTime: string
  status: string
}

/** 秒杀活动详情 — 对齐后端 FlashSaleDetailResponse */
export interface FlashSaleDetail extends FlashSale {
}

/** 创建秒杀订单结果 */
export interface FlashSaleOrderResult {
  orderNo: string
  payAmount: number
}

/** 秒杀查询参数 */
export interface FlashSaleQueryParams {
  page?: number
  size?: number
  status?: string
}

/** 获取进行中的秒杀活动列表 */
export function getActiveFlashSales(params?: FlashSaleQueryParams) {
  return request.get<unknown, PageResponse<FlashSale>>('/public/flash-sales', { params })
}

/** 获取秒杀活动详情 */
export function getFlashSaleDetail(saleId: number) {
  return request.get<unknown, FlashSaleDetail>(`/public/flash-sales/${saleId}`)
}

/** 创建秒杀订单 */
export function createFlashSaleOrder(saleId: number, data: { addressId: number; quantity: number }) {
  return request.post<unknown, FlashSaleOrderResult>(`/user/flash-sales/${saleId}/order`, data)
}
