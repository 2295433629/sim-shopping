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
  return [...productList.value].sort((a, b) => (a.sort ?? 0) - (b.sort ?? 0))
})

onMounted(() => {
  loadProducts()
  loadUserPoints()
})

async function loadProducts() {
  loading.value = true
  try {
    productList.value = await getPointsProducts()
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
    userPoints.value = data.balance ?? 0
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
      `确定使用 ${product.pointsRequired} 积分兑换 "${product.name}" 吗？`,
      '确认兑换',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    exchangingId.value = product.productId
    await exchangeProduct(product.productId)
    ElMessage.success('兑换成功')
    loadProducts()
    loadUserPoints()
  } catch (e: any) {
    if (e !== 'cancel') {
      ElMessage.error('兑换失败')
    }
  } finally {
    exchangingId.value = null
  }
}

function getDefaultImage() {
  return 'https://via.placeholder.com/240x180?text=No+Image'
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
              <el-icon color="#ff9900"><Coin /></el-icon>
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
            :key="product.productId"
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
                <div class="product-name" :title="product.name">{{ product.name }}</div>
                <div v-if="product.description" class="product-desc">{{ product.description }}</div>
                <div class="product-points">
                  <el-icon color="#ff9900"><Coin /></el-icon>
                  <span class="points-required">{{ product.pointsRequired }}</span>
                  <span class="points-text">积分</span>
                </div>
                <div class="product-stock">库存：{{ product.stock }}</div>
              </div>
              <div class="product-footer">
                <el-button
                  type="primary"
                  size="small"
                  :loading="exchangingId === product.productId"
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
    font-size: 16px;
    font-weight: bold;
  }

  .points-display {
    display: flex;
    align-items: center;
    gap: 6px;
    background: #fff7e6;
    border: 1px solid #ffd591;
    border-radius: 20px;
    padding: 4px 16px;

    .points-value {
      font-size: 18px;
      font-weight: bold;
      color: #ff9900;
    }

    .points-label {
      font-size: 12px;
      color: #666;
    }
  }

  .product-card {
    border: 1px solid #ebeef5;
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 16px;
    background: #fff;
    transition: box-shadow 0.3s;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
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
        background: #f5f7fa;
        color: #909399;
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
        color: #fff;
        font-size: 18px;
        font-weight: bold;
      }
    }

    .product-body {
      padding: 12px 16px;

      .product-name {
        font-size: 14px;
        font-weight: 600;
        color: #333;
        margin-bottom: 6px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .product-desc {
        font-size: 12px;
        color: #666;
        margin-bottom: 8px;
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
        gap: 4px;
        margin-bottom: 4px;

        .points-required {
          font-size: 18px;
          font-weight: bold;
          color: #ff9900;
        }

        .points-text {
          font-size: 12px;
          color: #999;
        }
      }

      .product-stock {
        font-size: 12px;
        color: #999;
      }
    }

    .product-footer {
      padding: 10px 16px;
      border-top: 1px dashed #ebeef5;
      text-align: center;
    }
  }
}
</style>
