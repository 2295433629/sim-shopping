<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createPayment, getPaymentStatus, getOrderDetail, type OrderDetailVO } from '@/api/modules/order'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const paying = ref(false)
const paymentSuccess = ref(false)
const order = ref<OrderDetailVO | null>(null)
const paymentMethod = ref('ALIPAY')

const orderNo = ref(route.params.orderNo as string)

let pollTimer: ReturnType<typeof setInterval> | null = null

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
  loadOrder()
})

onUnmounted(() => {
  if (pollTimer) {
    clearInterval(pollTimer)
    pollTimer = null
  }
})

async function loadOrder() {
  loading.value = true
  try {
    order.value = await getOrderDetail(orderNo.value)
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

async function handlePay() {
  if (!order.value) return
  paying.value = true
  try {
    await createPayment(orderNo.value, paymentMethod.value)
    ElMessage.success('支付已提交，等待确认...')
    startPolling()
  } catch {
    // handled by interceptor
  } finally {
    paying.value = false
  }
}

function startPolling() {
  if (pollTimer) clearInterval(pollTimer)
  let attempts = 0
  pollTimer = setInterval(async () => {
    attempts++
    if (attempts > 30) {
      if (pollTimer) clearInterval(pollTimer)
      ElMessage.warning('支付确认超时，请稍后到订单列表查看状态')
      return
    }
    try {
      const result = await getPaymentStatus(orderNo.value)
      if (result.status === 'SUCCESS') {
        if (pollTimer) clearInterval(pollTimer)
        paymentSuccess.value = true
        ElMessage.success('支付成功')
        setTimeout(() => {
          router.push(`/orders/${orderNo.value}`)
        }, 2000)
      }
    } catch {
      // ignore polling errors
    }
  }, 1000)
}
</script>

<template>
  <div class="payment-container" v-loading="loading">
    <el-page-header @back="router.back()" title="返回" content="支付订单" />

    <div v-if="paymentSuccess" class="success-box">
      <el-result icon="success" title="支付成功" sub-title="正在跳转订单详情..." />
    </div>

    <div v-else-if="order" class="payment-content">
      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">订单信息</span></template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单编号">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="店铺">{{ order.shopName }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag type="warning">{{ statusTextMap[order.status] || order.status }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="支付金额">
            <span class="pay-amount">¥{{ order.payAmount.toFixed(2) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header><span class="section-title">支付方式</span></template>
        <el-radio-group v-model="paymentMethod">
          <el-radio label="ALIPAY">💰 模拟支付宝</el-radio>
          <el-radio label="WECHAT">💬 模拟微信支付</el-radio>
        </el-radio-group>
      </el-card>

      <div class="pay-bar">
        <span class="pay-info">
          应付：<span class="pay-amount">¥{{ order.payAmount.toFixed(2) }}</span>
        </span>
        <el-button
          type="primary"
          size="large"
          :loading="paying"
          @click="handlePay"
        >
          {{ paying ? '支付中...' : '立即支付' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.payment-container {
  max-width: 600px;
  margin: 0 auto;

  .success-box {
    margin-top: var(--space-huge);
  }

  .section-card {
    margin-top: var(--space-lg);

    .section-title {
      font-size: var(--font-size-heading-sm);
      font-weight: 500;
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    }
  }

  .pay-amount {
    color: var(--color-price);
    font-size: 22px;
    font-weight: 700;
  }

  .pay-bar {
    margin-top: var(--space-xl);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: var(--space-lg) 20px;
    background: var(--color-canvas-light);
    border-radius: var(--rounded-lg);
    box-shadow: var(--shadow-md);

    .pay-info {
      font-size: var(--font-size-caption);
    }
  }
}
</style>
