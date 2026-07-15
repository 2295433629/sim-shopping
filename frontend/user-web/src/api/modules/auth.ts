import request from '@/api/request'
import type { UserInfo, LoginResult, RegisterResult } from '@/types/common'

/** 用户注册 */
export function registerApi(username: string, password: string, nickname?: string) {
  return request.post<unknown, RegisterResult>('/common/register', { username, password, nickname })
}

/** 用户登录 */
export function loginApi(username: string, password: string) {
  return request.post<unknown, LoginResult>('/common/login', { username, password })
}

/** 退出登录 */
export function logoutApi() {
  return request.post<unknown, void>('/common/logout')
}

/** 获取当前用户信息 */
export function getUserInfoApi() {
  return request.get<unknown, UserInfo>('/common/userinfo')
}

/** 更新个人信息 */
export function updateProfileApi(data: Partial<UserInfo>) {
  return request.put<unknown, void>('/user/profile', data)
}

/** 修改密码 */
export function changePasswordApi(oldPassword: string, newPassword: string) {
  return request.put<unknown, void>('/user/password', { oldPassword, newPassword })
}

/** 刷新 token */
export function refreshTokenApi(refreshToken: string) {
  return request.post<unknown, LoginResult>('/common/refresh-token', { refreshToken })
}
