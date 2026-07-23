<template>
  <div class="schedule-logs">
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-button @click="goBack">返回</el-button>
            <span class="card-title">
              执行历史<span v-if="jobName" class="job-name"> - {{ jobName }}</span>
            </span>
          </div>
        </div>
      </template>

      <el-table :data="logs" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="执行状态" width="110">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status] || 'info'" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="执行耗时" width="120">
          <template #default="{ row }">{{ formatDuration(row.durationMs) }}</template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column label="结束时间" width="180">
          <template #default="{ row }">{{ row.endTime || '-' }}</template>
        </el-table-column>
        <el-table-column label="错误信息" min-width="240" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.errorMsg" class="error-text">{{ row.errorMsg }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="执行结果" min-width="220" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.result" class="result-text">{{ row.result }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && logs.length === 0" description="暂无执行记录" />

      <el-pagination
        v-model:current-page="page"
        :page-size="size"
        :total="total"
        layout="prev, pager, next, total"
        @current-change="handlePageChange"
        style="margin-top: 16px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  getScheduleJobLogs,
  getScheduleJobDetail,
  type ScheduleLog,
} from '@/api/modules/schedule'

const route = useRoute()
const router = useRouter()

const jobId = Number(route.params.jobId)
const jobName = ref('')
const loading = ref(false)
const logs = ref<ScheduleLog[]>([])
const page = ref(1)
const size = ref(20)
const total = ref(0)

const statusTagType: Record<string, string> = {
  SUCCESS: 'success',
  FAIL: 'danger',
  RUNNING: 'warning',
}

onMounted(() => {
  loadJob()
  loadLogs()
})

async function loadJob() {
  if (!jobId || Number.isNaN(jobId)) return
  try {
    const job = await getScheduleJobDetail(jobId)
    jobName.value = job.jobName
  } catch {
    jobName.value = ''
  }
}

async function loadLogs() {
  if (!jobId || Number.isNaN(jobId)) return
  loading.value = true
  try {
    const data = await getScheduleJobLogs(jobId, { page: page.value, size: size.value })
    logs.value = data.list || []
    total.value = data.total || 0
  } catch {
    logs.value = []
    total.value = 0
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
  loadLogs()
}

function goBack() {
  router.push('/scheduler')
}

function formatDuration(ms: number): string {
  if (ms == null) return '-'
  if (ms < 1000) return `${ms}ms`
  return `${(ms / 1000).toFixed(2)}s`
}
</script>

<style scoped>
.schedule-logs {
  padding: var(--space-xl);
}
.card-header {
  display: flex;
  align-items: center;
}
.header-left {
  display: flex;
  align-items: center;
  gap: var(--space-md);
}
.card-title {
  font-size: var(--font-size-body-md);
  font-weight: 600;
}
.job-name {
  color: var(--color-shade-50);
  font-weight: 400;
}
.error-text {
  color: var(--color-price, #f56c6c);
}
.result-text {
  color: var(--color-shade-50);
}
</style>
