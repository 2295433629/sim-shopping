<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getMerchantOrders, type OrderListVO } from '@/api/modules/order'

const router = useRouter()
const loading = ref(false)
const orderList = ref<OrderListVO[]>([])
const activeTab = ref('all')
const keyword = ref('')
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)

const tabs = [
  { label: '全部', value: 'all' },
  { label: '待付款', value: 'CREATED' },
  { label: '已付款', value: 'PAID' },
  { label: '已发货', value: 'SHIPPED' },
  { label: '运输中', value: 'IN_TRANSIT' },
  { label: '配送中', value: 'OUT_FOR_DELIVERY' },
  { label: '已送达', value: 'DELIVERED' },
  { label: '已完成', value: 'COMPLETED' },
  { label: '已取消', value: 'CANCELLED' },
]

const statusTagType: Record<string, string> = {
  CREATED: 'warning',
  PAID: 'primary',
  SHIPPED: 'info',
  IN_TRANSIT: 'primary',
  OUT_FOR_DELIVERY: 'warning',
  DELIVERED: 'success',
  COMPLETED: 'success',
  CANCELLED: 'danger',
}

onMounted(() => {
  loadOrders()
})

watch([activeTab, page], () => {
  loadOrders()
})

async function loadOrders() {
  loading.value = true
  try {
    const params: Record<string, unknown> = { page: page.value, size: pageSize.value }
    if (activeTab.value !== 'all') {
      params.status = activeTab.value
    }
    if (keyword.value.trim()) {
      params.keyword = keyword.value.trim()
    }
    const data = await getMerchantOrders(params as any)
    orderList.value = data.list || []
    total.value = data.total || 0
  } catch {
    orderList.value = []
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
}

function handleDetail(row: OrderListVO) {
  router.push(`/orders/${row.orderNo}`)
}

function handleShip(row: OrderListVO) {
  router.push(`/merchant/orders/shipping?orderNo=${row.orderNo}`)
}
</script>

<template>
  <div class="order-list-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">订单管理</span>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane v-for="tab in tabs" :key="tab.value" :label="tab.label" :name="tab.value" />
      </el-tabs>

      <div style="margin-bottom:12px">
        <el-input v-model="keyword" placeholder="搜索订单号/买家" clearable style="width:280px" @keyup.enter="page=1;loadOrders()" @clear="page=1;loadOrders()">
          <template #append>
            <el-button @click="page=1;loadOrders()">搜索</el-button>
          </template>
        </el-input>
      </div>

      <el-table :data="orderList" v-loading="loading" style="width: 100%" stripe>
        <el-table-column label="订单号" prop="orderNo" width="200" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="(statusTagType[row.status] as any) || 'info'" size="small">
              {{ row.statusText }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="买家" prop="buyerName" width="120" />
        <el-table-column label="手机号" prop="buyerPhone" width="140" />
        <el-table-column label="商品数" prop="itemCount" width="80" align="center" />
        <el-table-column label="订单金额" width="120" align="right">
          <template #default="{ row }">
            <span class="amount">¥{{ row.payAmount.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" prop="createdAt" width="180" />
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.status === 'PAID'" link type="success" @click="handleShip(row)">发货</el-button>
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
  </div>
</template>

<style scoped lang="scss">
.order-list-container {
  .card-title {
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
    font-size: var(--font-size-heading-lg);
    color: var(--color-ink);
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
}
</style>
