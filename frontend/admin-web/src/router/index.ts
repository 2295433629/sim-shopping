import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { getToken } from '@/utils/storage'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/common/ForbiddenView.vue'),
    meta: { title: '无权限' },
  },
  {
    path: '/',
    component: () => import('@/layouts/BasicLayout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/DashboardView.vue'), meta: { title: '数据看板', requiresAuth: true } },
      { path: 'users', name: 'Users', component: () => import('@/views/users/UserListView.vue'), meta: { title: '用户管理', requiresAuth: true } },
      { path: 'merchants', name: 'Merchants', component: () => import('@/views/merchants/MerchantListView.vue'), meta: { title: '商家管理', requiresAuth: true } },
      { path: 'products', name: 'Products', component: () => import('@/views/products/ProductListView.vue'), meta: { title: '商品管理', requiresAuth: true } },
      { path: 'categories', name: 'Categories', component: () => import('@/views/products/CategoryManageView.vue'), meta: { title: '分类管理', requiresAuth: true } },
      { path: 'brands', name: 'Brands', component: () => import('@/views/products/BrandManageView.vue'), meta: { title: '品牌管理', requiresAuth: true } },
      { path: 'orders', name: 'Orders', component: () => import('@/views/orders/OrderListView.vue'), meta: { title: '订单管理', requiresAuth: true } },
      { path: 'orders/:orderNo', name: 'OrderDetail', component: () => import('@/views/orders/OrderDetailView.vue'), meta: { title: '订单详情', requiresAuth: true, parentTitle: '订单管理' } },
      { path: 'menus', name: 'Menus', component: () => import('@/views/menus/MenuListView.vue'), meta: { title: '菜单管理', requiresAuth: true } },
      { path: 'dicts', name: 'Dicts', component: () => import('@/views/dicts/DictListView.vue'), meta: { title: '字典管理', requiresAuth: true } },
      { path: 'roles', name: 'Roles', component: () => import('@/views/roles/RoleListView.vue'), meta: { title: '角色管理', requiresAuth: true } },
      { path: 'permissions', name: 'Permissions', component: () => import('@/views/permissions/PermissionListView.vue'), meta: { title: '权限管理', requiresAuth: true } },
      { path: 'reviews', name: 'ReviewManage', component: () => import('@/views/reviews/ReviewManageView.vue'), meta: { title: '评价管理', requiresAuth: true } },
      { path: 'banners', name: 'BannerManage', component: () => import('@/views/banners/BannerManageView.vue'), meta: { title: 'Banner管理', requiresAuth: true } },
      { path: 'coupons', name: 'Coupons', component: () => import('@/views/coupons/CouponListView.vue'), meta: { title: '优惠券管理', requiresAuth: true } },
      { path: 'points/products', name: 'PointsProducts', component: () => import('@/views/points/PointsProductListView.vue'), meta: { title: '积分商品管理', requiresAuth: true } },
      { path: 'points/records', name: 'PointsRecords', component: () => import('@/views/points/PointsRecordListView.vue'), meta: { title: '积分流水管理', requiresAuth: true } },
      { path: 'flash-sales', name: 'FlashSales', component: () => import('@/views/flashsale/FlashSaleListView.vue'), meta: { title: '秒杀活动管理', requiresAuth: true } },
      { path: 'activities', name: 'Activities', component: () => import('@/views/activity/ActivityListView.vue'), meta: { title: '专题活动管理', requiresAuth: true } },
      { path: 'logs/operation', name: 'OperationLog', component: () => import('@/views/logs/OperationLogView.vue'), meta: { title: '操作日志', requiresAuth: true } },
      { path: 'logs/login', name: 'LoginLog', component: () => import('@/views/logs/LoginLogView.vue'), meta: { title: '登录日志', requiresAuth: true } },
      { path: 'scheduler', name: 'ScheduleJobs', component: () => import('@/views/scheduler/ScheduleJobListView.vue'), meta: { title: '定时任务', requiresAuth: true } },
      { path: 'scheduler/:jobId/logs', name: 'ScheduleLogs', component: () => import('@/views/scheduler/ScheduleLogView.vue'), meta: { title: '执行历史', requiresAuth: true, parentTitle: '定时任务' } },
    ],
  },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/views/common/NotFoundView.vue'), meta: { title: '页面不存在' } },
]

const router = createRouter({ history: createWebHistory(import.meta.env.BASE_URL), routes })

// 路由守卫：登录检查 + 页面标题
router.beforeEach((to, _from, next) => {
  const token = getToken()
  if (to.meta.requiresAuth && !token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if (to.path === '/login' && token) {
    next({ path: '/dashboard' })
  } else {
    next()
  }
})

router.afterEach((to) => {
  const title = to.meta.title as string
  document.title = title ? `${title} - 模拟商城管理后台` : '模拟商城管理后台'
})

export default router
