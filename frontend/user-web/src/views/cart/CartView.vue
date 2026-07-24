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
    await ElMessageBox.confirm('确定从购物车移除该商品？', '提示', {
      confirmButtonText: '移除',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await removeCartItem(item.cartItemId)
    shopGroups.value.forEach(g => {
      g.items = g.items.filter(i => i.cartItemId !== item.cartItemId)
    })
    shopGroups.value = shopGroups.value.filter(g => g.items.length > 0)
    ElMessage.success('已移除')
  } catch {
    // cancelled or error
  }
}

function handleCheckout() {
  if (selectedCount.value === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  const selectedIds: number[] = []
  shopGroups.value.forEach(g => {
    g.items.forEach(i => {
      if (i.selected === 1) selectedIds.push(i.cartItemId)
    })
  })
  router.push({ path: '/checkout', query: { cartItemIds: selectedIds.join(',') } })
}
</script>

<template>
  <div class="cart-container" v-loading="loading">
    <el-card v-if="shopGroups.length > 0" shadow="never">
      <template #header>
        <span class="card-title">购物车</span>
      </template>

      <div v-for="group in shopGroups" :key="group.shopId" class="shop-group">
        <div class="shop-header">
          <el-checkbox
            :model-value="isShopAllSelected(group)"
            @change="handleShopSelect(group)"
          >
            {{ group.shopName }}
          </el-checkbox>
          <span class="shop-amount">小计：¥{{ shopSelectedAmount(group) }}</span>
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
            <template #error><div class="image-placeholder">暂无图片</div></template>
          </el-image>
          <div class="item-info">
            <div class="item-name">{{ item.productName }}</div>
            <div class="item-sku">{{ item.skuName }}</div>
            <div class="item-price">¥{{ (item.price ?? 0).toFixed(2) }}</div>
          </div>
          <el-input-number
            :model-value="item.quantity"
            :min="1"
            :max="item.stock"
            size="small"
            @change="(val: number | undefined) => handleQuantityChange(item, val)"
          />
          <div class="item-subtotal">
            ¥{{ (item.price * item.quantity).toFixed(2) }}
          </div>
          <el-button type="danger" link @click="handleRemove(item)">移除</el-button>
        </div>
      </div>
    </el-card>

    <el-empty v-if="!loading && shopGroups.length === 0" description="购物车是空的">
      <el-button type="primary" @click="router.push('/home')">去逛逛</el-button>
    </el-empty>

    <!-- Bottom bar -->
    <div v-if="shopGroups.length > 0" class="bottom-bar">
      <div class="bottom-bar-inner">
        <div class="bottom-left">
          <el-checkbox :model-value="allSelected" @change="handleSelectAll">全选</el-checkbox>
        </div>
        <div class="bottom-right">
          <span class="total-info">
            合计：<span class="total-amount">¥{{ selectedAmount }}</span>
            <span class="total-count">({{ selectedCount }} 件商品)</span>
          </span>
          <el-button type="primary" size="large" :disabled="selectedCount === 0" @click="handleCheckout">
            去结算
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.cart-container {
  max-width: 1040px;
  margin: 0 auto;
  padding-bottom: 80px;

  .card-title {
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-size: var(--font-size-heading-md, 20px);
    font-weight: 500;
    color: var(--color-ink);
    letter-spacing: 0.3px;
  }

  .shop-group {
    margin-bottom: var(--space-xl, 20px);
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    overflow: hidden;

    .shop-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: var(--space-md, 12px) var(--space-lg, 16px);
      background: var(--color-canvas-cream);
      font-weight: 500;
      font-size: var(--font-size-body-md);

      .shop-amount {
        color: var(--color-price);
        font-size: var(--font-size-caption);
        font-weight: 500;
      }
    }

    .cart-item {
      display: flex;
      align-items: center;
      gap: var(--space-md, 12px);
      padding: var(--space-lg, 16px);
      border-top: 1px solid var(--color-hairline-light);
      transition: background-color var(--transition-fast);

      &:hover {
        background-color: var(--color-canvas-cream);
      }

      .item-image {
        width: 88px;
        height: 88px;
        border-radius: var(--rounded-md);
        flex-shrink: 0;

        .image-placeholder {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          background: var(--color-canvas-cream);
          color: var(--color-shade-40);
          font-size: var(--font-size-micro);
        }
      }

      .item-info {
        flex: 1;
        min-width: 0;

        .item-name {
          font-size: var(--font-size-body-md);
          font-weight: 500;
          color: var(--color-ink);
          margin-bottom: 4px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .item-sku {
          font-size: var(--font-size-micro);
          color: var(--color-shade-50);
          margin-bottom: 4px;
        }

        .item-price {
          color: var(--color-price);
          font-weight: 500;
          font-size: var(--font-size-caption);
        }
      }

      .item-subtotal {
        width: 100px;
        text-align: right;
        color: var(--color-price);
        font-weight: 500;
        font-size: 16px;
        letter-spacing: -0.3px;
      }
    }
  }

  .bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 64px;
    background: var(--color-canvas-light);
    box-shadow: 0 -1px 0 var(--color-hairline-light), var(--shadow-sm);
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 var(--space-xl, 20px);
    z-index: 100;

    .bottom-bar-inner {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      max-width: 1040px;
    }

    .bottom-left {
      display: flex;
      align-items: center;
    }

    .bottom-right {
      display: flex;
      align-items: center;
      gap: var(--space-xl, 20px);

      .total-info {
        font-size: var(--font-size-caption);

        .total-amount {
          font-family: var(--font-display, 'Helvetica Neue', sans-serif);
          color: var(--color-price);
          font-size: 24px;
          font-weight: 500;
          margin: 0 4px;
          letter-spacing: -0.5px;
        }

        .total-count {
          color: var(--color-shade-50);
        }
      }

      .el-button--primary {
        border-radius: var(--rounded-pill) !important;
        padding: 12px 32px;
        font-weight: 500;
      }
    }
  }
}
</style>
