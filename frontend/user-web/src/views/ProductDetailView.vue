<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import DOMPurify from 'dompurify'
import { getProductDetail, addFavorite } from '@/api/modules/product'
import { addToCart } from '@/api/modules/cart'
import { getToken } from '@/utils/storage'
import type { ProductDetailVO, SkuVO } from '@/types/product'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const product = ref<ProductDetailVO | null>(null)
const selectedImage = ref('')
const selectedSku = ref<SkuVO | null>(null)
const quantity = ref(1)

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
  try {
    product.value = await getProductDetail(productId.value)
    if (product.value) {
      selectedImage.value = product.value.mainImage || product.value.images[0] || ''
      if (product.value.skus.length > 0) {
        selectedSku.value = product.value.skus[0]
      }
    }
  } finally {
    loading.value = false
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
    await addToCart(product.value.productId, selectedSku.value?.skuId || 0, quantity.value)
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
    await addToCart(product.value.productId, selectedSku.value?.skuId || 0, quantity.value)
    ElMessage.success('已加入购物车，正在前往结算页...')
    router.push('/checkout')
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
    await addFavorite(product.value.productId)
    ElMessage.success('已加入收藏')
  } catch {
    // error handled by interceptor
  }
}

function goToShop() {
  if (product.value) {
    router.push(`/shop/${product.value.shopId}`)
  }
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
              <el-button size="large" @click="handleFavorite">
                <el-icon><Star /></el-icon>
                收藏
              </el-button>
            </div>

            <!-- 评价摘要 -->
            <div class="review-summary" v-if="product.reviewSummary">
              <span>评分：{{ product.reviewSummary.avgRating }}</span>
              <span>评价数：{{ product.reviewSummary.reviewCount }}</span>
              <span>好评率：{{ product.reviewSummary.goodRate }}%</span>
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
  max-width: 1200px;
  margin: 0 auto;

  .image-section {
    .main-image {
      height: 400px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f5f5f5;
      border-radius: 4px;
      overflow: hidden;

      .main-img {
        width: 100%;
        height: 100%;
      }
    }

    .thumbnail-list {
      display: flex;
      gap: 8px;
      margin-top: 12px;
      overflow-x: auto;

      .thumbnail-item {
        width: 70px;
        height: 70px;
        border: 2px solid transparent;
        border-radius: 4px;
        overflow: hidden;
        cursor: pointer;
        flex-shrink: 0;

        &.active {
          border-color: #409eff;
        }

        .thumbnail-img {
          width: 100%;
          height: 100%;
        }
      }
    }
  }

  .info-section {
    .product-name {
      font-size: 20px;
      font-weight: bold;
      color: #333;
      margin: 0 0 8px 0;
    }

    .product-subtitle {
      font-size: 14px;
      color: #999;
      margin: 0 0 16px 0;
    }

    .price-box {
      background-color: #fff8f0;
      padding: 16px;
      border-radius: 4px;
      margin-bottom: 16px;

      .current-price {
        font-size: 28px;
        font-weight: bold;
        color: #f56c6c;
      }

      .original-price {
        font-size: 14px;
        color: #ccc;
        text-decoration: line-through;
        margin-left: 12px;
      }
    }

    .info-row {
      display: flex;
      gap: 16px;
      flex-wrap: wrap;
      margin-bottom: 16px;
      font-size: 14px;

      .info-label {
        color: #999;
      }

      .info-value {
        color: #333;
        font-weight: 500;
      }
    }

    .sku-section {
      margin-bottom: 16px;

      .sku-title {
        font-size: 14px;
        color: #666;
        margin: 0 0 8px 0;
      }

      .sku-list {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
      }
    }

    .quantity-section {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 24px;

      .quantity-label {
        font-size: 14px;
        color: #666;
      }
    }

    .action-buttons {
      display: flex;
      gap: 12px;
      margin-bottom: 24px;
    }

    .review-summary {
      display: flex;
      gap: 24px;
      font-size: 14px;
      color: #666;
      padding-top: 16px;
      border-top: 1px solid #eee;
    }
  }

  .product-description {
    line-height: 1.8;
    color: #333;

    :deep(img) {
      max-width: 100%;
    }
  }

  .shop-card {
    cursor: pointer;

    .shop-info {
      display: flex;
      align-items: center;
      gap: 12px;

      .shop-detail {
        .shop-name {
          font-size: 16px;
          font-weight: 500;
          margin: 0 0 4px 0;
        }
      }
    }
  }

  .card-title {
    font-size: 16px;
    font-weight: bold;
  }
}
</style>
