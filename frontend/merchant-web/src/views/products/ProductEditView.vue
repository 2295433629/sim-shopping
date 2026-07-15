<template>
  <div class="product-edit">
    <el-card>
      <template #header>
        <span>{{ isEdit ? '编辑商品' : '新建商品' }}</span>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="商品名称" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="副标题" prop="subtitle">
          <el-input v-model="form.subtitle" placeholder="副标题" maxlength="200" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-cascader v-model="form.categoryId" :options="categoryOptions" :props="{ value: 'id', label: 'name', checkStrictly: true, emitPath: false }" placeholder="请选择分类" clearable />
        </el-form-item>
        <el-form-item label="品牌" prop="brandId">
          <el-select v-model="form.brandId" placeholder="请选择品牌" clearable>
            <el-option v-for="b in brands" :key="b.brandId" :label="b.brandName" :value="b.brandId" />
          </el-select>
        </el-form-item>
        <el-form-item label="主图" prop="mainImage">
          <el-upload action="/api/common/upload" :show-file-list="false" :on-success="handleMainImageSuccess" :before-upload="beforeUpload">
            <el-image v-if="form.mainImage" :src="form.mainImage" fit="cover" style="width:120px;height:120px;border-radius:8px" />
            <el-button v-else type="primary" plain>上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品图片" prop="images">
          <el-upload action="/api/common/upload" list-type="picture-card" :file-list="imageFileList" :on-success="handleImageSuccess" :on-remove="handleImageRemove" :before-upload="beforeUpload">
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :precision="2" :min="0" :step="10" />
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input-number v-model="form.originalPrice" :precision="2" :min="0" :step="10" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" :step="10" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="form.description" type="textarea" :rows="5" placeholder="商品描述（支持HTML）" />
        </el-form-item>
        <el-form-item label="规格">
          <div v-for="(sku, index) in form.skus" :key="index" class="sku-row">
            <el-input v-model="sku.skuName" placeholder="规格名称" style="width:200px" />
            <el-input-number v-model="sku.price" :precision="2" :min="0" placeholder="价格" />
            <el-input-number v-model="sku.stock" :min="0" placeholder="库存" />
            <el-button type="danger" :icon="Delete" circle @click="removeSku(index)" />
          </div>
          <el-button type="primary" plain :icon="Plus" @click="addSku">添加规格</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(true)">发布</el-button>
          <el-button @click="submitForm(false)">保存草稿</el-button>
          <el-button @click="router.back()">返回</el-button>
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
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  mainImage: [{ required: true, message: '请上传主图', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}

const addSku = () => { form.skus.push({ skuName: '', price: form.price, stock: 0 }) }
const removeSku = (i: number) => { form.skus.splice(i, 1) }

const beforeUpload = (file: File) => {
  if (!file.type.startsWith('image/')) { ElMessage.error('仅支持图片文件'); return false }
  if (file.size / 1024 / 1024 > 5) { ElMessage.error('图片最大5MB'); return false }
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
  try {
    const valid = await formRef.value.validate()
    if (!valid) return
  } catch {
    return
  }
  form.publish = publish
  try {
    if (isEdit.value) { await updateProduct(Number(route.params.id), form); ElMessage.success('更新成功') }
    else { await createProduct(form); ElMessage.success(publish ? '发布成功' : '已保存为草稿') }
    router.push('/products')
  } catch (e: any) { ElMessage.error(e?.message || '操作失败') }
}

onMounted(async () => {
  try {
    const [catRes, brandRes] = await Promise.all([getCategories(), getBrands()])
    categoryOptions.value = catRes || []
    brands.value = brandRes || []
  } catch (e) { /* ignore */ }
  if (route.params.id) {
    isEdit.value = true
    try {
      const res = await getProductDetail(Number(route.params.id))
      Object.assign(form, res)
      if (res.images) { imageFileList.value = res.images.map((url: string, i: number) => ({ name: 'img-' + i, url })) }
    } catch (e: any) { ElMessage.error(e?.message || '加载失败') }
  }
})
</script>

<style scoped lang="scss">
.product-edit {
  max-width: 900px;
  margin: 0 auto;
  padding: var(--space-xl);

  .sku-row {
    display: flex;
    gap: var(--space-md);
    margin-bottom: var(--space-md);
    align-items: center;
  }
}
</style>