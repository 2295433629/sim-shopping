<template>
  <div class="points-record-manage">
    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom: 16px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">参与用户</div>
            <div class="stat-value">{{ statistics.totalUsers }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">总发放积分</div>
            <div class="stat-value" style="color: #67c23a">{{ statistics.totalEarned }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">总消耗积分</div>
            <div class="stat-value" style="color: #f56c6c">{{ statistics.totalDeducted }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">兑换次数</div>
            <div class="stat-value" style="color: #409eff">{{ statistics.totalExchanged }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>积分流水管理</span>
          <div class="header-actions">
            <el-input
              v-model.number="filter.userId"
              placeholder="用户ID"
              clearable
              style="width: 120px"
              @keyup.enter="handleSearch"
            />
            <el-select v-model="filter.type" placeholder="类型" clearable style="width: 120px" @change="handleSearch">
              <el-option label="全部" value="" />
              <el-option label="获得" value="EARN" />
              <el-option label="消耗" value="DEDUCT" />
            </el-select>
            <el-input v-model="filter.source" placeholder="来源" clearable style="width: 140px" @keyup.enter="handleSearch" />
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </div>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="userId" label="用户ID" width="90" />
        <el-table-column label="时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="类型" width="90">
          <template #default="{ row }">
            <el-tag :type="typeTagType[row.type]" size="small">
              {{ typeLabelMap[row.type] || row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="积分" width="100">
          <template #default="{ row }">
            <span :class="row.type === 'EARN' ? 'points-earn' : 'points-deduct'">
              {{ formatPoints(row) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="source" label="来源" width="120" />
        <el-table-column prop="description" label="说明" min-width="180" show-overflow-tooltip />
        <el-table-column prop="relatedId" label="关联ID" width="160" show-overflow-tooltip />
      </el-table>

      <el-empty v-if="!loading && list.length === 0" description="暂无积分流水" />

      <el-pagination
        v-model:current-page="page"
        :page-size="size"
        :total="total"
        layout="prev, pager, next, total"
        @current-change="loadList"
        style="margin-top: 16px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getAdminRecords,
  getPointsStatistics,
  type PointsRecord,
  type PointsStatistics,
} from '@/api/modules/points'

const list = ref<PointsRecord[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const statsLoading = ref(false)

const filter = reactive({
  userId: undefined as number | undefined,
  type: '',
  source: '',
})

const statistics = reactive<PointsStatistics>({
  totalUsers: 0,
  totalEarned: 0,
  totalDeducted: 0,
  totalExchanged: 0,
})

const typeTagType: Record<string, string> = {
  EARN: 'success',
  DEDUCT: 'danger',
}

const typeLabelMap: Record<string, string> = {
  EARN: '获得',
  DEDUCT: '消耗',
}

function formatDate(dateStr: string) {
  return dateStr ? dateStr.replace('T', ' ').substring(0, 19) : '-'
}

function formatPoints(row: PointsRecord) {
  const prefix = row.type === 'EARN' ? '+' : '-'
  return `${prefix}${Math.abs(row.points)}`
}

function handleSearch() {
  page.value = 1
  loadList()
}

function handleReset() {
  filter.userId = undefined
  filter.type = ''
  filter.source = ''
  page.value = 1
  loadList()
}

async function loadStatistics() {
  statsLoading.value = true
  try {
    const data = await getPointsStatistics()
    statistics.totalUsers = data.totalUsers ?? 0
    statistics.totalEarned = data.totalEarned ?? 0
    statistics.totalDeducted = data.totalDeducted ?? 0
    statistics.totalExchanged = data.totalExchanged ?? 0
  } catch {
    ElMessage.error('加载统计数据失败')
  } finally {
    statsLoading.value = false
  }
}

const loadList = async () => {
  loading.value = true
  try {
    const data = await getAdminRecords({
      page: page.value,
      size: size.value,
      userId: filter.userId,
      type: filter.type || undefined,
      source: filter.source || undefined,
    })
    list.value = data.list || []
    total.value = data.total || 0
  } catch {
    list.value = []
    total.value = 0
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadList()
  loadStatistics()
})
</script>

<style scoped>
.points-record-manage {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}
.stat-item {
  text-align: center;
}
.stat-label {
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}
.points-earn {
  color: #67c23a;
  font-weight: bold;
}
.points-deduct {
  color: #f56c6c;
  font-weight: bold;
}
</style>
