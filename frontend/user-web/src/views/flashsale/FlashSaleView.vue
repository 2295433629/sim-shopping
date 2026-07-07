<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getActiveFlashSales, type FlashSale } from '@/api/modules/flashsale'

const router = useRouter()
const loading = ref(false)
const flashSaleList = ref<FlashSale[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const countdownMap = ref<Record<number, string>>({})
let timer: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  loadFlashSales()
  timer = setInterval(updateCountdowns, 1000)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
})

async function loadFlashSales() {
  loading.value = true
  try {
    const res = await getActiveFlashSales({ page: page.value, size: size.value })
    flashSaleList.value = res.list || []
    total.value = res.total || 0
    updateCountdowns()
  } catch {
    flashSaleList.value = []
    total.value = 0
    ElMessage.error('加载秒杀活动失败')
  } finally {
    loading.value = false
  }
}

function updateCountdowns() {
  const now = Date.now()
  const map: Record<number, string> = {}
  flashSaleList.value.forEach((item) => {
    const end = new Date(item.endTime).getTime()
    const diff = end - now
    if (diff <= 0) {
      map[item.saleId] = '已结束'
    } else {
      map[item.saleId] = formatCountdown(diff)
    }
  })
  countdownMap.value = map
}

function formatCountdown(ms: number): string {
  const seconds = Math.floor(ms / 1000)
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60
  const pad = (n: number) => String(n).padStart(2, '0')
  return `${pad(hours)}:${pad(minutes)}:${pad(secs)}`
}

function handlePageChange(p: number) {
  page.value = p
  loadFlashSales()
}

function goToDetail(saleId: number) {
  router.push(`/flash-sale/${saleId}`)
}

function formatPrice(price: number): string {
  return `¥${price.toFixed(2)}`
}

function soldPercent(item: FlashSale): number {
  const total = item.sold + item.stock
  if (total <= 0) return 0
  return Math.round((item.sold / total) * 100)
}
</script>

<template>
  <div class="flash-sale-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">
            <el-icon><AlarmClock /></el-icon>
            限时秒杀
          </span>
          <span class="card-subtitle">精选好物，限时抢购</span>
        </div>
      </template>

      <div v-loading="loading" class="flash-sale-list">
        <el-row :gutter="16">
          <el-col v-for="item in flashSaleList" :key="item.saleId" :xs="24" :sm="12" :md="8" :lg="6">
            <div class="flash-sale-card" @click="goToDetail(item.saleId)">
              <div class="sale-image">
                <el-image :src="item.productImage" fit="cover" class="product-img">
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon :size="40" color="#ccc"><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="sale-badge">秒杀</div>
              </div>
              <div class="sale-body">
                <div class="product-name">{{ item.productName }}</div>
                <div class="price-row">
                  <span class="flash-price">{{ formatPrice(item.flashPrice) }}</span>
                  <span class="original-price">{{ formatPrice(item.originalPrice) }}</span>
                </div>
                <div class="sale-info">
                  <span class="limit-tag">限购 {{ item.limitPerUser }} 件</span>
                </div>
                <div class="stock-info">
                  <el-progress :percentage="soldPercent(item)" :show-text="false" :stroke-width="8" color="#f56c6c" />
                  <span class="stock-text">已抢 {{ item.sold }} 件 / 剩 {{ item.stock }} 件</span>
                </div>
                <div class="countdown-row">
                  <el-icon><Timer /></el-icon>
                  <span class="countdown-text" :class="{ expired: countdownMap[item.saleId] === '已结束' }">
                    {{ countdownMap[item.saleId] || '--:--:--' }}
                  </span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-empty v-if="!loading && flashSaleList.length === 0" description="暂无进行中的秒杀活动" />
      </div>

      <div class="pagination-wrapper" v-if="total > 0">
        <el-pagination
          v-model:current-page="page"
          :page-size="size"
          :total="total"
          layout="prev, pager, next, total"
          background
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.flash-sale-container {
  max-width: 1200px;
  margin: 0 auto;

  .card-header {
    display: flex;
    align-items: center;
    gap: 12px;

    .card-title {
      font-size: 18px;
      font-weight: bold;
      color: #f56c6c;
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .card-subtitle {
      font-size: 13px;
      color: #999;
    }
  }

  .flash-sale-list {
    min-height: 400px;
  }

  .flash-sale-card {
    background: #fff;
    border-radius: 8px;
    overflow: hidden;
    border: 1px solid #ebeef5;
    margin-bottom: 16px;
    cursor: pointer;
    transition: box-shadow 0.3s, transform 0.3s;

    &:hover {
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
      transform: translateY(-2px);
    }

    .sale-image {
      position: relative;
      height: 200px;
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

      .sale-badge {
        position: absolute;
        top: 8px;
        left: 8px;
        background: linear-gradient(135deg, #ff6b6b, #ee5a5a);
        color: #fff;
        font-size: 12px;
        font-weight: bold;
        padding: 4px 10px;
        border-radius: 4px;
      }
    }

    .sale-body {
      padding: 12px;

      .product-name {
        font-size: 14px;
        font-weight: 500;
        color: #333;
        margin-bottom: 8px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .price-row {
        display: flex;
        align-items: baseline;
        gap: 8px;
        margin-bottom: 8px;

        .flash-price {
          font-size: 22px;
          font-weight: bold;
          color: #f56c6c;
        }

        .original-price {
          font-size: 13px;
          color: #ccc;
          text-decoration: line-through;
        }
      }

      .sale-info {
        margin-bottom: 8px;

        .limit-tag {
          font-size: 12px;
          color: #e6a23c;
          background-color: #fdf6ec;
          padding: 2px 8px;
          border-radius: 4px;
        }
      }

      .stock-info {
        margin-bottom: 8px;

        .stock-text {
          font-size: 12px;
          color: #999;
          margin-top: 4px;
          display: block;
        }
      }

      .countdown-row {
        display: flex;
        align-items: center;
        gap: 6px;
        color: #f56c6c;
        font-size: 13px;

        .countdown-text {
          font-weight: bold;
          font-family: monospace;

          &.expired {
            color: #999;
          }
        }
      }
    }
  }

  .pagination-wrapper {
    margin-top: 24px;
    display: flex;
    justify-content: center;
  }
}
</style>
