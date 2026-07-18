<template>
  <div class="category-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类管理</span>
          <el-button type="primary" :icon="Plus" @click="openDialog(null)">新增分类</el-button>
        </div>
      </template>
      <el-table :data="categoryTree" row-key="id" border default-expand-all :tree-props="{ children: 'children' }">
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="primary" @click="openDialog(null, row.id)">添加子分类</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="editingCategory.id ? '编辑分类' : '新增分类'" width="500px">
      <el-form :model="editingCategory" label-width="80px">
        <el-form-item label="分类名称"><el-input v-model="editingCategory.name" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="editingCategory.icon" placeholder="图标URL（可选）" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="editingCategory.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="editingCategory.status" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategories, createCategory, updateCategory, deleteCategory } from '@/api/modules/product'

const categoryTree = ref<any[]>([])
const dialogVisible = ref(false)
const editingCategory = reactive({ id: null as number | null, parentId: 0, name: '', icon: '', sortOrder: 0, status: 1 })

const loadTree = async () => {
  try { const res = await getCategories(); categoryTree.value = res.data || [] } catch { ElMessage.error('加载失败') }
}
const openDialog = (row: any, parentId?: number) => {
  if (row) { Object.assign(editingCategory, row) }
  else { editingCategory.id = null; editingCategory.name = ''; editingCategory.icon = ''; editingCategory.sortOrder = 0; editingCategory.status = 1; editingCategory.parentId = parentId || 0 }
  dialogVisible.value = true
}
const handleSave = async () => {
  if (!editingCategory.name) { ElMessage.warning('请输入分类名称'); return }
  try {
    if (editingCategory.id) { await updateCategory(editingCategory.id, editingCategory) } else { await createCategory(editingCategory) }
    ElMessage.success('保存成功'); dialogVisible.value = false; loadTree()
  } catch (e: any) { ElMessage.error('保存失败') }
}
const handleDelete = async (row: any) => {
  try { await ElMessageBox.confirm('确认删除该分类？', '确认', { type: 'warning' }); await deleteCategory(row.id); ElMessage.success('删除成功'); loadTree() } catch (e: any) { if (e !== 'cancel') ElMessage.error('删除失败') }
}
onMounted(loadTree)
</script>

<style scoped>
.category-manage { padding: var(--space-xl); }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>