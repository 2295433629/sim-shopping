import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 财务概览响应 */
export interface ShopFinanceResponse {
  balance: number
  totalIncome: number
  totalSettled: number
  frozenAmount: number
  todayIncome: number
}

/** 结算记录响应 */
export interface SettlementRecordResponse {
  id: number
  orderNo: string
  amount: number
  type: string
  status: string
  description: string
  createdAt: string
}

/** 获取财务概览 */
export function getFinance() {
  return request.get<unknown, ShopFinanceResponse>('/merchant/settlement/finance')
}

/** 获取结算记录 */
export function getSettlementRecords(params: { page: number; size: number }) {
  return request.get<unknown, PageResponse<SettlementRecordResponse>>('/merchant/settlement/records', { params })
}
