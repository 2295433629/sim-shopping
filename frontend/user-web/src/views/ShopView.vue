<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getShop, getShopProducts } from '@/api/modules/product'
import type { ShopInfo, ProductCardVO } from '@/types/product'
import type { PageResponse } from '@/types/common'
import ProductCard from '@/components/ProductCard.vue'

const route = useRoute()

const loading = ref(false)
const shop = ref<ShopInfo | null>(null)
const productList = ref<ProductCardVO[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)

const shopId = ref(0)

onMounted(() => {
  shopId.value = Number(route.params.shopId)
  loadData()
})

watch(() => route.params.shopId, (val) => {
  if (val) {
    shopId.value = Number(val)
    currentPage.value = 1
    loadData()
  }
})

async function loadData() {
  loading.value = true
  try {
    const [shopRes, prodRes] = await Promise.allSettled([
      getShop(shopId.value),
      getShopProducts(shopId.value, { page: currentPage.value, size: pageSize.value }),
    ])
    if (shopRes.status === 'fulfilled') shop.value = shopRes.value
    if (prodRes.status === 'fulfilled') {
      productList.value = prodRes.value.list
      total.value = prodRes.value.total
    }
  } finally {
    loading.value = false
  }
}

function handlePageChange(page: number) {
  currentPage.value = page
  loadShopProducts()
}

async function loadShopProducts() {
  loading.value = true
  try {
    const res: PageResponse<ProductCardVO> = await getShopProducts(shopId.value, {
      page: currentPage.value,
      size: pageSize.value,
    })
    productList.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="shop-container" v-loading="loading">
    <!-- 店铺头部 -->
    <el-card shadow="never" class="shop-header" v-if="shop">
      <div class="shop-header-content">
        <el-avatar :size="64" :src="shop.shopLogo">
          {{ shop.shopName?.[0] || '店' }}
        </el-avatar>
        <div class="shop-meta">
          <h2 class="shop-name">{{ shop.shopName }}</h2>
          <p class="shop-desc">{{ shop.description || '暂无简介' }}</p>
          <div class="shop-stats">
            <span>商品数：{{ shop.productCount ?? productList.length }}</span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 店铺商品 -->
    <el-card shadow="never" style="margin-top: 16px">
      <template #header>
        <span class="card-title">全部商品</span>
      </template>
      <el-row :gutter="16">
        <el-col :xs="12" :sm="8" :md="6" v-for="item in productList" :key="item.productId">
          <ProductCard :product="item" />
        </el-col>
      </el-row>
      <el-empty v-if="!loading && productList.length === 0" description="该店铺暂无商品" />
      <div class="pagination-wrapper" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          :total="total"
          :page-size="pageSize"
          layout="prev, pager, next, jumper"
          background
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.shop-container {
  max-width: 1200px;
  margin: 0 auto;

  .shop-header {
    .shop-header-content {
      display: flex;
      align-items: center;
      gap: var(--space-xl);

      .shop-meta {
        .shop-name {
          font-size: var(--font-size-heading-xl);
          font-weight: 500;
          margin: 0 0 var(--space-sm) 0;
          font-family: var(--font-display, 'Helvetica Neue', sans-serif);
        }

        .shop-desc {
          font-size: var(--font-size-caption);
          color: var(--color-shade-40);
          margin: 0 0 var(--space-sm) 0;
        }

        .shop-stats {
          font-size: var(--font-size-caption);
          color: var(--color-shade-50);
        }
      }
    }
  }

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .pagination-wrapper {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
