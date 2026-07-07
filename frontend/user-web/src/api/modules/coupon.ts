import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 优惠券类型 */
export type CouponType = 'FIXED_AMOUNT' | 'PERCENTAGE' | 'FIXED_DISCOUNT'

/** 优惠券适用范围 */
export type CouponScope = 'ALL' | 'CATEGORY' | 'PRODUCT'

/** 用户优惠券状态 */
export type UserCouponStatus = 'UNUSED' | 'USED' | 'EXPIRED'

/** 可领取的优惠券 */
export interface AvailableCoupon {
  couponId: number
  name: string
  description?: string
  type: CouponType
  value: number
  minOrderAmount: number
  scope: CouponScope
  scopeName?: string
  remainCount: number
  limitPerUser: number
  startTime: string
  endTime: string
}

/** 我的优惠券 */
export interface MyCoupon {
  userCouponId: number
  couponId: number
  name: string
  description?: string
  type: CouponType
  value: number
  minOrderAmount: number
  status: UserCouponStatus
  claimedAt: string
  usedAt?: string
  expiredAt: string
  orderNo?: string
}

/** 领取优惠券 */
export function claimCoupon(couponId: number) {
  return request.post<unknown, void>(`/user/coupons/${couponId}/claim`)
}

/** 获取可领取的优惠券列表（公开，无需登录） */
export function getAvailableCoupons() {
  return request.get<unknown, AvailableCoupon[]>('/public/coupons/available')
}

/** 获取我的优惠券列表 */
export function getMyCoupons(params: { status?: string; page?: number; size?: number }) {
  return request.get<unknown, PageResponse<MyCoupon>>('/user/coupons/my', { params })
}
