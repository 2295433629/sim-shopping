import request from '@/api/request'

/** 排行榜周期类型 */
export type RankingPeriod = 'week' | 'month' | 'total'

/** 排行榜类型 */
export type RankingType = 'consumption' | 'signIn'

/** 排行榜条目 */
export interface RankingItem {
  rank: number
  userId: number
  nickname: string
  avatar?: string
  value: number
  unit?: string
}

/** 排行榜响应 — 对齐后端 RankingListResponse */
export interface RankingListResponse {
  period: string
  list: RankingItem[]
}

/** 获取消费排行榜（公开） */
export function getConsumptionRanking(period: RankingPeriod) {
  return request.get<unknown, RankingListResponse>('/public/rankings/consumption', { params: { period } })
}

/** 获取签到排行榜（公开） */
export function getSignInRanking(period: RankingPeriod) {
  return request.get<unknown, RankingListResponse>('/public/rankings/sign-in', { params: { period } })
}
