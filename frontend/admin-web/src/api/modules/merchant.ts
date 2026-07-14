import request from '@/api/request'

export interface MerchantItem {
  id: number
  name: string
  description?: string
  logo?: string
  status?: string
  contactName?: string
  contactPhone?: string
  createdAt?: string
}

export function getMerchants(params: { page: number; size: number; status?: string; keyword?: string }) {
  return request.get<unknown, { list: MerchantItem[]; total: number; page: number; size: number; totalPages: number }>('/admin/merchants', { params })
}

export function getPendingMerchants(params: { page: number; size: number }) {
  return request.get<unknown, { list: MerchantItem[]; total: number; page: number; size: number; totalPages: number }>('/admin/merchants/pending', { params })
}

export function getMerchantDetail(id: number) {
  return request.get<unknown, MerchantItem>(`/admin/merchants/${id}`)
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