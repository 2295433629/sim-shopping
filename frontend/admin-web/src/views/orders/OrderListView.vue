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
      <span class="card-title">订单管理</span>
    </template>

    <!-- Filters -->
    <div class="filter-bar">
      <el-select v-model="filterStatus" placeholder="状态" clearable style="width: 150px;">
        <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
      </el-select>
      <el-input-number v-model="filterShopId" placeholder="店铺ID" :min="1" controls-position="right" style="width: 130px;" />
      <el-input v-model="keyword" placeholder="订单号/买家" clearable style="width: 200px;" @keyup.enter="handleSearch" />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table :data="orderList" v-loading="loading" style="width: 100%; margin-top: 16px;" stripe>
      <el-table-column label="订单号" prop="orderNo" width="200" />
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="(statusTagType[row.status] as any) || 'info'" size="small">
            {{ row.statusText }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="店铺" prop="shopName" width="150" />
      <el-table-column label="买家" prop="buyerName" width="120" />
      <el-table-column label="商品数" prop="itemCount" width="80" align="center" />
      <el-table-column label="总金额" width="120" align="right">
        <template #default="{ row }">
          <span class="amount">¥{{ row.payAmount.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" prop="createdAt" width="180" />
      <el-table-column label="操作" width="100" align="center" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!loading && orderList.length === 0" description="暂无订单" />

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
