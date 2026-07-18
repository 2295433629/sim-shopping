<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getMenus, createMenu, updateMenu, deleteMenu } from '@/api/modules/system'
import type { MenuItem } from '@/api/modules/system'

// 菜单类型选项
const menuTypeOptions = [
  { label: '目录', value: 'directory' },
  { label: '菜单', value: 'menu' },
  { label: '按钮', value: 'button' }
]

// 菜单数据
const loading = ref(false)
const menuList = ref<MenuItem[]>([])

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('新增菜单')
const isEdit = ref(false)
const editingId = ref<number | null>(null)
const formRef = ref<FormInstance>()

// 表单数据
const form = reactive({
  menuName: '',
  parentId: 0,
  menuType: 'menu',
  path: '',
  component: '',
  icon: '',
  sortOrder: 0,
  visible: 1
})

// 表单校验规则
const rules = reactive<FormRules>({
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  menuType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }]
})

// 树形选择器数据（顶部增加一个"顶级菜单"选项）
const treeSelectData = computed(() => {
  return [
    { id: 0, menuName: '顶级菜单', children: menuList.value }
  ]
})

// 重置表单
function resetForm() {
  form.menuName = ''
  form.parentId = 0
  form.menuType = 'menu'
  form.path = ''
  form.component = ''
  form.icon = ''
  form.sortOrder = 0
  form.visible = 1
  isEdit.value = false
  editingId.value = null
}

// 新增菜单
function handleAdd() {
  resetForm()
  dialogTitle.value = '新增菜单'
  dialogVisible.value = true
}

// 新增子菜单
function handleAddChild(row: MenuItem) {
  resetForm()
  dialogTitle.value = '新增子菜单'
  form.parentId = row.id
  dialogVisible.value = true
}

// 编辑菜单
function handleEdit(row: MenuItem) {
  resetForm()
  dialogTitle.value = '编辑菜单'
  isEdit.value = true
  editingId.value = row.id
  form.menuName = row.menuName
  form.parentId = row.parentId
  form.menuType = row.menuType
  form.path = row.path || ''
  form.component = row.component || ''
  form.icon = row.icon || ''
  form.sortOrder = row.sortOrder
  form.visible = row.visible
  dialogVisible.value = true
}

// 删除菜单
async function handleDelete(row: MenuItem) {
  try {
    await ElMessageBox.confirm(`确定要删除菜单「${row.menuName}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteMenu(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // 取消删除，不做处理
  }
}

// 提交表单
async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const data = {
        menuName: form.menuName,
        parentId: form.parentId,
        menuType: form.menuType,
        path: form.path || undefined,
        component: form.component || undefined,
        icon: form.icon || undefined,
        sortOrder: form.sortOrder,
        visible: form.visible
      }
      if (isEdit.value && editingId.value !== null) {
        await updateMenu(editingId.value, data)
        ElMessage.success('编辑成功')
      } else {
        await createMenu(data)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      loadData()
    } catch {
      ElMessage.error('操作失败，请重试')
    }
  })
}

// 关闭对话框时重置表单
function handleDialogClose() {
  formRef.value?.resetFields()
  resetForm()
}

// 加载菜单数据
async function loadData() {
  loading.value = true
  try {
    const res = await getMenus()
    menuList.value = res || []
  } catch {
    menuList.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <div class="card-header">
        <span class="card-title">菜单管理</span>
        <el-button type="primary" @click="handleAdd">新增菜单</el-button>
      </div>
    </template>

    <el-table
      :data="menuList"
      v-loading="loading"
      row-key="id"
      default-expand-all
      style="width: 100%"
    >
      <el-table-column label="菜单名称" prop="menuName" min-width="200" />
      <el-table-column label="路径" prop="path" width="200" />
      <el-table-column label="组件" prop="component" width="180" />
      <el-table-column label="图标" prop="icon" width="100" />
      <el-table-column label="排序" prop="sortOrder" width="80" />
      <el-table-column label="可见" prop="visible" width="80">
        <template #default="{ row }">
          <el-tag :type="row.visible === 1 ? 'success' : 'info'" size="small">
            {{ row.visible === 1 ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="260" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="primary" @click="handleAddChild(row)">新增子菜单</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!loading && menuList.length === 0" description="暂无菜单" />
  </el-card>

  <!-- 新增/编辑弹窗 -->
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="520px"
    @close="handleDialogClose"
    destroy-on-close
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
      </el-form-item>

      <el-form-item label="父级菜单" prop="parentId">
        <el-tree-select
          v-model="form.parentId"
          :data="treeSelectData"
          :props="{ label: 'menuName', value: 'id', children: 'children' }"
          check-strictly
          :render-after-expand="false"
          placeholder="请选择父级菜单"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="菜单类型" prop="menuType">
        <el-radio-group v-model="form.menuType">
          <el-radio
            v-for="item in menuTypeOptions"
            :key="item.value"
            :value="item.value"
          >
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="路径" prop="path">
        <el-input v-model="form.path" placeholder="请输入路径" />
      </el-form-item>

      <el-form-item label="组件" prop="component">
        <el-input v-model="form.component" placeholder="请输入组件路径" />
      </el-form-item>

      <el-form-item label="图标" prop="icon">
        <el-input v-model="form.icon" placeholder="请输入图标" />
      </el-form-item>

      <el-form-item label="排序" prop="sortOrder">
        <el-input-number v-model="form.sortOrder" :min="0" controls-position="right" />
      </el-form-item>

      <el-form-item label="是否可见" prop="visible">
        <el-switch
          v-model="form.visible"
          :active-value="1"
          :inactive-value="0"
          active-text="显示"
          inactive-text="隐藏"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
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
