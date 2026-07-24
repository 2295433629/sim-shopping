import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** Order item */
export interface OrderItemVO {
  productId: number
  productName: string
  productImage: string
  skuId: number
  skuName: string
  price: number
  quantity: number
  subtotal: number
}

/** Order list item — 对齐后端 OrderListItemVO */
export interface OrderListVO {
  orderNo: string
  status: string
  shopId: number
  shopName: string
  items: OrderItemVO[]
  totalAmount: number
  payAmount: number
  createdAt: string
}

/** Payment info — 对齐后端 PaymentInfoVO */
export interface PaymentInfoVO {
  paymentNo: string
  paymentMethod: string
  amount: number
  status: string
  paidAt: string
}

/** Logistics info — 对齐后端 LogisticsInfoVO */
export interface LogisticsInfoVO {
  trackingNo: string
  logisticsCompany: string
  status: string
}

/** Refund VO — 对齐后端 RefundVO */
export interface RefundVO {
  refundId: number
  orderNo: string
  refundType: string
  status: string
  reason: string
  amount: number
  shopResponse: string | null
  handledAt: string | null
  completedAt: string | null
  createdAt: string
}

/** Order detail — 对齐后端 OrderDetailVO */
export interface OrderDetailVO {
  orderId?: number
  orderNo: string
  status: string
  shopId: number
  shopName: string
  items: OrderItemVO[]
  totalAmount: number
  shippingFee: number
  discountAmount: number
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
  payment: PaymentInfoVO | null
  logistics: LogisticsInfoVO | null
  refund: RefundVO | null
}

/** 创建订单返回的单个订单 — 对齐后端 OrderResponse */
export interface CreatedOrderInfo {
  orderId: number
  orderNo: string
  status: string
  totalAmount: number
  shippingFee: number
  discountAmount: number
  payAmount: number
  shopId: number
  shopName: string
  items: OrderItemVO[]
  createdAt: string
  paidAt: string | null
  shippedAt: string | null
}

/** Create order result — 对齐后端 CreateOrderResponse */
export interface CreateOrderResult {
  orders: CreatedOrderInfo[]
}

/** Payment result — 对齐后端 PaymentResponse */
export interface PaymentResult {
  paymentNo: string
  orderNo: string
  amount: number
  paymentMethod: string
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
export function createOrder(data: { addressId: number; remark?: string; cartItemIds: number[]; couponId?: number }) {
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

/** Apply refund */
export function applyRefund(orderNo: string, data: { refundType: string; reason: string; amount: number }) {
  return request.post<unknown, RefundVO>(`/user/orders/${orderNo}/refund`, data)
}

/** Get refund status */
export function getRefundStatus(orderNo: string) {
  return request.get<unknown, RefundVO>(`/user/orders/${orderNo}/refund`)
}
