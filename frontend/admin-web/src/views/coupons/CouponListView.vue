<template>
  <div class="coupon-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Coupons</span>
          <div class="header-actions">
            <el-select v-model="filter.status" placeholder="Status" clearable style="width: 140px" @change="loadList">
              <el-option label="All" value="" />
              <el-option label="Active" value="ACTIVE" />
              <el-option label="Inactive" value="INACTIVE" />
              <el-option label="Expired" value="EXPIRED" />
            </el-select>
            <el-input v-model="filter.keyword" placeholder="Search name" clearable style="width: 200px" @keyup.enter="loadList" />
            <el-button type="primary" @click="loadList">Search</el-button>
            <el-button type="success" @click="handleAdd">Add</el-button>
          </div>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="couponName" label="Name" min-width="160" />
        <el-table-column prop="couponType" label="Type" width="100">
          <template #default="{ row }">
            <el-tag size="small" :type="typeTagType[row.couponType]">
              {{ formatType(row.couponType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Value" width="100">
          <template #default="{ row }">
            {{ formatValue(row) }}
          </template>
        </el-table-column>
        <el-table-column prop="minSpend" label="Min Order" width="100">
          <template #default="{ row }">${{ row.minSpend.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="remainingQuantity" label="Remain / Total" width="120">
          <template #default="{ row }">{{ row.remainingQuantity }} / {{ row.totalQuantity }}</template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status]" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="validStartTime" label="Start" width="150" />
        <el-table-column prop="validEndTime" label="End" width="150" />
        <el-table-column label="Actions" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">Edit</el-button>
            <el-button size="small" type="info" @click="handleStats(row)">Stats</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && list.length === 0" description="No coupons" />

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
    <el-dialog v-model="dialogVisible" :title="isEdit ? 'Edit Coupon' : 'Add Coupon'" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" placeholder="Coupon name" />
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="form.description" type="textarea" placeholder="Description" />
        </el-form-item>
        <el-form-item label="Type" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio-button label="FIXED_AMOUNT">Fixed Amount</el-radio-button>
            <el-radio-button label="PERCENTAGE">Percentage</el-radio-button>
            <el-radio-button label="FIXED_DISCOUNT">Fixed Discount</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Value" prop="value">
          <el-input-number v-model="form.value" :min="0" :precision="form.type === 'PERCENTAGE' ? 0 : 2" style="width: 200px" />
          <span class="form-tip">{{ form.type === 'PERCENTAGE' ? '%' : '$' }}</span>
        </el-form-item>
        <el-form-item label="Min Order" prop="minOrderAmount">
          <el-input-number v-model="form.minOrderAmount" :min="0" :precision="2" style="width: 200px" />
        </el-form-item>
        <el-form-item label="Scope" prop="scope">
          <el-radio-group v-model="form.scope">
            <el-radio-button label="ALL">All</el-radio-button>
            <el-radio-button label="CATEGORY">Category</el-radio-button>
            <el-radio-button label="PRODUCT">Product</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.scope !== 'ALL'" label="Scope ID">
          <el-input-number v-model="form.scopeId" :min="1" style="width: 200px" placeholder="Category or Product ID" />
        </el-form-item>
        <el-form-item label="Total Count" prop="totalCount">
          <el-input-number v-model="form.totalCount" :min="1" style="width: 200px" />
        </el-form-item>
        <el-form-item label="Limit/User" prop="limitPerUser">
          <el-input-number v-model="form.limitPerUser" :min="1" style="width: 200px" />
        </el-form-item>
        <el-form-item label="Start Time" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="Start time" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="End Time" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="End time" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">Confirm</el-button>
      </template>
    </el-dialog>

    <!-- 统计弹窗 -->
    <el-dialog v-model="statsVisible" title="Coupon Statistics" width="400px">
      <div v-loading="statsLoading">
        <el-descriptions v-if="stats" :column="1" border>
          <el-descriptions-item label="Total Coupons">{{ stats.totalCoupons }}</el-descriptions-item>
          <el-descriptions-item label="Total Claimed">{{ stats.totalClaimed }}</el-descriptions-item>
          <el-descriptions-item label="Total Used">{{ stats.totalUsed }}</el-descriptions-item>
          <el-descriptions-item label="Active Coupons">{{ stats.activeCoupons }}</el-descriptions-item>
          <el-descriptions-item label="Expired Coupons">{{ stats.expiredCoupons }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getCouponList,
  createCoupon,
  updateCoupon,
  deleteCoupon,
  getCouponStats,
  type Coupon,
  type CouponFormData,
  type CouponStats,
} from '@/api/modules/coupon'

const list = ref<Coupon[]>([])
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

const form = reactive<CouponFormData>({
  name: '',
  description: '',
  type: 'FIXED_AMOUNT',
  value: 0,
  minOrderAmount: 0,
  scope: 'ALL',
  scopeId: undefined,
  totalCount: 100,
  limitPerUser: 1,
  startTime: '',
  endTime: '',
})

const rules: FormRules = {
  name: [{ required: true, message: 'Please enter name', trigger: 'blur' }],
  type: [{ required: true, message: 'Please select type', trigger: 'change' }],
  value: [{ required: true, message: 'Please enter value', trigger: 'blur' }],
  minOrderAmount: [{ required: true, message: 'Please enter min order amount', trigger: 'blur' }],
  scope: [{ required: true, message: 'Please select scope', trigger: 'change' }],
  totalCount: [{ required: true, message: 'Please enter total count', trigger: 'blur' }],
  limitPerUser: [{ required: true, message: 'Please enter limit per user', trigger: 'blur' }],
  startTime: [{ required: true, message: 'Please select start time', trigger: 'change' }],
  endTime: [{ required: true, message: 'Please select end time', trigger: 'change' }],
}

const statusTagType: Record<string, string> = {
  ACTIVE: 'success',
  INACTIVE: 'info',
  EXPIRED: 'danger',
}

const typeTagType: Record<string, string> = {
  FIXED_AMOUNT: 'primary',
  PERCENTAGE: 'warning',
  FIXED_DISCOUNT: 'success',
}

function formatType(type: string) {
  const map: Record<string, string> = {
    FIXED_AMOUNT: 'Fixed',
    PERCENTAGE: 'Percent',
    FIXED_DISCOUNT: 'Discount',
  }
  return map[type] || type
}

function formatValue(row: Coupon) {
  if (row.couponType === 'PERCENTAGE') {
    return `${row.discountValue}% OFF`
  }
  return `$${row.discountValue.toFixed(2)} OFF`
}

function resetForm() {
  form.name = ''
  form.description = ''
  form.type = 'FIXED_AMOUNT'
  form.value = 0
  form.minOrderAmount = 0
  form.scope = 'ALL'
  form.scopeId = undefined
  form.totalCount = 100
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

function handleEdit(row: Coupon) {
  resetForm()
  isEdit.value = true
  editingId.value = row.id
  form.name = row.couponName
  form.description = ''
  form.type = row.couponType
  form.value = Number(row.discountValue)
  form.minOrderAmount = Number(row.minSpend)
  form.scope = row.applicableScope
  form.scopeId = row.applicableIds ? Number(row.applicableIds) : undefined
  form.totalCount = row.totalQuantity
  form.limitPerUser = 1
  form.startTime = row.validStartTime
  form.endTime = row.validEndTime
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (isEdit.value && editingId.value != null) {
        await updateCoupon(editingId.value, form)
        ElMessage.success('Updated successfully')
      } else {
        await createCoupon(form)
        ElMessage.success('Created successfully')
      }
      dialogVisible.value = false
      loadList()
    } catch {
      ElMessage.error('Operation failed')
    } finally {
      submitting.value = false
    }
  })
}

async function handleDelete(row: Coupon) {
  try {
    await ElMessageBox.confirm(`Delete coupon "${row.couponName}"?`, 'Confirm', {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning',
    })
    await deleteCoupon(row.id)
    ElMessage.success('Deleted successfully')
    loadList()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error('Delete failed')
  }
}

const statsVisible = ref(false)
const statsLoading = ref(false)
const stats = ref<CouponStats | null>(null)

async function handleStats(_row: Coupon) {
  statsVisible.value = true
  statsLoading.value = true
  stats.value = null
  try {
    stats.value = await getCouponStats()
  } catch {
    ElMessage.error('Failed to load stats')
  } finally {
    statsLoading.value = false
  }
}

const loadList = async () => {
  loading.value = true
  try {
    const data = await getCouponList({ page: page.value, size: size.value, status: filter.status, keyword: filter.keyword })
    list.value = data.list || []
    total.value = data.total || 0
  } catch {
    list.value = []
    total.value = 0
    ElMessage.error('Load failed')
  } finally {
    loading.value = false
  }
}

onMounted(loadList)
</script>

<style scoped>
.coupon-manage {
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
.form-tip {
  margin-left: 8px;
  color: #666;
}
</style>
