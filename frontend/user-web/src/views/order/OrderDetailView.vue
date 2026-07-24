<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderDetail, cancelOrder, confirmReceive, applyRefund, type OrderDetailVO } from '@/api/modules/order'
import { ORDER_STATUS_TAG_TYPE, ORDER_STATUS_TEXT } from '@/constants/order'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref<OrderDetailVO | null>(null)
const refundDialogVisible = ref(false)
const refundForm = ref({ refundType: 'REFUND_ONLY', reason: '', amount: 0 })

const orderNo = route.params.orderNo as string

const statusTagType: Record<string, 'success' | 'warning' | 'info' | 'danger' | 'primary'> = ORDER_STATUS_TAG_TYPE as Record<string, 'success' | 'warning' | 'info' | 'danger' | 'primary'>
const statusTextMap = ORDER_STATUS_TEXT

const paymentMethodMap: Record<string, string> = {
  ALIPAY: '支付宝',
  WECHAT: '微信支付',
}

const paymentStatusMap: Record<string, string> = {
  PENDING: '待支付',
  SUCCESS: '支付成功',
  FAILED: '支付失败',
  CLOSED: '已关闭',
}

const refundTypeMap: Record<string, string> = {
  REFUND_ONLY: '仅退款',
  RETURN_REFUND: '退货退款',
}

const refundStatusMap: Record<string, string> = {
  PENDING: '退款申请中',
  APPROVED: '已同意退款',
  COMPLETED: '退款已完成',
  REJECTED: '退款已拒绝',
}

const refundStatusType: Record<string, 'success' | 'warning' | 'danger' | 'info'> = {
  PENDING: 'warning',
  APPROVED: 'success',
  COMPLETED: 'success',
  REJECTED: 'danger',
}

// 判断是否可以退款：已支付且未取消且3天内
const canRefund = computed(() => {
  if (!order.value) return false
  if (order.value.refund) return false // 已有退款申请
  const refundableStatuses = ['PAID', 'SHIPPED', 'IN_TRANSIT', 'OUT_FOR_DELIVERY', 'DELIVERED']
  if (!refundableStatuses.includes(order.value.status)) return false
  // 检查是否在3天内
  if (!order.value.paidAt) return false
  const paidTime = new Date(order.value.paidAt).getTime()
  const now = Date.now()
  const threeDays = 3 * 24 * 60 * 60 * 1000
  return now - paidTime <= threeDays
})

onMounted(() => {
  loadDetail()
})

