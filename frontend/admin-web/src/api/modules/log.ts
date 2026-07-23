import request from '../request'

export interface OperationLogItem {
  id: number
  operatorId: number
  operatorName: string
  operatorType: string
  module: string
  operationType: string
  method: string
  requestUrl: string
  ip: string
  costTime: number
  createdAt: string
}

export interface LoginLogItem {
  id: number
  userId: number
  username: string
  userType: string
  status: number
  ip: string
  loginType: string
  userAgent: string
  errorMsg: string
  createdAt: string
}

export function getOperationLogs(params: { page: number; size: number; module?: string; operationType?: string; keyword?: string }) {
  return request.get('/admin/logs', { params })
}

export function getLogModuleDict() {
  return request.get<any>('/admin/logs/modules')
}

export function getLogOperationTypes() {
  return request.get<any>('/admin/logs/operation-types')
}

export function getLoginLogs(params: { page: number; size: number; username?: string; status?: number; userType?: string }) {
  return request.get('/admin/login-logs', { params })
}
