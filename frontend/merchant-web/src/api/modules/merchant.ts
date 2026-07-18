import request from '@/api/request'

export function getMerchantInfo() {
  return request.get<unknown, any>('/merchant/info')
}

export function updateMerchantInfo(data: { merchantName: string; contactPhone: string; contactEmail?: string }) {
  return request.put<unknown, any>('/merchant/info', data)
}

export function getShopInfo() {
  return request.get<unknown, any>('/merchant/shop')
}

export function updateShopInfo(data: {
  shopName: string
  shopLogo: string
  description: string
  senderName?: string
  senderPhone?: string
  senderProvince?: string
  senderCity?: string
  senderDistrict?: string
  senderAddress?: string
}) {
  return request.put<unknown, any>('/merchant/shop', data)
}

export function addBanner(data: { imageUrl: string; sortOrder: number; linkUrl?: string }) {
  return request.post<unknown, any>('/merchant/shop/banners', data)
}

export function removeBanner(id: number) {
  return request.delete<unknown, any>(`/merchant/shop/banners/${id}`)
}