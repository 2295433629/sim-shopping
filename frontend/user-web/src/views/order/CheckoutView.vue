<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCart, type CartShopGroup, type CartItem } from '@/api/modules/cart'
import { createOrder } from '@/api/modules/order'
import { getAddressListApi } from '@/api/modules/address'
import type { AddressInfo } from '@/types/common'

const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const shopGroups = ref<CartShopGroup[]>([])
const addressList = ref<AddressInfo[]>([])
const selectedAddressId = ref<number | null>(null)
const remark = ref('')

const selectedItems = computed<CartItem[]>(() => {
  const items: CartItem[] = []
  shopGroups.value.forEach(g => {
    g.items.forEach(i => {
      if (i.selected === 1) items.push(i)
    })
  })
  return items
})

const totalAmount = computed(() => {
  return selectedItems.value.reduce((sum, i) => sum + i.price * i.quantity, 0)
})

const shippingFee = computed(() => {
  return shopGroups.value.length > 0 ? 10 * shopGroups.value.length : 0
})

const payAmount = computed(() => totalAmount.value + shippingFee.value)

const selectedAddress = computed(() => {
  return addressList.value.find(a => a.id === selectedAddressId.value)
})

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const [cartData, addrData] = await Promise.all([
      getCart(),
      getAddressListApi(),
    ])
    shopGroups.value = (cartData.shopGroups || []).map(g => ({
      ...g,
      items: g.items.filter(i => i.selected === 1),
    })).filter(g => g.items.length > 0)
    addressList.value = addrData.list || []
    const def = addressList.value.find(a => a.isDefault === 1)
    selectedAddressId.value = def ? def.id : (addressList.value[0]?.id ?? null)
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  if (!selectedAddressId.value) {
    ElMessage.warning('Please select a delivery address')
    return
  }
  if (selectedItems.value.length === 0) {
    ElMessage.warning('No items to checkout')
    return
  }
  submitting.value = true
  try {
    const cartItemIds = selectedItems.value.map(i => i.cartItemId)
    const result = await createOrder({
      addressId: selectedAddressId.value,
      remark: remark.value || undefined,
      cartItemIds,
    })
    ElMessage.success('Order created successfully')
    router.push(`/payment/${result.orderNo}`)
  } catch {
    // handled by interceptor
  } finally {
    submitting.value = false
  }
}

function formatAddress(addr: AddressInfo) {
  return `${addr.receiverName} ${addr.receiverPhone} ${addr.province}${addr.city}${addr.district}${addr.detailAddress}`
}
</script>

<template>
  <div class="checkout-container" v-loading="loading">
    <el-page-header @back="router.back()" title="Back" content="Checkout" />

    <el-card v-if="selectedItems.length > 0" shadow="never" class="section-card">
      <template #header><span class="section-title">Delivery Address</span></template>
      <el-select
        v-model="selectedAddressId"
        placeholder="Select address"
        style="width: 100%"
        size="large"
      >
        <el-option
          v-for="addr in addressList"
          :key="addr.id"
          :label="formatAddress(addr)"
          :value="addr.id"
        />
      </el-select>
      <div v-if="selectedAddress" class="address-preview">
        <el-icon><Location /></el-icon>
        <span>{{ formatAddress(selectedAddress) }}</span>
      </div>
      <el-button text type="primary" @click="router.push('/addresses')">Manage Addresses</el-button>
    </el-card>

    <el-card v-for="group in shopGroups" :key="group.shopId" shadow="never" class="section-card">
      <template #header>
        <span class="section-title">{{ group.shopName }}</span>
      </template>
      <div v-for="item in group.items" :key="item.cartItemId" class="checkout-item">
        <el-image :src="item.productImage" fit="cover" class="item-image">
          <template #error><div class="img-fallback">N/A</div></template>
        </el-image>
        <div class="item-detail">
          <div class="item-name">{{ item.productName }}</div>
          <div class="item-sku">{{ item.skuName }}</div>
        </div>
        <div class="item-price">$ {{ item.price.toFixed(2) }}</div>
        <div class="item-qty">x{{ item.quantity }}</div>
        <div class="item-subtotal">${{ (item.price * item.quantity).toFixed(2) }}</div>
      </div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header><span class="section-title">Order Remark</span></template>
      <el-input
        v-model="remark"
        type="textarea"
        :rows="2"
        placeholder="Leave a message to seller (optional)"
        maxlength="200"
        show-word-limit
      />
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header><span class="section-title">Order Summary</span></template>
      <div class="summary-row">
        <span>Items Total:</span>
        <span>${{ totalAmount.toFixed(2) }}</span>
      </div>
      <div class="summary-row">
        <span>Shipping Fee:</span>
        <span>${{ shippingFee.toFixed(2) }}</span>
      </div>
      <el-divider />
      <div class="summary-row total">
        <span>Total:</span>
        <span class="pay-amount">${{ payAmount.toFixed(2) }}</span>
      </div>
    </el-card>

    <div class="submit-bar">
      <span class="pay-info">
        Pay: <span class="pay-amount">${{ payAmount.toFixed(2) }}</span>
      </span>
      <el-button
        type="primary"
        size="large"
        :loading="submitting"
        :disabled="selectedItems.length === 0"
        @click="handleSubmit"
      >
        Submit Order
      </el-button>
    </div>

    <el-empty v-if="!loading && selectedItems.length === 0" description="No items selected for checkout">
      <el-button type="primary" @click="router.push('/cart')">Back to Cart</el-button>
    </el-empty>
  </div>
</template>

<style scoped lang="scss">
.checkout-container {
  max-width: 900px;
  margin: 0 auto;
  padding-bottom: 80px;

  .section-card {
    margin-top: 16px;

    .section-title {
      font-size: 15px;
      font-weight: 600;
    }
  }

  .address-preview {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-top: 12px;
    padding: 10px 12px;
    background: #f5f7fa;
    border-radius: 4px;
    font-size: 14px;
    color: #333;
  }

  .checkout-item {
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

    .item-detail {
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
      font-size: 13px;
    }

    .item-subtotal {
      width: 100px;
      text-align: right;
      color: #e4393c;
      font-weight: 600;
    }
  }

  .summary-row {
    display: flex;
    justify-content: space-between;
    padding: 8px 0;
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

  .submit-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    max-width: 900px;
    margin: 0 auto;
    height: 60px;
    background: #fff;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.08);
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 20px;
    padding: 0 20px;
    z-index: 100;

    .pay-info {
      font-size: 14px;

      .pay-amount {
        color: #e4393c;
        font-size: 22px;
        font-weight: 700;
      }
    }
  }
}
</style>
