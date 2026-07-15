<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getLogistics } from '@/api/modules/shipment'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const orderNo = route.params.orderNo as string
const loading = ref(false)
const trackingNo = ref('')
const company = ref('')
const currentStatus = ref('')
const tracks = ref<{ status: string; description: string; location: string; occurredAt: string }[]>([])

const statusMap: Record<string, string> = {
  CREATED: '已创建',
  PICKED_UP: '已揽收',
  SORTING: '分拣中',
  IN_TRANSIT: '运输中',
  OUT_FOR_DELIVERY: '派送中',
  DELIVERED: '已签收',
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getLogistics(orderNo)
    const data = res as any
    trackingNo.value = data.trackingNo || ''
    company.value = data.logisticsCompany || ''
    currentStatus.value = data.status || ''
    tracks.value = (data.tracks || []).map((t: any) => ({
      ...t,
      occurredAt: t.occurredAt,
    }))
  } catch (e: any) {
    ElMessage.error(e.message || '获取物流信息失败')
  } finally {
    loading.value = false
  }
})

function goBack() {
  router.back()
}
</script>

<template>
  <div class="logistics-page">
    <div class="page-header">
      <el-button @click="goBack" :icon="ArrowLeft" text>返回</el-button>
      <h3>物流追踪</h3>
    </div>
    <el-card v-loading="loading" shadow="never">
      <div class="logistics-info" v-if="trackingNo">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="订单号">{{ orderNo }}</el-descriptions-item>
          <el-descriptions-item label="物流公司">{{ company }}</el-descriptions-item>
          <el-descriptions-item label="运单号">{{ trackingNo }}</el-descriptions-item>
        </el-descriptions>
        <el-tag :type="currentStatus === 'DELIVERED' ? 'success' : 'warning'" class="status-tag">
          {{ statusMap[currentStatus] || currentStatus }}
        </el-tag>
      </div>
      <el-empty v-else-if="!loading" description="暂无物流信息" />
      <el-timeline v-if="tracks.length > 0" class="logistics-timeline">
        <el-timeline-item
          v-for="(track, index) in tracks"
          :key="index"
          :timestamp="track.occurredAt"
          placement="top"
          :type="index === 0 ? 'primary' : 'info'"
        >
          <div class="track-item">
            <div class="track-status">{{ statusMap[track.status] || track.status }}</div>
            <div class="track-desc">{{ track.description }}</div>
            <div class="track-location" v-if="track.location">{{ track.location }}</div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.logistics-page {
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  margin-bottom: var(--space-lg);

  h3 {
    margin: 0;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
  }
}

.logistics-info {
  margin-bottom: var(--space-xl);
}

.status-tag {
  margin-left: var(--space-md);
}

.logistics-timeline {
  padding-left: var(--space-sm);
}

.track-item {
  .track-status {
    font-weight: 500;
    color: var(--color-ink);
  }

  .track-desc {
    color: var(--color-shade-50);
    margin-top: var(--space-xs);
  }

  .track-location {
    color: var(--color-shade-40);
    margin-top: 2px;
    font-size: var(--font-size-micro);
  }
}
</style>
