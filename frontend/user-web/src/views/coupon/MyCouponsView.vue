<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyCoupons, type MyCoupon } from '@/api/modules/coupon'

const loading = ref(false)
const couponList = ref<MyCoupon[]>([])
const activeTab = ref('UNUSED')
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const tabs = [
  { label: 'Unused', value: 'UNUSED' },
  { label: 'Used', value: 'USED' },
  { label: 'Expired', value: 'EXPIRED' },
]

const statusTagType: Record<string, string> = {
  UNUSED: 'success',
  USED: 'info',
  EXPIRED: 'danger',
}

onMounted(() => {
  loadCoupons()
})

watch([activeTab, page], () => {
  loadCoupons()
})

async function loadCoupons() {
  loading.value = true
  try {
    const data = await getMyCoupons({ status: activeTab.value, page: page.value, size: pageSize.value })
    couponList.value = data.list || []
    total.value = data.total || 0
  } catch {
    couponList.value = []
    total.value = 0
    ElMessage.error('Failed to load coupons')
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
}

function formatValue(coupon: MyCoupon) {
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
  <div class="my-coupons-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">My Coupons</span>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane v-for="tab in tabs" :key="tab.value" :label="tab.label" :name="tab.value" />
      </el-tabs>

      <div v-loading="loading">
        <div v-for="coupon in couponList" :key="coupon.userCouponId" class="coupon-item">
          <div class="coupon-left">
            <div class="coupon-value">{{ formatValue(coupon) }}</div>
            <div class="coupon-type">{{ formatType(coupon.type) }}</div>
          </div>
          <div class="coupon-right">
            <div class="coupon-info">
              <div class="coupon-name">{{ coupon.name }}</div>
              <div v-if="coupon.description" class="coupon-desc">{{ coupon.description }}</div>
              <div class="coupon-meta">
                <span>Min order: ${{ coupon.minOrderAmount.toFixed(2) }}</span>
                <el-tag :type="(statusTagType[coupon.status] as any) || 'info'" size="small">
                  {{ coupon.status }}
                </el-tag>
              </div>
              <div class="coupon-time">
                <span v-if="coupon.status === 'UNUSED'">Expires: {{ coupon.expiredAt }}</span>
                <span v-else-if="coupon.status === 'USED'">Used: {{ coupon.usedAt }}</span>
                <span v-else>Expired: {{ coupon.expiredAt }}</span>
              </div>
              <div v-if="coupon.orderNo" class="coupon-order">Order: {{ coupon.orderNo }}</div>
            </div>
          </div>
        </div>

        <el-empty v-if="!loading && couponList.length === 0" :description="`No ${activeTab.toLowerCase()} coupons`" />

        <div v-if="total > pageSize" class="pagination-wrap">
          <el-pagination
            v-model:current-page="page"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.my-coupons-container {
  max-width: 900px;
  margin: 0 auto;

  .card-title {
    font-size: 16px;
    font-weight: bold;
  }

  .coupon-item {
    display: flex;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 16px;
    background: #fff;

    .coupon-left {
      width: 140px;
      background: linear-gradient(135deg, #409eff, #66b1ff);
      color: #fff;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      padding: 16px;

      .coupon-value {
        font-size: 20px;
        font-weight: 700;
      }

      .coupon-type {
        font-size: 12px;
        opacity: 0.9;
        margin-top: 4px;
      }
    }

    .coupon-right {
      flex: 1;
      padding: 16px;
      display: flex;
      align-items: center;

      .coupon-info {
        flex: 1;

        .coupon-name {
          font-size: 14px;
          font-weight: 600;
          color: #333;
        }

        .coupon-desc {
          font-size: 12px;
          color: #666;
          margin-top: 4px;
        }

        .coupon-meta {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-top: 8px;
          font-size: 12px;
          color: #999;
        }

        .coupon-time {
          font-size: 12px;
          color: #999;
          margin-top: 4px;
        }

        .coupon-order {
          font-size: 12px;
          color: #409eff;
          margin-top: 4px;
        }
      }
    }
  }

  .pagination-wrap {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>
