import request from '@/api/request'

export interface BannerItem {
  id: number
  title: string
  imageUrl: string
  linkUrl?: string
  productId?: number
  sortOrder: number
  startTime: string
  endTime: string
  status: string
}

/** 首页Banner列表 */
export function getBanners() {
  return request.get('/public/banners')
}
