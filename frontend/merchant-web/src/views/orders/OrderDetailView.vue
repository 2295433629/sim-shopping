<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMerchantOrderDetail, type OrderDetailVO } from '@/api/modules/order'
import { createShipment, getLogisticsInfo, type CreateShipmentParams } from '@/api/modules/shipment'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref<OrderDetailVO | null>(null)

const orderNo = route.params.orderNo as string

const statusTagType: Record<string, string> = {
  CREATED: 'warning',
  PAID: 'primary',
  SHIPPED: 'info',
  DELIVERED: 'success',
  COMPLETED: 'success',
  CANCELLED: 'danger',
}

const statusTextMap: Record<string, string> = {
  CREATED: '待付款',
  PAID: '已付款',
  SHIPPED: '已发货',
  IN_TRANSIT: '运输中',
  DELIVERED: '已送达',
  COMPLETED: '已完成',
  CANCELLED: '已取消',
}

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

// 物流信息对话框
const logisticsDialogVisible = ref(false)
const logisticsLoading = ref(false)
const logisticsData = ref<any>(null)

onMounted(() => {
  loadDetail()
})

async function loadDetail() {
  loading.value = true
  try {
    order.value = await getMerchantOrderDetail(orderNo)
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

function openShipDialog() {
  shipForm.value = {
    orderNo: orderNo,
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
    return
  }

  shipFormLoading.value = true
  try {
    await createShipment(shipForm.value)
    ElMessage.success('发货成功')
    shipDialogVisible.value = false
    loadDetail()
  } catch {
    // handled by interceptor
  } finally {
    shipFormLoading.value = false
  }
}

async function openLogisticsDialog() {
  logisticsDialogVisible.value = true
  logisticsLoading.value = true
  try {
    logisticsData.value = await getLogisticsInfo(orderNo)
  } catch {
    logisticsData.value = null
  } finally {
    logisticsLoading.value = false
  }
}
</script>

<template>
  <div class="order-detail-container" v-loading="loading">
    <el-page-header @back="router.back()" title="Back" content="Order Detail" />

    <div v-if="order" class="detail-content">
      <el-card shadow="never" class="section-card">
        <template #header>
          <div class="card-header">
            <span class="section-title">Order Info</span>
            <el-tag :type="(statusTagType[order.status] as any) || 'info'" size="large">
              {{ statusTextMap[order.status] || order.status }}
            </el-tag>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Order No">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="Created At">{{ order.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="Paid At">{{ order.paidAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Shipped At">{{ order.shippedAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Delivered At">{{ order.deliveredAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Completed At">{{ order.completedAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Cancelled At">{{ order.cancelledAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Remark">{{ order.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">Buyer Info</span></template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Buyer Name">{{ order.buyerName }}</el-descriptions-item>
          <el-descriptions-item label="Buyer Phone">{{ order.buyerPhone }}</el-descriptions-item>
          <el-descriptions-item label="Receiver Name">{{ order.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="Receiver Phone">{{ order.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item label="Receiver Address" :span="2">{{ order.receiverAddress }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">Order Items</span></template>
        <el-table :data="order.items" style="width: 100%">
          <el-table-column label="Image" width="80">
            <template #default="{ row }">
              <el-image :src="row.productImage" fit="cover" style="width: 50px; height: 50px; border-radius: 4px;">
                <template #error><div style="width:50px;height:50px;background:#f5f5f5;border-radius:4px;"></div></template>
              </el-image>
            </template>
          </el-table-column>
          <el-table-column label="Product" prop="productName" min-width="200" />
          <el-table-column label="SKU" prop="skuName" width="150" />
          <el-table-column label="Price" width="100" align="right">
            <template #default="{ row }">${{ row.price.toFixed(2) }}</template>
          </el-table-column>
          <el-table-column label="Qty" prop="quantity" width="80" align="center" />
          <el-table-column label="Subtotal" width="120" align="right">
            <template #default="{ row }">
              <span style="color: #e4393c; font-weight: 600;">${{ row.subtotal.toFixed(2) }}</span>
            </template>
          </el-table-column>
        </el-table>
        <el-divider />
        <div class="amount-summary">
          <div class="summary-row">
            <span>Items Total:</span>
            <span>${{ order.totalAmount.toFixed(2) }}</span>
          </div>
          <div class="summary-row">
            <span>Shipping Fee:</span>
            <span>${{ order.shippingFee.toFixed(2) }}</span>
          </div>
          <div class="summary-row total">
            <span>Total:</span>
            <span class="pay-amount">${{ order.payAmount.toFixed(2) }}</span>
          </div>
        </div>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">Payment & Logistics</span></template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Payment Method">{{ (order as any).payment?.paymentMethod || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Payment Status">{{ (order as any).payment?.paymentStatus || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Logistics Company">{{ (order as any).logistics?.company || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Tracking No">{{ (order as any).logistics?.trackingNo || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 操作按钮 -->
      <div class="action-bar">
        <el-button v-if="order.status === 'PAID'" type="primary" @click="openShipDialog">发货</el-button>
        <el-button v-if="order.status === 'SHIPPED' || order.status === 'IN_TRANSIT'" type="success" @click="openLogisticsDialog">查看物流</el-button>
      </div>
    </div>

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

    <!-- 物流信息对话框 -->
    <el-dialog v-model="logisticsDialogVisible" title="物流信息" width="600px">
      <div v-loading="logisticsLoading">
        <template v-if="logisticsData">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="物流公司">{{ logisticsData.logisticsCompany || order?.logisticsCompany || '-' }}</el-descriptions-item>
            <el-descriptions-item label="运单号">{{ logisticsData.trackingNo || order?.logisticsNo || '-' }}</el-descriptions-item>
          </el-descriptions>
          <div v-if="logisticsData.traces && logisticsData.traces.length > 0" class="logistics-traces">
            <div class="trace-title">物流轨迹</div>
            <el-timeline>
              <el-timeline-item
                v-for="(trace, idx) in logisticsData.traces"
                :key="idx"
                :timestamp="trace.occurredAt"
                placement="top"
              >
                {{ trace.description }}
              </el-timeline-item>
            </el-timeline>
          </div>
          <el-empty v-else description="暂无物流轨迹信息" />
        </template>
        <el-empty v-else description="暂无物流信息" />
      </div>
      <template #footer>
        <el-button @click="logisticsDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.order-detail-container {
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .section-title {
    font-size: 15px;
    font-weight: 600;
  }

  .section-card {
    margin-top: 16px;
  }

  .amount-summary {
    .summary-row {
      display: flex;
      justify-content: space-between;
      padding: 6px 0;
      font-size: 14px;
      color: #666;

      &.total {
        font-size: 16px;
        font-weight: 600;
        color: #333;
      }

      .pay-amount {
        color: #e4393c;
        font-size: 22px;
        font-weight: 700;
      }
    }
  }

  .action-bar {
    margin-top: 20px;
    display: flex;
    gap: 12px;
  }

  .logistics-traces {
    margin-top: 16px;

    .trace-title {
      font-size: 14px;
      font-weight: 600;
      color: #333;
      margin-bottom: 12px;
    }
  }
}
</style>
