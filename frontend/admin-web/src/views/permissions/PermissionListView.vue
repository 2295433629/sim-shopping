<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getPermissions,
  createPermission,
  updatePermission,
  deletePermission,
} from '@/api/modules/system'
import type { PermissionItem } from '@/api/modules/system'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增权限')
const isEdit = ref(false)
const formRef = ref()
const list = ref<PermissionItem[]>([])

const form = ref<Partial<PermissionItem>>({
  permissionName: '',
  permissionCode: '',
  permissionType: 1,
  module: '',
  description: '',
})

const rules = {
  permissionName: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  permissionCode: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
  permissionType: [{ required: true, message: '请选择权限类型', trigger: 'change' }],
  module: [{ required: true, message: '请输入所属模块', trigger: 'blur' }],
}

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getPermissions()
    list.value = res.list || []
  } catch {
    list.value = []
  } finally {
    loading.value = false
  }
}

function openDialog(row?: PermissionItem) {
  if (row) {
    isEdit.value = true
    dialogTitle.value = '编辑权限'
    form.value = { ...row }
  } else {
    isEdit.value = false
    dialogTitle.value = '新增权限'
    form.value = {
      permissionName: '',
      permissionCode: '',
      permissionType: 1,
      module: '',
      description: '',
    }
  }
  dialogVisible.value = true
}

async function submitForm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  try {
    if (isEdit.value && form.value.id) {
      await updatePermission(form.value.id, form.value as Omit<PermissionItem, 'id' | 'createdAt'>)
      ElMessage.success('更新成功')
    } else {
      await createPermission(form.value as Omit<PermissionItem, 'id' | 'createdAt'>)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch {
    // handled by interceptor
  }
}

async function handleDelete(row: PermissionItem) {
  try {
    await ElMessageBox.confirm(`确定删除权限 "${row.permissionName}" 吗？`, '提示', { type: 'warning' })
    await deletePermission(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}
</script>

<template>
  <div class="permission-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>权限管理</span>
          <el-button type="primary" @click="openDialog()">新增权限</el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="list" stripe border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="permissionName" label="权限名称" min-width="120" />
        <el-table-column prop="permissionCode" label="权限编码" min-width="150" />
        <el-table-column prop="permissionType" label="权限类型" width="100" />
        <el-table-column prop="module" label="所属模块" width="120" />
        <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="form.permissionName" placeholder="如：用户管理" />
        </el-form-item>
        <el-form-item label="权限编码" prop="permissionCode">
          <el-input v-model="form.permissionCode" placeholder="如：user:manage" />
        </el-form-item>
        <el-form-item label="权限类型" prop="permissionType">
          <el-select v-model="form.permissionType" placeholder="请选择">
            <el-option label="菜单权限" :value="1" />
            <el-option label="按钮权限" :value="2" />
            <el-option label="API权限" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属模块" prop="module">
          <el-input v-model="form.module" placeholder="如：system" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="权限描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.permission-page {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
