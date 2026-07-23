<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrders, cancelOrder, confirmReceive, applyRefund, type OrderListVO } from '@/api/modules/order'

const router = useRouter()
const loading = ref(false)
const orderList = ref<OrderListVO[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const activeTab = ref('')

const tabs = [
  { label: '全部', value: '' },
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
  loadOrders()
})

async function loadOrders() {
  loading.value = true
  try {
    const res = await getOrders({
      page: page.value,
      size: size.value,
      status: activeTab.value || undefined,
    })
    orderList.value = res.list || []
    total.value = res.total || 0
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

function handleTabChange(val: string) {
  activeTab.value = val
  page.value = 1
  loadOrders()
}

function handlePageChange(p: number) {
  page.value = p
  loadOrders()
}

async function handleCancel(order: OrderListVO) {
  try {
    await ElMessageBox.confirm('确定取消该订单？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await cancelOrder(order.orderNo)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch {
    // cancelled or error
  }
}

async function handleConfirmReceive(order: OrderListVO) {
  try {
    await ElMessageBox.confirm('确认已收到该订单商品？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success',
    })
    await confirmReceive(order.orderNo)
    ElMessage.success('已确认收货')
    loadOrders()
  } catch {
    // cancelled or error
  }
}

function handlePay(order: OrderListVO) {
  router.push(`/payment/${order.orderNo}`)
}

function handleDetail(order: OrderListVO) {
  router.push(`/orders/${order.orderNo}`)
}

async function handleRefund(order: OrderListVO) {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入退款原因', '申请退款', {
      confirmButtonText: '提交',
      cancelButtonText: '取消',
      inputType: 'textarea',
      inputValidator: (val: string) => (val && val.trim() ? true : '请输入退款原因'),
    })
    await applyRefund(order.orderNo, { refundType: 'REFUND_ONLY', reason: reason.trim(), amount: order.payAmount })
    ElMessage.success('退款申请已提交')
    loadOrders()
  } catch {
    // cancelled or error
  }
}
</script>

<template>
  <div class="order-list-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">我的订单</span>
      </template>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane
          v-for="tab in tabs"
          :key="tab.value"
          :label="tab.label"
          :name="tab.value"
        />
      </el-tabs>

      <div v-loading="loading" class="order-list">
        <div v-for="order in orderList" :key="order.orderNo" class="order-card">
          <div class="order-header">
            <span class="order-shop">{{ order.shopName }}</span>
            <el-tag :type="(statusTagType[order.status] as any) || 'info'" size="small">
              {{ statusTextMap[order.status] || order.status }}
            </el-tag>
          </div>

          <div class="order-items">
            <div
              v-for="item in order.items"
              :key="`${item.productId}-${item.skuId}`"
              class="order-item"
            >
              <el-image :src="item.productImage" fit="cover" class="item-image" />
              <div class="item-info">
                <div class="item-name">{{ item.productName }}</div>
                <div class="item-sku">{{ item.skuName }}</div>
              </div>
              <div class="item-price">¥{{ item.price.toFixed(2) }}</div>
              <div class="item-qty">x{{ item.quantity }}</div>
            </div>
          </div>

          <div class="order-footer">
            <span class="item-count">共 {{ order.items.length }} 件商品</span>
            <span class="order-total">
              合计：<span class="amount">¥{{ order.payAmount.toFixed(2) }}</span>
            </span>
          </div>

          <div class="order-actions">
            <el-button v-if="order.status === 'CREATED'" type="primary" size="small" @click="handlePay(order)">
              立即支付
            </el-button>
            <el-button v-if="order.status === 'CREATED'" size="small" @click="handleCancel(order)">
              取消订单
            </el-button>
            <el-button v-if="order.status === 'PAID'" size="small" type="warning" @click="handleRefund(order)">
              申请退款
            </el-button>
            <el-button v-if="order.status === 'DELIVERED'" type="success" size="small" @click="handleConfirmReceive(order)">
              确认收货
            </el-button>
            <el-button size="small" @click="handleDetail(order)">订单详情</el-button>
          </div>
        </div>

        <el-empty v-if="!loading && orderList.length === 0" description="暂无订单" />
      </div>

      <div class="pagination-wrapper" v-if="total > 0">
        <el-pagination
          v-model:current-page="page"
          :page-size="size"
          :total="total"
          layout="prev, pager, next, total"
          background
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.order-list-container {
  max-width: 900px;
  margin: 0 auto;

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .order-list {
    min-height: 300px;
  }

  .order-card {
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    padding: var(--space-lg);
    margin-bottom: var(--space-lg);
    background: var(--color-canvas-light);

    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: var(--space-md);
      padding-bottom: var(--space-md);
      border-bottom: 1px solid var(--color-hairline-light);

      .order-shop {
        font-weight: 500;
        font-size: var(--font-size-caption);
      }
    }

    .order-items {
      .order-item {
        display: flex;
        align-items: center;
        gap: var(--space-md);
        padding: var(--space-sm) 0;

        .item-image {
          width: 60px;
          height: 60px;
          border-radius: var(--rounded-md);
          flex-shrink: 0;
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
          font-size: var(--font-size-micro);
        }
      }
    }

    .order-footer {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      gap: var(--space-md);
      margin-top: var(--space-md);
      padding-top: var(--space-md);
      border-top: 1px solid var(--color-hairline-light);

      .item-count {
        font-size: var(--font-size-micro);
        color: var(--color-shade-40);
      }

      .order-total {
        font-size: var(--font-size-caption);

        .amount {
          color: var(--color-price);
          font-weight: 500;
          font-size: var(--font-size-body-md);
        }
      }
    }

    .order-actions {
      display: flex;
      justify-content: flex-end;
      gap: var(--space-sm);
      margin-top: var(--space-md);
    }
  }

  .pagination-wrapper {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
