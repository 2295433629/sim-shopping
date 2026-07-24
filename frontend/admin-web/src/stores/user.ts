import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { loginApi, logoutApi, getUserInfoApi } from '@/api/modules/auth'
import { getToken, setToken, removeToken, setRefreshToken, removeRefreshToken, setUserInfo, getUserInfo as getStoredUserInfo, removeUserInfo } from '@/utils/storage'
import type { UserInfo } from '@/types/common'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(getToken() || '')
  const userInfo = ref<UserInfo | null>(getStoredUserInfo())

  const isLoggedIn = computed(() => !!token.value)

  async function login(username: string, password: string) {
    const res = await loginApi(username, password)
    token.value = res.token
    setToken(res.token)
    if (res.refreshToken) {
      setRefreshToken(res.refreshToken)
    }
    const info: UserInfo = {
      userId: res.userId,
      username: res.username,
      nickname: res.nickname,
      avatar: res.avatar,
      role: res.role,
    }
    userInfo.value = info
    setUserInfo(info)
    return res
  }

  async function fetchUserInfo() {
    const res = await getUserInfoApi()
    userInfo.value = res
    setUserInfo(res)
    return res
  }

  async function logout() {
    // 先清除本地token，避免logout请求期间并发401触发token刷新
    token.value = ''
    userInfo.value = null
    removeToken()
    removeRefreshToken()
    removeUserInfo()
    // 尽力通知后端吊销token，失败不影响本地已清理的状态
    try {
      await logoutApi()
    } catch {
      // logout请求本身失败（如网络错误、token已过期等）不影响登出结果
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    fetchUserInfo,
    logout,
  }
})
