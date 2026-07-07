import request from '../request'

export function getMerchantInfo() {
  return request.get('/merchant/info')
}

export function updateMerchantInfo(data: { merchantName: string; contactPhone: string; contactEmail?: string }) {
  return request.put('/merchant/info', data)
}

export function getShopInfo() {
  return request.get('/merchant/shop')
}

export function updateShopInfo(data: { shopName: string; shopLogo: string; description: string }) {
  return request.put('/merchant/shop', data)
}

export function addBanner(data: { imageUrl: string; sortOrder: number; linkUrl?: string }) {
  return request.post('/merchant/shop/banners', data)
}

export function removeBanner(id: number) {
  return request.delete(`/merchant/shop/banners/${id}`)
}