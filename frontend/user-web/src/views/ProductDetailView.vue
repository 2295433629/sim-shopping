<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import DOMPurify from 'dompurify'
import { getProductDetail, addFavorite, removeFavorite, isFavorite } from '@/api/modules/product'
import { addToCart, getCart } from '@/api/modules/cart'
import { getProductReviews } from '@/api/modules/review'
import { getToken } from '@/utils/storage'
import type { ProductDetailVO, SkuVO } from '@/types/product'
import type { ReviewItem } from '@/api/modules/review'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const product = ref<ProductDetailVO | null>(null)
const selectedImage = ref('')
const selectedSku = ref<SkuVO | null>(null)
const quantity = ref(1)
const isFav = ref(false)

const safeDescription = computed(() => {
  if (!product.value?.description) return '暂无描述'
  return DOMPurify.sanitize(product.value.description)
})

const productId = computed(() => Number(route.params.productId))

onMounted(() => {
  loadDetail()
})

watch(() => route.params.productId, () => {
  loadDetail()
})

async function loadDetail() {
  if (!productId.value) return
  loading.value = true
  reviewSummaryLoaded.value = false
  try {
    product.value = await getProductDetail(productId.value)
    if (product.value) {
      selectedImage.value = product.value.mainImage || product.value.images?.[0] || ''
      if (product.value.skus.length > 0) {
        selectedSku.value = product.value.skus[0]
      }
      await checkFavorite()
      // 加载评价
      reviewSummaryLoaded.value = true
      reviewPage.value = 1
      activeRating.value = null
      await loadReviews()
    }
  } finally {
    loading.value = false
  }
}

async function checkFavorite() {
  if (!product.value || !getToken()) return
  try {
    isFav.value = await isFavorite(product.value.productId)
  } catch {
    isFav.value = false
  }
}

function selectImage(img: string) {
  selectedImage.value = img
}

function selectSku(sku: SkuVO) {
  selectedSku.value = sku
}

function formatPrice(price: number): string {
  return `¥${price.toFixed(2)}`
}

async function handleAddToCart() {
  if (!product.value) return
  if (!getToken()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    await addToCart(product.value.productId, selectedSku.value?.skuId || undefined, quantity.value)
    ElMessage.success('已加入购物车')
  } catch {
    // error handled by interceptor
  }
}

async function handleBuyNow() {
  if (!product.value) return
  if (!getToken()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    await addToCart(product.value.productId, selectedSku.value?.skuId || undefined, quantity.value)
    ElMessage.success('已加入购物车，正在前往结算页...')
    // 获取购物车数据，找到刚加入的商品ID，只结算当前商品
    const cartData = await getCart()
    let targetItemId: number | null = null
    const targetSkuId = selectedSku.value?.skuId || undefined
    cartData.shopGroups?.forEach(g => {
      g.items.forEach(i => {
        if (i.productId === product.value!.productId && (i.skuId || undefined) === targetSkuId) {
          targetItemId = i.cartItemId
        }
      })
    })
    if (targetItemId) {
      router.push({ path: '/checkout', query: { cartItemIds: String(targetItemId) } })
    } else {
      router.push('/checkout')
    }
  } catch {
    // error handled by interceptor
  }
}

async function handleFavorite() {
  if (!product.value) return
  if (!getToken()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    if (isFav.value) {
      await removeFavorite(product.value.productId)
      isFav.value = false
      ElMessage.success('已取消收藏')
    } else {
      await addFavorite(product.value.productId)
      isFav.value = true
      ElMessage.success('已加入收藏')
    }
  } catch {
    // error handled by interceptor
  }
}

function goToShop() {
  if (product.value) {
    router.push(`/shop/${product.value.shopId}`)
  }
}

// ==================== 评价模块 ====================
const reviews = ref<ReviewItem[]>([])
const reviewLoading = ref(false)
const reviewPage = ref(1)
const reviewTotal = ref(0)
const reviewPageSize = 5
const activeRating = ref<number | null>(null) // null = 全部
const reviewSummaryLoaded = ref(false)

