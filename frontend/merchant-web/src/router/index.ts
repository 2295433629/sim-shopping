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
    redirect: '/home',
    children: [
      { path: 'home', name: 'Home', component: () => import('@/views/home/HomeView.vue'), meta: { title: 'Dashboard', requiresAuth: true } },
      { path: 'products', name: 'Products', component: () => import('@/views/products/ProductListView.vue'), meta: { title: 'Products', requiresAuth: true } },
      { path: 'products/new', name: 'ProductCreate', component: () => import('@/views/products/ProductEditView.vue'), meta: { title: 'New Product', requiresAuth: true } },
      { path: 'products/:id/edit', name: 'ProductEdit', component: () => import('@/views/products/ProductEditView.vue'), meta: { title: 'Edit Product', requiresAuth: true } },
      { path: 'orders', name: 'Orders', component: () => import('@/views/orders/OrderListView.vue'), meta: { title: 'Orders', requiresAuth: true } },
      { path: 'orders/:orderNo', name: 'OrderDetail', component: () => import('@/views/orders/OrderDetailView.vue'), meta: { title: 'Order Detail', requiresAuth: true } },
      { path: 'orders/shipping', name: 'ShippingOrders', component: () => import('@/views/orders/ShippingListView.vue'), meta: { title: '待发货订单', requiresAuth: true } },
      { path: 'reviews', name: 'MerchantReviews', component: () => import('@/views/reviews/ReviewListView.vue'), meta: { title: '评价管理', requiresAuth: true } },
      { path: 'notifications', name: 'MerchantNotifications', component: () => import('@/views/notification/NotificationView.vue'), meta: { title: '消息中心', requiresAuth: true } },
      { path: 'settings', name: 'Settings', component: () => import('@/views/settings/ShopSettingsView.vue'), meta: { title: 'Settings', requiresAuth: true } },
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
    next({ path: '/home' })
  } else {
    next()
  }
})

export default router