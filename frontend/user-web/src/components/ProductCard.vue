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
  transition: transform 0.2s;
  overflow: hidden;

  &:hover {
    transform: translateY(-4px);
  }

  :deep(.el-card__body) {
    padding: 0;
  }

  .product-image {
    height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #f5f5f5;
    overflow: hidden;

    .image {
      width: 100%;
      height: 100%;
    }
  }

  .product-info {
    padding: 12px;

    .product-name {
      font-size: 14px;
      font-weight: 500;
      color: #333;
      margin: 0 0 4px 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .product-subtitle {
      font-size: 12px;
      color: #999;
      margin: 0 0 8px 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .product-bottom {
      display: flex;
      align-items: baseline;
      gap: 8px;
      flex-wrap: wrap;

      .product-price {
        font-size: 18px;
        font-weight: bold;
        color: #f56c6c;
      }

      .product-original-price {
        font-size: 12px;
        color: #ccc;
        text-decoration: line-through;
      }

      .product-sales {
        font-size: 12px;
        color: #999;
        margin-left: auto;
      }
    }

    .product-shop {
      font-size: 12px;
      color: #666;
      margin: 4px 0 0 0;
    }
  }
}
</style>
