<template>
  <div class="activity-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>专题活动管理</span>
          <div class="header-actions">
            <el-select v-model="filter.status" placeholder="状态" clearable style="width: 140px" @change="loadList">
              <el-option label="全部" value="" />
              <el-option label="进行中" value="ACTIVE" />
              <el-option label="未开始" value="PENDING" />
              <el-option label="已结束" value="ENDED" />
            </el-select>
            <el-input v-model="filter.keyword" placeholder="搜索活动名称" clearable style="width: 200px" @keyup.enter="loadList" />
            <el-button type="primary" @click="loadList">搜索</el-button>
            <el-button type="success" @click="handleAdd">新增</el-button>
          </div>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="activityId" label="ID" width="60" />
        <el-table-column label="活动横幅" width="120">
          <template #default="{ row }">
            <el-image :src="row.bannerImage" fit="cover" style="width: 80px; height: 50px; border-radius: 4px;">
              <template #error>
                <div style="width: 80px; height: 50px; background: #f5f5f5; display: flex; align-items: center; justify-content: center;">
                  <el-icon :size="20" color="#ccc"><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="activityName" label="活动名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="startTime" label="开始时间" width="150" />
        <el-table-column prop="endTime" label="结束时间" width="150" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status]" size="small">{{ formatStatus(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="productCount" label="商品数量" width="90" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && list.length === 0" description="暂无专题活动" />

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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑专题活动' : '新增专题活动'" width="700px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入活动名称" />
        </el-form-item>
        <el-form-item label="活动描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入活动描述" />
        </el-form-item>
        <el-form-item label="横幅图片" prop="bannerImage">
          <el-input v-model="form.bannerImage" placeholder="请输入横幅图片URL" />
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
        <el-form-item label="关联商品" prop="productIds">
          <div class="product-select-wrapper">
            <el-input
              v-model="productKeyword"
              placeholder="搜索商品名称"
              clearable
              style="width: 260px; margin-bottom: 8px;"
              @input="handleProductSearch"
            />
            <el-table
              :data="productList"
              v-loading="productLoading"
              border
              size="small"
              height="260"
              @selection-change="handleSelectionChange"
              ref="productTableRef"
            >
              <el-table-column type="selection" width="45" />
              <el-table-column prop="id" label="ID" width="60" />
              <el-table-column label="图片" width="70">
                <template #default="{ row }">
                  <el-image :src="row.mainImage" fit="cover" style="width: 50px; height: 40px; border-radius: 4px;">
                    <template #error>
                      <div style="width: 50px; height: 40px; background: #f5f5f5; display: flex; align-items: center; justify-content: center;">
                        <el-icon :size="16" color="#ccc"><Picture /></el-icon>
                      </div>
                    </template>
                  </el-image>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="商品名称" min-width="140" show-overflow-tooltip />
              <el-table-column prop="price" label="价格" width="90">
                <template #default="{ row }">¥{{ row.price.toFixed(2) }}</template>
              </el-table-column>
            </el-table>
            <div class="selected-info" v-if="form.productIds.length > 0">
              已选择 {{ form.productIds.length }} 个商品
            </div>
          </div>
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
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules, TableInstance } from 'element-plus'
import {
  getAdminActivities,
  createAdminActivity,
  updateAdminActivity,
  deleteAdminActivity,
  type Activity,
  type ActivityFormData,
} from '@/api/modules/activity'
import { getProducts, type ProductListItem } from '@/api/modules/product'

/** 商品卡片简要信息 — 复用 ProductListItem */
type ProductOption = ProductListItem

const list = ref<Activity[]>([])
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

const form = reactive<ActivityFormData>({
  name: '',
  description: '',
  bannerImage: '',
  startTime: '',
  endTime: '',
  productIds: [],
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  bannerImage: [{ required: true, message: '请输入横幅图片URL', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  productIds: [{ required: true, message: '请至少选择一个商品', trigger: 'change', type: 'array', min: 1 }],
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
  form.name = ''
  form.description = ''
  form.bannerImage = ''
  form.startTime = ''
  form.endTime = ''
  form.productIds = []
}

// 商品选择相关
const productList = ref<ProductOption[]>([])
const productLoading = ref(false)
const productKeyword = ref('')
const productTableRef = ref<TableInstance>()
let productSearchTimer: ReturnType<typeof setTimeout> | null = null

async function loadProducts(keyword = '') {
  productLoading.value = true
  try {
    const data = await getProducts({ page: 1, size: 50, keyword })
    productList.value = data.list || []
    await nextTick()
    syncSelection()
  } catch {
    productList.value = []
    ElMessage.error('加载商品列表失败')
  } finally {
    productLoading.value = false
  }
}

function handleProductSearch() {
  if (productSearchTimer) clearTimeout(productSearchTimer)
  productSearchTimer = setTimeout(() => {
    loadProducts(productKeyword.value)
  }, 300)
}

function handleSelectionChange(selection: ProductOption[]) {
  form.productIds = selection.map((item) => item.id)
}

function syncSelection() {
  if (!productTableRef.value) return
  productList.value.forEach((row) => {
    const shouldSelected = form.productIds.includes(row.id)
    productTableRef.value!.toggleRowSelection(row, shouldSelected)
  })
}

function handleAdd() {
  resetForm()
  isEdit.value = false
  editingId.value = null
  productKeyword.value = ''
  dialogVisible.value = true
  loadProducts()
}

function handleEdit(row: Activity) {
  resetForm()
  isEdit.value = true
  editingId.value = row.id
  form.name = row.activityName
  form.description = row.description || ''
  form.bannerImage = row.bannerImage
  form.startTime = row.startTime
  form.endTime = row.endTime
  productKeyword.value = ''
  dialogVisible.value = true
  loadProducts()
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (isEdit.value && editingId.value != null) {
        await updateAdminActivity(editingId.value, form)
        ElMessage.success('更新成功')
      } else {
        await createAdminActivity(form)
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

async function handleDelete(row: Activity) {
  try {
    await ElMessageBox.confirm(`确定删除专题活动 "${row.activityName}" 吗？`, '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteAdminActivity(row.id)
    ElMessage.success('删除成功')
    loadList()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

const loadList = async () => {
  loading.value = true
  try {
    const data = await getAdminActivities({
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
.activity-manage {
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
.product-select-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 12px;
  background: #fafafa;
}
.selected-info {
  margin-top: 8px;
  font-size: 13px;
  color: #409eff;
}
</style>
