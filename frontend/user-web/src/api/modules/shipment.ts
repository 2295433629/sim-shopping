import request from '@/api/request'

/** 查看物流信息 */
export function getLogistics(orderNo: string) {
  return request.get(`/common/logistics/${orderNo}`)
}

/** 查看发货信息 */
export function getShipment(orderNo: string) {
  return request.get(`/common/shipments/${orderNo}`)
}
