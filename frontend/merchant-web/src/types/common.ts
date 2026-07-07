/** 统一 API 响应结构 */
export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
  timestamp?: number
}

/** 分页响应结构 */
export interface PageResponse<T = unknown> {
  list: T[]
  total: number
  page: number
  /**
   * 后端字段为 size（API-001），这里保留 size 与 pageSize 两种写法以兼容不同模块。
   */
  size?: number
  pageSize?: number
  totalPages?: number
}

/** 商家用户信息 */
export interface UserInfo {
  userId: number
  username: string
  nickname?: string
  avatar?: string
  mobile?: string
  phone?: string
  email?: string
  role?: string
  roles?: string[]
  shopName?: string
  shopId?: number
  merchantId?: number
}

/** 登录结果 */
export interface LoginResult {
  userId: number
  username: string
  nickname?: string
  avatar?: string
  role?: string
  token: string
  expiresIn?: number
  refreshToken?: string
}

/** 登录请求参数 */
export interface LoginParams {
  username: string
  password: string
}

/** 分页请求参数 */
export interface PageParams {
  page: number
  pageSize: number
  keyword?: string
}