async function loadDetail() {
  loading.value = true
  try {
    order.value = await getOrderDetail(orderNo)
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

async function handleCancel() {
  if (!order.value) return
  try {
    await ElMessageBox.confirm('确定取消该订单？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await cancelOrder(order.value.orderNo)
    ElMessage.success('订单已取消')
    loadDetail()
  } catch {
    // cancelled or error
  }
}

async function handleConfirmReceive() {
  if (!order.value) return
  try {
    await ElMessageBox.confirm('确认已收到该订单商品？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success',
    })
    await confirmReceive(order.value.orderNo)
    ElMessage.success('已确认收货')
    loadDetail()
  } catch {
    // cancelled or error
  }
}

function handlePay() {
  if (!order.value) return
  router.push(`/payment/${order.value.orderNo}`)
}

function handleViewLogistics() {
  if (!order.value) return
  router.push(`/order/${order.value.orderNo}/logistics`)
}

function handleReview() {
  if (!order.value || !order.value.items?.length) return
  const firstItem = order.value.items[0]
  router.push(`/order/${order.value.orderNo}/review?productId=${firstItem.productId}`)
}

function openRefundDialog() {
  if (!order.value) return
  refundForm.value = {
    refundType: 'REFUND_ONLY',
    reason: '',
    amount: order.value.payAmount,
  }
  refundDialogVisible.value = true
}

async function handleRefundSubmit() {
  if (!order.value) return
  if (!refundForm.value.reason.trim()) {
    ElMessage.warning('请填写退款原因')
    return
  }
  try {
    await applyRefund(order.value.orderNo, {
      refundType: refundForm.value.refundType,
      reason: refundForm.value.reason,
      amount: refundForm.value.amount,
    })
    ElMessage.success('退款申请已提交')
    refundDialogVisible.value = false
    loadDetail()
  } catch {
    // handled by interceptor
  }
}
</script>

<template>
  <div class="order-detail-container" v-loading="loading">
    <el-page-header @back="router.back()" title="返回" content="订单详情" />

    <div v-if="order" class="detail-content">
      <el-card shadow="never" class="section-card">
        <template #header>
          <div class="card-header">
            <span class="section-title">订单状态</span>
            <el-tag :type="statusTagType[order.status] || 'info'" size="large">
              {{ statusTextMap[order.status] || order.status }}
            </el-tag>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单编号">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="店铺">{{ order.shopName }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ order.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="付款时间">{{ order.paidAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="发货时间">{{ order.shippedAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="送达时间">{{ order.deliveredAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="完成时间">{{ order.completedAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="取消时间">{{ order.cancelledAt || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 退款信息 -->
      <el-card v-if="order.refund" shadow="never" class="section-card">
        <template #header>
          <div class="card-header">
            <span class="section-title">退款信息</span>
            <el-tag :type="refundStatusType[order.refund.status] || 'info'" size="large">
              {{ refundStatusMap[order.refund.status] || order.refund.status }}
            </el-tag>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="退款类型">{{ refundTypeMap[order.refund.refundType] || order.refund.refundType }}</el-descriptions-item>
          <el-descriptions-item label="退款金额">¥{{ order.refund.amount.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="退款原因">{{ order.refund.reason }}</el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ order.refund.createdAt }}</el-descriptions-item>
          <el-descriptions-item v-if="order.refund.shopResponse" label="商家回复">{{ order.refund.shopResponse }}</el-descriptions-item>
          <el-descriptions-item v-if="order.refund.completedAt" label="完成时间">{{ order.refund.completedAt }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">商品信息</span></template>
        <div v-for="item in order.items" :key="`${item.productId}-${item.skuId}`" class="order-item">
          <el-image :src="item.productImage" fit="cover" class="item-image">
            <template #error><div class="img-fallback">暂无图片</div></template>
          </el-image>
          <div class="item-info">
            <div class="item-name">{{ item.productName }}</div>
            <div class="item-sku">{{ item.skuName }}</div>
          </div>
          <div class="item-price">¥{{ (item.price ?? 0).toFixed(2) }}</div>
          <div class="item-qty">x{{ item.quantity }}</div>
          <div class="item-subtotal">¥{{ (item.subtotal ?? 0).toFixed(2) }}</div>
        </div>
        <el-divider />
        <div class="amount-summary">
          <div class="summary-row">
            <span>商品总额：</span>
            <span>¥{{ (order.totalAmount ?? 0).toFixed(2) }}</span>
          </div>
          <div class="summary-row">
            <span>运费：</span>
            <span>¥{{ (order.shippingFee ?? 0).toFixed(2) }}</span>
          </div>
          <div class="summary-row">
            <span>优惠金额：</span>
            <span>¥{{ (order.discountAmount ?? 0).toFixed(2) }}</span>
          </div>
          <div class="summary-row total">
            <span>应付总额：</span>
            <span class="pay-amount">¥{{ (order.payAmount ?? 0).toFixed(2) }}</span>
          </div>
        </div>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">收货信息</span></template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="收货人">{{ order.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ order.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item label="收货地址">{{ order.receiverAddress }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">支付与物流</span></template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="支付方式">{{ paymentMethodMap[order.payment?.paymentMethod || ''] || order.payment?.paymentMethod || '-' }}</el-descriptions-item>
          <el-descriptions-item label="支付状态">{{ paymentStatusMap[order.payment?.status || ''] || order.payment?.status || '-' }}</el-descriptions-item>
          <el-descriptions-item label="物流公司">{{ order.logistics?.logisticsCompany || '-' }}</el-descriptions-item>
          <el-descriptions-item label="物流单号">{{ order.logistics?.trackingNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="订单备注">{{ order.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <div class="action-bar">
        <el-button v-if="order.status === 'CREATED'" type="primary" @click="handlePay">立即支付</el-button>
        <el-button v-if="order.status === 'CREATED'" @click="handleCancel">取消订单</el-button>
        <el-button v-if="['SHIPPED', 'IN_TRANSIT', 'OUT_FOR_DELIVERY'].includes(order.status)" type="primary" @click="handleViewLogistics">
          查看物流
        </el-button>
        <el-button v-if="order.status === 'DELIVERED'" type="success" @click="handleConfirmReceive">
          确认收货
        </el-button>
        <el-button v-if="['DELIVERED', 'COMPLETED'].includes(order.status)" type="warning" @click="handleReview">
          评价
        </el-button>
        <el-button v-if="canRefund" type="danger" @click="openRefundDialog">
          申请退款
        </el-button>
      </div>
    </div>

    <!-- 退款申请弹窗 -->
    <el-dialog v-model="refundDialogVisible" title="申请退款" width="500px">
      <el-form :model="refundForm" label-width="100px">
        <el-form-item label="退款类型">
          <el-radio-group v-model="refundForm.refundType">
            <el-radio label="REFUND_ONLY">仅退款</el-radio>
            <el-radio label="RETURN_REFUND">退货退款</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="退款金额">
          <el-input-number v-model="refundForm.amount" :min="0.01" :max="order?.payAmount || 0" :precision="2" controls-position="right" style="width: 200px" />
          <span style="margin-left: 8px; color: #999;">最多 ¥{{ order?.payAmount.toFixed(2) }}</span>
        </el-form-item>
        <el-form-item label="退款原因">
          <el-input v-model="refundForm.reason" type="textarea" :rows="3" placeholder="请填写退款原因" maxlength="255" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="refundDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRefundSubmit">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.order-detail-container {
  max-width: 800px;
  margin: 0 auto;

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .section-title {
    font-size: var(--font-size-heading-sm);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .section-card {
    margin-top: var(--space-lg);
  }

  .order-item {
    display: flex;
    align-items: center;
    gap: var(--space-md);
    padding: var(--space-md) 0;
    border-bottom: 1px solid var(--color-hairline-light);

    &:last-child {
      border-bottom: none;
    }

    .item-image {
      width: 64px;
      height: 64px;
      border-radius: var(--rounded-md);
      flex-shrink: 0;

      .img-fallback {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        background: var(--color-canvas-cream);
        color: var(--color-shade-30);
        font-size: var(--font-size-eyebrow);
      }
    }

    .item-info {
      flex: 1;

      .item-name {
        font-size: var(--font-size-caption);
        color: var(--color-ink);
      }

      .item-sku {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-40);
        margin-top: 2px;
      }
    }

    .item-price {
      color: var(--color-shade-50);
      font-size: var(--font-size-micro);
    }

    .item-qty {
      color: var(--color-shade-40);
    }

    .item-subtotal {
      width: 100px;
      text-align: right;
      color: var(--color-price);
      font-weight: 500;
    }
  }

  .amount-summary {
    .summary-row {
      display: flex;
      justify-content: space-between;
      padding: 6px 0;
      font-size: var(--font-size-caption);
      color: var(--color-shade-50);

      &.total {
        font-size: var(--font-size-body-md);
        font-weight: 500;
        color: var(--color-ink);
      }

      .pay-amount {
        color: var(--color-price);
        font-size: 22px;
        font-weight: 700;
      }
    }
  }

  .action-bar {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
    gap: var(--space-md);
    flex-wrap: wrap;
  }
}
</style>
