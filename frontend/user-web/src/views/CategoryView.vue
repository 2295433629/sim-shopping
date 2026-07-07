<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getCategories, getProducts } from '@/api/modules/product'
import type { CategoryNode, ProductCardVO, ProductQueryParams } from '@/types/product'
import type { PageResponse } from '@/types/common'
import ProductCard from '@/components/ProductCard.vue'

const route = useRoute()

const loading = ref(false)
const categoryTree = ref<CategoryNode[]>([])
const selectedCategoryId = ref<number | undefined>(undefined)
const productList = ref<ProductCardVO[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)

const defaultProps = {
  label: 'name',
  children: 'children',
}

onMounted(async () => {
  await loadCategories()
  if (route.params.categoryId) {
    selectedCategoryId.value = Number(route.params.categoryId)
  } else if (categoryTree.value.length > 0) {
    selectedCategoryId.value = categoryTree.value[0].id
  }
  if (selectedCategoryId.value) {
    loadProducts()
  }
})

watch(() => route.params.categoryId, (val) => {
  if (val) {
    selectedCategoryId.value = Number(val)
    loadProducts()
  }
})

async function loadCategories() {
  try {
    categoryTree.value = await getCategories()
  } catch {
    // ignore
  }
}

async function loadProducts() {
  if (!selectedCategoryId.value) return
  loading.value = true
  const params: ProductQueryParams = {
    page: currentPage.value,
    size: pageSize.value,
    categoryId: selectedCategoryId.value,
  }
  try {
    const res: PageResponse<ProductCardVO> = await getProducts(params)
    productList.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

function handleNodeClick(data: CategoryNode) {
  selectedCategoryId.value = data.id
  currentPage.value = 1
  loadProducts()
}

function handlePageChange(page: number) {
  currentPage.value = page
  loadProducts()
}
</script>

<template>
  <div class="category-container">
    <el-row :gutter="20">
      <!-- 左侧分类树 -->
      <el-col :xs="24" :sm="6" :md="5">
        <el-card shadow="never" class="category-tree-card">
          <template #header>
            <span class="card-title">商品分类</span>
          </template>
          <el-tree
            :data="categoryTree"
            :props="defaultProps"
            node-key="id"
            :default-expanded-keys="selectedCategoryId ? [selectedCategoryId] : []"
            :current-node-key="selectedCategoryId"
            @node-click="handleNodeClick"
            highlight-current
          />
        </el-card>
      </el-col>

      <!-- 右侧商品列表 -->
      <el-col :xs="24" :sm="18" :md="19">
        <div v-loading="loading" class="product-area">
          <el-row :gutter="16">
            <el-col :xs="12" :sm="8" :md="6" v-for="item in productList" :key="item.productId">
              <ProductCard :product="item" />
            </el-col>
          </el-row>
          <el-empty v-if="!loading && productList.length === 0" description="该分类下暂无商品" />
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
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.category-container {
  max-width: 1200px;
  margin: 0 auto;

  .category-tree-card {
    .card-title {
      font-size: 16px;
      font-weight: bold;
    }
  }

  .product-area {
    min-height: 400px;
  }

  .pagination-wrapper {
    margin-top: 24px;
    display: flex;
    justify-content: center;
  }
}
</style>
