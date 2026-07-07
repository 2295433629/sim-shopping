<template>
  <div class="brand-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Brands</span>
          <el-button type="primary" :icon="Plus" @click="openDialog(null)">New Brand</el-button>
        </div>
      </template>
      <el-table :data="brandList" border>
        <el-table-column prop="brandId" label="ID" width="60" />
        <el-table-column prop="brandName" label="Name" />
        <el-table-column label="Logo" width="100">
          <template #default="{ row }"><el-image v-if="row.brandLogo" :src="row.brandLogo" fit="cover" style="width:40px;height:40px" /><span v-else>-</span></template>
        </el-table-column>
        <el-table-column prop="brandDescription" label="Description" show-overflow-tooltip />
        <el-table-column label="Actions" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">Edit</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="editingBrand.brandId ? 'Edit Brand' : 'New Brand'" width="500px">
      <el-form :model="editingBrand" label-width="80px">
        <el-form-item label="Name"><el-input v-model="editingBrand.brandName" /></el-form-item>
        <el-form-item label="Logo">
          <el-upload action="/api/common/upload" :show-file-list="false" :on-success="handleLogoSuccess" :before-upload="beforeUpload">
            <el-image v-if="editingBrand.brandLogo" :src="editingBrand.brandLogo" fit="cover" style="width:80px;height:80px;border-radius:8px" />
            <el-button v-else type="primary" plain>Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Description"><el-input v-model="editingBrand.brandDescription" type="textarea" :rows="3" /></el-form-item>
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
import { getBrands, createBrand, updateBrand, deleteBrand } from '@/api/modules/product'

const brandList = ref<any[]>([])
const dialogVisible = ref(false)
const editingBrand = reactive({ brandId: null as number | null, brandName: '', brandLogo: '', brandDescription: '' })

const beforeUpload = (file: File) => { if (!file.type.startsWith('image/')) { ElMessage.error('Image only'); return false } if (file.size / 1024 / 1024 > 5) { ElMessage.error('Max 5MB'); return false } return true }
const handleLogoSuccess = (res: any) => { if (res.code === 200) editingBrand.brandLogo = res.data }
const loadList = async () => { try { const res = await getBrands(); brandList.value = res.data || [] } catch { ElMessage.error('Load failed') } }
const openDialog = (row: any) => { if (row) { Object.assign(editingBrand, row) } else { editingBrand.brandId = null; editingBrand.brandName = ''; editingBrand.brandLogo = ''; editingBrand.brandDescription = '' } dialogVisible.value = true }
const handleSave = async () => {
  if (!editingBrand.brandName) { ElMessage.warning('Name required'); return }
  try { if (editingBrand.brandId) { await updateBrand(editingBrand.brandId, editingBrand) } else { await createBrand(editingBrand) } ElMessage.success('Saved'); dialogVisible.value = false; loadList() } catch { ElMessage.error('Save failed') }
}
const handleDelete = async (row: any) => {
  try { await ElMessageBox.confirm('Delete brand "' + row.brandName + '"?', 'Confirm', { type: 'warning' }); await deleteBrand(row.brandId); ElMessage.success('Deleted'); loadList() } catch (e: any) { if (e !== 'cancel') ElMessage.error('Delete failed') }
}
onMounted(loadList)
</script>

<style scoped>
.brand-manage { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>