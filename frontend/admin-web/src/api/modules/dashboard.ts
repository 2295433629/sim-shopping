import request from '../request'

export interface DashboardData {
  totalUsers: number
  totalMerchants: number
  totalOrders: number
  totalProducts: number
  totalSales: number
  todayOrders: number
  todaySales: number
  pendingMerchants: number
  recentOrders: {
    orderId: number
    orderNo: string
    status: string
    totalAmount: number
    payAmount: number
    shopId: number
    shopName: string
    createdAt: string
  }[]
}

export function getDashboard() {
  return request.get<unknown, DashboardData>('/admin/dashboard')
}
