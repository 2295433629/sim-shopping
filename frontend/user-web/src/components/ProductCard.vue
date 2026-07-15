<script setup lang="ts">
import { useRouter } from 'vue-router'
import type { ProductCardVO } from '@/types/product'

const props = defineProps<{
  product: ProductCardVO
}>()

const router = useRouter()

function goToDetail() {
  router.push(`/products/${props.product.productId}`)
}

function formatPrice(price: number): string {
  return `¥${price.toFixed(2)}`
}
</script>

<template>
  <el-card shadow="hover" class="product-card" @click="goToDetail">
    <div class="product-image">
      <el-image
        v-if="product.mainImage"
        :src="product.mainImage"
        fit="cover"
        lazy
        class="image"
      />
      <el-icon v-else :size="48" color="#ddd"><Picture /></el-icon>
    </div>
    <div class="product-info">
      <p class="product-name">{{ product.name }}</p>
      <p v-if="product.subtitle" class="product-subtitle">{{ product.subtitle }}</p>
      <div class="product-bottom">
        <span class="product-price">{{ formatPrice(product.price) }}</span>
        <span v-if="product.originalPrice && product.originalPrice > product.price" class="product-original-price">
          {{ formatPrice(product.originalPrice) }}
        </span>
        <span class="product-sales">已售{{ product.sales }}</span>
      </div>
      <p v-if="product.shopName" class="product-shop">{{ product.shopName }}</p>
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.product-card {
  cursor: pointer;
  transition: transform var(--transition-fast), box-shadow var(--transition-fast);
  overflow: hidden;
  border-radius: var(--rounded-lg);

  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-lg);
  }

  :deep(.el-card__body) {
    padding: 0;
  }

  .product-image {
    height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: var(--color-canvas-cream);
    overflow: hidden;

    .image {
      width: 100%;
      height: 100%;
    }
  }

  .product-info {
    padding: var(--space-md);

    .product-name {
      font-size: var(--font-size-caption);
      font-weight: 500;
      color: var(--color-ink);
      margin: 0 0 var(--space-xs) 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .product-subtitle {
      font-size: var(--font-size-eyebrow);
      color: var(--color-shade-40);
      margin: 0 0 var(--space-sm) 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .product-bottom {
      display: flex;
      align-items: baseline;
      gap: var(--space-sm);
      flex-wrap: wrap;

      .product-price {
        font-size: var(--font-size-body-lg);
        font-weight: 500;
        color: var(--color-price);
      }

      .product-original-price {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-30);
        text-decoration: line-through;
      }

      .product-sales {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-40);
        margin-left: auto;
      }
    }

    .product-shop {
      font-size: var(--font-size-eyebrow);
      color: var(--color-shade-50);
      margin: var(--space-xs) 0 0 0;
    }
  }
}
</style>
