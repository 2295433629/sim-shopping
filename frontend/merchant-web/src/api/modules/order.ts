import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** Order item */
export interface OrderItemVO {
  orderItemId: number
  productId: number
  skuId: number
  productName: string
  productImage: string
  skuName: string
  price: number
  quantity: number
  subtotal: number
}

/** Order list item */
export interface OrderListVO {
  orderNo: string
  status: string
  statusText: string
  buyerId: number
  buyerName: string
  buyerPhone: string
  itemCount: number
  totalAmount: number
  shippingFee: number
  payAmount: number
  createdAt: string
}

/** Order detail */
export interface OrderDetailVO {
  orderNo: string
  status: string
  statusText: string
  buyerId: number
  buyerName: string
  buyerPhone: string
  items: OrderItemVO[]
  totalAmount: number
  shippingFee: number
  payAmount: number
  remark: string
  createdAt: string
  paidAt: string | null
  shippedAt: string | null
  deliveredAt: string | null
  completedAt: string | null
  cancelledAt: string | null
  receiverName: string
  receiverPhone: string
  receiverAddress: string
  paymentMethod: string | null
  paymentStatus: string | null
  logisticsCompany: string | null
  logisticsNo: string | null
}

/** Order query params */
export interface OrderQueryParams {
  page?: number
  size?: number
  status?: string
}

/** Get merchant orders */
export function getMerchantOrders(params: OrderQueryParams) {
  return request.get<unknown, PageResponse<OrderListVO>>('/merchant/orders', { params })
}

/** Get merchant order detail */
export function getMerchantOrderDetail(orderNo: string) {
  return request.get<unknown, OrderDetailVO>(`/merchant/orders/${orderNo}`)
}
