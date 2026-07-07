<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFavorites, removeFavorite } from '@/api/modules/product'
import type { ProductCardVO } from '@/types/product'
import type { PageResponse } from '@/types/common'
import ProductCard from '@/components/ProductCard.vue'

const loading = ref(false)
const favoriteList = ref<ProductCardVO[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)

onMounted(() => {
  loadFavorites()
})

async function loadFavorites() {
  loading.value = true
  try {
    const res: PageResponse<ProductCardVO> = await getFavorites({
      page: currentPage.value,
      size: pageSize.value,
    })
    favoriteList.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

async function handleRemove(productId: number, productName: string) {
  try {
    await ElMessageBox.confirm(`确定要取消收藏「${productName}」吗？`, '提示', { type: 'warning' })
    await removeFavorite(productId)
    ElMessage.success('已取消收藏')
    loadFavorites()
  } catch {
    // cancelled
  }
}

function handlePageChange(page: number) {
  currentPage.value = page
  loadFavorites()
}
</script>

<template>
  <div class="favorite-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">❤️ 我的收藏</span>
      </template>

      <div v-loading="loading">
        <el-row :gutter="16">
          <el-col :xs="12" :sm="8" :md="6" v-for="item in favoriteList" :key="item.productId">
            <div class="favorite-item-wrapper">
              <ProductCard :product="item" />
              <el-button
                class="remove-btn"
                type="danger"
                size="small"
                circle
                @click.stop="handleRemove(item.productId, item.name)"
              >
                <el-icon><Close /></el-icon>
              </el-button>
            </div>
          </el-col>
        </el-row>

        <el-empty v-if="!loading && favoriteList.length === 0" description="暂无收藏商品" />

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
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.favorite-container {
  max-width: 1200px;
  margin: 0 auto;

  .card-title {
    font-size: 16px;
    font-weight: bold;
  }

  .favorite-item-wrapper {
    position: relative;
    margin-bottom: 16px;

    .remove-btn {
      position: absolute;
      top: 8px;
      right: 8px;
      z-index: 10;
    }
  }

  .pagination-wrapper {
    margin-top: 24px;
    display: flex;
    justify-content: center;
  }
}
</style>
