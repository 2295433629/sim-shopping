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
    path: '/',
    component: () => import('@/layouts/BasicLayout.vue'),
    redirect: '/home',
    children: [
      { path: 'home', name: 'Home', component: () => import('@/views/home/HomeView.vue'), meta: { title: '数据看板', requiresAuth: true } },
      { path: 'products', name: 'Products', component: () => import('@/views/products/ProductListView.vue'), meta: { title: '商品管理', requiresAuth: true } },
      { path: 'products/new', name: 'ProductCreate', component: () => import('@/views/products/ProductEditView.vue'), meta: { title: '新增商品', requiresAuth: true } },
      { path: 'products/:id/edit', name: 'ProductEdit', component: () => import('@/views/products/ProductEditView.vue'), meta: { title: '编辑商品', requiresAuth: true } },
      { path: 'orders', name: 'Orders', component: () => import('@/views/orders/OrderListView.vue'), meta: { title: '订单管理', requiresAuth: true } },
      { path: 'orders/:orderNo', name: 'OrderDetail', component: () => import('@/views/orders/OrderDetailView.vue'), meta: { title: '订单详情', requiresAuth: true } },
      { path: 'orders/shipping', name: 'ShippingOrders', component: () => import('@/views/orders/ShippingListView.vue'), meta: { title: '待发货订单', requiresAuth: true } },
      { path: 'reviews', name: 'MerchantReviews', component: () => import('@/views/reviews/ReviewListView.vue'), meta: { title: '评价管理', requiresAuth: true } },
      { path: 'notifications', name: 'MerchantNotifications', component: () => import('@/views/notification/NotificationView.vue'), meta: { title: '消息中心', requiresAuth: true } },
      { path: 'finance', name: 'Finance', component: () => import('@/views/settlement/FinanceView.vue'), meta: { title: '财务收入', requiresAuth: true } },
      { path: 'settings', name: 'Settings', component: () => import('@/views/settings/ShopSettingsView.vue'), meta: { title: '店铺设置', requiresAuth: true } },
    ],
  },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/views/common/NotFoundView.vue'), meta: { title: '页面不存在' } },
]

const router = createRouter({ history: createWebHistory('/merchant/'), routes })

router.beforeEach((to, _from, next) => {
  const token = getToken()
  if (to.meta.requiresAuth && !token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if (to.path === '/login' && token) {
    next({ path: '/home' })
  } else {
    next()
  }
})

export default router
