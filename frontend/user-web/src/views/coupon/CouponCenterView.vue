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
    ElMessage.error('Failed to load coupons')
  } finally {
    loading.value = false
  }
}

async function handleClaim(coupon: AvailableCoupon) {
  claimingId.value = coupon.couponId
  try {
    await claimCoupon(coupon.couponId)
    ElMessage.success('Claimed successfully')
    loadCoupons()
  } catch {
    ElMessage.error('Claim failed')
  } finally {
    claimingId.value = null
  }
}

function formatValue(coupon: AvailableCoupon) {
  if (coupon.type === 'PERCENTAGE') {
    return `${coupon.value}% OFF`
  }
  return `$${coupon.value.toFixed(2)} OFF`
}

function formatType(type: string) {
  const map: Record<string, string> = {
    FIXED_AMOUNT: 'Fixed Amount',
    PERCENTAGE: 'Percentage',
    FIXED_DISCOUNT: 'Fixed Discount',
  }
  return map[type] || type
}
</script>

<template>
  <div class="coupon-center-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">Coupon Center</span>
      </template>

      <div v-loading="loading">
        <el-row :gutter="16">
          <el-col v-for="coupon in couponList" :key="coupon.couponId" :xs="24" :sm="12" :md="8" :lg="6">
            <div class="coupon-card">
              <div class="coupon-header">
                <div class="coupon-value">{{ formatValue(coupon) }}</div>
                <div class="coupon-type">{{ formatType(coupon.type) }}</div>
              </div>
              <div class="coupon-body">
                <div class="coupon-name">{{ coupon.name }}</div>
                <div v-if="coupon.description" class="coupon-desc">{{ coupon.description }}</div>
                <div class="coupon-rule">Min order: ${{ coupon.minOrderAmount.toFixed(2) }}</div>
                <div class="coupon-scope">Scope: {{ coupon.scopeName || coupon.scope }}</div>
                <div class="coupon-time">
                  <el-icon><Clock /></el-icon>
                  {{ coupon.startTime }} ~ {{ coupon.endTime }}
                </div>
                <div class="coupon-remain">Remaining: {{ coupon.remainCount }}</div>
              </div>
              <div class="coupon-footer">
                <el-button
                  type="primary"
                  size="small"
                  :loading="claimingId === coupon.couponId"
                  :disabled="coupon.remainCount <= 0"
                  @click="handleClaim(coupon)"
                >
                  {{ coupon.remainCount > 0 ? 'Claim' : 'Out of Stock' }}
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-empty v-if="!loading && couponList.length === 0" description="No coupons available" />
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.coupon-center-container {
  max-width: 1200px;
  margin: 0 auto;

  .card-title {
    font-size: 16px;
    font-weight: bold;
  }

  .coupon-card {
    border: 1px solid #ebeef5;
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 16px;
    background: #fff;
    transition: box-shadow 0.3s;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    .coupon-header {
      background: linear-gradient(135deg, #ff6b6b, #ee5a5a);
      color: #fff;
      padding: 16px;
      text-align: center;

      .coupon-value {
        font-size: 24px;
        font-weight: 700;
      }

      .coupon-type {
        font-size: 12px;
        opacity: 0.9;
        margin-top: 4px;
      }
    }

    .coupon-body {
      padding: 16px;

      .coupon-name {
        font-size: 14px;
        font-weight: 600;
        color: #333;
        margin-bottom: 8px;
      }

      .coupon-desc {
        font-size: 12px;
        color: #666;
        margin-bottom: 8px;
        line-height: 1.4;
      }

      .coupon-rule,
      .coupon-scope,
      .coupon-time,
      .coupon-remain {
        font-size: 12px;
        color: #999;
        margin-top: 4px;
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }

    .coupon-footer {
      padding: 12px 16px;
      border-top: 1px dashed #ebeef5;
      text-align: center;
    }
  }
}
</style>
