<template>
  <div class="schedule-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>定时任务管理</span>
          <div class="header-actions">
            <el-input
              v-model="filter.keyword"
              placeholder="搜索任务名称"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
            />
            <el-select
              v-model="filter.group"
              placeholder="分组"
              clearable
              style="width: 160px"
              @change="handleSearch"
            >
              <el-option v-for="g in groups" :key="g" :label="g" :value="g" />
            </el-select>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
            <el-button type="success" @click="handleAdd">新增任务</el-button>
          </div>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="jobName" label="任务名称" min-width="140" />
        <el-table-column label="分组" width="110">
          <template #default="{ row }">
            <el-tag size="small">{{ row.jobGroup || 'default' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="执行目标" min-width="180">
          <template #default="{ row }">
            <span class="mono">{{ row.beanName }}.{{ row.methodName }}()</span>
          </template>
        </el-table-column>
        <el-table-column label="调度方式" min-width="170">
          <template #default="{ row }">
            <el-tag v-if="row.cronExpression" size="small" type="warning">{{ row.cronExpression }}</el-tag>
            <span v-else-if="row.fixedDelay != null" class="mono">fixedDelay {{ row.fixedDelay }}ms</span>
            <span v-else-if="row.fixedRate != null" class="mono">fixedRate {{ row.fixedRate }}ms</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status] || 'info'" size="small">
              {{ row.statusText || row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastRunTime" label="上次执行时间" width="170">
          <template #default="{ row }">{{ row.lastRunTime || '-' }}</template>
        </el-table-column>
        <el-table-column label="上次执行状态" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.lastRunStatus" :type="lastRunTagType[row.lastRunStatus] || 'info'" size="small">
              {{ row.lastRunStatus }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="340" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleDetail(row)">详情</el-button>
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 'ACTIVE'" size="small" type="warning" @click="handlePause(row)">暂停</el-button>
            <el-button v-else size="small" type="success" @click="handleResume(row)">恢复</el-button>
            <el-button size="small" type="info" @click="handleExecute(row)">执行</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && list.length === 0" description="暂无任务" />

      <el-pagination
        v-model:current-page="page"
        :page-size="size"
        :total="total"
        layout="prev, pager, next, total"
        @current-change="loadList"
        style="margin-top: 16px; justify-content: flex-end"
      />
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="任务详情" width="640px">
      <el-descriptions v-if="detail" :column="1" border v-loading="detailLoading">
        <el-descriptions-item label="任务ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="任务名称">{{ detail.jobName }}</el-descriptions-item>
        <el-descriptions-item label="分组">{{ detail.jobGroup || 'default' }}</el-descriptions-item>
        <el-descriptions-item label="Bean名称">{{ detail.beanName }}</el-descriptions-item>
        <el-descriptions-item label="方法名称">{{ detail.methodName }}</el-descriptions-item>
        <el-descriptions-item label="参数">{{ detail.params || '-' }}</el-descriptions-item>
        <el-descriptions-item label="Cron表达式">{{ detail.cronExpression || '-' }}</el-descriptions-item>
        <el-descriptions-item label="固定延迟">
          {{ detail.fixedDelay != null ? detail.fixedDelay + 'ms' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="固定频率">
          {{ detail.fixedRate != null ? detail.fixedRate + 'ms' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">{{ detail.statusText || detail.status }}</el-descriptions-item>
        <el-descriptions-item label="允许并发">{{ detail.concurrent ? '是' : '否' }}</el-descriptions-item>
        <el-descriptions-item label="上次执行时间">{{ detail.lastRunTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="下次执行时间">{{ detail.nextRunTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="上次执行状态">{{ detail.lastRunStatus || '-' }}</el-descriptions-item>
        <el-descriptions-item label="错误信息">{{ detail.lastErrorMsg || '-' }}</el-descriptions-item>
        <el-descriptions-item label="描述">{{ detail.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createdAt || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updatedAt || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="goLogs">执行历史</el-button>
        <el-button type="primary" @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑任务' : '新增任务'" width="640px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="任务名称" prop="jobName">
          <el-input v-model="form.jobName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务分组">
          <el-input v-model="form.jobGroup" placeholder="默认 default" />
        </el-form-item>
        <el-form-item label="Bean名称" prop="beanName">
          <el-input v-model="form.beanName" placeholder="如 logisticsScheduler" />
        </el-form-item>
        <el-form-item label="方法名称" prop="methodName">
          <el-input v-model="form.methodName" placeholder="如 advanceLogisticsStatus" />
        </el-form-item>
        <el-form-item label="参数">
          <el-input v-model="form.params" type="textarea" :rows="2" placeholder='JSON格式，如 {"key":"value"}' />
        </el-form-item>
        <el-form-item label="调度方式" prop="scheduleType">
          <el-radio-group v-model="form.scheduleType">
            <el-radio-button label="cron">Cron表达式</el-radio-button>
            <el-radio-button label="fixedDelay">固定延迟</el-radio-button>
            <el-radio-button label="fixedRate">固定频率</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.scheduleType === 'cron'" label="Cron表达式" prop="cronExpression">
          <el-input v-model="form.cronExpression" placeholder="如 0 0 2 * * ?（每天凌晨2点）" style="width: 320px" />
          <el-tooltip
            placement="top"
            content="常用示例：0 0 * * * ? 每小时 / 0 0 2 * * ? 每天2点 / 0 */5 * * * ? 每5分钟 / 0 0 0 ? * MON 每周一"
          >
            <el-icon class="cron-tip"><QuestionFilled /></el-icon>
          </el-tooltip>
        </el-form-item>
        <el-form-item v-if="form.scheduleType === 'fixedDelay'" label="固定延迟(ms)" prop="fixedDelay">
          <el-input-number v-model="form.fixedDelay" :min="1000" :step="1000" style="width: 220px" />
          <span class="form-tip">上一次执行结束后间隔多少毫秒再次执行</span>
        </el-form-item>
        <el-form-item v-if="form.scheduleType === 'fixedRate'" label="固定频率(ms)" prop="fixedRate">
          <el-input-number v-model="form.fixedRate" :min="1000" :step="1000" style="width: 220px" />
          <span class="form-tip">每隔多少毫秒执行一次</span>
        </el-form-item>
        <el-form-item label="任务描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入任务描述" />
        </el-form-item>
        <el-form-item label="允许并发">
          <el-switch v-model="form.concurrent" :active-value="1" :inactive-value="0" />
          <span class="form-tip">开启后允许任务并发执行</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getScheduleJobs,
  getScheduleGroups,
  getScheduleJobDetail,
  createScheduleJob,
  updateScheduleJob,
  deleteScheduleJob,
  pauseScheduleJob,
  resumeScheduleJob,
  executeScheduleJob,
  type ScheduleJob,
  type ScheduleJobRequest,
} from '@/api/modules/schedule'

const router = useRouter()

const list = ref<ScheduleJob[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const filter = reactive({ keyword: '', group: '' })
const groups = ref<string[]>([])

const statusTagType: Record<string, string> = {
  ACTIVE: 'success',
  PAUSED: 'info',
  ERROR: 'danger',
}

const lastRunTagType: Record<string, string> = {
  SUCCESS: 'success',
  FAIL: 'danger',
}

const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref<number | null>(null)
const submitting = ref(false)
const formRef = ref<FormInstance>()

const detailVisible = ref(false)
const detail = ref<ScheduleJob | null>(null)
const detailLoading = ref(false)

type ScheduleType = 'cron' | 'fixedDelay' | 'fixedRate'

const form = reactive({
  jobName: '',
  jobGroup: 'default',
  beanName: '',
  methodName: '',
  params: '',
  scheduleType: 'cron' as ScheduleType,
  cronExpression: '',
  fixedDelay: undefined as number | undefined,
  fixedRate: undefined as number | undefined,
  description: '',
  concurrent: 0,
})

const rules: FormRules = {
  jobName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
  beanName: [{ required: true, message: '请输入Bean名称', trigger: 'blur' }],
  methodName: [{ required: true, message: '请输入方法名称', trigger: 'blur' }],
  scheduleType: [{ required: true, message: '请选择调度方式', trigger: 'change' }],
  cronExpression: [{ required: true, message: '请输入Cron表达式', trigger: 'blur' }],
  fixedDelay: [{ required: true, message: '请输入固定延迟毫秒数', trigger: 'blur' }],
  fixedRate: [{ required: true, message: '请输入固定频率毫秒数', trigger: 'blur' }],
}

function resetForm() {
  form.jobName = ''
  form.jobGroup = 'default'
  form.beanName = ''
  form.methodName = ''
  form.params = ''
  form.scheduleType = 'cron'
  form.cronExpression = ''
  form.fixedDelay = undefined
  form.fixedRate = undefined
  form.description = ''
  form.concurrent = 0
}

function fillFormFromJob(job: ScheduleJob) {
  form.jobName = job.jobName
  form.jobGroup = job.jobGroup || 'default'
  form.beanName = job.beanName
  form.methodName = job.methodName
  form.params = job.params || ''
  form.cronExpression = job.cronExpression || ''
  form.fixedDelay = job.fixedDelay ?? undefined
  form.fixedRate = job.fixedRate ?? undefined
  if (job.cronExpression) {
    form.scheduleType = 'cron'
  } else if (job.fixedDelay != null) {
    form.scheduleType = 'fixedDelay'
  } else if (job.fixedRate != null) {
    form.scheduleType = 'fixedRate'
  } else {
    form.scheduleType = 'cron'
  }
  form.description = job.description || ''
  form.concurrent = job.concurrent ?? 0
}

function handleAdd() {
  resetForm()
  isEdit.value = false
  editingId.value = null
  dialogVisible.value = true
}

async function handleEdit(row: ScheduleJob) {
  resetForm()
  isEdit.value = true
  editingId.value = row.id
  try {
    const data = await getScheduleJobDetail(row.id)
    fillFormFromJob(data)
  } catch {
    fillFormFromJob(row)
  }
  dialogVisible.value = true
}

function buildRequest(): ScheduleJobRequest {
  const data: ScheduleJobRequest = {
    jobName: form.jobName,
    jobGroup: form.jobGroup || 'default',
    beanName: form.beanName,
    methodName: form.methodName,
    params: form.params || undefined,
    description: form.description || undefined,
    concurrent: form.concurrent,
  }
  if (form.scheduleType === 'cron') {
    data.cronExpression = form.cronExpression
  } else if (form.scheduleType === 'fixedDelay') {
    data.fixedDelay = form.fixedDelay
  } else if (form.scheduleType === 'fixedRate') {
    data.fixedRate = form.fixedRate
  }
  return data
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      const req = buildRequest()
      if (isEdit.value && editingId.value != null) {
        await updateScheduleJob(editingId.value, req)
        ElMessage.success('更新成功')
      } else {
        await createScheduleJob(req)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadList()
    } catch {
      ElMessage.error('操作失败')
    } finally {
      submitting.value = false
    }
  })
}

async function handleDetail(row: ScheduleJob) {
  detailVisible.value = true
  detail.value = null
  detailLoading.value = true
  try {
    detail.value = await getScheduleJobDetail(row.id)
  } catch {
    detail.value = row
  } finally {
    detailLoading.value = false
  }
}

function goLogs() {
  if (detail.value) {
    const id = detail.value.id
    detailVisible.value = false
    router.push(`/scheduler/${id}/logs`)
  }
}

async function handlePause(row: ScheduleJob) {
  try {
    await ElMessageBox.confirm(`确认暂停任务"${row.jobName}"吗？`, '确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await pauseScheduleJob(row.id)
    ElMessage.success('已暂停')
    loadList()
  } catch (e: unknown) {
    if (e !== 'cancel') ElMessage.error('暂停失败')
  }
}

async function handleResume(row: ScheduleJob) {
  try {
    await ElMessageBox.confirm(`确认恢复任务"${row.jobName}"吗？`, '确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await resumeScheduleJob(row.id)
    ElMessage.success('已恢复')
    loadList()
  } catch (e: unknown) {
    if (e !== 'cancel') ElMessage.error('恢复失败')
  }
}

async function handleExecute(row: ScheduleJob) {
  try {
    await ElMessageBox.confirm('确认立即执行该任务？', '确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await executeScheduleJob(row.id)
    ElMessage.success('已触发执行')
    loadList()
  } catch (e: unknown) {
    if (e !== 'cancel') ElMessage.error('执行失败')
  }
}

async function handleDelete(row: ScheduleJob) {
  try {
    await ElMessageBox.confirm(`确定删除任务"${row.jobName}"吗？`, '确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteScheduleJob(row.id)
    ElMessage.success('删除成功')
    loadList()
  } catch (e: unknown) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

function handleSearch() {
  page.value = 1
  loadList()
}

function handleReset() {
  filter.keyword = ''
  filter.group = ''
  page.value = 1
  loadList()
}

const loadList = async () => {
  loading.value = true
  try {
    const params: { page: number; size: number; keyword?: string; group?: string } = {
      page: page.value,
      size: size.value,
    }
    if (filter.keyword.trim()) params.keyword = filter.keyword.trim()
    if (filter.group) params.group = filter.group
    const data = await getScheduleJobs(params)
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

async function loadGroups() {
  try {
    const data = await getScheduleGroups()
    groups.value = data || []
  } catch {
    groups.value = []
  }
}

onMounted(() => {
  loadList()
  loadGroups()
})
</script>

<style scoped>
.schedule-manage {
  padding: var(--space-xl);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-actions {
  display: flex;
  gap: var(--space-sm);
  align-items: center;
}
.mono {
  font-family: var(--font-display, 'Helvetica Neue', monospace);
  font-size: var(--font-size-caption);
}
.form-tip {
  margin-left: var(--space-sm);
  color: var(--color-shade-50);
  font-size: var(--font-size-caption);
}
.cron-tip {
  margin-left: var(--space-sm);
  color: var(--color-shade-50);
  cursor: pointer;
  font-size: var(--font-size-heading-sm);
}
</style>
