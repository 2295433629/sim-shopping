import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { loginApi, registerApi, logoutApi, getUserInfoApi, updateProfileApi } from '@/api/modules/auth'
import { getToken, setToken, removeToken, setUserInfo, getUserInfo as getStoredUserInfo, removeUserInfo } from '@/utils/storage'
import type { UserInfo } from '@/types/common'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(getToken() || '')
  const userInfo = ref<UserInfo | null>(getStoredUserInfo())

  const isLoggedIn = computed(() => !!token.value)

  async function login(username: string, password: string) {
    const res = await loginApi(username, password)
    token.value = res.token
    setToken(res.token)
    // 登录返回中已含基本信息，先存一份
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

  async function register(username: string, password: string, nickname?: string) {
    const res = await registerApi(username, password, nickname)
    token.value = res.token
    setToken(res.token)
    const info: UserInfo = {
      userId: res.userId,
      username: res.username,
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

  async function updateProfile(data: Partial<UserInfo>) {
    await updateProfileApi(data)
    userInfo.value = { ...userInfo.value, ...data } as UserInfo
    if (userInfo.value) {
      setUserInfo(userInfo.value)
    }
    return userInfo.value
  }

  async function logout() {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      userInfo.value = null
      removeToken()
      removeUserInfo()
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    register,
    fetchUserInfo,
    updateProfile,
    logout,
  }
})
