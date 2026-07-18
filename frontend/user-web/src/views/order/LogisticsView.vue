<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
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
const senderAddress = ref('')
const receiverAddress = ref('')
const senderCity = ref('')

const statusMap: Record<string, string> = {
  CREATED: '已创建',
  PICKED_UP: '已揽收',
  SORTING: '分拣中',
  IN_TRANSIT: '运输中',
  OUT_FOR_DELIVERY: '派送中',
  DELIVERED: '已签收',
}

// 物流状态对应的进度百分比
const statusProgressMap: Record<string, number> = {
  CREATED: 0,
  PICKED_UP: 15,
  SORTING: 30,
  IN_TRANSIT: 50,
  OUT_FOR_DELIVERY: 75,
  DELIVERED: 100,
}

// 物流路线节点
const routeNodes = [
  { key: 'CREATED', label: '已创建' },
  { key: 'PICKED_UP', label: '已揽收' },
  { key: 'SORTING', label: '分拣中' },
  { key: 'IN_TRANSIT', label: '运输中' },
  { key: 'OUT_FOR_DELIVERY', label: '派送中' },
  { key: 'DELIVERED', label: '已签收' },
]

// 当前进度百分比
const progressPercent = computed(() => {
  return statusProgressMap[currentStatus.value] ?? 0
})

// 当前步骤索引
const currentStepIndex = computed(() => {
  return routeNodes.findIndex((n) => n.key === currentStatus.value)
})

// 发货地显示名称
const senderLabel = computed(() => {
  if (senderCity.value) return senderCity.value
  return '商家仓库'
})

// 收货地显示名称
const receiverLabel = computed(() => {
  if (receiverAddress.value) {
    // 提取城市名（取前几个字符作为简短显示）
    const addr = receiverAddress.value
    if (addr.length > 8) return addr.substring(0, 8) + '...'
    return addr
  }
  return '收货地址'
})

