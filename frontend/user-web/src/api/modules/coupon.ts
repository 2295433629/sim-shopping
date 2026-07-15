import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 优惠券类型 */
export type CouponType = 'FIXED_AMOUNT' | 'PERCENTAGE' | 'FIXED_DISCOUNT'

/** 优惠券适用范围 */
export type CouponScope = 'ALL' | 'CATEGORY' | 'PRODUCT'

/** 用户优惠券状态 */
export type UserCouponStatus = 'UNUSED' | 'USED' | 'EXPIRED'

/** 可领取的优惠券 — 对齐后端 CouponResponse */
export interface AvailableCoupon {
  id: number
  couponCode?: string
  couponName: string
  couponType: CouponType
  discountValue: number
  minSpend: number
  applicableScope: CouponScope
  remainingQuantity: number
  validStartTime: string
  validEndTime: string
  status: string
  createdAt?: string
  updatedAt?: string
}

/** 我的优惠券 — 对齐后端 UserCouponResponse */
export interface MyCoupon {
  id: number
  userId: number
  couponId: number
  couponCode: string
  couponName: string
  couponType: CouponType
  discountValue: number
  minSpend: number
  validStartTime: string
  validEndTime: string
  applicableScope: CouponScope
  applicableIds: string
  claimedAt: string
  usedAt?: string
  orderNo?: string
  status: UserCouponStatus
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
  return request.get<unknown, PageResponse<MyCoupon>>('/user/coupons', { params })
}
