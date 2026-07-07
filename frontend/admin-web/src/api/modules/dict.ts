import request from '@/api/request'
import type { DictType, DictItem } from '@/types/common'

/** 字典类型列表 */
export function getDictTypeListApi() {
  return request.get<unknown, DictType[]>('/admin/dicts')
}

/** 创建字典类型 */
export function createDictTypeApi(data: Omit<DictType, 'id'>) {
  return request.post<unknown, DictType>('/admin/dicts', data)
}

/** 字典项列表 */
export function getDictItemListApi(dictTypeId: number) {
  return request.get<unknown, DictItem[]>(`/admin/dicts/${dictTypeId}/items`)
}

/** 创建字典项 */
export function createDictItemApi(dictTypeId: number, data: Omit<DictItem, 'id' | 'dictTypeId'>) {
  return request.post<unknown, DictItem>(`/admin/dicts/${dictTypeId}/items`, data)
}

/** 编辑字典项 */
export function updateDictItemApi(dictTypeId: number, itemId: number, data: Partial<DictItem>) {
  return request.put<unknown, void>(`/admin/dicts/${dictTypeId}/items/${itemId}`, data)
}

/** 删除字典项 */
export function deleteDictItemApi(dictTypeId: number, itemId: number) {
  return request.delete<unknown, void>(`/admin/dicts/${dictTypeId}/items/${itemId}`)
}
