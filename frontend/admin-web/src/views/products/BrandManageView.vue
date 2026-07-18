<template>
  <div class="brand-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>品牌管理</span>
          <el-button type="primary" :icon="Plus" @click="openDialog(null)">新增品牌</el-button>
        </div>
      </template>
      <el-table :data="brandList" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="品牌名称" />
        <el-table-column label="Logo" width="100">
          <template #default="{ row }"><el-image v-if="row.logo" :src="row.logo" fit="cover" style="width:40px;height:40px" /><span v-else>-</span></template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="editingBrand.id ? '编辑品牌' : '新增品牌'" width="500px">
      <el-form :model="editingBrand" label-width="80px">
        <el-form-item label="品牌名称"><el-input v-model="editingBrand.name" /></el-form-item>
        <el-form-item label="Logo">
          <el-upload action="/api/common/upload" :show-file-list="false" :on-success="handleLogoSuccess" :before-upload="beforeUpload">
            <el-image v-if="editingBrand.logo" :src="editingBrand.logo" fit="cover" style="width:80px;height:80px;border-radius:8px" />
            <el-button v-else type="primary" plain>上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="editingBrand.description" type="textarea" :rows="3" /></el-form-item>
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
import { getBrands, createBrand, updateBrand, deleteBrand } from '@/api/modules/product'

const brandList = ref<any[]>([])
const dialogVisible = ref(false)
const editingBrand = reactive({ id: null as number | null, name: '', logo: '', description: '' })

const beforeUpload = (file: File) => { if (!file.type.startsWith('image/')) { ElMessage.error('仅支持图片格式'); return false } if (file.size / 1024 / 1024 > 5) { ElMessage.error('最大5MB'); return false } return true }
const handleLogoSuccess = (res: any) => { if (res && res.url) editingBrand.logo = res.url }
const loadList = async () => { try { const res = await getBrands(); brandList.value = res.data || [] } catch { ElMessage.error('加载失败') } }
const openDialog = (row: any) => { if (row) { Object.assign(editingBrand, row) } else { editingBrand.id = null; editingBrand.name = ''; editingBrand.logo = ''; editingBrand.description = '' } dialogVisible.value = true }
const handleSave = async () => {
  if (!editingBrand.name) { ElMessage.warning('请输入品牌名称'); return }
  try { if (editingBrand.id) { await updateBrand(editingBrand.id, editingBrand) } else { await createBrand(editingBrand) } ElMessage.success('保存成功'); dialogVisible.value = false; loadList() } catch { ElMessage.error('保存失败') }
}
const handleDelete = async (row: any) => {
  try { await ElMessageBox.confirm('确认删除该品牌？', '确认', { type: 'warning' }); await deleteBrand(row.id); ElMessage.success('删除成功'); loadList() } catch (e: any) { if (e !== 'cancel') ElMessage.error('删除失败') }
}
onMounted(loadList)
</script>

<style scoped>
.brand-manage { padding: var(--space-xl); }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>