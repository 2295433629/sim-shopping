<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCart, type CartShopGroup, type CartItem } from '@/api/modules/cart'
import { createOrder } from '@/api/modules/order'
import { getAddressListApi } from '@/api/modules/address'
import { getMyCoupons, type MyCoupon } from '@/api/modules/coupon'
import type { AddressInfo } from '@/types/common'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const submitting = ref(false)
const shopGroups = ref<CartShopGroup[]>([])
const addressList = ref<AddressInfo[]>([])
const selectedAddressId = ref<number | null>(null)
const remark = ref('')

// 优惠券相关
const myCoupons = ref<MyCoupon[]>([])
const selectedCouponId = ref<number | null>(null)

// 从 URL query 参数获取要结算的商品ID列表（购物车页面或立即购买传入）
const queryCartItemIds = computed<number[] | null>(() => {
  const ids = route.query.cartItemIds
  if (!ids) return null
  return String(ids).split(',').map(Number).filter(id => !isNaN(id))
})

const selectedItems = computed<CartItem[]>(() => {
  const items: CartItem[] = []
  shopGroups.value.forEach(g => {
    g.items.forEach(i => items.push(i))
  })
  return items
})

const totalAmount = computed(() => {
  return selectedItems.value.reduce((sum, i) => sum + i.price * i.quantity, 0)
})

// TODO: 运费暂时保留前端计算，后端尚未提供运费API，后续应以后端返回的运费为准
const shippingFee = computed(() => {
  return shopGroups.value.length > 0 ? 10 * shopGroups.value.length : 0
})

// 计算优惠券折扣金额
const discountAmount = computed(() => {
  if (!selectedCouponId.value) return 0
  const coupon = myCoupons.value.find(c => c.id === selectedCouponId.value)
  if (!coupon) return 0
  const amount = totalAmount.value
  if (coupon.couponType === 'FIXED_AMOUNT') {
    // 固定金额：直接减去 discountValue
    return Math.min(coupon.discountValue, amount)
  } else if (coupon.couponType === 'PERCENTAGE') {
    // 折扣比例：discountValue 为百分比值（如 20 表示 8 折）
    return Math.round(amount * coupon.discountValue) / 100
  } else if (coupon.couponType === 'FIXED_DISCOUNT') {
    // 满减：满 minSpend 减 discountValue
    if (amount >= coupon.minSpend) {
      return Math.min(coupon.discountValue, amount)
    }
    return 0
  }
  return 0
})

const payAmount = computed(() => {
  return Math.max(0, totalAmount.value + shippingFee.value - discountAmount.value)
})

const selectedAddress = computed(() => {
  return addressList.value.find(a => a.id === selectedAddressId.value)
})

// 过滤可用的优惠券：状态为 UNUSED，且在有效期内，且满足金额门槛
const availableCoupons = computed(() => {
  const now = new Date()
  return myCoupons.value.filter(c => {
    if (c.status !== 'UNUSED') return false
    const start = new Date(c.validStartTime)
    const end = new Date(c.validEndTime)
    if (now < start || now > end) return false
    // 门槛检查
    if (c.minSpend > 0 && totalAmount.value < c.minSpend) return false
    return true
  })
})

