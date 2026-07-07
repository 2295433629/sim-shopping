<script setup lang="ts">
import { ref, reactive, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProducts, getCategories } from '@/api/modules/product'
import type { ProductCardVO, CategoryNode, ProductQueryParams } from '@/types/product'
import type { PageResponse } from '@/types/common'
import ProductCard from '@/components/ProductCard.vue'

const route = useRoute()

const loading = ref(false)
const productList = ref<ProductCardVO[]>([])
const total = ref(0)
const categories = ref<CategoryNode[]>([])

const query = reactive<ProductQueryParams>({
  page: 1,
  size: 20,
  categoryId: undefined,
  keyword: undefined,
  minPrice: undefined,
  maxPrice: undefined,
  sort: 'comprehensive',
})

const sortOptions = [
  { label: '综合排序', value: 'comprehensive' },
  { label: '价格升序', value: 'price_asc' },
  { label: '价格降序', value: 'price_desc' },
  { label: '销量优先', value: 'sales' },
]

const priceRange = reactive<{ min: number | undefined; max: number | undefined }>({
  min: undefined,
  max: undefined,
})

onMounted(() => {
  // 从路由 query 读取参数
  if (route.query.keyword) {
    query.keyword = String(route.query.keyword)
  }
  if (route.query.categoryId) {
    query.categoryId = Number(route.query.categoryId)
  }
  loadCategories()
  loadProducts()
})

async function loadCategories() {
  try {
    categories.value = await getCategories()
  } catch {
    // ignore
  }
}

async function loadProducts() {
  loading.value = true
  try {
    const res: PageResponse<ProductCardVO> = await getProducts(query)
    productList.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.page = 1
  query.minPrice = priceRange.min
  query.maxPrice = priceRange.max
  loadProducts()
}

function handleSortChange() {
  query.page = 1
  loadProducts()
}

function handleCategoryChange() {
  query.page = 1
  loadProducts()
}

function handlePageChange(page: number) {
  query.page = page
  loadProducts()
}

function handleSizeChange(size: number) {
  query.size = size
  query.page = 1
  loadProducts()
}

watch(() => route.query, (newQuery) => {
  if (newQuery.keyword !== undefined) {
    query.keyword = newQuery.keyword ? String(newQuery.keyword) : undefined
  }
  if (newQuery.categoryId !== undefined) {
    query.categoryId = newQuery.categoryId ? Number(newQuery.categoryId) : undefined
  }
  query.page = 1
  loadProducts()
})
</script>

<template>
  <div class="product-list-container">
    <!-- 筛选栏 -->
    <el-card shadow="never" class="filter-bar">
      <el-row :gutter="16" align="middle">
        <el-col :xs="24" :sm="6" :md="5">
          <span class="filter-label">分类：</span>
          <el-select
            v-model="query.categoryId"
            placeholder="全部分类"
            clearable
            @change="handleCategoryChange"
            style="width: 140px"
          >
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-col>
        <el-col :xs="24" :sm="8" :md="7">
          <span class="filter-label">价格：</span>
          <el-input-number
            v-model="priceRange.min"
            :min="0"
            placeholder="最低"
            :controls="false"
            style="width: 90px"
          />
          <span class="price-separator">-</span>
          <el-input-number
            v-model="priceRange.max"
            :min="0"
            placeholder="最高"
            :controls="false"
            style="width: 90px"
          />
          <el-button type="primary" link @click="handleSearch">确定</el-button>
        </el-col>
        <el-col :xs="24" :sm="6" :md="5">
          <span class="filter-label">排序：</span>
          <el-select
            v-model="query.sort"
            @change="handleSortChange"
            style="width: 130px"
          >
            <el-option
              v-for="opt in sortOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <!-- 商品网格 -->
    <div v-loading="loading" class="product-grid">
      <el-row :gutter="16">
        <el-col
          :xs="12" :sm="8" :md="6"
          v-for="item in productList"
          :key="item.productId"
        >
          <ProductCard :product="item" />
        </el-col>
      </el-row>
      <el-empty v-if="!loading && productList.length === 0" description="暂无商品" />
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > 0">
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :total="total"
        :page-sizes="[20, 40, 60, 80]"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.product-list-container {
  max-width: 1200px;
  margin: 0 auto;

  .filter-bar {
    margin-bottom: 20px;

    .filter-label {
      font-size: 14px;
      color: #666;
    }

    .price-separator {
      margin: 0 4px;
      color: #999;
    }
  }

  .product-grid {
    min-height: 400px;
  }

  .pagination-wrapper {
    margin-top: 24px;
    display: flex;
    justify-content: center;
  }
}
</style>
