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
  { label: '未使用', value: 'UNUSED' },
  { label: '已使用', value: 'USED' },
  { label: '已过期', value: 'EXPIRED' },
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
    ElMessage.error('加载优惠券失败')
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
}

function formatValue(coupon: MyCoupon) {
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
</script>

<template>
  <div class="my-coupons-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">我的优惠券</span>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane v-for="tab in tabs" :key="tab.value" :label="tab.label" :name="tab.value" />
      </el-tabs>

      <div v-loading="loading">
        <div v-for="coupon in couponList" :key="coupon.id" class="coupon-item">
          <div class="coupon-left">
            <div class="coupon-value">{{ formatValue(coupon) }}</div>
            <div class="coupon-type">{{ formatType(coupon.couponType) }}</div>
          </div>
          <div class="coupon-right">
            <div class="coupon-info">
              <div class="coupon-name">{{ coupon.couponName }}</div>
              <div class="coupon-meta">
                <span>最低消费: ¥{{ coupon.minSpend.toFixed(2) }}</span>
                <el-tag :type="(statusTagType[coupon.status] as any) || 'info'" size="small">
                  {{ coupon.status === 'UNUSED' ? '未使用' : coupon.status === 'USED' ? '已使用' : '已过期' }}
                </el-tag>
              </div>
              <div class="coupon-time">
                <span v-if="coupon.status === 'UNUSED'">有效期至: {{ coupon.validEndTime }}</span>
                <span v-else-if="coupon.status === 'USED'">使用时间: {{ coupon.usedAt }}</span>
                <span v-else>已过期: {{ coupon.validEndTime }}</span>
              </div>
              <div v-if="coupon.orderNo" class="coupon-order">关联订单: {{ coupon.orderNo }}</div>
            </div>
          </div>
        </div>

        <el-empty v-if="!loading && couponList.length === 0" :description="activeTab === 'UNUSED' ? '暂无未使用优惠券' : activeTab === 'USED' ? '暂无已使用优惠券' : '暂无已过期优惠券'" />

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
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .coupon-item {
    display: flex;
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    overflow: hidden;
    margin-bottom: var(--space-lg);
    background: var(--color-canvas-light);

    .coupon-left {
      width: 140px;
      background: linear-gradient(135deg, var(--color-aloe-10), var(--color-pistachio-10));
      color: var(--color-ink);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      padding: var(--space-lg);

      .coupon-value {
        font-size: var(--font-size-heading-lg);
        font-weight: 700;
        font-family: var(--font-display, 'Helvetica Neue', sans-serif);
      }

      .coupon-type {
        font-size: var(--font-size-eyebrow);
        opacity: 0.8;
        margin-top: var(--space-xs);
      }
    }

    .coupon-right {
      flex: 1;
      padding: var(--space-lg);
      display: flex;
      align-items: center;

      .coupon-info {
        flex: 1;

        .coupon-name {
          font-size: var(--font-size-caption);
          font-weight: 500;
          color: var(--color-ink);
        }

        .coupon-desc {
          font-size: var(--font-size-eyebrow);
          color: var(--color-shade-50);
          margin-top: var(--space-xs);
        }

        .coupon-meta {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-top: var(--space-sm);
          font-size: var(--font-size-eyebrow);
          color: var(--color-shade-40);
        }

        .coupon-time {
          font-size: var(--font-size-eyebrow);
          color: var(--color-shade-40);
          margin-top: var(--space-xs);
        }

        .coupon-order {
          font-size: var(--font-size-eyebrow);
          color: var(--color-link-mint);
          margin-top: var(--space-xs);
        }
      }
    }
  }

  .pagination-wrap {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
