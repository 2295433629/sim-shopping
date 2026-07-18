<template>
  <div class="merchant-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商家管理</span>
          <div>
            <el-select v-model="filter.status" placeholder="状态" clearable style="width:120px" @change="handleSearch">
              <el-option label="全部" value="" />
              <el-option label="待审核" value="PENDING" />
              <el-option label="已通过" value="ACTIVE" />
              <el-option label="已拒绝" value="REJECTED" />
              <el-option label="已禁用" value="DISABLED" />
            </el-select>
            <el-input v-model="filter.keyword" placeholder="搜索商家" clearable style="width:200px" @keyup.enter="handleSearch" />
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </div>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="merchantId" label="ID" width="60" />
        <el-table-column prop="merchantName" label="店铺名称" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="160" />
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetail(row)">详情</el-button>
            <el-button v-if="row.status === 'PENDING'" size="small" type="success" @click="handleApprove(row)">通过</el-button>
            <el-button v-if="row.status === 'PENDING'" size="small" type="danger" @click="handleReject(row)">拒绝</el-button>
            <el-button v-if="row.status === 'ACTIVE'" size="small" type="warning" @click="handleDisable(row)">禁用</el-button>
            <el-button v-if="row.status === 'DISABLED'" size="small" type="success" @click="handleEnable(row)">启用</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" :page-size="size" :total="total" layout="prev, pager, next, total" @current-change="loadList" style="margin-top:16px;justify-content:flex-end" />
    </el-card>

    <el-dialog v-model="detailVisible" title="商家详情" width="500px">
      <el-descriptions :column="1" border v-loading="detailLoading">
        <el-descriptions-item label="ID">{{ detail?.merchantId }}</el-descriptions-item>
        <el-descriptions-item label="店铺名称">{{ detail?.merchantName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail?.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detail?.contactEmail ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="关联店铺">{{ detail?.shopName ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detail" :type="statusType(detail.status)">{{ statusLabel(detail.status) }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMerchants, getMerchantDetail, approveMerchant, rejectMerchant, disableMerchant, enableMerchant } from '@/api/modules/merchant'
import type { MerchantListItem, MerchantDetailInfo } from '@/api/modules/merchant'

const list = ref<MerchantListItem[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const filter = reactive({ status: '', keyword: '' })

const detailVisible = ref(false)
const detailLoading = ref(false)
const detail = ref<MerchantDetailInfo | null>(null)

const statusMap: Record<string, string> = {
  PENDING: '待审核',
  ACTIVE: '已通过',
  REJECTED: '已拒绝',
  DISABLED: '已禁用',
}
const statusLabel = (s: string) => statusMap[s] || s
const statusType = (s: string) => ({ PENDING: 'warning', ACTIVE: 'success', REJECTED: 'danger', DISABLED: 'info' }[s] as any || '')

const handleSearch = () => {
  page.value = 1
  loadList()
}

const loadList = async () => {
  loading.value = true
  try {
    const res = await getMerchants({ page: page.value, size: size.value, status: filter.status || undefined, keyword: filter.keyword || undefined })
    list.value = res.list || []
    total.value = res.total || 0
  } catch { ElMessage.error('加载失败') }
  loading.value = false
}

const viewDetail = async (row: MerchantListItem) => {
  detail.value = null
  detailVisible.value = true
  detailLoading.value = true
  try {
    detail.value = await getMerchantDetail(row.merchantId)
  } catch {
    ElMessage.error('获取详情失败')
    detailVisible.value = false
  } finally {
    detailLoading.value = false
  }
}

const handleApprove = async (row: MerchantListItem) => {
  try {
    await ElMessageBox.confirm('确认通过该商家"' + row.merchantName + '"？', '确认', { type: 'success' })
    await approveMerchant(row.merchantId)
    ElMessage.success('已通过')
    loadList()
  } catch (e: any) { if (e !== 'cancel') ElMessage.error('操作失败') }
}
const handleReject = async (row: MerchantListItem) => {
  try {
    const { value } = await ElMessageBox.prompt('拒绝原因', '拒绝', { inputType: 'textarea' })
    await rejectMerchant(row.merchantId, value)
    ElMessage.success('已拒绝')
    loadList()
  } catch (e: any) { if (e !== 'cancel') ElMessage.error('操作失败') }
}
const handleDisable = async (row: MerchantListItem) => {
  try {
    await ElMessageBox.confirm('确认禁用该商家"' + row.merchantName + '"？', '确认', { type: 'warning' })
    await disableMerchant(row.merchantId)
    ElMessage.success('已禁用')
    loadList()
  } catch (e: any) { if (e !== 'cancel') ElMessage.error('操作失败') }
}
const handleEnable = async (row: MerchantListItem) => {
  try {
    await enableMerchant(row.merchantId)
    ElMessage.success('已启用')
    loadList()
  } catch { ElMessage.error('操作失败') }
}

onMounted(loadList)
</script>

<style scoped>
.merchant-manage { padding: var(--space-xl); }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>