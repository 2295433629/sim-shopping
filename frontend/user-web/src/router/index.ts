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
      // === 免登录浏览（真实电商体验）===
      { path: 'home', name: 'Home', component: () => import('@/views/home/HomeView.vue'), meta: { title: 'Home' } },
      { path: 'products', name: 'ProductList', component: () => import('@/views/ProductListView.vue'), meta: { title: 'Products' } },
      { path: 'products/:productId', name: 'ProductDetail', component: () => import('@/views/ProductDetailView.vue'), meta: { title: 'Detail' } },
      { path: 'search', name: 'Search', component: () => import('@/views/SearchView.vue'), meta: { title: 'Search' } },
      { path: 'category', name: 'Category', component: () => import('@/views/CategoryView.vue'), meta: { title: 'Category' } },
      { path: 'category/:categoryId', name: 'CategoryWithId', component: () => import('@/views/CategoryView.vue'), meta: { title: 'Category' } },
      { path: 'shop/:shopId', name: 'Shop', component: () => import('@/views/ShopView.vue'), meta: { title: 'Shop' } },
      // === 需要登录 ===
      { path: 'favorites', name: 'Favorites', component: () => import('@/views/FavoriteView.vue'), meta: { title: 'Favorites', requiresAuth: true } },
      { path: 'profile', name: 'Profile', component: () => import('@/views/profile/ProfileView.vue'), meta: { title: 'Profile', requiresAuth: true } },
      { path: 'addresses', name: 'Addresses', component: () => import('@/views/address/AddressView.vue'), meta: { title: 'Addresses', requiresAuth: true } },
      { path: 'cart', name: 'Cart', component: () => import('@/views/cart/CartView.vue'), meta: { title: 'Cart', requiresAuth: true } },
      { path: 'checkout', name: 'Checkout', component: () => import('@/views/order/CheckoutView.vue'), meta: { title: 'Checkout', requiresAuth: true } },
      { path: 'payment/:orderNo', name: 'Payment', component: () => import('@/views/order/PaymentView.vue'), meta: { title: 'Payment', requiresAuth: true } },
      { path: 'orders', name: 'Orders', component: () => import('@/views/order/OrderListView.vue'), meta: { title: 'Orders', requiresAuth: true } },
      { path: 'orders/:orderNo', name: 'OrderDetail', component: () => import('@/views/order/OrderDetailView.vue'), meta: { title: 'Order Detail', requiresAuth: true } },
      { path: 'order/:orderNo/logistics', name: 'Logistics', component: () => import('@/views/order/LogisticsView.vue'), meta: { title: '物流追踪', requiresAuth: true } },
      { path: 'order/:orderNo/review', name: 'ReviewSubmit', component: () => import('@/views/order/ReviewSubmitView.vue'), meta: { title: '提交评价', requiresAuth: true } },
      { path: 'reviews', name: 'MyReviews', component: () => import('@/views/review/MyReviewsView.vue'), meta: { title: '我的评价', requiresAuth: true } },
      { path: 'signin', name: 'SignIn', component: () => import('@/views/signin/SignInView.vue'), meta: { title: '每日签到', requiresAuth: true } },
      { path: 'notifications', name: 'Notifications', component: () => import('@/views/notification/NotificationView.vue'), meta: { title: '消息中心', requiresAuth: true } },
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