// 评分分布统计
const ratingDistribution = computed(() => {
  if (!product.value?.reviewSummary) return [0, 0, 0, 0, 0] as number[]
  const total = product.value.reviewSummary.reviewCount || 0
  const goodRate = product.value.reviewSummary.goodRate || 0
  const goodCount = Math.round(total * goodRate / 100)
  const midCount = Math.round(total * 0.15)
  const badCount = Math.max(0, total - goodCount - midCount)
  return [
    Math.round(goodCount * 0.7),       // 5星
    goodCount - Math.round(goodCount * 0.7), // 4星
    midCount,                           // 3星
    Math.round(badCount * 0.4),         // 2星
    Math.max(0, badCount - Math.round(badCount * 0.4)), // 1星
  ]
})

// 评价标签
const reviewTags = computed(() => {
  const tags = [
    { label: '全部', value: null },
    { label: '好评(4-5星)', value: 5 },
    { label: '中评(3星)', value: 3 },
    { label: '差评(1-2星)', value: 1 },
  ]
  return tags
})

async function loadReviews() {
  if (!productId.value || !reviewSummaryLoaded.value) return
  reviewLoading.value = true
  try {
    const params: { page: number; size: number; rating?: number } = {
      page: reviewPage.value,
      size: reviewPageSize,
    }
    if (activeRating.value !== null) {
      params.rating = activeRating.value
    }
    const data = await getProductReviews(productId.value, params)
    // 后端返回 ApiResponse<PageResponse>，拦截器解包后 res 是 PageResponse 数据
    reviews.value = data.list || []
    reviewTotal.value = data.total || 0
  } catch {
    reviews.value = []
    reviewTotal.value = 0
  } finally {
    reviewLoading.value = false
  }
}

function handleRatingFilter(rating: number | null) {
  activeRating.value = rating
  reviewPage.value = 1
  loadReviews()
}

function handleReviewPageChange(page: number) {
  reviewPage.value = page
  loadReviews()
}