onMounted(async () => {
  loading.value = true
  try {
    const res = await getLogistics(orderNo)
    const data = res as any
    trackingNo.value = data.trackingNo || ''
    company.value = data.logisticsCompany || ''
    currentStatus.value = data.status || ''
    senderAddress.value = data.senderAddress || ''
    receiverAddress.value = data.receiverAddress || ''
    senderCity.value = data.senderCity || ''
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

      <!-- 物流路线图 -->
      <div class="logistics-route" v-if="trackingNo">
        <div class="route-header">
          <span class="route-title">物流路线</span>
        </div>

        <!-- 横向路线进度条 -->
        <div class="route-progress-bar">
          <!-- 左端点：发货地 -->
          <div class="route-endpoint endpoint-sender">
            <div class="endpoint-dot sender-dot"></div>
            <div class="endpoint-label">{{ senderLabel }}</div>
          </div>

          <!-- 中间进度条 -->
          <div class="route-track-area">
            <div class="route-track-line">
              <div class="route-track-fill" :style="{ width: progressPercent + '%' }"></div>
            </div>
            <!-- 移动的图标 -->
            <div
              class="route-moving-icon"
              :style="{ left: progressPercent + '%' }"
              :class="{ 'icon-delivered': currentStatus === 'DELIVERED' }"
            >
              <span v-if="currentStatus !== 'DELIVERED'" class="truck-icon">&#x1F69A;</span>
              <span v-else class="check-icon">&#x2705;</span>
            </div>
          </div>

          <!-- 右端点：收货地 -->
          <div class="route-endpoint endpoint-receiver">
            <div class="endpoint-dot receiver-dot" :class="{ 'dot-active': currentStatus === 'DELIVERED' }"></div>
            <div class="endpoint-label">{{ receiverLabel }}</div>
          </div>
        </div>

        <!-- 底部状态节点 -->
        <div class="route-status-nodes">
          <div
            v-for="(node, index) in routeNodes"
            :key="node.key"
            class="status-node"
            :class="{
              'node-active': index <= currentStepIndex,
              'node-current': index === currentStepIndex,
              'node-completed': index < currentStepIndex,
            }"
          >
            <div class="node-circle">
              <span v-if="index < currentStepIndex" class="node-check">&#x2713;</span>
              <span v-else>{{ index + 1 }}</span>
            </div>
            <div class="node-label">{{ node.label }}</div>
          </div>
        </div>

        <!-- 进度百分比 -->
        <div class="route-progress-text">
          物流进度：<strong>{{ progressPercent }}%</strong>
          <span v-if="currentStatus === 'DELIVERED'" class="delivered-tip"> - 已完成</span>
        </div>
      </div>

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

/* ========== 物流路线图样式 ========== */
.logistics-route {
  margin: var(--space-xl) 0;
  padding: var(--space-lg);
  background: #f9fafb;
  border-radius: 12px;
  border: 1px solid #eef0f3;
}

.route-header {
  margin-bottom: var(--space-lg);

  .route-title {
    font-size: 16px;
    font-weight: 500;
    color: var(--color-ink);
  }
}

/* 横向路线进度条 */
.route-progress-bar {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  gap: 16px;
}

.route-endpoint {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-shrink: 0;
  min-width: 60px;
}

.endpoint-dot {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  margin-bottom: 6px;

  &.sender-dot {
    background: #409eff;
    box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
  }

  &.receiver-dot {
    background: #c0c4cc;
    box-shadow: 0 0 0 3px rgba(192, 196, 204, 0.2);
    transition: all 0.6s ease;

    &.dot-active {
      background: #67c23a;
      box-shadow: 0 0 0 3px rgba(103, 194, 58, 0.2);
    }
  }
}

.endpoint-label {
  font-size: 12px;
  color: #606266;
  text-align: center;
  max-width: 80px;
  word-break: break-all;
}

/* 中间轨道区域 */
.route-track-area {
  flex: 1;
  position: relative;
  height: 40px;
  display: flex;
  align-items: center;
}

.route-track-line {
  width: 100%;
  height: 4px;
  background: #e4e7ed;
  border-radius: 2px;
  position: relative;
  overflow: hidden;
}

.route-track-fill {
  height: 100%;
  background: linear-gradient(90deg, #409eff, #67c23a);
  border-radius: 2px;
  transition: width 1.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.route-moving-icon {
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
  font-size: 22px;
  transition: left 1.2s cubic-bezier(0.4, 0, 0.2, 1);
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.15));
  z-index: 1;

  .truck-icon {
    display: inline-block;
    animation: truckBounce 1.5s ease-in-out infinite;
  }

  .check-icon {
    display: inline-block;
    font-size: 18px;
  }
}

@keyframes truckBounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}

/* 底部状态节点 */
.route-status-nodes {
  display: flex;
  justify-content: space-between;
  margin-top: var(--space-lg);
  padding: 0 10px;
}

.status-node {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  opacity: 0.4;
  transition: opacity 0.4s ease;

  &.node-active {
    opacity: 1;
  }

  &.node-current {
    .node-circle {
      background: #409eff;
      color: #fff;
      box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
    }
  }

  &.node-completed {
    .node-circle {
      background: #67c23a;
      color: #fff;
    }
  }
}

.node-circle {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #e4e7ed;
  color: #909399;
  font-size: 12px;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.4s ease;

  .node-check {
    font-size: 14px;
    font-weight: bold;
  }
}

.node-label {
  font-size: 11px;
  color: #909399;
  white-space: nowrap;

  .node-active & {
    color: #303133;
  }
}

/* 进度文字 */
.route-progress-text {
  margin-top: var(--space-md);
  text-align: center;
  font-size: 14px;
  color: #606266;

  strong {
    color: #409eff;
    font-size: 18px;
  }

  .delivered-tip {
    color: #67c23a;
  }
}

/* ========== 时间线样式 ========== */
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
