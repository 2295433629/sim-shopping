import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { getToken } from '@/utils/storage'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { title: 'Login' },
  },
  {
    path: '/',
    component: () => import('@/layouts/BasicLayout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/DashboardView.vue'), meta: { title: 'Dashboard', requiresAuth: true } },
      { path: 'users', name: 'Users', component: () => import('@/views/users/UserListView.vue'), meta: { title: 'Users', requiresAuth: true } },
      { path: 'merchants', name: 'Merchants', component: () => import('@/views/merchants/MerchantListView.vue'), meta: { title: 'Merchants', requiresAuth: true } },
      { path: 'products', name: 'Products', component: () => import('@/views/products/ProductListView.vue'), meta: { title: 'Products', requiresAuth: true } },
      { path: 'categories', name: 'Categories', component: () => import('@/views/products/CategoryManageView.vue'), meta: { title: 'Categories', requiresAuth: true } },
      { path: 'brands', name: 'Brands', component: () => import('@/views/products/BrandManageView.vue'), meta: { title: 'Brands', requiresAuth: true } },
      { path: 'orders', name: 'Orders', component: () => import('@/views/orders/OrderListView.vue'), meta: { title: 'Orders', requiresAuth: true } },
      { path: 'orders/:orderNo', name: 'OrderDetail', component: () => import('@/views/orders/OrderDetailView.vue'), meta: { title: 'Order Detail', requiresAuth: true } },
      { path: 'menus', name: 'Menus', component: () => import('@/views/menus/MenuListView.vue'), meta: { title: 'Menus', requiresAuth: true } },
      { path: 'dicts', name: 'Dicts', component: () => import('@/views/dicts/DictListView.vue'), meta: { title: 'Dicts', requiresAuth: true } },
      { path: 'roles', name: 'Roles', component: () => import('@/views/roles/RoleListView.vue'), meta: { title: 'Roles', requiresAuth: true } },
      { path: 'permissions', name: 'Permissions', component: () => import('@/views/permissions/PermissionListView.vue'), meta: { title: 'Permissions', requiresAuth: true } },
      { path: 'reviews', name: 'ReviewManage', component: () => import('@/views/reviews/ReviewManageView.vue'), meta: { title: 'ReviewManage', requiresAuth: true } },
      { path: 'banners', name: 'BannerManage', component: () => import('@/views/banners/BannerManageView.vue'), meta: { title: 'BannerManage', requiresAuth: true } },
      { path: 'coupons', name: 'Coupons', component: () => import('@/views/coupons/CouponListView.vue'), meta: { title: 'Coupons', requiresAuth: true } },
      { path: 'points/products', name: 'PointsProducts', component: () => import('@/views/points/PointsProductListView.vue'), meta: { title: '积分商品管理', requiresAuth: true } },
      { path: 'points/records', name: 'PointsRecords', component: () => import('@/views/points/PointsRecordListView.vue'), meta: { title: '积分流水管理', requiresAuth: true } },
      { path: 'flash-sales', name: 'FlashSales', component: () => import('@/views/flashsale/FlashSaleListView.vue'), meta: { title: '秒杀活动管理', requiresAuth: true } },
      { path: 'activities', name: 'Activities', component: () => import('@/views/activity/ActivityListView.vue'), meta: { title: '专题活动管理', requiresAuth: true } },
      { path: 'logs/operation', name: 'OperationLog', component: () => import('@/views/logs/OperationLogView.vue'), meta: { title: 'OperationLog', requiresAuth: true } },
      { path: 'logs/login', name: 'LoginLog', component: () => import('@/views/logs/LoginLogView.vue'), meta: { title: 'LoginLog', requiresAuth: true } },
    ],
  },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/views/login/LoginView.vue'), meta: { title: 'Not Found' } },
]

const router = createRouter({ history: createWebHistory(), routes })

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

export default router