<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { getFinance, getSettlementRecords, type ShopFinanceResponse, type SettlementRecordResponse } from '@/api/modules/settlement'

const loading = ref(false)
const finance = ref<ShopFinanceResponse>({
  balance: 0,
  totalIncome: 0,
  totalSettled: 0,
  frozenAmount: 0,
  todayIncome: 0,
})

const recordList = ref<SettlementRecordResponse[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
  loadFinance()
  loadRecords()
})

watch(page, () => {
  loadRecords()
})

async function loadFinance() {
  try {
    finance.value = await getFinance()
  } catch {
    // handled by interceptor
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const data = await getSettlementRecords({ page: page.value, size: pageSize.value })
    recordList.value = data.list || []
    total.value = data.total || 0
  } catch {
    recordList.value = []
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
}

function formatAmount(amount: number): string {
  return `¥${amount.toFixed(2)}`
}

function getTypeLabel(type: string): string {
  return type === 'ORDER' ? '订单结算' : type === 'REFUND' ? '退款扣减' : type
}

function getTypeTagType(type: string): string {
  return type === 'ORDER' ? 'success' : type === 'REFUND' ? 'danger' : 'info'
}

function getAmountClass(row: SettlementRecordResponse): string {
  return row.type === 'REFUND' ? 'amount-refund' : 'amount-income'
}
</script>

<template>
  <div class="finance-container">
    <!-- 数据概览卡片 -->
    <div class="finance-cards">
      <el-card shadow="never" class="finance-card">
        <div class="card-body">
          <div class="card-label">可用余额</div>
          <div class="card-value">{{ formatAmount(finance.balance) }}</div>
        </div>
      </el-card>
      <el-card shadow="never" class="finance-card">
        <div class="card-body">
          <div class="card-label">累计总收入</div>
          <div class="card-value">{{ formatAmount(finance.totalIncome) }}</div>
        </div>
      </el-card>
      <el-card shadow="never" class="finance-card">
        <div class="card-body">
          <div class="card-label">已结算金额</div>
          <div class="card-value">{{ formatAmount(finance.totalSettled) }}</div>
        </div>
      </el-card>
      <el-card shadow="never" class="finance-card">
        <div class="card-body">
          <div class="card-label">今日收入</div>
          <div class="card-value">{{ formatAmount(finance.todayIncome) }}</div>
        </div>
      </el-card>
    </div>

    <!-- 结算记录列表 -->
    <el-card shadow="never">
      <template #header>
        <span class="card-title">结算记录</span>
      </template>

      <el-table :data="recordList" v-loading="loading" style="width: 100%" stripe>
        <el-table-column label="订单号" prop="orderNo" width="200" />
        <el-table-column label="结算金额" width="140" align="right">
          <template #default="{ row }">
            <span :class="getAmountClass(row)">{{ formatAmount(row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="类型" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="(getTypeTagType(row.type) as any)" size="small">
              {{ getTypeLabel(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="描述" prop="description" min-width="200" show-overflow-tooltip />
        <el-table-column label="时间" prop="createdAt" width="180" />
      </el-table>

      <el-empty v-if="!loading && recordList.length === 0" description="暂无结算记录" />

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
.finance-container {
  .card-title {
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
    font-size: var(--font-size-heading-lg);
    color: var(--color-ink);
  }

  .finance-cards {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: var(--space-lg);
    margin-bottom: var(--space-xl);
  }

  .finance-card {
    .card-body {
      text-align: center;
      padding: var(--space-md) 0;
    }

    .card-label {
      font-size: var(--font-size-caption);
      color: var(--color-shade-40);
      margin-bottom: var(--space-sm);
    }

    .card-value {
      font-size: var(--font-size-heading-lg);
      font-weight: 600;
      color: var(--color-ink);
    }
  }

  .amount-income {
    color: #e74c3c;
    font-weight: 600;
  }

  .amount-refund {
    color: #27ae60;
    font-weight: 600;
  }

  .pagination-wrap {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}

@media (max-width: 1200px) {
  .finance-container .finance-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .finance-container .finance-cards {
    grid-template-columns: 1fr;
  }
}
</style>
