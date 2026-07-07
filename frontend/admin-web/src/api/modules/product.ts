import request from '../request'

export function getProducts(params: { page: number; size: number; status?: string; shopId?: number; keyword?: string }) {
  return request.get('/admin/products', { params })
}

export function getProductDetail(id: number) {
  return request.get(`/admin/products/${id}`)
}

export function forceOfflineProduct(id: number, reason: string) {
  return request.patch(`/admin/products/${id}/force-offline`, { reason })
}

export function getCategories() {
  return request.get('/public/categories')
}

export function createCategory(data: unknown) {
  return request.post('/admin/categories', data)
}

export function updateCategory(id: number, data: unknown) {
  return request.put(`/admin/categories/${id}`, data)
}

export function deleteCategory(id: number) {
  return request.delete(`/admin/categories/${id}`)
}

export function getBrands() {
  return request.get('/public/brands')
}

export function createBrand(data: unknown) {
  return request.post('/admin/brands', data)
}

export function updateBrand(id: number, data: unknown) {
  return request.put(`/admin/brands/${id}`, data)
}

export function deleteBrand(id: number) {
  return request.delete(`/admin/brands/${id}`)
}