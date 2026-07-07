import request from '../request'

export function getMerchants(params: { page: number; size: number; status?: string; keyword?: string }) {
  return request.get('/admin/merchants', { params })
}

export function getPendingMerchants(params: { page: number; size: number }) {
  return request.get('/admin/merchants/pending', { params })
}

export function getMerchantDetail(id: number) {
  return request.get(`/admin/merchants/${id}`)
}

export function approveMerchant(id: number) {
  return request.patch(`/admin/merchants/${id}/approve`)
}

export function rejectMerchant(id: number, reason: string) {
  return request.patch(`/admin/merchants/${id}/reject`, { reason })
}

export function disableMerchant(id: number) {
  return request.patch(`/admin/merchants/${id}/disable`)
}

export function enableMerchant(id: number) {
  return request.patch(`/admin/merchants/${id}/enable`)
}