<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrders, cancelOrder, confirmReceive, type OrderListVO } from '@/api/modules/order'

const router = useRouter()
const loading = ref(false)
const orderList = ref<OrderListVO[]>([])
const activeTab = ref('all')
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const tabs = [
  { label: 'All', value: 'all' },
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
    const data = await getOrders(params as any)
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

async function handleCancel(order: OrderListVO) {
  try {
    await ElMessageBox.confirm('Cancel this order?', 'Confirm', {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning',
    })
    await cancelOrder(order.orderNo)
    ElMessage.success('Order cancelled')
    loadOrders()
  } catch {
    // cancelled or error
  }
}

async function handleConfirmReceive(order: OrderListVO) {
  try {
    await ElMessageBox.confirm('Confirm receipt of this order?', 'Confirm', {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'success',
    })
    await confirmReceive(order.orderNo)
    ElMessage.success('Confirmed receipt')
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
</script>

<template>
  <div class="order-list-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">My Orders</span>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane v-for="tab in tabs" :key="tab.value" :label="tab.label" :name="tab.value" />
      </el-tabs>

      <div v-loading="loading">
        <div v-for="order in orderList" :key="order.orderNo" class="order-card">
          <div class="order-header">
            <div class="header-left">
              <span class="shop-name">{{ order.shopName }}</span>
              <el-tag :type="(statusTagType[order.status] as any) || 'info'" size="small">
                {{ order.statusText }}
              </el-tag>
            </div>
            <span class="order-no">No: {{ order.orderNo }}</span>
          </div>

          <div class="order-body">
            <div class="order-items">
              <el-image
                v-for="item in order.items.slice(0, 4)"
                :key="item.orderItemId"
                :src="item.productImage"
                fit="cover"
                class="item-thumb"
              >
                <template #error><div class="thumb-fallback">N/A</div></template>
              </el-image>
              <div class="items-info">
                <div class="item-name" v-if="order.items[0]">{{ order.items[0].productName }}</div>
                <div class="item-count">Total {{ order.items.length }} item(s)</div>
              </div>
            </div>
            <div class="order-amount">
              <div class="amount-label">Total</div>
              <div class="amount-value">${{ order.payAmount.toFixed(2) }}</div>
            </div>
          </div>

          <div class="order-footer">
            <span class="order-time">{{ order.createdAt }}</span>
            <div class="order-actions">
              <el-button v-if="order.status === 'CREATED'" type="primary" size="small" @click="handlePay(order)">
                Pay Now
              </el-button>
              <el-button v-if="order.status === 'CREATED'" size="small" @click="handleCancel(order)">
                Cancel
              </el-button>
              <el-button v-if="order.status === 'DELIVERED'" type="success" size="small" @click="handleConfirmReceive(order)">
                Confirm Receive
              </el-button>
              <el-button size="small" @click="handleDetail(order)">Detail</el-button>
            </div>
          </div>
        </div>

        <el-empty v-if="!loading && orderList.length === 0" description="No orders" />

        <div v-if="total > pageSize" class="pagination-wrap">
          <el-pagination
            v-model:current-page="page"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.order-list-container {
  max-width: 900px;
  margin: 0 auto;

  .card-title {
    font-size: 16px;
    font-weight: bold;
  }

  .order-card {
    border: 1px solid #ebeef5;
    border-radius: 8px;
    margin-bottom: 16px;
    overflow: hidden;

    .order-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 12px 16px;
      background: #fafafa;
      border-bottom: 1px solid #f0f0f0;

      .header-left {
        display: flex;
        align-items: center;
        gap: 12px;

        .shop-name {
          font-weight: 600;
          font-size: 14px;
        }
      }

      .order-no {
        font-size: 12px;
        color: #999;
      }
    }

    .order-body {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 16px;

      .order-items {
        display: flex;
        align-items: center;
        gap: 8px;

        .item-thumb {
          width: 60px;
          height: 60px;
          border-radius: 4px;
          flex-shrink: 0;

          .thumb-fallback {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #f5f5f5;
            color: #ccc;
            font-size: 10px;
          }
        }

        .items-info {
          .item-name {
            font-size: 14px;
            color: #333;
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .item-count {
            font-size: 12px;
            color: #999;
            margin-top: 4px;
          }
        }
      }

      .order-amount {
        text-align: right;

        .amount-label {
          font-size: 12px;
          color: #999;
        }

        .amount-value {
          font-size: 18px;
          font-weight: 700;
          color: #e4393c;
        }
      }
    }

    .order-footer {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 12px 16px;
      border-top: 1px solid #f0f0f0;
      background: #fff;

      .order-time {
        font-size: 12px;
        color: #999;
      }

      .order-actions {
        display: flex;
        gap: 8px;
      }
    }
  }

  .pagination-wrap {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>
