<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getPointsRecords, type PointsRecord, type PointsRecordType } from '@/api/modules/points'

const loading = ref(false)
const list = ref<PointsRecord[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const filter = reactive({
  type: '' as '' | PointsRecordType,
})

const typeOptions = [
  { label: '全部', value: '' },
  { label: '获得', value: 'EARN' },
  { label: '消耗', value: 'DEDUCT' },
]

const typeTagType: Record<string, string> = {
  EARN: 'success',
  DEDUCT: 'danger',
}

const typeLabelMap: Record<string, string> = {
  EARN: '获得',
  DEDUCT: '消耗',
}

onMounted(() => {
  loadRecords()
})

async function loadRecords() {
  loading.value = true
  try {
    const data = await getPointsRecords({
      type: filter.type || undefined,
      page: page.value,
      size: size.value,
    })
    list.value = data.list || []
    total.value = data.total || 0
  } catch {
    list.value = []
    total.value = 0
    ElMessage.error('加载积分记录失败')
  } finally {
    loading.value = false
  }
}

function handleFilterChange() {
  page.value = 1
  loadRecords()
}

function formatDate(dateStr: string) {
  return dateStr ? dateStr.replace('T', ' ').substring(0, 19) : '-'
}

function formatPoints(row: PointsRecord) {
  const prefix = row.type === 'EARN' ? '+' : '-'
  return `${prefix}${Math.abs(row.points)}`
}
</script>

<template>
  <div class="points-record-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">积分明细</span>
          <div class="header-actions">
            <el-select
              v-model="filter.type"
              placeholder="类型筛选"
              clearable
              style="width: 120px"
              @change="handleFilterChange"
            >
              <el-option
                v-for="opt in typeOptions"
                :key="opt.value"
                :label="opt.label"
                :value="opt.value"
              />
            </el-select>
            <el-button type="primary" @click="loadRecords">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" border stripe>
        <el-table-column prop="id" label="编号" width="70" />
        <el-table-column label="时间" width="170">
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
        <el-table-column prop="source" label="来源" width="130" />
        <el-table-column prop="description" label="说明" min-width="200" show-overflow-tooltip />
        <el-table-column prop="relatedId" label="关联ID" width="120" show-overflow-tooltip />
      </el-table>

      <el-empty v-if="!loading && list.length === 0" description="暂无积分记录" />

      <el-pagination
        v-model:current-page="page"
        :page-size="size"
        :total="total"
        layout="prev, pager, next, total"
        @current-change="loadRecords"
        style="margin-top: 16px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.points-record-container {
  max-width: 1200px;
  margin: 0 auto;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .header-actions {
    display: flex;
    gap: var(--space-sm);
    align-items: center;
  }

  .points-earn {
    color: var(--color-success);
    font-weight: 500;
  }

  .points-deduct {
    color: var(--color-danger);
    font-weight: 500;
  }
}
</style>
