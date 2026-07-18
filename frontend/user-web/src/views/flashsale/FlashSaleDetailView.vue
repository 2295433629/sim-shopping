<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getFlashSaleDetail, createFlashSaleOrder, type FlashSaleDetail } from '@/api/modules/flashsale'
import { getAddressListApi } from '@/api/modules/address'
import type { AddressInfo } from '@/types/common'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const detail = ref<FlashSaleDetail | null>(null)
const quantity = ref(1)
const ordering = ref(false)
const selectedAddressId = ref<number | null>(null)
const addressList = ref<AddressInfo[]>([])
const countdownText = ref('--:--:--')
let timer: ReturnType<typeof setInterval> | null = null

const saleId = ref<number>(0)

onMounted(() => {
  saleId.value = Number(route.params.id)
  loadDetail()
  loadAddresses()
  timer = setInterval(updateCountdown, 1000)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
})

async function loadDetail() {
  if (!saleId.value) return
  loading.value = true
  try {
    detail.value = await getFlashSaleDetail(saleId.value)
    quantity.value = 1
    updateCountdown()
  } catch {
    detail.value = null
    ElMessage.error('加载秒杀详情失败')
  } finally {
    loading.value = false
  }
}

async function loadAddresses() {
  try {
    const list = await getAddressListApi()
    addressList.value = Array.isArray(list) ? list : []
    if (addressList.value.length > 0) {
      const defaultAddr = addressList.value.find(a => a.isDefault === 1)
      selectedAddressId.value = defaultAddr ? defaultAddr.id : addressList.value[0].id
    }
  } catch {
    addressList.value = []
  }
}

function updateCountdown() {
  if (!detail.value) {
    countdownText.value = '--:--:--'
    return
  }
  const now = Date.now()
  const end = new Date(detail.value.endTime).getTime()
  const diff = end - now
  if (diff <= 0) {
    countdownText.value = '已结束'
  } else {
    countdownText.value = formatCountdown(diff)
  }
}

function formatCountdown(ms: number): string {
  const seconds = Math.floor(ms / 1000)
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60
  const pad = (n: number) => String(n).padStart(2, '0')
  return `距结束 ${pad(hours)}:${pad(minutes)}:${pad(secs)}`
}

function formatPrice(price: number): string {
  return `¥${price.toFixed(2)}`
}

function soldPercent(): number {
  if (!detail.value) return 0
  const total = detail.value.soldCount + detail.value.stock
  if (total === 0) return 0
  return Math.round((detail.value.soldCount / total) * 100)
}

function isEnded(): boolean {
  if (!detail.value) return true
  const now = Date.now()
  const end = new Date(detail.value.endTime).getTime()
  return end - now <= 0 || detail.value.stock <= 0
}

async function handleOrder() {
  if (!detail.value) return
  if (!selectedAddressId.value) {
    ElMessage.warning('请先选择收货地址')
    return
  }
  if (quantity.value < 1 || quantity.value > detail.value.limitPerUser) {
    ElMessage.warning(`限购 ${detail.value.limitPerUser} 件`)
    return
  }
  if (quantity.value > detail.value.stock) {
    ElMessage.warning('库存不足')
    return
  }
  ordering.value = true
  try {
    const res = await createFlashSaleOrder(detail.value.saleId, { quantity: quantity.value, addressId: selectedAddressId.value })
    ElMessage.success('抢购成功')
    router.push(`/orders/${res.orderNo}`)
  } catch {
    ElMessage.error('抢购失败，请稍后重试')
  } finally {
    ordering.value = false
  }
}

function goBack() {
  router.back()
}
</script>

