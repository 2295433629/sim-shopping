<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCart, updateCartItem, removeCartItem, selectAll, type CartItem, type CartShopGroup } from '@/api/modules/cart'

const router = useRouter()
const loading = ref(false)
const shopGroups = ref<CartShopGroup[]>([])

const allSelected = computed(() => {
  if (shopGroups.value.length === 0) return false
  return shopGroups.value.every(g => g.items.every(i => i.selected === 1))
})

const selectedAmount = computed(() => {
  let total = 0
  shopGroups.value.forEach(g => {
    g.items.forEach(i => {
      if (i.selected === 1) total += i.price * i.quantity
    })
  })
  return total.toFixed(2)
})

const selectedCount = computed(() => {
  let count = 0
  shopGroups.value.forEach(g => {
    g.items.forEach(i => {
      if (i.selected === 1) count += i.quantity
    })
  })
  return count
})

function isShopAllSelected(group: CartShopGroup) {
  return group.items.length > 0 && group.items.every(i => i.selected === 1)
}

function shopSelectedAmount(group: CartShopGroup) {
  return group.items.filter(i => i.selected === 1).reduce((s, i) => s + i.price * i.quantity, 0).toFixed(2)
}

onMounted(() => {
  loadCart()
})

async function loadCart() {
  loading.value = true
  try {
    const data = await getCart()
    shopGroups.value = data.shopGroups || []
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

async function handleItemSelect(item: CartItem) {
  try {
    await updateCartItem(item.cartItemId, { selected: item.selected === 1 ? 0 : 1 })
    item.selected = item.selected === 1 ? 0 : 1
  } catch {
    // handled by interceptor
  }
}

async function handleShopSelect(group: CartShopGroup) {
  const target = isShopAllSelected(group) ? 0 : 1
  try {
    for (const item of group.items) {
      if (item.selected !== target) {
        await updateCartItem(item.cartItemId, { selected: target })
        item.selected = target
      }
    }
  } catch {
    // handled by interceptor
  }
}

async function handleSelectAll() {
  const target = allSelected.value ? 0 : 1
  try {
    await selectAll(target)
    shopGroups.value.forEach(g => g.items.forEach(i => (i.selected = target)))
  } catch {
    // handled by interceptor
  }
}

async function handleQuantityChange(item: CartItem, val: number | undefined) {
  if (!val || val < 1) return
  try {
    await updateCartItem(item.cartItemId, { quantity: val })
    item.quantity = val
  } catch {
    // handled by interceptor
  }
}

async function handleRemove(item: CartItem) {
  try {
    await ElMessageBox.confirm('Remove this item from cart?', 'Confirm', {
      confirmButtonText: 'Remove',
      cancelButtonText: 'Cancel',
      type: 'warning',
    })
    await removeCartItem(item.cartItemId)
    shopGroups.value.forEach(g => {
      g.items = g.items.filter(i => i.cartItemId !== item.cartItemId)
    })
    shopGroups.value = shopGroups.value.filter(g => g.items.length > 0)
    ElMessage.success('Removed')
  } catch {
    // cancelled or error
  }
}

function handleCheckout() {
  if (selectedCount.value === 0) {
    ElMessage.warning('Please select items to checkout')
    return
  }
  router.push('/checkout')
}
</script>

<template>
  <div class="cart-container" v-loading="loading">
    <el-card v-if="shopGroups.length > 0" shadow="never">
      <template #header>
        <span class="card-title">Shopping Cart</span>
      </template>

      <div v-for="group in shopGroups" :key="group.shopId" class="shop-group">
        <div class="shop-header">
          <el-checkbox
            :model-value="isShopAllSelected(group)"
            @change="handleShopSelect(group)"
          >
            {{ group.shopName }}
          </el-checkbox>
          <span class="shop-amount">Subtotal: ${{ shopSelectedAmount(group) }}</span>
        </div>

        <div v-for="item in group.items" :key="item.cartItemId" class="cart-item">
          <el-checkbox
            :model-value="item.selected === 1"
            @change="handleItemSelect(item)"
          />
          <el-image
            :src="item.productImage"
            fit="cover"
            class="item-image"
          >
            <template #error><div class="image-placeholder">No Image</div></template>
          </el-image>
          <div class="item-info">
            <div class="item-name">{{ item.productName }}</div>
            <div class="item-sku">{{ item.skuName }}</div>
            <div class="item-price">${{ item.price.toFixed(2) }}</div>
          </div>
          <el-input-number
            :model-value="item.quantity"
            :min="1"
            :max="item.stock"
            size="small"
            @change="(val: number | undefined) => handleQuantityChange(item, val)"
          />
          <div class="item-subtotal">
            ${{ (item.price * item.quantity).toFixed(2) }}
          </div>
          <el-button type="danger" link @click="handleRemove(item)">Remove</el-button>
        </div>
      </div>
    </el-card>

    <el-empty v-if="!loading && shopGroups.length === 0" description="Your cart is empty">
      <el-button type="primary" @click="router.push('/home')">Go Shopping</el-button>
    </el-empty>

    <!-- Bottom bar -->
    <div v-if="shopGroups.length > 0" class="bottom-bar">
      <div class="bottom-left">
        <el-checkbox :model-value="allSelected" @change="handleSelectAll">Select All</el-checkbox>
      </div>
      <div class="bottom-right">
        <span class="total-info">
          Total: <span class="total-amount">${{ selectedAmount }}</span>
          <span class="total-count">({{ selectedCount }} items)</span>
        </span>
        <el-button type="primary" size="large" :disabled="selectedCount === 0" @click="handleCheckout">
          Checkout
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.cart-container {
  max-width: 1000px;
  margin: 0 auto;
  padding-bottom: 80px;

  .card-title {
    font-size: 16px;
    font-weight: bold;
  }

  .shop-group {
    margin-bottom: 20px;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    overflow: hidden;

    .shop-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 12px 16px;
      background: #fafafa;
      font-weight: 600;

      .shop-amount {
        color: #e4393c;
        font-size: 14px;
      }
    }

    .cart-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 16px;
      border-top: 1px solid #f0f0f0;

      .item-image {
        width: 80px;
        height: 80px;
        border-radius: 4px;
        flex-shrink: 0;

        .image-placeholder {
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
          margin-bottom: 4px;
        }

        .item-sku {
          font-size: 12px;
          color: #999;
          margin-bottom: 4px;
        }

        .item-price {
          color: #e4393c;
          font-weight: 600;
        }
      }

      .item-subtotal {
        width: 100px;
        text-align: right;
        color: #e4393c;
        font-weight: 600;
        font-size: 15px;
      }
    }
  }

  .bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    max-width: 1000px;
    margin: 0 auto;
    height: 60px;
    background: #fff;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.08);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    z-index: 100;

    .bottom-right {
      display: flex;
      align-items: center;
      gap: 20px;

      .total-info {
        font-size: 14px;

        .total-amount {
          color: #e4393c;
          font-size: 22px;
          font-weight: 700;
          margin: 0 4px;
        }

        .total-count {
          color: #999;
        }
      }
    }
  }
}
</style>
