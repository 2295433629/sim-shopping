import request from '@/api/request'

export interface ShipmentItem {
  id: number
  orderNo: string
  shipmentNo: string
  logisticsCompany: string
  trackingNo: string
  status: string
  shippedAt: string
}

export interface CreateShipmentParams {
  orderNo: string
  logisticsCompany: string
  trackingNo: string
}

/** 待发货订单列表 */
export function getShippingOrders(params: { page: number; size: number }) {
  return request.get('/merchant/orders/shipping', { params })
}

/** 创建发货单 */
export function createShipment(data: CreateShipmentParams) {
  return request.post('/merchant/shipments', data)
}

/** 查看发货信息 */
export function getShipmentInfo(orderNo: string) {
  return request.get(`/common/shipments/${orderNo}`)
}

/** 查看物流信息 */
export function getLogisticsInfo(orderNo: string) {
  return request.get(`/common/logistics/${orderNo}`)
}
