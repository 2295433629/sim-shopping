<template>
  <div class="dashboard">
    <h2>Dashboard</h2>
    <el-row :gutter="20" v-loading="loading">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.todayOrders }}</div>
            <div class="stat-label">Today Orders</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ formatPrice(data.todaySales) }}</div>
            <div class="stat-label">Today Sales</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.totalProducts }}</div>
            <div class="stat-label">Total Products</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.publishedProducts }}</div>
            <div class="stat-label">Published</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.pendingShipments }}</div>
            <div class="stat-label">Pending Shipments</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.totalReviews }}</div>
            <div class="stat-label">Reviews</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ data.avgReviewScore }}</div>
            <div class="stat-label">Avg Score</div>
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
    Object.assign(data, res.data)
  } catch (e) { /* ignore */ }
  loading.value = false
})
</script>

<style scoped>
.dashboard { padding: 20px; }
.stat-card { text-align: center; padding: 20px 0; }
.stat-value { font-size: 28px; font-weight: 600; color: #409eff; }
.stat-label { font-size: 14px; color: #999; margin-top: 8px; }
</style>