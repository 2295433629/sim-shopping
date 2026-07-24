import request from '@/api/request'
import type { PageResponse } from '@/types/common'

export interface SignInResult {
  points: number
  consecutiveDays: number
  signed: boolean
}

export interface SignInRecord {
  id: number
  signDate: string
  consecutiveDays: number
  pointsEarned: number
}

/** 每日签到 */
export function signIn() {
  return request.post<unknown, SignInResult>('/user/sign-in')
}

/** 查询今日签到状态 */
export function getTodaySignInStatus() {
  return request.get<unknown, SignInResult>('/user/sign-in/today')
}

/** 签到记录 */
export function getSignInRecords(params: { page: number; size: number }) {
  return request.get<unknown, PageResponse<SignInRecord>>('/user/sign-in/records', { params })
}
