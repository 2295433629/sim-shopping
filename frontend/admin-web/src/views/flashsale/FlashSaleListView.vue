<template>
  <div class="flash-sale-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>秒杀活动管理</span>
          <div class="header-actions">
            <el-select v-model="filter.status" placeholder="状态" clearable style="width: 140px" @change="loadList">
              <el-option label="全部" value="" />
              <el-option label="进行中" value="ACTIVE" />
              <el-option label="未开始" value="PENDING" />
              <el-option label="已结束" value="ENDED" />
            </el-select>
            <el-input v-model="filter.keyword" placeholder="搜索商品名称" clearable style="width: 200px" @keyup.enter="loadList" />
            <el-button type="primary" @click="loadList">搜索</el-button>
            <el-button type="success" @click="handleAdd">新增</el-button>
          </div>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="saleId" label="ID" width="60" />
        <el-table-column prop="productName" label="商品名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="originalPrice" label="原价" width="100">
          <template #default="{ row }">¥{{ row.originalPrice.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="flashPrice" label="秒杀价" width="100">
          <template #default="{ row }">
            <span style="color: var(--color-price); font-weight: bold;">¥{{ row.flashPrice.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="soldCount" label="已售" width="80" />
        <el-table-column prop="limitPerUser" label="限购" width="80" />
        <el-table-column prop="startTime" label="开始时间" width="150" />
        <el-table-column prop="endTime" label="结束时间" width="150" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status]" size="small">{{ formatStatus(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && list.length === 0" description="暂无秒杀活动" />

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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑秒杀活动' : '新增秒杀活动'" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="商品ID" prop="productId">
          <el-input-number v-model="form.productId" :min="1" style="width: 200px" placeholder="请输入商品ID" />
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input-number v-model="form.originalPrice" :min="0" :precision="2" style="width: 200px" />
        </el-form-item>
        <el-form-item label="秒杀价" prop="flashPrice">
          <el-input-number v-model="form.flashPrice" :min="0" :precision="2" style="width: 200px" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="1" style="width: 200px" />
        </el-form-item>
        <el-form-item label="每人限购" prop="limitPerUser">
          <el-input-number v-model="form.limitPerUser" :min="1" style="width: 200px" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 260px"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 260px"
          />
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
  getAdminFlashSales,
  createAdminFlashSale,
  updateAdminFlashSale,
  deleteAdminFlashSale,
  type FlashSale,
  type FlashSaleFormData,
} from '@/api/modules/flashsale'

const list = ref<FlashSale[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const filter = reactive({ status: '', keyword: '' })

const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref<number | null>(null)
const submitting = ref(false)
const formRef = ref<FormInstance>()

const form = reactive<FlashSaleFormData>({
  productId: 1,
  originalPrice: 0,
  flashPrice: 0,
  stock: 100,
  limitPerUser: 1,
  startTime: '',
  endTime: '',
})

const rules: FormRules = {
  productId: [{ required: true, message: '请输入商品ID', trigger: 'blur' }],
  originalPrice: [{ required: true, message: '请输入原价', trigger: 'blur' }],
  flashPrice: [{ required: true, message: '请输入秒杀价', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }],
  limitPerUser: [{ required: true, message: '请输入每人限购数量', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
}

const statusTagType: Record<string, string> = {
  ACTIVE: 'success',
  PENDING: 'warning',
  ENDED: 'info',
}

function formatStatus(status: string): string {
  const map: Record<string, string> = {
    ACTIVE: '进行中',
    PENDING: '未开始',
    ENDED: '已结束',
  }
  return map[status] || status
}

function resetForm() {
  form.productId = 1
  form.originalPrice = 0
  form.flashPrice = 0
  form.stock = 100
  form.limitPerUser = 1
  form.startTime = ''
  form.endTime = ''
}

function handleAdd() {
  resetForm()
  isEdit.value = false
  editingId.value = null
  dialogVisible.value = true
}

function handleEdit(row: FlashSale) {
  resetForm()
  isEdit.value = true
  editingId.value = row.saleId
  form.productId = row.productId
  form.originalPrice = row.originalPrice
  form.flashPrice = row.flashPrice
  form.stock = row.stock
  form.limitPerUser = row.limitPerUser
  form.startTime = row.startTime
  form.endTime = row.endTime
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (isEdit.value && editingId.value != null) {
        await updateAdminFlashSale(editingId.value, form)
        ElMessage.success('更新成功')
      } else {
        await createAdminFlashSale(form)
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

async function handleDelete(row: FlashSale) {
  try {
    await ElMessageBox.confirm(`确定删除秒杀活动 "${row.productName}" 吗？`, '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteAdminFlashSale(row.saleId)
    ElMessage.success('删除成功')
    loadList()
  } catch (e: unknown) {
    if (!(typeof e === 'string' && e === 'cancel')) ElMessage.error('删除失败')
  }
}

const loadList = async () => {
  loading.value = true
  try {
    const data = await getAdminFlashSales({
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
.flash-sale-manage {
  padding: var(--space-xl);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-actions {
  display: flex;
  gap: var(--space-sm);
  align-items: center;
}
</style>
