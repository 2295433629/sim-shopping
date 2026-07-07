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
  shopId: number
  shopName: string
  items: OrderItemVO[]
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
  shopId: number
  shopName: string
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

/** Create order result */
export interface CreateOrderResult {
  orderNo: string
  payAmount: number
}

/** Payment result */
export interface PaymentResult {
  paymentId: number
  orderNo: string
  status: string
  paidAt: string
}

/** Order query params */
export interface OrderQueryParams {
  page?: number
  size?: number
  status?: string
}

/** Create order */
export function createOrder(data: { addressId: number; remark?: string; cartItemIds: number[] }) {
  return request.post<unknown, CreateOrderResult>('/user/orders', data)
}

/** Get order list */
export function getOrders(params: OrderQueryParams) {
  return request.get<unknown, PageResponse<OrderListVO>>('/user/orders', { params })
}

/** Get order detail */
export function getOrderDetail(orderNo: string) {
  return request.get<unknown, OrderDetailVO>(`/user/orders/${orderNo}`)
}

/** Cancel order */
export function cancelOrder(orderNo: string) {
  return request.patch<unknown, void>(`/user/orders/${orderNo}/cancel`)
}

/** Confirm receive */
export function confirmReceive(orderNo: string) {
  return request.patch<unknown, void>(`/user/orders/${orderNo}/confirm`)
}

/** Create payment */
export function createPayment(orderNo: string, paymentMethod: string) {
  return request.post<unknown, PaymentResult>(`/user/payments/${orderNo}`, { paymentMethod })
}

/** Get payment status */
export function getPaymentStatus(orderNo: string) {
  return request.get<unknown, PaymentResult>(`/user/payments/${orderNo}`)
}
