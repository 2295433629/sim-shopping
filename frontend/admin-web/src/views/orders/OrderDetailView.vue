<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAdminOrderDetail, type OrderDetailVO } from '@/api/modules/order'
import { ORDER_STATUS_TAG_TYPE } from '@/constants/order'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref<OrderDetailVO | null>(null)

const orderNo = route.params.orderNo as string

const statusTagType = ORDER_STATUS_TAG_TYPE

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
    <el-page-header @back="router.back()" title="返回" content="订单详情" />

    <div v-if="order" class="detail-content">
      <el-card shadow="never" class="section-card">
        <template #header>
          <div class="card-header">
            <span class="section-title">订单信息</span>
            <el-tag :type="(statusTagType[order.status] as any) || 'info'" size="large">
              {{ order.statusText }}
            </el-tag>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="所属店铺">{{ order.shopName }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ order.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="支付时间">{{ order.paidAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="发货时间">{{ order.shippedAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="送达时间">{{ order.deliveredAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="完成时间">{{ order.completedAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="取消时间">{{ order.cancelledAt || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ order.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">买家与收货人</span></template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="买家姓名">{{ order.buyerName }}</el-descriptions-item>
          <el-descriptions-item label="买家电话">{{ order.buyerPhone }}</el-descriptions-item>
          <el-descriptions-item label="收货人姓名">{{ order.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="收货人电话">{{ order.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2">{{ order.receiverAddress }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">商品明细</span></template>
        <el-table :data="order.items" style="width: 100%">
          <el-table-column label="图片" width="80">
            <template #default="{ row }">
              <el-image :src="row.productImage" fit="cover" style="width: 50px; height: 50px; border-radius: 4px;">
                <template #error><div style="width:50px;height:50px;background:var(--color-canvas-cream);border-radius:4px;"></div></template>
              </el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" prop="productName" min-width="200" />
          <el-table-column label="规格" prop="skuName" width="150" />
          <el-table-column label="单价" width="100" align="right">
            <template #default="{ row }">¥{{ (row.price ?? 0).toFixed(2) }}</template>
          </el-table-column>
          <el-table-column label="数量" prop="quantity" width="80" align="center" />
          <el-table-column label="小计" width="120" align="right">
            <template #default="{ row }">
              <span style="color: var(--color-price); font-weight: 600;">¥{{ (row.subtotal ?? 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
        </el-table>
        <el-divider />
        <div class="amount-summary">
          <div class="summary-row">
            <span>商品合计：</span>
            <span>¥{{ order.totalAmount.toFixed(2) }}</span>
          </div>
          <div class="summary-row">
            <span>运费：</span>
            <span>¥{{ order.shippingFee.toFixed(2) }}</span>
          </div>
          <div class="summary-row total">
            <span>订单总计：</span>
            <span class="pay-amount">¥{{ order.payAmount.toFixed(2) }}</span>
          </div>
        </div>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">支付与物流</span></template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="支付方式">{{ order.paymentMethod || '-' }}</el-descriptions-item>
          <el-descriptions-item label="支付状态">{{ order.paymentStatus || '-' }}</el-descriptions-item>
          <el-descriptions-item label="物流公司">{{ order.logisticsCompany || '-' }}</el-descriptions-item>
          <el-descriptions-item label="运单号">{{ order.logisticsNo || '-' }}</el-descriptions-item>
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
    font-size: var(--font-size-body-md);
    font-weight: 600;
  }

  .section-card {
    margin-top: var(--space-lg);
  }

  .amount-summary {
    .summary-row {
      display: flex;
      justify-content: space-between;
      padding: var(--space-sm) 0;
      font-size: var(--font-size-caption);
      color: var(--color-shade-50);

      &.total {
        font-size: var(--font-size-body-md);
        font-weight: 600;
        color: var(--color-ink);
      }

      .pay-amount {
        color: var(--color-price);
        font-size: var(--font-size-heading-lg);
        font-weight: 700;
      }
    }
  }
}
</style>
