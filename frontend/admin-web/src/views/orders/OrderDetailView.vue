<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAdminOrderDetail, type OrderDetailVO } from '@/api/modules/order'

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

onMounted(() => {
  loadDetail()
})

async function loadDetail() {
  loading.value = true
  try {
    order.value = await getAdminOrderDetail(orderNo)
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
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
              {{ order.statusText }}
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
          <el-descriptions-item label="Remark" :span="2">{{ order.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">Buyer & Receiver</span></template>
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
          <el-descriptions-item label="Payment Method">{{ order.paymentMethod || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Payment Status">{{ order.paymentStatus || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Logistics Company">{{ order.logisticsCompany || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Tracking No">{{ order.logisticsNo || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
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
}
</style>
