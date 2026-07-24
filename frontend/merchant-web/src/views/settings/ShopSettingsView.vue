<template>
  <div class="shop-settings">
    <el-card>
      <template #header>店铺设置</template>
      <el-form :model="shopForm" label-width="100px" v-loading="loading">
        <el-form-item label="店铺名称">
          <el-input v-model="shopForm.shopName" />
        </el-form-item>
        <el-form-item label="Logo">
          <el-upload :http-request="uploadFile" :show-file-list="false" :on-success="handleLogoSuccess" :before-upload="beforeUpload">
            <el-image v-if="shopForm.shopLogo" :src="shopForm.shopLogo" fit="cover" style="width:80px;height:80px;border-radius:8px" />
            <el-button v-else type="primary" plain>上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="店铺描述">
          <el-input v-model="shopForm.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-divider content-position="left">发货地址</el-divider>
        <el-form-item label="发货人姓名">
          <el-input v-model="shopForm.senderName" placeholder="请输入发货人姓名" />
        </el-form-item>
        <el-form-item label="发货人电话">
          <el-input v-model="shopForm.senderPhone" placeholder="请输入发货人电话" />
        </el-form-item>
        <el-form-item label="所在地区">
          <el-cascader
            v-model="senderRegion"
            :options="regionData"
            placeholder="请选择省/市/区"
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="shopForm.senderAddress" placeholder="请输入详细地址（街道、门牌号等）" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveShop">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top:20px">
      <template #header>
        <div class="card-header">
          <span>轮播图管理</span>
          <el-button type="primary" :icon="Plus" @click="showBannerDialog = true">添加轮播图</el-button>
        </div>
      </template>
      <el-table :data="banners" border>
        <el-table-column label="图片" width="120">
          <template #default="{ row }"><el-image :src="row.imageUrl" fit="cover" style="width:80px;height:40px" /></template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="linkUrl" label="链接" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button size="small" type="danger" @click="handleRemoveBanner(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="showBannerDialog" title="添加轮播图" width="500px">
      <el-form :model="bannerForm" label-width="80px">
        <el-form-item label="图片">
          <el-upload :http-request="uploadFile" :show-file-list="false" :on-success="handleBannerSuccess" :before-upload="beforeUpload">
            <el-image v-if="bannerForm.imageUrl" :src="bannerForm.imageUrl" fit="cover" style="width:120px;height:60px" />
            <el-button v-else type="primary" plain>上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="排序"><el-input-number v-model="bannerForm.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="链接"><el-input v-model="bannerForm.linkUrl" placeholder="链接地址（可选）" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showBannerDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAddBanner">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, type UploadRawFile, type UploadRequestOptions } from 'element-plus'
import {
  getShopInfo,
  updateShopInfo,
  addBanner as addShopBanner,
  removeBanner as removeShopBanner,
} from '@/api/modules/merchant'
import request from '@/api/request'
import regionData from '@/data/regions'

const loading = ref(false)
const shopForm = reactive({
  shopName: '',
  shopLogo: '',
  description: '',
  senderName: '',
  senderPhone: '',
  senderProvince: '',
  senderCity: '',
  senderDistrict: '',
  senderAddress: '',
})
const senderRegion = ref<string[]>([])

// 监听级联选择器变化，同步到 shopForm
watch(senderRegion, (val) => {
  if (val && val.length >= 1) shopForm.senderProvince = val[0] || ''
  else shopForm.senderProvince = ''
  if (val && val.length >= 2) shopForm.senderCity = val[1] || ''
  else shopForm.senderCity = ''
  if (val && val.length >= 3) shopForm.senderDistrict = val[2] || ''
  else shopForm.senderDistrict = ''
})

interface BannerItem {
  id: number
  imageUrl: string
  sortOrder: number
  linkUrl: string
}

interface ShopInfoResponse {
  banners?: BannerItem[]
  shopName?: string
  shopLogo?: string
  description?: string
  senderName?: string
  senderPhone?: string
  senderProvince?: string
  senderCity?: string
  senderDistrict?: string
  senderAddress?: string
}

interface UploadResult {
  url?: string
  data?: { url?: string }
}

const banners = ref<BannerItem[]>([])
const showBannerDialog = ref(false)
const bannerForm = reactive({ imageUrl: '', sortOrder: 0, linkUrl: '' })

const beforeUpload = (file: UploadRawFile) => {
  if (!file.type.startsWith('image/')) { ElMessage.error('仅支持图片文件'); return false }
  if (file.size / 1024 / 1024 > 5) { ElMessage.error('图片最大5MB'); return false }
  return true
}

const uploadFile = async (options: UploadRequestOptions) => {
  const formData = new FormData()
  formData.append('file', options.file)
  try {
    const res = await request.post('/common/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } }) as unknown as UploadResult
    const url = res?.url || res?.data?.url || ''
    if (url) {
      options.onSuccess({ url })
    } else {
      const onError = options.onError as (err: Error) => void
      onError(new Error('上传失败'))
    }
  } catch (e: unknown) {
    const onError = options.onError as (err: Error) => void
    onError(e instanceof Error ? e : new Error(String(e)))
  }
}

const handleLogoSuccess = (res: unknown) => {
  const r = res as UploadResult
  if (r.url) shopForm.shopLogo = r.url
}

const handleBannerSuccess = (res: unknown) => {
  const r = res as UploadResult
  if (r.url) bannerForm.imageUrl = r.url
}

const loadShop = async () => {
  loading.value = true
  try {
    const res = await getShopInfo() as unknown as ShopInfoResponse
    Object.assign(shopForm, res)
    // 还原省市区级联选择器的值
    if (shopForm.senderProvince || shopForm.senderCity || shopForm.senderDistrict) {
      senderRegion.value = [
        shopForm.senderProvince || '',
        shopForm.senderCity || '',
        shopForm.senderDistrict || '',
      ].filter(Boolean)
    }
  } catch (e: unknown) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  }
  loading.value = false
}

const saveShop = async () => {
  try {
    await updateShopInfo(shopForm)
    ElMessage.success('保存成功')
  } catch (e: unknown) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

const handleAddBanner = async () => {
  if (!bannerForm.imageUrl) { ElMessage.warning('请先上传图片'); return }
  try {
    await addShopBanner(bannerForm)
    ElMessage.success('添加成功')
    showBannerDialog.value = false
    bannerForm.imageUrl = ''
    bannerForm.sortOrder = 0
    bannerForm.linkUrl = ''
    loadBanners()
  } catch (e: unknown) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  }
}

const loadBanners = async () => {
  try {
    const res = await getShopInfo() as unknown as ShopInfoResponse
    banners.value = res.banners || []
  } catch {
    /* ignore */
  }
}

const handleRemoveBanner = async (row: BannerItem) => {
  try {
    await removeShopBanner(row.id)
    ElMessage.success('删除成功')
    banners.value = banners.value.filter((b) => b.id !== row.id)
  } catch (e: unknown) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  }
}

onMounted(() => { loadShop(); loadBanners() })
</script>

<style scoped lang="scss">
.shop-settings {
  padding: var(--space-xl);
  max-width: 800px;
  margin: 0 auto;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    span {
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
      font-weight: 330;
      font-size: var(--font-size-heading-lg);
      color: var(--color-ink);
    }
  }
}
</style>
