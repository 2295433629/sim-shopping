<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getRoles,
  createRole,
  updateRole,
  deleteRole,
  getPermissions,
  getRolePermissions,
  assignPermissions
} from '@/api/modules/system'
import type { RoleItem, PermissionItem } from '@/api/modules/system'

// ========== 列表相关 ==========
const loading = ref(false)
const roleList = ref<RoleItem[]>([])

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getRoles()
    roleList.value = res.list || []
  } catch {
    roleList.value = []
  } finally {
    loading.value = false
  }
}

// ========== 新增/编辑角色弹窗 ==========
const roleDialogVisible = ref(false)
const roleDialogTitle = ref('新增角色')
const roleFormRef = ref<FormInstance>()
const isEdit = ref(false)
const editingRoleId = ref<number | null>(null)

const roleForm = reactive({
  roleName: '',
  roleCode: '',
  description: ''
})

const roleFormRules: FormRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

function handleAddRole() {
  isEdit.value = false
  editingRoleId.value = null
  roleDialogTitle.value = '新增角色'
  roleForm.roleName = ''
  roleForm.roleCode = ''
  roleForm.description = ''
  roleDialogVisible.value = true
}

function handleEditRole(row: RoleItem) {
  isEdit.value = true
  editingRoleId.value = row.id
  roleDialogTitle.value = '编辑角色'
  roleForm.roleName = row.roleName
  roleForm.roleCode = row.roleCode
  roleForm.description = row.description || ''
  roleDialogVisible.value = true
}

async function handleRoleSubmit() {
  const formEl = roleFormRef.value
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (!valid) return
    try {
      if (isEdit.value && editingRoleId.value !== null) {
        await updateRole(editingRoleId.value, {
          roleName: roleForm.roleName,
          roleCode: roleForm.roleCode,
          description: roleForm.description,
          status: 'ACTIVE'
        })
        ElMessage.success('编辑成功')
      } else {
        await createRole({
          roleName: roleForm.roleName,
          roleCode: roleForm.roleCode,
          description: roleForm.description,
          status: 'ACTIVE'
        })
        ElMessage.success('新增成功')
      }
      roleDialogVisible.value = false
      loadData()
    } catch {
      ElMessage.error(isEdit.value ? '编辑失败' : '新增失败')
    }
  })
}

// ========== 删除角色 ==========
async function handleDeleteRole(row: RoleItem) {
  try {
    await ElMessageBox.confirm(`确定要删除角色「${row.roleName}」吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // 用户取消或请求失败
  }
}

// ========== 分配权限弹窗 ==========
const permDialogVisible = ref(false)
const permLoading = ref(false)
const permSubmitting = ref(false)
const currentRoleId = ref<number | null>(null)
const currentRoleName = ref('')
const allPermissions = ref<PermissionItem[]>([])
const checkedPermissionIds = ref<number[]>([])

async function handleAssignPermission(row: RoleItem) {
  currentRoleId.value = row.id
  currentRoleName.value = row.roleName
  checkedPermissionIds.value = []
  permDialogVisible.value = true
  permLoading.value = true
  try {
    const [permRes, rolePermList] = await Promise.all([
      getPermissions(1, 100),
      getRolePermissions(row.id)
    ])
    allPermissions.value = permRes?.list || []
    checkedPermissionIds.value = rolePermList?.map((p: PermissionItem) => p.id) || []
  } catch {
    allPermissions.value = []
    checkedPermissionIds.value = []
    ElMessage.error('获取权限数据失败')
  } finally {
    permLoading.value = false
  }
}

async function handlePermSubmit() {
  if (currentRoleId.value === null) return
  permSubmitting.value = true
  try {
    await assignPermissions(currentRoleId.value, checkedPermissionIds.value)
    ElMessage.success('权限分配成功')
    permDialogVisible.value = false
  } catch {
    ElMessage.error('权限分配失败')
  } finally {
    permSubmitting.value = false
  }
}
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <div class="card-header">
        <span class="card-title">角色管理</span>
        <el-button type="primary" @click="handleAddRole">新增角色</el-button>
      </div>
    </template>

    <el-table :data="roleList" v-loading="loading" style="width: 100%">
      <el-table-column label="ID" prop="id" width="80" />
      <el-table-column label="角色名称" prop="roleName" width="200" />
      <el-table-column label="角色编码" prop="roleCode" width="200" />
      <el-table-column label="描述" prop="description" min-width="200" />
      <el-table-column label="操作" width="280" align="center" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEditRole(row)">编辑</el-button>
          <el-button link type="primary" @click="handleAssignPermission(row)">分配权限</el-button>
          <el-button link type="danger" @click="handleDeleteRole(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && roleList.length === 0" description="暂无角色" />
  </el-card>

  <!-- 新增/编辑角色弹窗 -->
  <el-dialog
    v-model="roleDialogVisible"
    :title="roleDialogTitle"
    width="500px"
    destroy-on-close
  >
    <el-form
      ref="roleFormRef"
      :model="roleForm"
      :rules="roleFormRules"
      label-width="80px"
      label-position="right"
    >
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item label="角色编码" prop="roleCode">
        <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
          v-model="roleForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入描述"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="roleDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleRoleSubmit">确定</el-button>
    </template>
  </el-dialog>

  <!-- 分配权限弹窗 -->
  <el-dialog
    v-model="permDialogVisible"
    :title="`分配权限 - ${currentRoleName}`"
    width="600px"
    destroy-on-close
  >
    <div v-loading="permLoading">
      <el-checkbox-group v-model="checkedPermissionIds">
        <div v-for="perm in allPermissions" :key="perm.id" style="margin-bottom: 8px">
          <el-checkbox :label="perm.id" :value="perm.id">
            {{ perm.permissionName }}
            <span style="color: var(--el-text-color-secondary); font-size: 12px; margin-left: 4px">
              ({{ perm.permissionCode }})
            </span>
          </el-checkbox>
        </div>
      </el-checkbox-group>
      <el-empty
        v-if="!permLoading && allPermissions.length === 0"
        description="暂无权限数据"
      />
    </div>
    <template #footer>
      <el-button @click="permDialogVisible = false">取消</el-button>
      <el-button
        type="primary"
        :loading="permSubmitting"
        @click="handlePermSubmit"
      >
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 600;
  }
}
</style>