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

const paymentMethods = [
  { label: 'Mock Alipay', value: 'ALIPAY', icon: '💰' },
  { label: 'Mock WeChat Pay', value: 'WECHAT', icon: '💬' },
]

onMounted(() => {
  loadOrder()
})

onUnmounted(() => {
  if (pollTimer) clearInterval(pollTimer)
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
  paying.value = true
  try {
    await createPayment(orderNo.value, paymentMethod.value)
    ElMessage.success('Payment submitted, waiting for confirmation...')
    startPolling()
  } catch {
    paying.value = false
  }
}

function startPolling() {
  pollTimer = setInterval(async () => {
    try {
      const result = await getPaymentStatus(orderNo.value)
      if (result.status === 'PAID') {
        paymentSuccess.value = true
        paying.value = false
        if (pollTimer) {
          clearInterval(pollTimer)
          pollTimer = null
        }
        setTimeout(() => {
          router.push(`/orders/${orderNo.value}`)
        }, 2000)
      }
    } catch {
      // keep polling
    }
  }, 2000)

  // Stop polling after 30s
  setTimeout(() => {
    if (pollTimer) {
      clearInterval(pollTimer)
      pollTimer = null
      paying.value = false
      ElMessage.warning('Payment confirmation timeout. Please check order status later.')
    }
  }, 30000)
}
</script>

<template>
  <div class="payment-container" v-loading="loading">
    <el-page-header @back="router.back()" title="Back" content="Payment" />

    <div v-if="paymentSuccess" class="success-box">
      <el-result icon="success" title="Payment Successful" sub-title="Redirecting to order detail...">
      </el-result>
    </div>

    <div v-else-if="order" class="payment-content">
      <el-card shadow="never" class="order-card">
        <template #header><span class="section-title">Order Info</span></template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Order No">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="Shop">{{ order.shopName }}</el-descriptions-item>
          <el-descriptions-item label="Status">
            <el-tag type="warning">{{ order.statusText }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Amount">
            <span class="pay-amount">${{ order.payAmount.toFixed(2) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="method-card">
        <template #header><span class="section-title">Payment Method</span></template>
        <el-radio-group v-model="paymentMethod" class="method-group">
          <el-radio
            v-for="m in paymentMethods"
            :key="m.value"
            :value="m.value"
            border
            class="method-option"
          >
            <span class="method-icon">{{ m.icon }}</span>
            {{ m.label }}
          </el-radio>
        </el-radio-group>
      </el-card>

      <div class="pay-bar">
        <span class="pay-label">
          Pay: <span class="pay-amount">${{ order.payAmount.toFixed(2) }}</span>
        </span>
        <el-button
          type="primary"
          size="large"
          :loading="paying"
          @click="handlePay"
        >
          {{ paying ? 'Processing...' : 'Pay Now' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.payment-container {
  max-width: 600px;
  margin: 0 auto;

  .section-title {
    font-size: 15px;
    font-weight: 600;
  }

  .order-card {
    margin-top: 16px;
  }

  .method-card {
    margin-top: 16px;

    .method-group {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    .method-option {
      width: 100%;
      height: 50px;
      display: flex;
      align-items: center;

      .method-icon {
        font-size: 20px;
        margin-right: 8px;
      }
    }
  }

  .pay-amount {
    color: #e4393c;
    font-size: 24px;
    font-weight: 700;
  }

  .pay-bar {
    margin-top: 24px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);

    .pay-label {
      font-size: 14px;
    }
  }

  .success-box {
    margin-top: 40px;
  }
}
</style>
