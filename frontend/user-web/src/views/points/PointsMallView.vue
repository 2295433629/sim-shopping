<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import {
  getPointsProducts,
  getPointsBalance,
  exchangeProduct,
  type PointsProduct,
} from '@/api/modules/points'

const userStore = useUserStore()
const loading = ref(false)
const exchangingId = ref<number | null>(null)
const productList = ref<PointsProduct[]>([])
const userPoints = ref(0)

const sortedProducts = computed(() => {
  return productList.value
})

onMounted(() => {
  loadProducts()
  loadUserPoints()
})

async function loadProducts() {
  loading.value = true
  try {
    const result = await getPointsProducts()
    productList.value = result.list || []
  } catch {
    productList.value = []
    ElMessage.error('加载商品列表失败')
  } finally {
    loading.value = false
  }
}

async function loadUserPoints() {
  if (!userStore.isLoggedIn) return
  try {
    const data = await getPointsBalance()
    userPoints.value = data.currentPoints ?? 0
  } catch {
    userPoints.value = 0
  }
}

function canExchange(product: PointsProduct): boolean {
  if (!userStore.isLoggedIn) return false
  if (product.stock <= 0) return false
  if (userPoints.value < product.pointsRequired) return false
  if (product.status !== 'ACTIVE') return false
  return true
}

async function handleExchange(product: PointsProduct) {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    await ElMessageBox.confirm(
      `确定使用 ${product.pointsRequired} 积分兑换 "${product.productName}" 吗？`,
      '确认兑换',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    exchangingId.value = product.id
    await exchangeProduct(product.id)
    ElMessage.success('兑换成功')
    loadProducts()
    loadUserPoints()
  } catch (e: unknown) {
    if (e !== 'cancel') {
      ElMessage.error('兑换失败')
    }
  } finally {
    exchangingId.value = null
  }
}

function getDefaultImage() {
  return 'https://via.placeholder.com/240x180?text=暂无图片'
}
</script>

<template>
  <div class="points-mall-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">积分商城</span>
          <div class="points-display">
            <template v-if="userStore.isLoggedIn">
              <el-icon color="var(--color-warning)"><Coin /></el-icon>
              <span class="points-value">{{ userPoints }}</span>
              <span class="points-label">当前积分</span>
            </template>
            <el-button v-else type="primary" size="small" @click="$router.push('/login')">
              登录查看积分
            </el-button>
          </div>
        </div>
      </template>

      <div v-loading="loading">
        <el-row :gutter="16">
          <el-col
            v-for="product in sortedProducts"
            :key="product.id"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
          >
            <div class="product-card">
              <div class="product-image">
                <el-image
                  :src="product.imageUrl || getDefaultImage()"
                  fit="cover"
                  style="width: 100%; height: 180px"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon :size="40"><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div v-if="product.stock <= 0" class="stock-overlay">
                  <span>已售罄</span>
                </div>
                <div v-else-if="product.status !== 'ACTIVE'" class="stock-overlay">
                  <span>已下架</span>
                </div>
              </div>
              <div class="product-body">
                <div class="product-name" :title="product.productName">{{ product.productName }}</div>
                <div v-if="product.description" class="product-desc">{{ product.description }}</div>
                <div class="product-points">
                  <el-icon color="var(--color-warning)"><Coin /></el-icon>
                  <span class="points-required">{{ product.pointsRequired }}</span>
                  <span class="points-text">积分</span>
                </div>
                <div class="product-stock">库存：{{ product.stock }}</div>
              </div>
              <div class="product-footer">
                <el-button
                  type="primary"
                  size="small"
                  :loading="exchangingId === product.id"
                  :disabled="!canExchange(product)"
                  @click="handleExchange(product)"
                >
                  <template v-if="!userStore.isLoggedIn">请先登录</template>
                  <template v-else-if="product.stock <= 0">已售罄</template>
                  <template v-else-if="product.status !== 'ACTIVE'">已下架</template>
                  <template v-else-if="userPoints < product.pointsRequired">积分不足</template>
                  <template v-else>立即兑换</template>
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-empty v-if="!loading && productList.length === 0" description="暂无积分商品" />
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.points-mall-container {
  max-width: 1200px;
  margin: 0 auto;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .points-display {
    display: flex;
    align-items: center;
    gap: var(--space-xs);
    background: var(--color-canvas-cream);
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-xl);
    padding: var(--space-xs) var(--space-lg);

    .points-value {
      font-size: var(--font-size-body-lg);
      font-weight: 500;
      color: var(--color-warning);
    }

    .points-label {
      font-size: var(--font-size-eyebrow);
      color: var(--color-shade-50);
    }
  }

  .product-card {
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    overflow: hidden;
    margin-bottom: var(--space-lg);
    background: var(--color-canvas-light);
    transition: box-shadow var(--transition-normal);

    &:hover {
      box-shadow: var(--shadow-md);
    }

    .product-image {
      position: relative;
      width: 100%;
      height: 180px;

      .image-placeholder {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        background: var(--color-canvas-cream);
        color: var(--color-shade-40);
      }

      .stock-overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        color: var(--color-on-primary);
        font-size: var(--font-size-body-lg);
        font-weight: 500;
      }
    }

    .product-body {
      padding: var(--space-md) var(--space-lg);

      .product-name {
        font-size: var(--font-size-caption);
        font-weight: 500;
        color: var(--color-ink);
        margin-bottom: 6px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .product-desc {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-50);
        margin-bottom: var(--space-sm);
        line-height: 1.4;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }

      .product-points {
        display: flex;
        align-items: center;
        gap: var(--space-xs);
        margin-bottom: var(--space-xs);

        .points-required {
          font-size: var(--font-size-body-lg);
          font-weight: 500;
          color: var(--color-warning);
        }

        .points-text {
          font-size: var(--font-size-eyebrow);
          color: var(--color-shade-40);
        }
      }

      .product-stock {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-40);
      }
    }

    .product-footer {
      padding: 10px var(--space-lg);
      border-top: 1px dashed var(--color-hairline-light);
      text-align: center;
    }
  }
}
</style>
