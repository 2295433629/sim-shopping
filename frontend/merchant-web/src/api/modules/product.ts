import request from '../request'

export function getProducts(params: { page: number; size: number; status?: string; keyword?: string }) {
  return request.get('/merchant/products', { params })
}

export function getProductDetail(id: number) {
  return request.get(`/merchant/products/${id}`)
}

export function createProduct(data: unknown) {
  return request.post('/merchant/products', data)
}

export function updateProduct(id: number, data: unknown) {
  return request.put(`/merchant/products/${id}`, data)
}

export function publishProduct(id: number) {
  return request.patch(`/merchant/products/${id}/publish`)
}

export function offlineProduct(id: number) {
  return request.patch(`/merchant/products/${id}/offline`)
}

export function deleteProduct(id: number) {
  return request.delete(`/merchant/products/${id}`)
}

export function getCategories() {
  return request.get('/public/categories')
}

export function getBrands() {
  return request.get('/public/brands')
}

export function getDashboard() {
  return request.get('/merchant/dashboard')
}