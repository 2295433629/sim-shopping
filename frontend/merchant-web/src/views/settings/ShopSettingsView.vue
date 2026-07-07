<template>
  <div class="shop-settings">
    <el-card>
      <template #header>Shop Settings</template>
      <el-form :model="shopForm" label-width="100px" v-loading="loading">
        <el-form-item label="Shop Name">
          <el-input v-model="shopForm.shopName" />
        </el-form-item>
        <el-form-item label="Logo">
          <el-upload action="/api/common/upload" :show-file-list="false" :on-success="handleLogoSuccess" :before-upload="beforeUpload">
            <el-image v-if="shopForm.shopLogo" :src="shopForm.shopLogo" fit="cover" style="width:80px;height:80px;border-radius:8px" />
            <el-button v-else type="primary" plain>Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="shopForm.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveShop">Save</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top:20px">
      <template #header>
        <div class="card-header">
          <span>Banners</span>
          <el-button type="primary" :icon="Plus" @click="showBannerDialog = true">Add Banner</el-button>
        </div>
      </template>
      <el-table :data="banners" border>
        <el-table-column label="Image" width="120">
          <template #default="{ row }"><el-image :src="row.imageUrl" fit="cover" style="width:80px;height:40px" /></template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="Sort" width="80" />
        <el-table-column prop="linkUrl" label="Link" />
        <el-table-column label="Actions" width="100">
          <template #default="{ row }"
            ><el-button size="small" type="danger" @click="handleRemoveBanner(row)">Delete</el-button></template
          >
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="showBannerDialog" title="Add Banner" width="500px">
      <el-form :model="bannerForm" label-width="80px">
        <el-form-item label="Image">
          <el-upload action="/api/common/upload" :show-file-list="false" :on-success="handleBannerSuccess" :before-upload="beforeUpload">
            <el-image v-if="bannerForm.imageUrl" :src="bannerForm.imageUrl" fit="cover" style="width:120px;height:60px" />
            <el-button v-else type="primary" plain>Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Sort"><el-input-number v-model="bannerForm.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="Link"><el-input v-model="bannerForm.linkUrl" placeholder="Link URL (optional)" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showBannerDialog = false">Cancel</el-button>
        <el-button type="primary" @click="handleAddBanner">OK</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import {
  getShopInfo,
  updateShopInfo,
  addBanner as addShopBanner,
  removeBanner as removeShopBanner,
} from '@/api/modules/merchant'

const loading = ref(false)
const shopForm = reactive({ shopName: '', shopLogo: '', description: '' })
const banners = ref<any[]>([])
const showBannerDialog = ref(false)
const bannerForm = reactive({ imageUrl: '', sortOrder: 0, linkUrl: '' })

const beforeUpload = (file: File) => {
  if (!file.type.startsWith('image/')) { ElMessage.error('Image only'); return false }
  if (file.size / 1024 / 1024 > 5) { ElMessage.error('Max 5MB'); return false }
  return true
}
const handleLogoSuccess = (res: any) => { if (res.code === 200) shopForm.shopLogo = res.data }
const handleBannerSuccess = (res: any) => { if (res.code === 200) bannerForm.imageUrl = res.data }

const loadShop = async () => {
  loading.value = true
  try { const res = await getShopInfo(); Object.assign(shopForm, res.data) } catch { ElMessage.error('Load failed') }
  loading.value = false
}
const saveShop = async () => {
  try { await updateShopInfo(shopForm); ElMessage.success('Saved') } catch { ElMessage.error('Save failed') }
}
const handleAddBanner = async () => {
  if (!bannerForm.imageUrl) { ElMessage.warning('Upload image first'); return }
  try {
    await addShopBanner(bannerForm)
    ElMessage.success('Added')
    showBannerDialog.value = false
    bannerForm.imageUrl = ''
    bannerForm.sortOrder = 0
    bannerForm.linkUrl = ''
  } catch {
    ElMessage.error('Failed')
  }
}
const handleRemoveBanner = async (row: any) => {
  try {
    await removeShopBanner(row.id)
    ElMessage.success('Deleted')
    banners.value = banners.value.filter((b) => b.id !== row.id)
  } catch {
    ElMessage.error('Failed')
  }
}
onMounted(loadShop)
</script>

<style scoped>
.shop-settings { padding: 20px; max-width: 800px; margin: 0 auto; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
