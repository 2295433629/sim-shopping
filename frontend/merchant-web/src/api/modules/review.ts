import request from '@/api/request'

export interface MerchantReviewItem {
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

/** 店铺评价列表 */
export function getMerchantReviews(params: { page: number; size: number }) {
  return request.get('/merchant/reviews', { params })
}

/** 回复评价 */
export function replyReview(reviewId: number, content: string) {
  return request.post(`/merchant/reviews/${reviewId}/reply`, { content })
}