<template>
  <div class="flash-sale-detail-container" v-loading="loading">
    <template v-if="detail">
      <el-page-header @back="goBack" title="限时秒杀" />

      <el-row :gutter="24" style="margin-top: 20px">
        <!-- 左侧：商品图片 -->
        <el-col :xs="24" :md="10">
          <div class="image-section">
            <div class="main-image">
              <el-image
                v-if="detail.productImage"
                :src="detail.productImage"
                fit="cover"
                class="main-img"
              />
              <el-icon v-else :size="80" color="#ddd"><Picture /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 右侧：商品信息 -->
        <el-col :xs="24" :md="14">
          <div class="info-section">
            <h1 class="product-name">{{ detail.productName }}</h1>
            <p v-if="detail.productName" class="product-desc">{{ detail.productName }}</p>

            <div class="countdown-box">
              <el-icon><Timer /></el-icon>
              <span class="countdown-text" :class="{ expired: countdownText === '已结束' }">
                {{ countdownText }}
              </span>
            </div>

            <div class="price-box">
              <span class="flash-price">{{ formatPrice(detail.flashPrice) }}</span>
              <span class="original-price">{{ formatPrice(detail.originalPrice) }}</span>
              <el-tag type="danger" size="small" effect="dark">
                省 {{ formatPrice(detail.originalPrice - detail.flashPrice) }}
              </el-tag>
            </div>

            <div class="info-row">
              <span class="info-label">库存</span>
              <span class="info-value">{{ detail.stock }} 件</span>
              <span class="info-label">已售</span>
              <span class="info-value">{{ detail.soldCount }} 件</span>
              <span class="info-label">限购</span>
              <span class="info-value">每人 {{ detail.limitPerUser }} 件</span>
            </div>

            <div class="progress-row">
              <el-progress :percentage="soldPercent()" :stroke-width="12" color="#f56c6c">
                <template #default="{ percentage }">
                  <span class="progress-text">已抢 {{ percentage }}%</span>
                </template>
              </el-progress>
            </div>

            <div class="quantity-section">
              <span class="quantity-label">数量</span>
              <el-input-number
                v-model="quantity"
                :min="1"
                :max="Math.min(detail.limitPerUser, detail.stock)"
                :disabled="isEnded()"
              />
            </div>

            <div class="address-section">
              <span class="address-label">收货地址</span>
              <el-select
                v-model="selectedAddressId"
                placeholder="请选择收货地址"
                style="width: 320px"
                :disabled="isEnded()"
              >
                <el-option
                  v-for="addr in addressList"
                  :key="addr.id"
                  :label="`${addr.receiverName} ${addr.receiverPhone} - ${addr.province}${addr.city}${addr.district}${addr.detailAddress}`"
                  :value="addr.id"
                />
              </el-select>
              <router-link to="/addresses" class="manage-address-link">管理地址</router-link>
            </div>

            <div class="action-buttons">
              <el-button
                type="danger"
                size="large"
                :loading="ordering"
                :disabled="isEnded()"
                @click="handleOrder"
              >
                <el-icon><Lightning /></el-icon>
                {{ isEnded() ? (detail.stock <= 0 ? '已售罄' : '已结束') : '立即抢购' }}
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>

    </template>

    <el-empty v-if="!loading && !detail" description="秒杀活动不存在或已结束" />
  </div>
</template>

<style scoped lang="scss">
.flash-sale-detail-container {
  max-width: 1200px;
  margin: 0 auto;

  .image-section {
    .main-image {
      height: 400px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: var(--color-canvas-cream);
      border-radius: var(--rounded-lg);
      overflow: hidden;

      .main-img {
        width: 100%;
        height: 100%;
      }
    }
  }

  .info-section {
    .product-name {
      font-size: var(--font-size-heading-xl);
      font-weight: 500;
      color: var(--color-ink);
      margin: 0 0 var(--space-sm) 0;
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    }

    .product-desc {
      font-size: var(--font-size-caption);
      color: var(--color-shade-50);
      margin: 0 0 var(--space-lg) 0;
    }

    .countdown-box {
      display: inline-flex;
      align-items: center;
      gap: var(--space-sm);
      background: linear-gradient(135deg, #ff6b6b, #ee5a5a);
      color: #fff;
      padding: var(--space-sm) var(--space-lg);
      border-radius: var(--rounded-md);
      margin-bottom: var(--space-lg);
      font-size: var(--font-size-caption);

      .countdown-text {
        font-weight: 500;
        font-family: var(--font-mono);
        font-size: var(--font-size-body-md);

        &.expired {
          opacity: 0.8;
        }
      }
    }

    .price-box {
      background-color: #fff8f0;
      padding: var(--space-lg);
      border-radius: var(--rounded-md);
      margin-bottom: var(--space-lg);
      display: flex;
      align-items: baseline;
      gap: var(--space-md);

      .flash-price {
        font-size: var(--font-size-display-lg);
        font-weight: 500;
        color: var(--color-danger);
      }

      .original-price {
        font-size: var(--font-size-caption);
        color: var(--color-shade-30);
        text-decoration: line-through;
      }
    }

    .info-row {
      display: flex;
      gap: var(--space-lg);
      flex-wrap: wrap;
      margin-bottom: var(--space-lg);
      font-size: var(--font-size-caption);

      .info-label {
        color: var(--color-shade-40);
      }

      .info-value {
        color: var(--color-ink);
        font-weight: 500;
      }
    }

    .progress-row {
      margin-bottom: var(--space-xl);

      .progress-text {
        font-size: var(--font-size-eyebrow);
        color: #fff;
      }
    }

    .quantity-section {
      display: flex;
      align-items: center;
      gap: var(--space-lg);
      margin-bottom: var(--space-xl);

      .quantity-label {
        font-size: var(--font-size-caption);
        color: var(--color-shade-50);
      }
    }

    .address-section {
      display: flex;
      align-items: center;
      gap: var(--space-lg);
      margin-bottom: var(--space-xl);
      flex-wrap: wrap;

      .address-label {
        font-size: var(--font-size-caption);
        color: var(--color-shade-50);
      }

      .manage-address-link {
        font-size: var(--font-size-caption);
        color: var(--color-primary, #409eff);
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }

    .action-buttons {
      display: flex;
      gap: var(--space-md);

      .el-button {
        min-width: 180px;
      }
    }
  }

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .product-description {
    line-height: 1.8;
    color: var(--color-ink);

    :deep(img) {
      max-width: 100%;
    }
  }
}
</style>
