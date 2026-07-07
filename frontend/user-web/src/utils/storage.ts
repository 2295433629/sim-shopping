import type { UserInfo } from '@/types/common'

const TOKEN_KEY = 'token'
const USER_INFO_KEY = 'user_web_user_info'

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

export function removeToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

export function getUserInfo(): UserInfo | null {
  const raw = localStorage.getItem(USER_INFO_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw) as UserInfo
  } catch {
    return null
  }
}

export function setUserInfo(info: UserInfo): void {
  localStorage.setItem(USER_INFO_KEY, JSON.stringify(info))
}

export function removeUserInfo(): void {
  localStorage.removeItem(USER_INFO_KEY)
}

export function clearAll(): void {
  removeToken()
  removeUserInfo()
}
