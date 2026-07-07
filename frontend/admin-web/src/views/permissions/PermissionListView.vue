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
  resourceType: 'api',
  httpMethod: 'GET',
  httpPath: '',
  description: '',
})

const rules = {
  permissionName: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  permissionCode: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
  resourceType: [{ required: true, message: '请选择资源类型', trigger: 'change' }],
  httpMethod: [{ required: true, message: '请选择HTTP方法', trigger: 'change' }],
  httpPath: [{ required: true, message: '请输入请求路径', trigger: 'blur' }],
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
      resourceType: 'api',
      httpMethod: 'GET',
      httpPath: '',
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
      await updatePermission(form.value.id, form.value as Omit<PermissionItem, 'id'>)
      ElMessage.success('更新成功')
    } else {
      await createPermission(form.value as Omit<PermissionItem, 'id'>)
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
        <el-table-column prop="resourceType" label="资源类型" width="100" />
        <el-table-column prop="httpMethod" label="方法" width="80" />
        <el-table-column prop="httpPath" label="路径" min-width="180" />
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
        <el-form-item label="资源类型" prop="resourceType">
          <el-select v-model="form.resourceType" placeholder="请选择">
            <el-option label="API接口" value="api" />
            <el-option label="菜单" value="menu" />
            <el-option label="按钮" value="button" />
          </el-select>
        </el-form-item>
        <el-form-item label="HTTP方法" prop="httpMethod">
          <el-select v-model="form.httpMethod" placeholder="请选择">
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
          </el-select>
        </el-form-item>
        <el-form-item label="请求路径" prop="httpPath">
          <el-input v-model="form.httpPath" placeholder="如：/api/admin/users" />
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
