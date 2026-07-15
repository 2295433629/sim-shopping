<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getAdminOrders, type OrderListVO } from '@/api/modules/order'

const router = useRouter()
const loading = ref(false)
const orderList = ref<OrderListVO[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filterStatus = ref('')
const filterShopId = ref<number | undefined>(undefined)
const keyword = ref('')

const statusOptions = [
  { label: 'All', value: '' },
  { label: 'Unpaid', value: 'CREATED' },
  { label: 'Paid', value: 'PAID' },
  { label: 'Shipped', value: 'SHIPPED' },
  { label: 'Delivered', value: 'DELIVERED' },
  { label: 'Completed', value: 'COMPLETED' },
  { label: 'Cancelled', value: 'CANCELLED' },
]

const statusTagType: Record<string, string> = {
  CREATED: 'warning',
  PAID: 'primary',
  SHIPPED: 'info',
  DELIVERED: 'success',
  COMPLETED: 'success',
  CANCELLED: 'danger',
}

onMounted(() => {
  loadOrders()
})

watch(page, () => {
  loadOrders()
})

async function loadOrders() {
  loading.value = true
  try {
    const params: Record<string, unknown> = { page: page.value, size: pageSize.value }
    if (filterStatus.value) params.status = filterStatus.value
    if (filterShopId.value) params.shopId = filterShopId.value
    if (keyword.value.trim()) params.keyword = keyword.value.trim()
    const data = await getAdminOrders(params as any)
    orderList.value = data.list || []
    total.value = data.total || 0
  } catch {
    orderList.value = []
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  page.value = 1
  loadOrders()
}

function handleReset() {
  filterStatus.value = ''
  filterShopId.value = undefined
  keyword.value = ''
  page.value = 1
  loadOrders()
}

function handlePageChange(p: number) {
  page.value = p
}

function handleDetail(row: OrderListVO) {
  router.push(`/orders/${row.orderNo}`)
}
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <span class="card-title">Order Management</span>
    </template>

    <!-- Filters -->
    <div class="filter-bar">
      <el-select v-model="filterStatus" placeholder="Status" clearable style="width: 150px;">
        <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
      </el-select>
      <el-input-number v-model="filterShopId" placeholder="Shop ID" :min="1" controls-position="right" style="width: 130px;" />
      <el-input v-model="keyword" placeholder="Order No / Buyer" clearable style="width: 200px;" @keyup.enter="handleSearch" />
      <el-button type="primary" @click="handleSearch">Search</el-button>
      <el-button @click="handleReset">Reset</el-button>
    </div>

    <el-table :data="orderList" v-loading="loading" style="width: 100%; margin-top: 16px;" stripe>
      <el-table-column label="Order No" prop="orderNo" width="200" />
      <el-table-column label="Status" width="120">
        <template #default="{ row }">
          <el-tag :type="(statusTagType[row.status] as any) || 'info'" size="small">
            {{ row.statusText }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Shop" prop="shopName" width="150" />
      <el-table-column label="Buyer" prop="buyerName" width="120" />
      <el-table-column label="Items" prop="itemCount" width="80" align="center" />
      <el-table-column label="Total" width="120" align="right">
        <template #default="{ row }">
          <span class="amount">${{ row.payAmount.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Created At" prop="createdAt" width="180" />
      <el-table-column label="Action" width="100" align="center" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleDetail(row)">Detail</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!loading && orderList.length === 0" description="No orders" />

    <div v-if="total > pageSize" class="pagination-wrap">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.card-title {
  font-size: var(--font-size-body-md);
  font-weight: 600;
}

.filter-bar {
  display: flex;
  gap: var(--space-md);
  align-items: center;
  flex-wrap: wrap;
}

.amount {
  color: var(--color-price);
  font-weight: 600;
}

.pagination-wrap {
  margin-top: var(--space-xl);
  display: flex;
  justify-content: center;
}
</style>