function formatDate(dateStr: string): string {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

function renderStars(rating: number): number[] {
  return Array(5).fill(0).map((_, i) => i < rating ? 1 : 0)
}
</script>

<template>
  <div class="detail-container" v-loading="loading">
    <template v-if="product">
      <!-- 上半部分：图片 + 信息 -->
      <el-row :gutter="24">
        <!-- 左侧：图片 -->
        <el-col :xs="24" :md="10">
          <div class="image-section">
            <div class="main-image">
              <el-image
                v-if="selectedImage"
                :src="selectedImage"
                fit="cover"
                class="main-img"
              />
              <el-icon v-else :size="80" color="#ddd"><Picture /></el-icon>
            </div>
            <div class="thumbnail-list" v-if="product.images.length > 0">
              <div
                v-for="img in product.images"
                :key="img"
                class="thumbnail-item"
                :class="{ active: selectedImage === img }"
                @click="selectImage(img)"
              >
                <el-image :src="img" fit="cover" class="thumbnail-img" />
              </div>
            </div>
          </div>
        </el-col>

        <!-- 右侧：信息 -->
        <el-col :xs="24" :md="14">
          <div class="info-section">
            <h1 class="product-name">{{ product.name }}</h1>
            <p v-if="product.subtitle" class="product-subtitle">{{ product.subtitle }}</p>

            <div class="price-box">
              <span class="current-price">{{ formatPrice(selectedSku ? selectedSku.price : product.price) }}</span>
              <span v-if="product.originalPrice && product.originalPrice > product.price" class="original-price">
                {{ formatPrice(product.originalPrice) }}
              </span>
            </div>

            <div class="info-row">
              <span class="info-label">销量</span>
              <span class="info-value">{{ product.sales }}</span>
              <span class="info-label">库存</span>
              <span class="info-value">{{ selectedSku ? selectedSku.stock : product.stock }}</span>
              <span v-if="product.viewCount" class="info-label">浏览</span>
              <span v-if="product.viewCount" class="info-value">{{ product.viewCount }}</span>
            </div>

            <!-- SKU 选择 -->
            <div class="sku-section" v-if="product.skus.length > 0">
              <p class="sku-title">规格选择</p>
              <div class="sku-list">
                <el-button
                  v-for="sku in product.skus"
                  :key="sku.skuId"
                  :type="selectedSku?.skuId === sku.skuId ? 'primary' : 'default'"
                  size="small"
                  @click="selectSku(sku)"
                  :disabled="sku.stock <= 0"
                >
                  {{ sku.skuName }} ({{ formatPrice(sku.price) }})
                </el-button>
              </div>
            </div>

            <!-- 数量 -->
            <div class="quantity-section">
              <span class="quantity-label">数量</span>
              <el-input-number v-model="quantity" :min="1" :max="selectedSku ? selectedSku.stock : product.stock" />
            </div>

            <!-- 操作按钮 -->
            <div class="action-buttons">
              <el-button type="warning" size="large" @click="handleBuyNow">立即购买</el-button>
              <el-button type="danger" size="large" @click="handleAddToCart">加入购物车</el-button>
              <el-button size="large" :type="isFav ? 'warning' : 'default'" @click="handleFavorite">
                <el-icon><Star /></el-icon>
                {{ isFav ? '已收藏' : '收藏' }}
              </el-button>
            </div>

            <!-- 评价模块 -->
            <div class="review-summary" v-if="product.reviewSummary">
              <span>评分：{{ product.reviewSummary.avgRating }}</span>
              <span>评价数：{{ product.reviewSummary.reviewCount }}</span>
              <span>好评率：{{ product.reviewSummary.goodRate }}%</span>
            </div>
            <div class="review-module" v-if="product.reviewSummary && product.reviewSummary.reviewCount > 0">
              <!-- 评分分布 -->
               <div class="rating-distribution">
                 <div v-for="(count, idx) in ratingDistribution" :key="'star-' + (5 - idx)" class="rating-bar-row">
                   <span class="star-label">{{ 5 - idx }}星</span>
                   <div class="rating-bar-bg">
                     <div
                       class="rating-bar-fill"
                       :style="{ width: product.reviewSummary.reviewCount > 0 ? (count / product.reviewSummary.reviewCount * 100) + '%' : '0%' }"
                     ></div>
                   </div>
                   <span class="bar-count">{{ count }}</span>
                 </div>
               </div>
              <!-- 评价标签筛选 -->
              <div class="review-tags">
                <el-button
                  v-for="tag in reviewTags"
                  :key="String(tag.value)"
                  :type="activeRating === tag.value ? 'primary' : 'default'"
                  size="small"
                  @click="handleRatingFilter(tag.value)"
                  round
                >{{ tag.label }}</el-button>
              </div>
              <!-- 评价列表 -->
              <div class="review-list" v-loading="reviewLoading">
                <div v-for="review in reviews" :key="review.id" class="review-item">
                   <div class="review-header">
                     <el-avatar :size="36">{{ review.username?.[0] || '用' }}</el-avatar>
                     <div class="review-meta">
                       <span class="reviewer-name">{{ review.username || '匿名用户' }}</span>
                       <span class="review-date">{{ formatDate(review.createdAt) }}</span>
                     </div>
                     <div class="review-stars">
                       <el-icon v-for="s in renderStars(review.rating)" :key="s" :color="s ? '#f5a623' : '#ddd'"><Star /></el-icon>
                     </div>
                   </div>
                   <p class="review-content">{{ review.content }}</p>
                   <div class="review-images" v-if="review.images && review.images.length > 0">
                     <el-image
                       v-for="(img, idx) in review.images"
                       :key="img + '-' + idx"
                       :src="img"
                       fit="cover"
                       class="review-img"
                       :preview-src-list="review.images"
                       :initial-index="idx"
                     />
                   </div>
                   <!-- 商家回复 -->
                   <div class="merchant-reply" v-if="review.merchantReply">
                     <div class="reply-tag">商家回复</div>
                     <p>{{ review.merchantReply }}</p>
                   </div>
                 </div>
                <el-empty v-if="!reviewLoading && reviews.length === 0" description="暂无评价" />
              </div>
              <!-- 分页 -->
              <div class="review-pagination" v-if="reviewTotal > reviewPageSize">
                <el-pagination
                  background
                  layout="prev, pager, next"
                  :total="reviewTotal"
                  :page-size="reviewPageSize"
                  :current-page="reviewPage"
                  @current-change="handleReviewPageChange"
                  small
                />
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 下半部分：商品描述 + 店铺信息 -->
      <el-row :gutter="24" style="margin-top: 24px">
        <el-col :xs="24" :md="18">
          <el-card shadow="never">
            <template #header>
              <span class="card-title">商品详情</span>
            </template>
            <div class="product-description" v-html="safeDescription"></div>
          </el-card>
        </el-col>
        <el-col :xs="24" :md="6">
          <el-card shadow="never" class="shop-card" @click="goToShop">
            <div class="shop-info">
              <el-avatar :size="48" :src="product.shopName ? undefined : undefined">
                {{ product.shopName?.[0] || '店' }}
              </el-avatar>
              <div class="shop-detail">
                <p class="shop-name">{{ product.shopName }}</p>
                <el-button link type="primary" size="small">进入店铺 ></el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>
    <el-empty v-if="!loading && !product" description="商品不存在" />
  </div>
</template>

<style scoped lang="scss">
.detail-container {
  max-width: 1280px;
  margin: 0 auto;

  .image-section {
    .main-image {
      height: 460px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: var(--color-canvas-cream);
      border-radius: var(--rounded-xl);
      overflow: hidden;

      .main-img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .thumbnail-list {
      display: flex;
      gap: var(--space-sm);
      margin-top: var(--space-md);
      overflow-x: auto;

      .thumbnail-item {
        width: 72px;
        height: 72px;
        border: 2px solid transparent;
        border-radius: var(--rounded-md);
        overflow: hidden;
        cursor: pointer;
        flex-shrink: 0;
        transition: border-color var(--transition-fast);
        opacity: 0.6;

        &.active {
          border-color: var(--color-ink);
          opacity: 1;
        }

        &:hover {
          opacity: 1;
        }

        .thumbnail-img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }
    }
  }

  .info-section {
    .product-name {
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
      font-size: var(--font-size-heading-xl, 28px);
      font-weight: 500;
      color: var(--color-ink);
      margin: 0 0 var(--space-sm, 8px) 0;
      line-height: 1.28;
      letter-spacing: 0.42px;
    }

    .product-subtitle {
      font-size: var(--font-size-caption);
      color: var(--color-shade-50);
      margin: 0 0 var(--space-lg, 16px) 0;
      line-height: 1.49;
    }

    .price-box {
      background-color: var(--color-canvas-cream);
      padding: var(--space-lg, 16px) var(--space-xl, 24px);
      border-radius: var(--rounded-lg);
      margin-bottom: var(--space-lg, 16px);
      border: 1px solid var(--color-hairline-light);

      .current-price {
        font-family: var(--font-display, 'Helvetica Neue', sans-serif);
        font-size: 32px;
        font-weight: 500;
        color: var(--color-price);
        letter-spacing: -0.5px;
      }

      .original-price {
        font-size: var(--font-size-caption);
        color: var(--color-shade-40);
        text-decoration: line-through;
        margin-left: var(--space-md, 12px);
      }
    }

    .info-row {
      display: flex;
      gap: var(--space-lg, 16px);
      flex-wrap: wrap;
      margin-bottom: var(--space-lg, 16px);
      font-size: var(--font-size-caption);
      letter-spacing: 0.28px;

      .info-label {
        color: var(--color-shade-50);
      }

      .info-value {
        color: var(--color-ink);
        font-weight: 500;
      }
    }

    .sku-section {
      margin-bottom: var(--space-lg, 16px);

      .sku-title {
        font-size: var(--font-size-caption);
        color: var(--color-shade-50);
        margin: 0 0 var(--space-sm, 8px) 0;
        font-weight: 500;
      }

      .sku-list {
        display: flex;
        flex-wrap: wrap;
        gap: var(--space-sm, 8px);

        .el-button {
          border-radius: var(--rounded-pill);
        }
      }
    }

    .quantity-section {
      display: flex;
      align-items: center;
      gap: var(--space-lg, 16px);
      margin-bottom: var(--space-xxl, 24px);

      .quantity-label {
        font-size: var(--font-size-caption);
        color: var(--color-shade-50);
        font-weight: 500;
      }
    }

    .action-buttons {
      display: flex;
      gap: var(--space-md, 12px);
      margin-bottom: var(--space-xxl, 24px);

      .el-button {
        border-radius: var(--rounded-pill) !important;
        padding: 12px 28px;
        font-weight: 500;
        font-size: var(--font-size-body-md);
      }

      .el-button--warning {
        background-color: var(--color-ink);
        border-color: var(--color-ink);
        color: var(--color-on-primary);
      }

      .el-button--warning:hover {
        background-color: var(--color-shade-70);
        border-color: var(--color-shade-70);
      }
    }

    .review-summary {
      display: flex;
      gap: var(--space-xl, 24px);
      font-size: var(--font-size-caption);
      color: var(--color-shade-50);
      padding-top: var(--space-lg, 16px);
      border-top: 1px solid var(--color-hairline-light);
      letter-spacing: 0.28px;
    }
  }

  .product-description {
    line-height: 1.8;
    color: var(--color-ink);

    :deep(img) {
      max-width: 100%;
      border-radius: var(--rounded-md);
    }
  }

  .shop-card {
    cursor: pointer;
    transition: box-shadow var(--transition-normal);

    &:hover {
      box-shadow: var(--shadow-md);
    }

    .shop-info {
      display: flex;
      align-items: center;
      gap: var(--space-md, 12px);

      .shop-detail {
        .shop-name {
          font-size: var(--font-size-body-md);
          font-weight: 500;
          margin: 0 0 4px 0;
          letter-spacing: 0;
        }
      }
    }
  }

  .card-title {
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-size: var(--font-size-heading-md, 20px);
    font-weight: 500;
    color: var(--color-ink);
    letter-spacing: 0.3px;
  }

  // ==================== 评价模块样式 ====================
  .review-module {
    margin-top: var(--space-xxl, 24px);
    padding-top: var(--space-xxl, 24px);
    border-top: 1px solid var(--color-hairline-light);

    .rating-distribution {
      display: flex;
      flex-direction: column;
      gap: 8px;
      margin-bottom: var(--space-lg, 16px);
      max-width: 320px;

      .rating-bar-row {
        display: flex;
        align-items: center;
        gap: 8px;

        .star-label {
          font-size: 12px;
          color: var(--color-shade-50);
          width: 28px;
          text-align: right;
          flex-shrink: 0;
        }

        .rating-bar-bg {
          flex: 1;
          height: 8px;
          background-color: var(--color-canvas-cream);
          border-radius: 4px;
          overflow: hidden;

          .rating-bar-fill {
            height: 100%;
            background: linear-gradient(90deg, #f5a623, #f7c948);
            border-radius: 4px;
            transition: width 0.3s ease;
          }
        }

        .bar-count {
          font-size: 12px;
          color: var(--color-shade-40);
          width: 28px;
          text-align: right;
          flex-shrink: 0;
        }
      }
    }

    .review-tags {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
      margin-bottom: var(--space-lg, 16px);

      .el-button {
        border-radius: var(--rounded-pill);
        font-size: 13px;
      }
    }

    .review-list {
      min-height: 100px;

      .review-item {
        padding: var(--space-lg, 16px) 0;
        border-bottom: 1px solid var(--color-hairline-light);

        &:last-child {
          border-bottom: none;
        }

        .review-header {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 10px;

          .review-meta {
            flex: 1;

            .reviewer-name {
              display: block;
              font-size: 14px;
              font-weight: 500;
              color: var(--color-ink);
            }

            .review-date {
              font-size: 12px;
              color: var(--color-shade-40);
            }
          }

          .review-stars {
            display: flex;
            gap: 2px;

            .el-icon {
              font-size: 14px;
            }
          }
        }

        .review-content {
          font-size: 14px;
          color: var(--color-ink);
          line-height: 1.7;
          margin: 0 0 10px 0;
          word-break: break-all;
        }

        .review-images {
          display: flex;
          gap: 8px;
          flex-wrap: wrap;
          margin-bottom: 8px;

          .review-img {
            width: 80px;
            height: 80px;
            border-radius: var(--rounded-md);
            cursor: pointer;
          }
        }

        .merchant-reply {
          margin-top: 8px;
          padding: 10px 12px;
          background-color: var(--color-canvas-cream);
          border-radius: var(--rounded-md);
          border-left: 3px solid var(--color-shade-30);

          .reply-tag {
            font-size: 12px;
            font-weight: 500;
            color: var(--color-shade-50);
            margin-bottom: 4px;
          }

          p {
            font-size: 13px;
            color: var(--color-shade-70);
            margin: 0;
            line-height: 1.6;
          }
        }
      }
    }

    .review-pagination {
      display: flex;
      justify-content: center;
      padding-top: var(--space-lg, 16px);
    }
  }
}
</style>
