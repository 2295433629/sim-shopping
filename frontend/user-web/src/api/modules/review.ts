import request from '@/api/request'

export interface ReviewItem {
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
  merchantRepliedAt?: string
  createdAt: string
}

export interface CreateReviewParams {
  productId: number
  orderId: number
  orderNo: string
  rating: number
  content: string
  imageUrls: string[]
}

/** 提交评价 */
export function submitReview(data: CreateReviewParams) {
  return request.post('/user/reviews', data)
}

/** 我的评价 */
export function getMyReviews(params: { page: number; size: number }) {
  return request.get('/user/reviews', { params })
}

/** 删除评价 */
export function deleteReview(reviewId: number) {
  return request.delete(`/user/reviews/${reviewId}`)
}

/** 商品评价列表 */
export function getProductReviews(productId: number, params: { page: number; size: number }) {
  return request.get(`/public/products/${productId}/reviews`, { params })
}
