<template>
  <div class="product-edit">
    <el-card>
      <template #header>
        <span>{{ isEdit ? 'Edit Product' : 'New Product' }}</span>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" placeholder="Product name" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="Subtitle" prop="subtitle">
          <el-input v-model="form.subtitle" placeholder="Subtitle" maxlength="200" />
        </el-form-item>
        <el-form-item label="Category" prop="categoryId">
          <el-cascader v-model="form.categoryId" :options="categoryOptions" :props="{ value: 'id', label: 'name', checkStrictly: true, emitPath: false }" placeholder="Select category" clearable />
        </el-form-item>
        <el-form-item label="Brand" prop="brandId">
          <el-select v-model="form.brandId" placeholder="Select brand" clearable>
            <el-option v-for="b in brands" :key="b.brandId" :label="b.brandName" :value="b.brandId" />
          </el-select>
        </el-form-item>
        <el-form-item label="Main Image" prop="mainImage">
          <el-upload action="/api/common/upload" :show-file-list="false" :on-success="handleMainImageSuccess" :before-upload="beforeUpload">
            <el-image v-if="form.mainImage" :src="form.mainImage" fit="cover" style="width:120px;height:120px;border-radius:8px" />
            <el-button v-else type="primary" plain>Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Images" prop="images">
          <el-upload action="/api/common/upload" list-type="picture-card" :file-list="imageFileList" :on-success="handleImageSuccess" :on-remove="handleImageRemove" :before-upload="beforeUpload">
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="Price" prop="price">
          <el-input-number v-model="form.price" :precision="2" :min="0" :step="10" />
        </el-form-item>
        <el-form-item label="Original Price" prop="originalPrice">
          <el-input-number v-model="form.originalPrice" :precision="2" :min="0" :step="10" />
        </el-form-item>
        <el-form-item label="Stock" prop="stock">
          <el-input-number v-model="form.stock" :min="0" :step="10" />
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="form.description" type="textarea" :rows="5" placeholder="Product description (HTML supported)" />
        </el-form-item>
        <el-form-item label="SKU">
          <div v-for="(sku, index) in form.skus" :key="index" class="sku-row">
            <el-input v-model="sku.skuName" placeholder="SKU name" style="width:200px" />
            <el-input-number v-model="sku.price" :precision="2" :min="0" placeholder="Price" />
            <el-input-number v-model="sku.stock" :min="0" placeholder="Stock" />
            <el-button type="danger" :icon="Delete" circle @click="removeSku(index)" />
          </div>
          <el-button type="primary" plain :icon="Plus" @click="addSku">Add SKU</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(true)">Publish</el-button>
          <el-button @click="submitForm(false)">Save Draft</el-button>
          <el-button @click="router.back()">Back</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage, type FormInstance } from 'element-plus'
import { getCategories, getBrands, createProduct, updateProduct, getProductDetail } from '@/api/modules/product'

const route = useRoute()
const router = useRouter()
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const categoryOptions = ref<any[]>([])
const brands = ref<any[]>([])
const imageFileList = ref<any[]>([])

const form = reactive({
  name: '',
  subtitle: '',
  categoryId: null as number | null,
  brandId: null as number | null,
  mainImage: '',
  images: [] as string[],
  price: 0,
  originalPrice: 0,
  stock: 0,
  description: '',
  skus: [] as { skuName: string; price: number; stock: number }[],
  publish: false
})

const rules = {
  name: [{ required: true, message: 'Name required', trigger: 'blur' }],
  categoryId: [{ required: true, message: 'Category required', trigger: 'change' }],
  mainImage: [{ required: true, message: 'Main image required', trigger: 'change' }],
  price: [{ required: true, message: 'Price required', trigger: 'blur' }]
}

const addSku = () => { form.skus.push({ skuName: '', price: form.price, stock: 0 }) }
const removeSku = (i: number) => { form.skus.splice(i, 1) }

const beforeUpload = (file: File) => {
  if (!file.type.startsWith('image/')) { ElMessage.error('Image only'); return false }
  if (file.size / 1024 / 1024 > 5) { ElMessage.error('Max 5MB'); return false }
  return true
}

const handleMainImageSuccess = (res: any) => { if (res.code === 200) form.mainImage = res.data }
const handleImageSuccess = (res: any, file: any) => {
  if (res.code === 200) { form.images.push(res.data); imageFileList.value.push({ name: file.name, url: res.data }) }
}
const handleImageRemove = (file: any) => {
  const idx = imageFileList.value.findIndex(f => f.url === file.url)
  if (idx >= 0) { form.images.splice(idx, 1); imageFileList.value.splice(idx, 1) }
}

const submitForm = async (publish: boolean) => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    form.publish = publish
    try {
      if (isEdit.value) { await updateProduct(Number(route.params.id), form); ElMessage.success('Updated') }
      else { await createProduct(form); ElMessage.success(publish ? 'Published' : 'Saved as draft') }
      router.push('/products')
    } catch (e: any) { ElMessage.error('Failed') }
  })
}

onMounted(async () => {
  try {
    const [catRes, brandRes] = await Promise.all([getCategories(), getBrands()])
    categoryOptions.value = catRes.data || []
    brands.value = brandRes.data || []
  } catch (e) { /* ignore */ }
  if (route.params.id) {
    isEdit.value = true
    try {
      const res = await getProductDetail(Number(route.params.id))
      Object.assign(form, res.data)
      if (res.data.images) { imageFileList.value = res.data.images.map((url: string, i: number) => ({ name: 'img-' + i, url })) }
    } catch (e) { ElMessage.error('Load failed') }
  }
})
</script>

<style scoped>
.product-edit { max-width: 900px; margin: 0 auto; padding: 20px; }
.sku-row { display: flex; gap: 10px; margin-bottom: 10px; align-items: center; }
</style>