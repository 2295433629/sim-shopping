import request from '../request'

export interface AdminReviewItem {
  id: number
  orderNo: string
  productId: number
  productName: string
  userId: number
  username: string
  rating: number
  content: string
  images: string[]
  status: string
  merchantReply?: string
  createdAt: string
}

export function getAdminReviews(params: { page: number; size: number; keyword?: string }) {
  return request.get('/admin/reviews', { params })
}

export function hideReview(reviewId: number) {
  return request.patch(`/admin/reviews/${reviewId}/hide`)
}

export function showReview(reviewId: number) {
  return request.patch(`/admin/reviews/${reviewId}/show`)
}

export function deleteReview(reviewId: number) {
  return request.delete(`/admin/reviews/${reviewId}`)
}
