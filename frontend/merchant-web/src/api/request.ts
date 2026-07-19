import axios, { type AxiosInstance, type InternalAxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from '@/utils/storage'
import type { ApiResponse } from '@/types/common'

const request: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器：自动添加 token
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器：统一错误处理
request.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const res = response.data
    if (res.code === 200) {
      return res.data as unknown as AxiosResponse
    }
    if (res.code === 401) {
      removeToken()
      ElMessage.error('登录已过期，请重新登录')
      window.location.href = '/login'
      return Promise.reject(new Error(res.message || '未认证'))
    }
    if (res.code === 403) {
      removeToken()
      ElMessage.error('登录已过期，请重新登录')
      window.location.href = '/login'
      return Promise.reject(new Error(res.message || '无权限'))
    }
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || 'Error'))
  },
  (error) => {
    const { response } = error
    if (response) {
      switch (response.status) {
        case 401:
          removeToken()
          ElMessage.error('登录已过期，请重新登录')
          window.location.href = import.meta.env.BASE_URL + 'login'
          break
        case 403:
          removeToken()
          ElMessage.error('登录已过期，请重新登录')
          window.location.href = import.meta.env.BASE_URL + 'login'
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(response.data?.message || `请求错误 (${response.status})`)
      }
    } else {
      ElMessage.error('网络异常，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

export default request
