<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const stats = [
  { label: '注册用户', value: '--', icon: 'User', color: '#409eff' },
  { label: '入驻商家', value: '--', icon: 'Shop', color: '#67c23a' },
  { label: '商品总数', value: '--', icon: 'Goods', color: '#e6a23c' },
  { label: '今日订单', value: '--', icon: 'Document', color: '#f56c6c' },
  { label: '今日交易额', value: '--', icon: 'Money', color: '#909399' },
  { label: '活跃用户', value: '--', icon: 'View', color: '#9c27b0' },
]
</script>

<template>
  <div class="home-container">
    <el-card shadow="never" style="margin-bottom: 20px">
      <template #header>
        <span>欢迎回来，{{ userStore.userInfo?.nickname || userStore.userInfo?.username || 'Admin' }}</span>
      </template>
      <p>这里是管理后台仪表盘骨架，后续将展示平台核心数据概览。</p>
    </el-card>

    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :md="8" v-for="(stat, i) in stats" :key="i">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: stat.color }">
              <el-icon :size="28" color="#fff"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-value">{{ stat.value }}</p>
              <p class="stat-label">{{ stat.label }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.home-container {
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
}
</style>
