import request from '@/api/request'
import type { PageResponse } from '@/types/common'

/** Order list item */
export interface OrderListVO {
  orderNo: string
  status: string
  statusText: string
  shopId: number
  shopName: string
  buyerId: number
  buyerName: string
  itemCount: number
  totalAmount: number
  shippingFee: number
  payAmount: number
  createdAt: string
}

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

/** Order detail */
export interface OrderDetailVO {
  orderNo: string
  status: string
  statusText: string
  shopId: number
  shopName: string
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
  shopId?: number
  keyword?: string
}

/** Get admin orders */
export function getAdminOrders(params: OrderQueryParams) {
  return request.get<unknown, PageResponse<OrderListVO>>('/admin/orders', { params })
}

/** Get admin order detail */
export function getAdminOrderDetail(orderNo: string) {
  return request.get<unknown, OrderDetailVO>(`/admin/orders/${orderNo}`)
}
