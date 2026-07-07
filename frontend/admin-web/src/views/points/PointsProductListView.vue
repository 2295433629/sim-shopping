<template>
  <div class="points-product-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>积分商品管理</span>
          <div class="header-actions">
            <el-select v-model="filter.status" placeholder="状态" clearable style="width: 120px" @change="loadList">
              <el-option label="全部" value="" />
              <el-option label="上架" value="ACTIVE" />
              <el-option label="下架" value="INACTIVE" />
            </el-select>
            <el-input v-model="filter.keyword" placeholder="搜索商品名称" clearable style="width: 200px" @keyup.enter="loadList" />
            <el-button type="primary" @click="loadList">搜索</el-button>
            <el-button type="success" @click="handleAdd">新增</el-button>
          </div>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" border stripe>
        <el-table-column prop="productId" label="ID" width="60" />
        <el-table-column label="图片" width="80">
          <template #default="{ row }">
            <el-image
              :src="row.imageUrl || defaultImage"
              fit="cover"
              style="width: 50px; height: 50px; border-radius: 4px"
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="pointsRequired" label="所需积分" width="100">
          <template #default="{ row }">
            <span style="color: #ff9900; font-weight: bold">{{ row.pointsRequired }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status]" size="small">{{ statusLabelMap[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="70" />
        <el-table-column prop="createdAt" label="创建时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && list.length === 0" description="暂无积分商品" />

      <el-pagination
        v-model:current-page="page"
        :page-size="size"
        :total="total"
        layout="prev, pager, next, total"
        @current-change="loadList"
        style="margin-top: 16px; justify-content: flex-end"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑积分商品' : '新增积分商品'" width="560px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入商品描述" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-input v-model="form.imageUrl" placeholder="请输入图片URL" />
        </el-form-item>
        <el-form-item label="所需积分" prop="pointsRequired">
          <el-input-number v-model="form.pointsRequired" :min="1" style="width: 200px" />
        </el-form-item>
        <el-form-item label="库存数量" prop="stock">
          <el-input-number v-model="form.stock" :min="0" style="width: 200px" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio-button label="ACTIVE">上架</el-radio-button>
            <el-radio-button label="INACTIVE">下架</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" style="width: 200px" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getAdminProducts,
  createAdminProduct,
  updateAdminProduct,
  deleteAdminProduct,
  type PointsProduct,
  type PointsProductFormData,
} from '@/api/modules/points'

const list = ref<PointsProduct[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const filter = reactive({ status: '', keyword: '' })
const defaultImage = 'https://via.placeholder.com/50?text=No+Image'

const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref<number | null>(null)
const submitting = ref(false)
const formRef = ref<FormInstance>()

const form = reactive<PointsProductFormData>({
  name: '',
  description: '',
  imageUrl: '',
  pointsRequired: 100,
  stock: 0,
  status: 'ACTIVE',
  sort: 0,
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  pointsRequired: [{ required: true, message: '请输入所需积分', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存数量', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
}

const statusTagType: Record<string, string> = {
  ACTIVE: 'success',
  INACTIVE: 'info',
}

const statusLabelMap: Record<string, string> = {
  ACTIVE: '上架',
  INACTIVE: '下架',
}

function resetForm() {
  form.name = ''
  form.description = ''
  form.imageUrl = ''
  form.pointsRequired = 100
  form.stock = 0
  form.status = 'ACTIVE'
  form.sort = 0
}

function handleAdd() {
  resetForm()
  isEdit.value = false
  editingId.value = null
  dialogVisible.value = true
}

function handleEdit(row: PointsProduct) {
  resetForm()
  isEdit.value = true
  editingId.value = row.productId
  form.name = row.name
  form.description = row.description || ''
  form.imageUrl = row.imageUrl || ''
  form.pointsRequired = row.pointsRequired
  form.stock = row.stock
  form.status = row.status
  form.sort = row.sort
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (isEdit.value && editingId.value != null) {
        await updateAdminProduct(editingId.value, form)
        ElMessage.success('更新成功')
      } else {
        await createAdminProduct(form)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadList()
    } catch {
      ElMessage.error('操作失败')
    } finally {
      submitting.value = false
    }
  })
}

async function handleDelete(row: PointsProduct) {
  try {
    await ElMessageBox.confirm(`确定删除积分商品 "${row.name}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteAdminProduct(row.productId)
    ElMessage.success('删除成功')
    loadList()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

const loadList = async () => {
  loading.value = true
  try {
    const data = await getAdminProducts({
      page: page.value,
      size: size.value,
      status: filter.status,
      keyword: filter.keyword,
    })
    list.value = data.list || []
    total.value = data.total || 0
  } catch {
    list.value = []
    total.value = 0
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(loadList)
</script>

<style scoped>
.points-product-manage {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}
.image-error {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
  border-radius: 4px;
}
</style>
