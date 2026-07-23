import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** 商家列表项（对应后端 MerchantListResponse） */
export interface MerchantListItem {
  merchantId: number
  userId: number
  merchantName: string
  contactPhone: string
  status: string
  createdAt: string
}

/** 商家详情（对应后端 MerchantInfoResponse） */
export interface MerchantDetailInfo {
  merchantId: number
  merchantName: string
  contactPhone: string
  contactEmail: string
  status: string
  shopId: number
  shopName: string
  businessLicense: string
  rejectReason: string
}

export function getMerchants(params: { page: number; size: number; status?: string; keyword?: string }) {
  return request.get<unknown, PageResponse<MerchantListItem>>('/admin/merchants', { params })
}

export function getPendingMerchants(params: { page: number; size: number }) {
  return request.get<unknown, PageResponse<MerchantListItem>>('/admin/merchants/pending', { params })
}

export function getMerchantDetail(id: number) {
  return request.get<unknown, MerchantDetailInfo>(`/admin/merchants/${id}`)
}

export function approveMerchant(id: number) {
  return request.patch<unknown, void>(`/admin/merchants/${id}/approve`)
}

export function rejectMerchant(id: number, reason: string) {
  return request.patch<unknown, void>(`/admin/merchants/${id}/reject`, { reason })
}

export function disableMerchant(id: number) {
  return request.patch<unknown, void>(`/admin/merchants/${id}/disable`)
}

export function enableMerchant(id: number) {
  return request.patch<unknown, void>(`/admin/merchants/${id}/enable`)
}