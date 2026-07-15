<template>
  <div class="category-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Categories</span>
          <el-button type="primary" :icon="Plus" @click="openDialog(null)">New Category</el-button>
        </div>
      </template>
      <el-table :data="categoryTree" row-key="id" border default-expand-all :tree-props="{ children: 'children' }">
        <el-table-column prop="name" label="Name" />
        <el-table-column prop="sortOrder" label="Sort" width="80" />
        <el-table-column prop="status" label="Status" width="80">
          <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? 'Active' : 'Disabled' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="Actions" width="250">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">Edit</el-button>
            <el-button size="small" type="primary" @click="openDialog(null, row.id)">Add Child</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="editingCategory.id ? 'Edit Category' : 'New Category'" width="500px">
      <el-form :model="editingCategory" label-width="80px">
        <el-form-item label="Name"><el-input v-model="editingCategory.name" /></el-form-item>
        <el-form-item label="Icon"><el-input v-model="editingCategory.icon" placeholder="Icon URL (optional)" /></el-form-item>
        <el-form-item label="Sort"><el-input-number v-model="editingCategory.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="Status"><el-switch v-model="editingCategory.status" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSave">Save</el-button>
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
  try { const res = await getCategories(); categoryTree.value = res.data || [] } catch { ElMessage.error('Load failed') }
}
const openDialog = (row: any, parentId?: number) => {
  if (row) { Object.assign(editingCategory, row) }
  else { editingCategory.id = null; editingCategory.name = ''; editingCategory.icon = ''; editingCategory.sortOrder = 0; editingCategory.status = 1; editingCategory.parentId = parentId || 0 }
  dialogVisible.value = true
}
const handleSave = async () => {
  if (!editingCategory.name) { ElMessage.warning('Name required'); return }
  try {
    if (editingCategory.id) { await updateCategory(editingCategory.id, editingCategory) } else { await createCategory(editingCategory) }
    ElMessage.success('Saved'); dialogVisible.value = false; loadTree()
  } catch (e: any) { ElMessage.error('Save failed') }
}
const handleDelete = async (row: any) => {
  try { await ElMessageBox.confirm('Delete category "' + row.name + '"?', 'Confirm', { type: 'warning' }); await deleteCategory(row.id); ElMessage.success('Deleted'); loadTree() } catch (e: any) { if (e !== 'cancel') ElMessage.error('Delete failed') }
}
onMounted(loadTree)
</script>

<style scoped>
.category-manage { padding: var(--space-xl); }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>