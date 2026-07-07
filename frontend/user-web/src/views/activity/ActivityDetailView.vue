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
            <h1 class="activity-title">{{ detail.name }}</h1>
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
                <el-image :src="product.mainImage" fit="cover" class="product-img">
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon :size="32" color="#ccc"><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
              <div class="product-body">
                <div class="product-name" :title="product.name">{{ product.name }}</div>
                <div class="product-price">{{ formatPrice(product.price) }}</div>
                <div class="product-sales">已售 {{ product.sales }} 件</div>
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
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 20px;
    background-color: #f5f5f5;

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
      background-color: #f0f0f0;
    }

    .banner-overlay {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
      padding: 40px 24px 24px;

      .banner-content {
        color: #fff;

        .activity-title {
          font-size: 28px;
          font-weight: bold;
          margin: 0 0 8px 0;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }

        .activity-desc {
          font-size: 14px;
          margin: 0 0 12px 0;
          opacity: 0.9;
        }

        .activity-time {
          display: inline-flex;
          align-items: center;
          gap: 6px;
          font-size: 13px;
          background: rgba(255, 255, 255, 0.2);
          padding: 4px 12px;
          border-radius: 4px;
          backdrop-filter: blur(4px);
        }
      }
    }
  }

  .products-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .card-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .card-subtitle {
        font-size: 13px;
        color: #999;
      }
    }
  }

  .product-card {
    background: #fff;
    border-radius: 8px;
    overflow: hidden;
    border: 1px solid #ebeef5;
    margin-bottom: 16px;
    cursor: pointer;
    transition: box-shadow 0.3s, transform 0.3s;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transform: translateY(-2px);
    }

    .product-image {
      height: 160px;
      background-color: #f5f5f5;

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
        font-size: 13px;
        color: #333;
        margin-bottom: 6px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .product-price {
        font-size: 16px;
        font-weight: bold;
        color: #f56c6c;
        margin-bottom: 4px;
      }

      .product-sales {
        font-size: 12px;
        color: #999;
      }
    }
  }

  .back-action {
    display: flex;
    justify-content: center;
    margin-top: 24px;
  }
}
</style>
