<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getActivityDetail, type ActivityDetail } from '@/api/modules/activity'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const detail = ref<ActivityDetail | null>(null)

const activityId = ref<number>(0)

onMounted(() => {
  activityId.value = Number(route.params.id)
  loadDetail()
})

async function loadDetail() {
  if (!activityId.value) return
  loading.value = true
  try {
    detail.value = await getActivityDetail(activityId.value)
  } catch {
    detail.value = null
    ElMessage.error('加载专题活动详情失败')
  } finally {
    loading.value = false
  }
}

function formatPrice(price: number): string {
  return `¥${price.toFixed(2)}`
}

function formatTimeRange(startTime: string, endTime: string): string {
  const start = new Date(startTime)
  const end = new Date(endTime)
  const format = (d: Date) => `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
  return `${format(start)} 至 ${format(end)}`
}

function goToProduct(productId: number) {
  router.push(`/products/${productId}`)
}

function goBack() {
  router.back()
}
</script>

<template>
  <div class="activity-detail-container" v-loading="loading">
    <template v-if="detail">
      <!-- 顶部横幅 -->
      <div class="activity-banner">
        <el-image :src="detail.bannerImage" fit="cover" class="banner-bg">
          <template #error>
            <div class="banner-placeholder">
              <el-icon :size="60" color="#ccc"><Picture /></el-icon>
            </div>
          </template>
        </el-image>
        <div class="banner-overlay">
          <div class="banner-content">
            <h1 class="activity-title">{{ detail.activityName }}</h1>
            <p v-if="detail.description" class="activity-desc">{{ detail.description }}</p>
            <div class="activity-time">
              <el-icon><Clock /></el-icon>
              <span>{{ formatTimeRange(detail.startTime, detail.endTime) }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 关联商品 -->
      <el-card shadow="never" class="products-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">
              <el-icon><Goods /></el-icon>
              活动商品
            </span>
            <span class="card-subtitle">共 {{ detail.productCount }} 件商品</span>
          </div>
        </template>

        <el-row :gutter="16" v-if="detail.products && detail.products.length > 0">
          <el-col v-for="product in detail.products" :key="product.productId" :xs="12" :sm="8" :md="6" :lg="4">
            <div class="product-card" @click="goToProduct(product.productId)">
              <div class="product-image">
                <el-image :src="product.productImage" fit="cover" class="product-img">
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon :size="32" color="#ccc"><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
              <div class="product-body">
                <div class="product-name" :title="product.productName">{{ product.productName }}</div>
                <div class="product-price">{{ formatPrice(product.price) }}</div>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-empty v-else description="暂无关联商品" />
      </el-card>

      <div class="back-action">
        <el-button @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回活动列表
        </el-button>
      </div>
    </template>

    <el-empty v-if="!loading && !detail" description="专题活动不存在或已结束" />
  </div>
</template>

<style scoped lang="scss">
.activity-detail-container {
  max-width: 1200px;
  margin: 0 auto;

  .activity-banner {
    position: relative;
    height: 280px;
    border-radius: var(--rounded-lg);
    overflow: hidden;
    margin-bottom: var(--space-xl);
    background-color: var(--color-canvas-cream);

    .banner-bg {
      width: 100%;
      height: 100%;
    }

    .banner-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: var(--color-hairline-light);
    }

    .banner-overlay {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
      padding: var(--space-xl) var(--space-lg) var(--space-lg);

      .banner-content {
        color: #fff;

        .activity-title {
          font-size: var(--font-size-heading-xl);
          font-weight: 500;
          margin: 0 0 var(--space-sm) 0;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
          font-family: var(--font-display, 'Helvetica Neue', sans-serif);
        }

        .activity-desc {
          font-size: var(--font-size-caption);
          margin: 0 0 var(--space-md) 0;
          opacity: 0.9;
        }

        .activity-time {
          display: inline-flex;
          align-items: center;
          gap: 6px;
          font-size: var(--font-size-micro);
          background: rgba(255, 255, 255, 0.2);
          padding: var(--space-xs) var(--space-md);
          border-radius: var(--rounded-md);
          backdrop-filter: blur(4px);
        }
      }
    }
  }

  .products-card {
    margin-bottom: var(--space-xl);

    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .card-title {
        font-size: var(--font-size-body-md);
        font-weight: 500;
        font-family: var(--font-display, 'Helvetica Neue', sans-serif);
        color: var(--color-ink);
        display: flex;
        align-items: center;
        gap: var(--space-sm);
      }

      .card-subtitle {
        font-size: var(--font-size-micro);
        color: var(--color-shade-40);
      }
    }
  }

  .product-card {
    background: var(--color-canvas-light);
    border-radius: var(--rounded-lg);
    overflow: hidden;
    border: 1px solid var(--color-hairline-light);
    margin-bottom: var(--space-lg);
    cursor: pointer;
    transition: box-shadow var(--transition-normal), transform var(--transition-normal);

    &:hover {
      box-shadow: var(--shadow-md);
      transform: translateY(-2px);
    }

    .product-image {
      height: 160px;
      background-color: var(--color-canvas-cream);

      .product-img {
        width: 100%;
        height: 100%;
      }

      .image-placeholder {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }

    .product-body {
      padding: 10px;

      .product-name {
        font-size: var(--font-size-micro);
        color: var(--color-ink);
        margin-bottom: 6px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .product-price {
        font-size: var(--font-size-body-md);
        font-weight: 500;
        color: var(--color-price);
        margin-bottom: var(--space-xs);
      }

      .product-sales {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-40);
      }
    }
  }

  .back-action {
    display: flex;
    justify-content: center;
    margin-top: var(--space-xl);
  }
}
</style>