// 优惠券描述文字
function couponLabel(coupon: MyCoupon): string {
  let desc = ''
  if (coupon.couponType === 'FIXED_AMOUNT') {
    desc = `¥${coupon.discountValue.toFixed(2)}`
  } else if (coupon.couponType === 'PERCENTAGE') {
    desc = `${coupon.discountValue}% 折扣`
  } else if (coupon.couponType === 'FIXED_DISCOUNT') {
    desc = `满${coupon.minSpend.toFixed(0)}减${coupon.discountValue.toFixed(2)}`
  }
  if (coupon.minSpend > 0 && coupon.couponType !== 'FIXED_DISCOUNT') {
    desc += `（满${coupon.minSpend.toFixed(0)}可用）`
  }
  return `${coupon.couponName} - ${desc}`
}

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const [cartData, addrData, couponData] = await Promise.all([
      getCart(),
      getAddressListApi(),
      getMyCoupons({ status: 'UNUSED', page: 1, size: 100 }),
    ])
    // 优先使用 URL query 中的 cartItemIds 过滤（购物车页面/立即购买传入）
    // 没有 query 参数时回退到 selected === 1 的过滤（兼容直接访问 /checkout）
    if (queryCartItemIds.value) {
      const idSet = new Set(queryCartItemIds.value)
      const filteredGroups: CartShopGroup[] = []
      ;(cartData.shopGroups || []).forEach(g => {
        const items = g.items.filter(i => idSet.has(i.cartItemId))
        if (items.length > 0) {
          filteredGroups.push({ ...g, items })
        }
      })
      shopGroups.value = filteredGroups
    } else {
      shopGroups.value = (cartData.shopGroups || []).map(g => ({
        ...g,
        items: g.items.filter(i => i.selected === 1),
      })).filter(g => g.items.length > 0)
    }
    addressList.value = addrData || []
    const def = addressList.value.find(a => a.isDefault === 1)
    selectedAddressId.value = def ? def.id : (addressList.value[0]?.id ?? null)
    // 优惠券数据
    myCoupons.value = couponData?.list || []
    // 如果当前选中的优惠券不再可用，自动清除
    if (selectedCouponId.value) {
      const stillValid = availableCoupons.value.some(c => c.id === selectedCouponId.value)
      if (!stillValid) {
        selectedCouponId.value = null
      }
    }
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  if (!selectedAddressId.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  if (selectedItems.value.length === 0) {
    ElMessage.warning('没有可结算的商品')
    return
  }
  submitting.value = true
  try {
    const cartItemIds = selectedItems.value.map(i => i.cartItemId)
    const result = await createOrder({
      addressId: selectedAddressId.value,
      remark: remark.value || undefined,
      cartItemIds,
      couponId: selectedCouponId.value || undefined,
    })
    ElMessage.success('订单创建成功')
    // 后端按店铺拆单，取第一个订单跳支付页
    const firstOrder = result.orders?.[0]
    if (firstOrder) {
      router.push(`/payment/${firstOrder.orderNo}`)
    } else {
      router.push('/orders')
    }
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
    <el-page-header @back="router.back()" title="返回" content="确认订单" />

    <el-card v-if="selectedItems.length > 0" shadow="never" class="section-card">
      <template #header><span class="section-title">收货地址</span></template>
      <template v-if="addressList.length === 0">
        <div class="no-address-tip">
          <span>暂无收货地址</span>
          <el-button type="primary" link @click="router.push('/addresses')">去添加</el-button>
        </div>
      </template>
      <template v-else>
        <el-select
          v-model="selectedAddressId"
          placeholder="请选择收货地址"
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
        <el-button text type="primary" @click="router.push('/addresses')">管理收货地址</el-button>
      </template>
    </el-card>

    <el-card v-for="group in shopGroups" :key="group.shopId" shadow="never" class="section-card">
      <template #header>
        <span class="section-title">{{ group.shopName }}</span>
      </template>
      <div v-for="item in group.items" :key="item.cartItemId" class="checkout-item">
        <el-image :src="item.productImage" fit="cover" class="item-image">
          <template #error><div class="img-fallback">暂无图片</div></template>
        </el-image>
        <div class="item-detail">
          <div class="item-name">{{ item.productName }}</div>
          <div class="item-sku">{{ item.skuName }}</div>
        </div>
        <div class="item-price">¥ {{ (item.price ?? 0).toFixed(2) }}</div>
        <div class="item-qty">x{{ item.quantity }}</div>
        <div class="item-subtotal">¥{{ ((item.price ?? 0) * item.quantity).toFixed(2) }}</div>
      </div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header><span class="section-title">优惠券</span></template>
      <el-select
        v-model="selectedCouponId"
        placeholder="请选择优惠券"
        style="width: 100%"
        size="large"
        clearable
        @clear="selectedCouponId = null"
      >
        <el-option
          v-for="coupon in availableCoupons"
          :key="coupon.id"
          :label="couponLabel(coupon)"
          :value="coupon.id"
        />
      </el-select>
      <el-empty
        v-if="availableCoupons.length === 0"
        description="暂无可用优惠券"
        :image-size="60"
      />
      <div v-if="selectedCouponId && discountAmount > 0" class="coupon-discount-info">
        <el-icon><Ticket /></el-icon>
        <span>已选优惠券，优惠 <b class="discount-text">-¥{{ discountAmount.toFixed(2) }}</b></span>
        <el-button text type="danger" size="small" @click="selectedCouponId = null">取消选择</el-button>
      </div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header><span class="section-title">订单备注</span></template>
      <el-input
        v-model="remark"
        type="textarea"
        :rows="2"
        placeholder="给卖家留言（选填）"
        maxlength="200"
        show-word-limit
      />
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header><span class="section-title">订单金额</span></template>
      <div class="summary-row">
        <span>商品总额：</span>
        <span>¥{{ totalAmount.toFixed(2) }}</span>
      </div>
      <div v-if="discountAmount > 0" class="summary-row discount-row">
        <span>优惠券折扣：</span>
        <span class="discount-text">-¥{{ discountAmount.toFixed(2) }}</span>
      </div>
      <div class="summary-row">
        <span>运费：</span>
        <span>¥{{ shippingFee.toFixed(2) }}</span>
      </div>
      <el-divider />
      <div class="summary-row total">
        <span>应付总额：</span>
        <span class="pay-amount">¥{{ payAmount.toFixed(2) }}</span>
      </div>
    </el-card>

    <div class="submit-bar">
      <span class="pay-info">
        应付：<span class="pay-amount">¥{{ payAmount.toFixed(2) }}</span>
      </span>
      <el-button
        type="primary"
        size="large"
        :loading="submitting"
        :disabled="selectedItems.length === 0 || addressList.length === 0"
        @click="handleSubmit"
      >
        提交订单
      </el-button>
    </div>

    <el-empty v-if="!loading && selectedItems.length === 0" description="没有可结算的商品">
      <el-button type="primary" @click="router.push('/cart')">返回购物车</el-button>
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
    background: var(--color-canvas-cream);
    border-radius: 4px;
    font-size: 14px;
    color: var(--color-ink);
  }

  .no-address-tip {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    padding: 20px 0;
    font-size: 14px;
    color: var(--color-shade-40);
  }

  .coupon-discount-info {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-top: 12px;
    padding: 10px 12px;
    background: #f0f9eb;
    border-radius: 4px;
    font-size: 14px;
    color: var(--color-ink);

    .discount-text {
      color: var(--color-price);
      font-weight: 600;
    }
  }

  .checkout-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid var(--color-canvas-cream);

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
        background: var(--color-canvas-cream);
        color: #ccc;
        font-size: 12px;
      }
    }

    .item-detail {
      flex: 1;

      .item-name {
        font-size: 14px;
        color: var(--color-ink);
      }

      .item-sku {
        font-size: 12px;
        color: var(--color-shade-40);
        margin-top: 2px;
      }
    }

    .item-price {
      color: var(--color-shade-50);
      font-size: 13px;
    }

    .item-qty {
      color: var(--color-shade-40);
      font-size: 13px;
    }

    .item-subtotal {
      width: 100px;
      text-align: right;
      color: var(--color-price);
      font-weight: 600;
    }
  }

  .summary-row {
    display: flex;
    justify-content: space-between;
    padding: 8px 0;
    font-size: 14px;
    color: var(--color-shade-50);

    &.total {
      font-size: 16px;
      font-weight: 600;
      color: var(--color-ink);
    }

    &.discount-row {
      color: #67c23a;
    }

    .pay-amount {
      color: var(--color-price);
      font-size: 22px;
      font-weight: 700;
    }

    .discount-text {
      color: #67c23a;
      font-weight: 600;
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
    background: var(--color-canvas-light);
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
        color: var(--color-price);
        font-size: 22px;
        font-weight: 700;
      }
    }
  }
}
</style>
