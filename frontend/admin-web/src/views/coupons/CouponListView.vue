<template>
  <div class="coupon-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>优惠券管理</span>
          <div class="header-actions">
            <el-select v-model="filter.status" placeholder="状态" clearable style="width: 140px" @change="loadList">
              <el-option label="全部" value="" />
              <el-option label="生效中" value="ACTIVE" />
              <el-option label="未生效" value="INACTIVE" />
              <el-option label="已过期" value="EXPIRED" />
            </el-select>
            <el-input v-model="filter.keyword" placeholder="搜索优惠券名称" clearable style="width: 200px" @keyup.enter="loadList" />
            <el-button type="primary" @click="loadList">搜索</el-button>
            <el-button type="success" @click="handleAdd">新增</el-button>
          </div>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="couponName" label="名称" min-width="160" />
        <el-table-column prop="couponType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small" :type="typeTagType[row.couponType]">
              {{ formatType(row.couponType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="面值" width="100">
          <template #default="{ row }">
            {{ formatValue(row) }}
          </template>
        </el-table-column>
        <el-table-column prop="minSpend" label="最低消费" width="100">
          <template #default="{ row }">${{ row.minSpend.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="remainingQuantity" label="剩余/总量" width="120">
          <template #default="{ row }">{{ row.remainingQuantity }} / {{ row.totalQuantity }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status]" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="validStartTime" label="开始时间" width="150" />
        <el-table-column prop="validEndTime" label="结束时间" width="150" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="info" @click="handleStats(row)">统计</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && list.length === 0" description="暂无优惠券" />

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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑优惠券' : '新增优惠券'" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="优惠券名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio-button label="FIXED_AMOUNT">固定金额</el-radio-button>
            <el-radio-button label="PERCENTAGE">折扣比例</el-radio-button>
            <el-radio-button label="THRESHOLD">满减门槛</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="面值" prop="value">
          <el-input-number v-model="form.value" :min="0" :precision="form.type === 'PERCENTAGE' ? 0 : 2" style="width: 200px" />
          <span class="form-tip">{{ form.type === 'PERCENTAGE' ? '%' : '$' }}</span>
        </el-form-item>
        <el-form-item label="最低消费金额" prop="minOrderAmount">
          <el-input-number v-model="form.minOrderAmount" :min="0" :precision="2" style="width: 200px" />
        </el-form-item>
        <el-form-item label="适用范围" prop="scope">
          <el-radio-group v-model="form.scope">
            <el-radio-button label="ALL">全部商品</el-radio-button>
            <el-radio-button label="CATEGORY">指定分类</el-radio-button>
            <el-radio-button label="PRODUCT">指定商品</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.scope !== 'ALL'" label="范围ID">
          <el-input-number v-model="form.scopeId" :min="1" style="width: 200px" placeholder="分类ID或商品ID" />
        </el-form-item>
        <el-form-item label="发放总量" prop="totalCount">
          <el-input-number v-model="form.totalCount" :min="1" style="width: 200px" />
        </el-form-item>
        <el-form-item label="每人限领" prop="limitPerUser">
          <el-input-number v-model="form.limitPerUser" :min="1" style="width: 200px" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 统计弹窗 -->
    <el-dialog v-model="statsVisible" title="优惠券统计" width="400px">
      <div v-loading="statsLoading">
        <el-descriptions v-if="stats" :column="1" border>
          <el-descriptions-item label="优惠券总数">{{ stats.totalCoupons }}</el-descriptions-item>
          <el-descriptions-item label="已领取数">{{ stats.totalClaimed }}</el-descriptions-item>
          <el-descriptions-item label="已使用数">{{ stats.totalUsed }}</el-descriptions-item>
          <el-descriptions-item label="生效中数量">{{ stats.activeCoupons }}</el-descriptions-item>
          <el-descriptions-item label="已过期数量">{{ stats.expiredCoupons }}</el-descriptions-item>
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
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  value: [{ required: true, message: '请输入面值', trigger: 'blur' }],
  minOrderAmount: [{ required: true, message: '请输入最低消费金额', trigger: 'blur' }],
  scope: [{ required: true, message: '请选择适用范围', trigger: 'change' }],
  totalCount: [{ required: true, message: '请输入发放总量', trigger: 'blur' }],
  limitPerUser: [{ required: true, message: '请输入每人限领数量', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
}

const statusTagType: Record<string, string> = {
  ACTIVE: 'success',
  INACTIVE: 'info',
  EXPIRED: 'danger',
}

const typeTagType: Record<string, string> = {
  FIXED_AMOUNT: 'primary',
  PERCENTAGE: 'warning',
  THRESHOLD: 'success',
}

function formatType(type: string) {
  const map: Record<string, string> = {
    FIXED_AMOUNT: '固定金额',
    PERCENTAGE: '折扣比例',
    THRESHOLD: '满减',
  }
  return map[type] || type
}

function formatValue(row: Coupon) {
  if (row.couponType === 'PERCENTAGE') {
    return `${row.discountValue}%折扣`
  }
  return `满减¥${row.discountValue.toFixed(2)}`
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
        ElMessage.success('更新成功')
      } else {
        await createCoupon(form)
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

async function handleDelete(row: Coupon) {
  try {
    await ElMessageBox.confirm(`确定删除优惠券"${row.couponName}"吗？`, '确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteCoupon(row.id)
    ElMessage.success('删除成功')
    loadList()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error('删除失败')
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
    ElMessage.error('加载统计数据失败')
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
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(loadList)
</script>

<style scoped>
.coupon-manage {
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
.form-tip {
  margin-left: var(--space-sm);
  color: var(--color-shade-50);
}
</style>
