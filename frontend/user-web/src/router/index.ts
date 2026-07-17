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
      // === 免登录浏览 ===
      { path: 'home', name: 'Home', component: () => import('@/views/home/HomeView.vue'), meta: { title: '首页' } },
      { path: 'products', name: 'ProductList', component: () => import('@/views/ProductListView.vue'), meta: { title: '商品列表' } },
      { path: 'products/:productId', name: 'ProductDetail', component: () => import('@/views/ProductDetailView.vue'), meta: { title: '商品详情' } },
      { path: 'search', name: 'Search', component: () => import('@/views/SearchView.vue'), meta: { title: '搜索' } },
      { path: 'category', name: 'Category', component: () => import('@/views/CategoryView.vue'), meta: { title: '分类' } },
      { path: 'category/:categoryId', name: 'CategoryWithId', component: () => import('@/views/CategoryView.vue'), meta: { title: '分类' } },
      { path: 'shop/:shopId', name: 'Shop', component: () => import('@/views/ShopView.vue'), meta: { title: '店铺' } },
      // === 需要登录 ===
      { path: 'favorites', name: 'Favorites', component: () => import('@/views/FavoriteView.vue'), meta: { title: '我的收藏', requiresAuth: true } },
      { path: 'history', name: 'History', component: () => import('@/views/history/HistoryView.vue'), meta: { title: '浏览历史', requiresAuth: true } },
      { path: 'profile', name: 'Profile', component: () => import('@/views/profile/ProfileView.vue'), meta: { title: '个人中心', requiresAuth: true } },
      { path: 'addresses', name: 'Addresses', component: () => import('@/views/address/AddressView.vue'), meta: { title: '收货地址', requiresAuth: true } },
      { path: 'cart', name: 'Cart', component: () => import('@/views/cart/CartView.vue'), meta: { title: '购物车', requiresAuth: true } },
      { path: 'checkout', name: 'Checkout', component: () => import('@/views/order/CheckoutView.vue'), meta: { title: '结算', requiresAuth: true } },
      { path: 'payment/:orderNo', name: 'Payment', component: () => import('@/views/order/PaymentView.vue'), meta: { title: '支付', requiresAuth: true } },
      { path: 'orders', name: 'Orders', component: () => import('@/views/order/OrderListView.vue'), meta: { title: '我的订单', requiresAuth: true } },
      { path: 'orders/:orderNo', name: 'OrderDetail', component: () => import('@/views/order/OrderDetailView.vue'), meta: { title: '订单详情', requiresAuth: true } },
      { path: 'order/:orderNo/logistics', name: 'Logistics', component: () => import('@/views/order/LogisticsView.vue'), meta: { title: '物流追踪', requiresAuth: true } },
      { path: 'order/:orderNo/review', name: 'ReviewSubmit', component: () => import('@/views/order/ReviewSubmitView.vue'), meta: { title: '提交评价', requiresAuth: true } },
      { path: 'reviews', name: 'MyReviews', component: () => import('@/views/review/MyReviewsView.vue'), meta: { title: '我的评价', requiresAuth: true } },
      { path: 'signin', name: 'SignIn', component: () => import('@/views/signin/SignInView.vue'), meta: { title: '每日签到', requiresAuth: true } },
      { path: 'notifications', name: 'Notifications', component: () => import('@/views/notification/NotificationView.vue'), meta: { title: '消息中心', requiresAuth: true } },
      { path: 'coupon-center', name: 'CouponCenter', component: () => import('@/views/coupon/CouponCenterView.vue'), meta: { title: '领券中心' } },
      { path: 'my-coupons', name: 'MyCoupons', component: () => import('@/views/coupon/MyCouponsView.vue'), meta: { title: '我的优惠券', requiresAuth: true } },
      { path: 'points-mall', name: 'PointsMall', component: () => import('@/views/points/PointsMallView.vue'), meta: { title: '积分商城' } },
      { path: 'points-records', name: 'PointsRecords', component: () => import('@/views/points/PointsRecordView.vue'), meta: { title: '积分明细', requiresAuth: true } },
      { path: 'flash-sale', name: 'FlashSale', component: () => import('@/views/flashsale/FlashSaleView.vue'), meta: { title: '限时秒杀' } },
      { path: 'flash-sale/:id', name: 'FlashSaleDetail', component: () => import('@/views/flashsale/FlashSaleDetailView.vue'), meta: { title: '秒杀详情' } },
      { path: 'activities', name: 'ActivityList', component: () => import('@/views/activity/ActivityListView.vue'), meta: { title: '专题活动' } },
      { path: 'activities/:id', name: 'ActivityDetail', component: () => import('@/views/activity/ActivityDetailView.vue'), meta: { title: '活动详情' } },
      { path: 'rankings', name: 'Rankings', component: () => import('@/views/ranking/RankingView.vue'), meta: { title: '排行榜' } },
    ],
  },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/views/common/NotFoundView.vue'), meta: { title: '页面不存在' } },
]

const router = createRouter({ history: createWebHistory(import.meta.env.BASE_URL), routes })

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
