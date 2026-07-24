<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getDashboard, type DashboardData } from '@/api/modules/dashboard'
import { ORDER_STATUS_TAG_TYPE } from '@/constants/order'

const userStore = useUserStore()
const loading = ref(false)
const dashboard = ref<DashboardData | null>(null)

const statusTagType = ORDER_STATUS_TAG_TYPE

const firstRow = [
  { label: '总用户数', key: 'totalUsers' as const, icon: 'User', color: '#409eff', bgColor: '#ecf5ff' },
  { label: '总商家数', key: 'totalMerchants' as const, icon: 'Shop', color: '#67c23a', bgColor: '#f0f9eb' },
  { label: '总订单数', key: 'totalOrders' as const, icon: 'Document', color: '#e6a23c', bgColor: '#fdf6ec' },
  { label: '总商品数', key: 'totalProducts' as const, icon: 'Goods', color: '#f56c6c', bgColor: '#fef0f0' },
]

const secondRow = [
  { label: '总销售额', key: 'totalSales' as const, icon: 'Money', color: '#9c27b0', bgColor: '#f3e5f5', prefix: '¥' },
  { label: '今日订单', key: 'todayOrders' as const, icon: 'Tickets', color: '#00bcd4', bgColor: '#e0f7fa' },
  { label: '今日销售额', key: 'todaySales' as const, icon: 'Coin', color: '#ff9800', bgColor: '#fff3e0', prefix: '¥' },
  { label: '待审核商家', key: 'pendingMerchants' as const, icon: 'WarningFilled', color: '#e4393c', bgColor: '#fef0f0' },
]

function formatMoney(value: number | string): string {
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

onMounted(async () => {
  loading.value = true
  try {
    dashboard.value = await getDashboard()
  } catch {
    dashboard.value = null
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="dashboard-container" v-loading="loading">
    <!-- 欢迎卡片 -->
    <el-card shadow="never" style="margin-bottom: var(--space-lg)">
      <template #header>
        <span>欢迎回来，{{ userStore.userInfo?.nickname || userStore.userInfo?.username || 'Admin' }}</span>
      </template>
      <p style="color: var(--color-shade-50); margin: 0;">以下是平台核心数据概览。</p>
    </el-card>

    <!-- 第一行统计卡片 -->
    <el-row :gutter="20" style="margin-bottom: var(--space-lg)">
      <el-col :xs="24" :sm="12" :md="6" v-for="stat in firstRow" :key="stat.key">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: stat.bgColor, color: stat.color }">
              <el-icon :size="28"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-value">{{ dashboard?.[stat.key] ?? '--' }}</p>
              <p class="stat-label">{{ stat.label }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行统计卡片 -->
    <el-row :gutter="20" style="margin-bottom: var(--space-lg)">
      <el-col :xs="24" :sm="12" :md="6" v-for="stat in secondRow" :key="stat.key">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: stat.bgColor, color: stat.color }">
              <el-icon :size="28"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-value">
                <template v-if="stat.prefix">{{ stat.prefix }}</template>
                <template v-if="dashboard?.[stat.key] != null">
                  {{ formatMoney(dashboard[stat.key] as number) }}
                </template>
                <template v-else>--</template>
              </p>
              <p class="stat-label">{{ stat.label }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近订单 -->
    <el-card shadow="never">
      <template #header>
        <span class="card-title">最近订单</span>
      </template>
      <el-table
        :data="dashboard?.recentOrders || []"
        style="width: 100%"
        stripe
      >
        <el-table-column label="订单号" prop="orderNo" width="200" />
        <el-table-column label="店铺" prop="shopName" width="150" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status] || 'info'" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="订单金额" width="130" align="right">
          <template #default="{ row }">
            <span class="amount">¥{{ row.totalAmount?.toFixed(2) ?? '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实付金额" width="130" align="right">
          <template #default="{ row }">
            <span class="amount">¥{{ row.payAmount?.toFixed(2) ?? '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" prop="createdAt" min-width="180" />
      </el-table>
      <el-empty v-if="!loading && (!dashboard?.recentOrders || dashboard.recentOrders.length === 0)" description="暂无订单" />
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.dashboard-container {
  .stat-card {
    margin-bottom: var(--space-xl);

    .stat-content {
      display: flex;
      align-items: center;
      gap: var(--space-lg);

      .stat-icon {
        width: 56px;
        height: 56px;
        border-radius: var(--rounded-md);
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;
      }

      .stat-info {
        .stat-value {
          font-size: var(--font-size-heading-lg);
          font-weight: 700;
          color: var(--color-ink);
          margin: 0;
        }

        .stat-label {
          font-size: var(--font-size-caption);
          color: var(--color-shade-40);
          margin: var(--space-xs) 0 0 0;
        }
      }
    }
  }

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 600;
  }

  .amount {
    color: var(--color-price);
    font-weight: 600;
  }
}
</style>
