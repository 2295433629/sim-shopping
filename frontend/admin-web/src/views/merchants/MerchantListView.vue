<template>
  <div class="merchant-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Merchants</span>
          <div>
            <el-select v-model="filter.status" placeholder="Status" clearable style="width:120px" @change="loadList">
              <el-option label="All" value="" />
              <el-option label="Pending" value="PENDING" />
              <el-option label="Active" value="ACTIVE" />
              <el-option label="Rejected" value="REJECTED" />
              <el-option label="Disabled" value="DISABLED" />
            </el-select>
            <el-input v-model="filter.keyword" placeholder="Search" clearable style="width:200px" @keyup.enter="loadList" />
            <el-button type="primary" @click="loadList">Search</el-button>
          </div>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="merchantId" label="ID" width="60" />
        <el-table-column prop="merchantName" label="Name" />
        <el-table-column prop="contactPhone" label="Phone" width="120" />
        <el-table-column prop="status" label="Status" width="80">
          <template #default="{ row }"><el-tag :type="statusType(row.status)">{{ row.status }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="createdAt" label="Created" width="160" />
        <el-table-column label="Actions" width="280">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetail(row)">Detail</el-button>
            <el-button v-if="row.status === 'PENDING'" size="small" type="success" @click="handleApprove(row)">Approve</el-button>
            <el-button v-if="row.status === 'PENDING'" size="small" type="danger" @click="handleReject(row)">Reject</el-button>
            <el-button v-if="row.status === 'ACTIVE'" size="small" type="warning" @click="handleDisable(row)">Disable</el-button>
            <el-button v-if="row.status === 'DISABLED'" size="small" type="success" @click="handleEnable(row)">Enable</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" :page-size="size" :total="total" layout="prev, pager, next, total" @current-change="loadList" style="margin-top:16px;justify-content:flex-end" />
    </el-card>
    <el-dialog v-model="detailVisible" title="Merchant Detail" width="500px">
      <el-descriptions :column="1" border v-if="currentMerchant">
        <el-descriptions-item label="ID">{{ currentMerchant.merchantId }}</el-descriptions-item>
        <el-descriptions-item label="Name">{{ currentMerchant.merchantName }}</el-descriptions-item>
        <el-descriptions-item label="Phone">{{ currentMerchant.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="Email">{{ currentMerchant.contactEmail }}</el-descriptions-item>
        <el-descriptions-item label="Status">{{ currentMerchant.status }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMerchants, approveMerchant, rejectMerchant, disableMerchant, enableMerchant } from '@/api/modules/merchant'

const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const filter = reactive({ status: '', keyword: '' })
const detailVisible = ref(false)
const currentMerchant = ref<any>(null)

const statusType = (s: string) => ({ PENDING: 'warning', ACTIVE: 'success', REJECTED: 'danger', DISABLED: 'info' }[s] as any || '')

const loadList = async () => {
  loading.value = true
  try {
    const res = await getMerchants({ page: page.value, size: size.value, status: filter.status, keyword: filter.keyword })
    list.value = res.list || []
    total.value = res.total || 0
  } catch { ElMessage.error('Load failed') }
  loading.value = false
}

const viewDetail = (row: any) => { currentMerchant.value = row; detailVisible.value = true }

const handleApprove = async (row: any) => {
  try { await ElMessageBox.confirm('Approve merchant "' + row.merchantName + '"?', 'Confirm', { type: 'success' }); await approveMerchant(row.merchantId); ElMessage.success('Approved'); loadList() } catch (e: any) { if (e !== 'cancel') ElMessage.error('Failed') }
}
const handleReject = async (row: any) => {
  try { const { value } = await ElMessageBox.prompt('Reject reason', 'Reject', { inputType: 'textarea' }); await rejectMerchant(row.merchantId, value); ElMessage.success('Rejected'); loadList() } catch (e: any) { if (e !== 'cancel') ElMessage.error('Failed') }
}
const handleDisable = async (row: any) => {
  try { await ElMessageBox.confirm('Disable merchant "' + row.merchantName + '"?', 'Confirm', { type: 'warning' }); await disableMerchant(row.merchantId); ElMessage.success('Disabled'); loadList() } catch (e: any) { if (e !== 'cancel') ElMessage.error('Failed') }
}
const handleEnable = async (row: any) => {
  try { await enableMerchant(row.merchantId); ElMessage.success('Enabled'); loadList() } catch { ElMessage.error('Failed') }
}
onMounted(loadList)
</script>

<style scoped>
.merchant-manage { padding: var(--space-xl); }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>