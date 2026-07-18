<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAvailableCoupons, claimCoupon, type AvailableCoupon } from '@/api/modules/coupon'

const loading = ref(false)
const couponList = ref<AvailableCoupon[]>([])
const claimingId = ref<number | null>(null)

onMounted(() => {
  loadCoupons()
})

async function loadCoupons() {
  loading.value = true
  try {
    couponList.value = await getAvailableCoupons()
  } catch {
    couponList.value = []
    ElMessage.error('加载优惠券失败')
  } finally {
    loading.value = false
  }
}

async function handleClaim(coupon: AvailableCoupon) {
  claimingId.value = coupon.id
  try {
    await claimCoupon(coupon.id)
    ElMessage.success('领取成功')
    loadCoupons()
  } catch {
    ElMessage.error('领取失败')
  } finally {
    claimingId.value = null
  }
}

function formatValue(coupon: AvailableCoupon) {
  if (coupon.couponType === 'PERCENTAGE') {
    return `${coupon.discountValue}折`
  }
  return `减¥${coupon.discountValue.toFixed(2)}`
}

function formatType(type: string) {
  const map: Record<string, string> = {
    FIXED_AMOUNT: '固定金额',
    PERCENTAGE: '百分比折扣',
    FIXED_DISCOUNT: '固定折扣',
  }
  return map[type] || type
}

function formatScope(scope: string) {
  const map: Record<string, string> = {
    ALL: '全场通用',
    CATEGORY: '指定分类',
    PRODUCT: '指定商品',
  }
  return map[scope] || scope
}
</script>

<template>
  <div class="coupon-center-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">领券中心</span>
      </template>

      <div v-loading="loading">
        <el-row :gutter="16">
          <el-col v-for="coupon in couponList" :key="coupon.id" :xs="24" :sm="12" :md="8" :lg="6">
            <div class="coupon-card">
              <div class="coupon-header">
                <div class="coupon-value">{{ formatValue(coupon) }}</div>
                <div class="coupon-type">{{ formatType(coupon.couponType) }}</div>
              </div>
              <div class="coupon-body">
                <div class="coupon-name">{{ coupon.couponName }}</div>
                <div class="coupon-rule">最低消费: ¥{{ coupon.minSpend.toFixed(2) }}</div>
                <div class="coupon-scope">适用范围: {{ formatScope(coupon.applicableScope) }}</div>
                <div class="coupon-time">
                  <el-icon><Clock /></el-icon>
                  {{ coupon.validStartTime }} ~ {{ coupon.validEndTime }}
                </div>
                <div class="coupon-remain">剩余数量: {{ coupon.remainingQuantity }}</div>
              </div>
              <div class="coupon-footer">
                <el-button
                  type="primary"
                  size="small"
                  :loading="claimingId === coupon.id"
                  :disabled="coupon.remainingQuantity <= 0"
                  @click="handleClaim(coupon)"
                >
                  {{ coupon.remainingQuantity > 0 ? '立即领取' : '已领完' }}
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-empty v-if="!loading && couponList.length === 0" description="暂无可用优惠券" />
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.coupon-center-container {
  max-width: 1200px;
  margin: 0 auto;

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .coupon-card {
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    overflow: hidden;
    margin-bottom: var(--space-lg);
    background: var(--color-canvas-light);
    transition: box-shadow var(--transition-normal);

    &:hover {
      box-shadow: var(--shadow-md);
    }

    .coupon-header {
      background: linear-gradient(135deg, var(--color-aloe-10), var(--color-pistachio-10));
      color: var(--color-ink);
      padding: var(--space-lg);
      text-align: center;

      .coupon-value {
        font-size: var(--font-size-heading-xl);
        font-weight: 700;
        font-family: var(--font-display, 'Helvetica Neue', sans-serif);
      }

      .coupon-type {
        font-size: var(--font-size-eyebrow);
        opacity: 0.8;
        margin-top: var(--space-xs);
      }
    }

    .coupon-body {
      padding: var(--space-lg);

      .coupon-name {
        font-size: var(--font-size-caption);
        font-weight: 500;
        color: var(--color-ink);
        margin-bottom: var(--space-sm);
      }

      .coupon-desc {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-50);
        margin-bottom: var(--space-sm);
        line-height: 1.4;
      }

      .coupon-rule,
      .coupon-scope,
      .coupon-time,
      .coupon-remain {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-40);
        margin-top: var(--space-xs);
        display: flex;
        align-items: center;
        gap: var(--space-xs);
      }
    }

    .coupon-footer {
      padding: var(--space-md) var(--space-lg);
      border-top: 1px dashed var(--color-hairline-light);
      text-align: center;
    }
  }
}
</style>
