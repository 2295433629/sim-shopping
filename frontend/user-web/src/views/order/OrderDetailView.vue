<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderDetail, cancelOrder, confirmReceive, type OrderDetailVO } from '@/api/modules/order'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref<OrderDetailVO | null>(null)

const orderNo = route.params.orderNo as string

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

const statusTextMap: Record<string, string> = {
  CREATED: '待付款',
  PAID: '已付款',
  SHIPPED: '已发货',
  IN_TRANSIT: '运输中',
  OUT_FOR_DELIVERY: '配送中',
  DELIVERED: '已送达',
  COMPLETED: '已完成',
  CANCELLED: '已取消',
}

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
    await ElMessageBox.confirm('Cancel this order?', 'Confirm', {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning',
    })
    await cancelOrder(order.value.orderNo)
    ElMessage.success('Order cancelled')
    loadDetail()
  } catch {
    // cancelled or error
  }
}

async function handleConfirmReceive() {
  if (!order.value) return
  try {
    await ElMessageBox.confirm('Confirm receipt of this order?', 'Confirm', {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'success',
    })
    await confirmReceive(order.value.orderNo)
    ElMessage.success('Confirmed receipt')
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
  if (!order.value) return
  const firstItem = order.value.items[0]
  router.push(`/order/${order.value.orderNo}/review?productId=${firstItem.productId}`)
}
</script>

<template>
  <div class="order-detail-container" v-loading="loading">
    <el-page-header @back="router.back()" title="Back" content="Order Detail" />

    <div v-if="order" class="detail-content">
      <el-card shadow="never" class="section-card">
        <template #header>
          <div class="card-header">
            <span class="section-title">Order Status</span>
            <el-tag :type="(statusTagType[order.status] as any) || 'info'" size="large">
              {{ statusTextMap[order.status] || order.status }}
            </el-tag>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Order No">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="Shop">{{ order.shopName }}</el-descriptions-item>
          <el-descriptions-item label="Created At">{{ order.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="Paid At">{{ order.paidAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Shipped At">{{ order.shippedAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Delivered At">{{ order.deliveredAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Completed At">{{ order.completedAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Cancelled At">{{ order.cancelledAt || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">Items</span></template>
        <div v-for="item in order.items" :key="item.orderItemId" class="order-item">
          <el-image :src="item.productImage" fit="cover" class="item-image">
            <template #error><div class="img-fallback">N/A</div></template>
          </el-image>
          <div class="item-info">
            <div class="item-name">{{ item.productName }}</div>
            <div class="item-sku">{{ item.skuName }}</div>
          </div>
          <div class="item-price">${{ item.price.toFixed(2) }}</div>
          <div class="item-qty">x{{ item.quantity }}</div>
          <div class="item-subtotal">${{ item.subtotal.toFixed(2) }}</div>
        </div>
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
        <template #header><span class="section-title">Receiver Info</span></template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Name">{{ order.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="Phone">{{ order.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item label="Address">{{ order.receiverAddress }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">Payment & Logistics</span></template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Payment Method">{{ (order as any).payment?.paymentMethod || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Payment Status">{{ (order as any).payment?.paymentStatus || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Logistics Company">{{ (order as any).logistics?.company || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Tracking No">{{ (order as any).logistics?.trackingNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Remark">{{ order.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <div class="action-bar">
        <el-button v-if="order.status === 'CREATED'" type="primary" @click="handlePay">Pay Now</el-button>
        <el-button v-if="order.status === 'CREATED'" @click="handleCancel">Cancel Order</el-button>
        <el-button v-if="['SHIPPED', 'IN_TRANSIT', 'OUT_FOR_DELIVERY'].includes(order.status)" type="primary" @click="handleViewLogistics">
          查看物流
        </el-button>
        <el-button v-if="order.status === 'DELIVERED'" type="success" @click="handleConfirmReceive">
          Confirm Receive
        </el-button>
        <el-button v-if="['DELIVERED', 'COMPLETED'].includes(order.status)" type="warning" @click="handleReview">
          评价
        </el-button>
      </div>
    </div>
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
    font-size: 15px;
    font-weight: 600;
  }

  .section-card {
    margin-top: 16px;
  }

  .order-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .item-image {
      width: 64px;
      height: 64px;
      border-radius: 4px;
      flex-shrink: 0;

      .img-fallback {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #f5f5f5;
        color: #ccc;
        font-size: 12px;
      }
    }

    .item-info {
      flex: 1;

      .item-name {
        font-size: 14px;
        color: #333;
      }

      .item-sku {
        font-size: 12px;
        color: #999;
        margin-top: 2px;
      }
    }

    .item-price {
      color: #666;
      font-size: 13px;
    }

    .item-qty {
      color: #999;
    }

    .item-subtotal {
      width: 100px;
      text-align: right;
      color: #e4393c;
      font-weight: 600;
    }
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
    margin-top: 24px;
    display: flex;
    justify-content: center;
    gap: 12px;
  }
}
</style>
