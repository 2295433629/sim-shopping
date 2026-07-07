import request from '@/api/request'
import type { UserInfo, LoginResult } from '@/types/common'

/** 管理员登录 */
export function loginApi(username: string, password: string) {
  return request.post<unknown, LoginResult>('/admin/login', { username, password })
}

/** 退出登录 */
export function logoutApi() {
  return request.post<unknown, void>('/common/logout')
}

/** 获取当前用户信息 */
export function getUserInfoApi() {
  return request.get<unknown, UserInfo>('/common/userinfo')
}

/** 刷新 token */
export function refreshTokenApi(refreshToken: string) {
  return request.post<unknown, LoginResult>('/common/refresh-token', { refreshToken })
}
