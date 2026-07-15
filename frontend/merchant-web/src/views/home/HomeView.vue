<template>
  <div class="dashboard">
    <h2>工作台</h2>
    <el-row :gutter="20" v-loading="loading">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.todayOrders }}</div>
            <div class="stat-label">今日订单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">¥{{ formatPrice(data.todaySales) }}</div>
            <div class="stat-label">今日销售额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.totalProducts }}</div>
            <div class="stat-label">商品总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.publishedProducts }}</div>
            <div class="stat-label">已发布</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.pendingShipments }}</div>
            <div class="stat-label">待发货</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.totalReviews }}</div>
            <div class="stat-label">评价数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.avgReviewScore }}</div>
            <div class="stat-label">平均评分</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getDashboard } from '@/api/modules/product'

const loading = ref(false)
const data = reactive({
  todayOrders: 0,
  todaySales: 0,
  totalProducts: 0,
  publishedProducts: 0,
  pendingShipments: 0,
  totalReviews: 0,
  avgReviewScore: 0
})

function formatPrice(val: number) {
  return val != null ? val.toFixed(2) : '0.00'
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getDashboard()
    Object.assign(data, res)
  } catch (e) { /* ignore */ }
  loading.value = false
})
</script>

<style scoped lang="scss">
.dashboard {
  padding: var(--space-xl);

  h2 {
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
    font-size: var(--font-size-heading-xl);
    color: var(--color-ink);
    margin-bottom: var(--space-xl);
  }
}

.stat-card {
  text-align: center;
  padding: var(--space-xl) 0;
}

.stat-value {
  font-size: var(--font-size-heading-xl);
  font-weight: 600;
  color: var(--color-ink);
  font-family: var(--font-display, 'Helvetica Neue', sans-serif);
}

.stat-label {
  font-size: var(--font-size-caption);
  color: var(--color-shade-50);
  margin-top: var(--space-sm);
}
</style>