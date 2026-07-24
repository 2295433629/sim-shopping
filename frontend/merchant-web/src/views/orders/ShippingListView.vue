<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getShippingOrders, createShipment, type CreateShipmentParams } from '@/api/modules/shipment'
import { usePagination } from '@/composables/usePagination'
import type { PageResponse } from '@/types/common'

interface ShippingOrderItem {
  orderNo: string
  totalAmount: number | string
  createdAt: string
}

const {
  loading,
  page,
  pageSize,
  total,
  list: orderList,
  loadList: loadOrders,
  handlePageChange,
} = usePagination<ShippingOrderItem>(
  async () => {
    const data = await getShippingOrders({ page: page.value, size: pageSize.value }) as unknown as PageResponse<ShippingOrderItem>
    return { list: data.list || [], total: data.total || 0 }
  }
)

// 发货对话框
const shipDialogVisible = ref(false)
const shipForm = ref<CreateShipmentParams>({
  orderNo: '',
  logisticsCompany: '',
  trackingNo: '',
})
const shipFormLoading = ref(false)

const logisticsOptions = [
  { label: '顺丰快递', value: '顺丰快递' },
  { label: '圆通快递', value: '圆通快递' },
  { label: '中通快递', value: '中通快递' },
  { label: '韵达快递', value: '韵达快递' },
  { label: '申通快递', value: '申通快递' },
  { label: '京东物流', value: '京东物流' },
]

onMounted(() => {
  loadOrders()
})

function openShipDialog(row: ShippingOrderItem) {
  shipForm.value = {
    orderNo: row.orderNo,
    logisticsCompany: '',
    trackingNo: '',
  }
  shipDialogVisible.value = true
}

async function handleShip() {
  if (!shipForm.value.logisticsCompany) {
    ElMessage.warning('请选择物流公司')
    return
  }
  if (!shipForm.value.trackingNo.trim()) {
    ElMessage.warning('请输入运单号')
    return
  }
  try {
    await ElMessageBox.confirm(
      `确认发货？物流公司：${shipForm.value.logisticsCompany}，运单号：${shipForm.value.trackingNo}`,
      '确认发货',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'info' }
    )
  } catch {
    return // 用户取消
  }

  shipFormLoading.value = true
  try {
    await createShipment(shipForm.value)
    ElMessage.success('发货成功')
    shipDialogVisible.value = false
    loadOrders()
  } catch {
    // handled by interceptor
  } finally {
    shipFormLoading.value = false
  }
}
</script>

<template>
  <div class="shipping-list-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">待发货订单</span>
      </template>

      <el-table :data="orderList" v-loading="loading" style="width: 100%" stripe>
        <el-table-column label="订单号" prop="orderNo" width="220" />
        <el-table-column label="订单金额" width="150" align="right">
          <template #default="{ row }">
            <span>¥{{ row.totalAmount != null ? Number(row.totalAmount).toFixed(2) : '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" prop="createdAt" min-width="180" />
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openShipDialog(row)">发货</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && orderList.length === 0" description="暂无待发货订单" />

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

    <!-- 发货对话框 -->
    <el-dialog v-model="shipDialogVisible" title="订单发货" width="500px" :close-on-click-modal="false">
      <el-form :model="shipForm" label-width="90px">
        <el-form-item label="订单号">
          <span>{{ shipForm.orderNo }}</span>
        </el-form-item>
        <el-form-item label="物流公司" required>
          <el-select v-model="shipForm.logisticsCompany" placeholder="请选择物流公司" style="width: 100%">
            <el-option
              v-for="opt in logisticsOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="运单号" required>
          <el-input v-model="shipForm.trackingNo" placeholder="请输入运单号" maxlength="30" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shipDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="shipFormLoading" @click="handleShip">确认发货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.shipping-list-container {
  .card-title {
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
    font-size: var(--font-size-heading-lg);
    color: var(--color-ink);
  }

  .pagination-wrap {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
