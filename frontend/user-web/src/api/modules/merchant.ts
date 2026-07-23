import request from '@/api/request'

export interface MerchantApplyRequest {
  merchantName: string
  contactPhone: string
  contactEmail?: string
  businessLicense?: string
}

export interface MerchantInfo {
  merchantId: number
  merchantName: string
  contactPhone: string
  contactEmail?: string
  status: string
  shopId?: number
  shopName?: string
  rejectReason?: string
}

export function applyMerchant(data: MerchantApplyRequest) {
  return request.post('/merchant/apply', data)
}

export function getMerchantInfo() {
  return request.get('/merchant/info')
}
