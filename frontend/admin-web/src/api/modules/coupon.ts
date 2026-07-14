import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 优惠券类型 */
export type CouponType = 'FIXED_AMOUNT' | 'PERCENTAGE' | 'FIXED_DISCOUNT'

/** 优惠券适用范围 */
export type CouponScope = 'ALL' | 'CATEGORY' | 'PRODUCT'

/** 优惠券状态 */
export type CouponStatus = 'ACTIVE' | 'INACTIVE' | 'EXPIRED'

/** 优惠券 — 对齐后端 CouponResponse */
export interface Coupon {
  id: number
  couponCode: string
  couponName: string
  couponType: CouponType
  discountValue: number
  minSpend: number
  totalQuantity: number
  claimedQuantity: number
  usedQuantity: number
  remainingQuantity: number
  validStartTime: string
  validEndTime: string
  applicableScope: CouponScope
  applicableIds: string
  status: CouponStatus
  createdAt: string
  updatedAt: string
}

/** 优惠券统计 — 对齐后端 CouponStatisticsVO */
export interface CouponStats {
  totalCoupons: number
  totalClaimed: number
  totalUsed: number
  activeCoupons: number
  expiredCoupons: number
}

/** 创建/编辑优惠券请求 */
export interface CouponFormData {
  name: string
  description?: string
  type: CouponType
  value: number
  minOrderAmount: number
  scope: CouponScope
  scopeId?: number
  totalCount: number
  limitPerUser: number
  startTime: string
  endTime: string
}

/** 查询参数 */
export interface CouponQueryParams {
  page: number
  size: number
  status?: string
  keyword?: string
}

/** 获取优惠券列表 */
export function getCouponList(params: CouponQueryParams) {
  return request.get<unknown, PageResponse<Coupon>>('/admin/coupons', { params })
}

/** 将前端 CouponFormData 映射为后端 CouponDO 的 camelCase 字段名 */
function mapFormToBackend(data: CouponFormData): Record<string, unknown> {
  return {
    couponName: data.name,
    couponType: data.type,
    discountValue: data.value,
    minSpend: data.minOrderAmount,
    applicableScope: data.scope,
    applicableIds: data.scopeId ? String(data.scopeId) : null,
    totalQuantity: data.totalCount,
    validStartTime: data.startTime,
    validEndTime: data.endTime,
    couponCode: 'COUPON_' + Date.now(),
    status: 'ACTIVE',
  }
}

/** 创建优惠券 */
export function createCoupon(data: CouponFormData) {
  return request.post<unknown, Coupon>('/admin/coupons', mapFormToBackend(data))
}

/** 更新优惠券 */
export function updateCoupon(id: number, data: CouponFormData) {
  return request.put<unknown, Coupon>(`/admin/coupons/${id}`, mapFormToBackend(data))
}

/** 删除优惠券 */
export function deleteCoupon(id: number) {
  return request.delete<unknown, void>(`/admin/coupons/${id}`)
}

/** 获取优惠券统计（全局） */
export function getCouponStats() {
  return request.get<unknown, CouponStats>('/admin/coupons/statistics')
}
