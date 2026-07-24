<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUsers, disableUser, enableUser } from '@/api/modules/user'
import type { UserItem } from '@/api/modules/user'

const loading = ref(false)
const userList = ref<UserItem[]>([])
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)

const statusMap: Record<string, string> = {
  ACTIVE: '正常',
  DISABLED: '已禁用',
}

const statusLabel = (s: string) => statusMap[s] || s

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getUsers({ page: page.value, size: pageSize.value })
    userList.value = res.list || []
    total.value = res.total || 0
  } catch {
    userList.value = []
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
  loadData()
}

async function handleDisable(row: UserItem) {
  try {
    await ElMessageBox.confirm(`确认禁用用户"${row.username}"？`, '确认', { type: 'warning' })
    await disableUser(row.userId)
    ElMessage.success('已禁用')
    loadData()
  } catch (e: unknown) {
    if (!(typeof e === 'string' && e === 'cancel')) ElMessage.error('操作失败')
  }
}

async function handleEnable(row: UserItem) {
  try {
    await ElMessageBox.confirm(`确认启用用户"${row.username}"？`, '确认', { type: 'success' })
    await enableUser(row.userId)
    ElMessage.success('已启用')
    loadData()
  } catch (e: unknown) {
    if (!(typeof e === 'string' && e === 'cancel')) ElMessage.error('操作失败')
  }
}
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <span class="card-title">用户管理</span>
    </template>
    <el-table :data="userList" v-loading="loading" style="width: 100%">
      <el-table-column label="ID" prop="userId" width="80" />
      <el-table-column label="用户名" prop="username" width="150" />
      <el-table-column label="昵称" prop="nickname" width="150" />
      <el-table-column label="手机号" prop="phone" width="130" />
      <el-table-column label="角色" prop="role" width="100" />
      <el-table-column label="状态" prop="status" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'danger'">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" prop="createdAt" width="180" />
      <el-table-column label="操作" width="150" align="center">
        <template #default="{ row }">
          <el-button v-if="row.status === 'ACTIVE'" link type="warning" @click="handleDisable(row)">禁用</el-button>
          <el-button v-if="row.status === 'DISABLED'" link type="success" @click="handleEnable(row)">启用</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && userList.length === 0" description="暂无用户" />
    <div v-if="total > pageSize" style="margin-top: 16px; display: flex; justify-content: center;">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.card-title {
  font-size: var(--font-size-body-md);
  font-weight: 600;
}
</style>