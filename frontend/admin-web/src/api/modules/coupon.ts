import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 优惠券类型 */
export type CouponType = 'FIXED_AMOUNT' | 'PERCENTAGE' | 'FIXED_DISCOUNT'

/** 优惠券适用范围 */
export type CouponScope = 'ALL' | 'CATEGORY' | 'PRODUCT'

/** 优惠券状态 */
export type CouponStatus = 'ACTIVE' | 'INACTIVE' | 'EXPIRED'

/** 优惠券 */
export interface Coupon {
  couponId: number
  name: string
  description?: string
  type: CouponType
  value: number
  minOrderAmount: number
  scope: CouponScope
  scopeId?: number
  scopeName?: string
  totalCount: number
  remainCount: number
  limitPerUser: number
  status: CouponStatus
  startTime: string
  endTime: string
  createdAt: string
  updatedAt: string
}

/** 优惠券统计 */
export interface CouponStats {
  couponId: number
  totalClaimed: number
  totalUsed: number
  usageRate: number
  totalDiscountAmount: number
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

/** 创建优惠券 */
export function createCoupon(data: CouponFormData) {
  return request.post<unknown, Coupon>('/admin/coupons', data)
}

/** 更新优惠券 */
export function updateCoupon(id: number, data: CouponFormData) {
  return request.put<unknown, Coupon>(`/admin/coupons/${id}`, data)
}

/** 删除优惠券 */
export function deleteCoupon(id: number) {
  return request.delete<unknown, void>(`/admin/coupons/${id}`)
}

/** 启用优惠券 */
export function enableCoupon(id: number) {
  return request.patch<unknown, void>(`/admin/coupons/${id}/enable`)
}

/** 禁用优惠券 */
export function disableCoupon(id: number) {
  return request.patch<unknown, void>(`/admin/coupons/${id}/disable`)
}

/** 获取优惠券统计 */
export function getCouponStats(id: number) {
  return request.get<unknown, CouponStats>(`/admin/coupons/${id}/stats`)
}
