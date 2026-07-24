import request from '@/api/request'

/** 物流轨迹 */
export interface LogisticsTrack {
  status: string
  description: string
  location: string
  occurredAt: string
}

/** 物流信息响应 */
export interface LogisticsData {
  trackingNo: string
  logisticsCompany: string
  status: string
  senderAddress: string
  receiverAddress: string
  senderCity: string
  tracks: LogisticsTrack[]
}

/** 查看物流信息 */
export function getLogistics(orderNo: string) {
  return request.get<unknown, LogisticsData>(`/common/logistics/${orderNo}`)
}

/** 查看发货信息 */
export function getShipment(orderNo: string) {
  return request.get<unknown, unknown>(`/common/shipments/${orderNo}`)
}
