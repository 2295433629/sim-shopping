import request from '@/api/request'
import type { AddressInfo } from '@/types/common'

/** 获取地址列表 */
export function getAddressListApi() {
  return request.get<unknown, { list: AddressInfo[] }>('/user/addresses')
}

/** 新增地址 */
export function addAddressApi(data: Omit<AddressInfo, 'id'>) {
  return request.post<unknown, AddressInfo>('/user/addresses', data)
}

/** 编辑地址 */
export function updateAddressApi(addressId: number, data: Omit<AddressInfo, 'id'>) {
  return request.put<unknown, void>(`/user/addresses/${addressId}`, data)
}

/** 删除地址 */
export function deleteAddressApi(addressId: number) {
  return request.delete<unknown, void>(`/user/addresses/${addressId}`)
}

/** 设为默认地址 */
export function setDefaultAddressApi(addressId: number) {
  return request.put<unknown, void>(`/user/addresses/${addressId}/default`)
}
