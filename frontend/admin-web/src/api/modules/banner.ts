import request from '../request'

export interface BannerItem {
  id: number
  title: string
  imageUrl: string
  linkUrl: string
  sortOrder: number
  startTime: string
  endTime: string
  status: string
}

export function getAdminBanners() {
  return request.get('/admin/banners')
}

export function createBanner(data: Omit<BannerItem, 'id'>) {
  return request.post('/admin/banners', data)
}

export function updateBanner(id: number, data: Partial<BannerItem>) {
  return request.put(`/admin/banners/${id}`, data)
}

export function deleteBanner(id: number) {
  return request.delete(`/admin/banners/${id}`)
}
