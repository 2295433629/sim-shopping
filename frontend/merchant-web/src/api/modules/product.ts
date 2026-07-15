import request from '../request'
import type { PageResponse } from '@/types/common'

export function getProducts(params: { page: number; size: number; status?: string; keyword?: string }) {
  return request.get<unknown, PageResponse>('/merchant/products', { params })
}

export function getProductDetail(id: number) {
  return request.get<unknown, any>(`/merchant/products/${id}`)
}

export function createProduct(data: unknown) {
  return request.post<unknown, any>('/merchant/products', data)
}

export function updateProduct(id: number, data: unknown) {
  return request.put<unknown, any>(`/merchant/products/${id}`, data)
}

export function publishProduct(id: number) {
  return request.patch<unknown, any>(`/merchant/products/${id}/publish`)
}

export function offlineProduct(id: number) {
  return request.patch<unknown, any>(`/merchant/products/${id}/offline`)
}

export function deleteProduct(id: number) {
  return request.delete<unknown, any>(`/merchant/products/${id}`)
}

export function getCategories() {
  return request.get<unknown, any[]>('/public/categories')
}

export function getBrands() {
  return request.get<unknown, any[]>('/public/brands')
}

export function getDashboard() {
  return request.get<unknown, any>('/merchant/dashboard')
}