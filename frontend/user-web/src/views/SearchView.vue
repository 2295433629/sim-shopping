<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  searchProducts,
  suggest,
  hotKeywords,
  getSearchHistory,
  clearSearchHistory,
  getCategories,
} from '@/api/modules/product'
import type { ProductCardVO, SearchHistoryItem, CategoryNode } from '@/types/product'
import type { PageResponse } from '@/types/common'
import ProductCard from '@/components/ProductCard.vue'

const route = useRoute()

const loading = ref(false)
const keyword = ref('')
const productList = ref<ProductCardVO[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)

const suggestions = ref<string[]>([])
const hotWordList = ref<string[]>([])
const historyList = ref<SearchHistoryItem[]>([])
const showSuggest = ref(false)

// 筛选条件
const categoryList = ref<CategoryNode[]>([])
const selectedCategoryId = ref<number | undefined>(undefined)
const minPrice = ref<number | undefined>(undefined)
const maxPrice = ref<number | undefined>(undefined)

const sortOptions = [
  { label: '综合', value: 'comprehensive' },
  { label: '价格升', value: 'price_asc' },
  { label: '价格降', value: 'price_desc' },
  { label: '销量', value: 'sales' },
]
const selectedSort = ref('comprehensive')

// 扁平化分类树，用于下拉选择（取所有层级的分类）
const flatCategories = ref<{ id: number; name: string; level: number }[]>([])

function flattenCategories(nodes: CategoryNode[], level = 0): { id: number; name: string; level: number }[] {
  const result: { id: number; name: string; level: number }[] = []
  for (const node of nodes) {
    result.push({ id: node.id, name: '  '.repeat(level) + node.name, level })
    if (node.children && node.children.length > 0) {
      result.push(...flattenCategories(node.children, level + 1))
    }
  }
  return result
}

async function loadCategories() {
  try {
    const res = await getCategories()
    categoryList.value = res
    flatCategories.value = flattenCategories(res)
  } catch {
    // ignore
  }
}

let suggestTimer: ReturnType<typeof setTimeout> | null = null

onMounted(() => {
  loadHotKeywords()
  loadHistory()
  loadCategories()
  if (route.query.keyword) {
    keyword.value = String(route.query.keyword)
    doSearch()
  }
})

watch(() => route.query.keyword, (val) => {
  if (val) {
    keyword.value = String(val)
    doSearch()
  }
})

async function loadHotKeywords() {
  try {
    const res = await hotKeywords()
    hotWordList.value = res.keywords
  } catch {
    // ignore
  }
}

async function loadHistory() {
  try {
    const res = await getSearchHistory()
    historyList.value = res || []
  } catch {
    // ignore
  }
}

function handleInput() {
  if (suggestTimer) clearTimeout(suggestTimer)
  if (!keyword.value.trim()) {
    suggestions.value = []
    showSuggest.value = false
    return
  }
  suggestTimer = setTimeout(async () => {
    try {
      const res = await suggest(keyword.value.trim())
      suggestions.value = res.suggestions
      showSuggest.value = suggestions.value.length > 0
    } catch {
      suggestions.value = []
      showSuggest.value = false
    }
  }, 300)
}

function selectSuggestion(word: string) {
  keyword.value = word
  showSuggest.value = false
  doSearch()
}

function searchHot(word: string) {
  keyword.value = word
  doSearch()
}

async function doSearch() {
  if (!keyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  showSuggest.value = false
  loading.value = true
  currentPage.value = 1
  try {
    const res: PageResponse<ProductCardVO> = await searchProducts({
      keyword: keyword.value.trim(),
      page: currentPage.value,
      size: pageSize.value,
      sort: selectedSort.value,
      categoryId: selectedCategoryId.value,
      minPrice: minPrice.value,
      maxPrice: maxPrice.value,
    })
    productList.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

async function handlePageChange(page: number) {
  currentPage.value = page
  loading.value = true
  try {
    const res: PageResponse<ProductCardVO> = await searchProducts({
      keyword: keyword.value.trim(),
      page: currentPage.value,
      size: pageSize.value,
      sort: selectedSort.value,
      categoryId: selectedCategoryId.value,
      minPrice: minPrice.value,
      maxPrice: maxPrice.value,
    })
    productList.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

async function handleClearHistory() {
  try {
    await ElMessageBox.confirm('确定要清空搜索历史吗？', '提示', { type: 'warning' })
    await clearSearchHistory()
    historyList.value = []
    ElMessage.success('已清空搜索历史')
  } catch {
    // cancelled
  }
}

function resetFilters() {
  selectedCategoryId.value = undefined
  minPrice.value = undefined
  maxPrice.value = undefined
  doSearch()
}

</script>

<template>
  <div class="search-container">
    <!-- 搜索框 -->
    <div class="search-header">
      <div class="search-box">
        <el-input
          v-model="keyword"
          placeholder="搜索商品"
          clearable
          size="large"
          @input="handleInput"
          @keyup.enter="doSearch"
          @focus="showSuggest = suggestions.length > 0"
        >
          <template #append>
            <el-button @click="doSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
          </template>
        </el-input>
        <!-- 搜索联想 -->
        <div v-if="showSuggest" class="suggest-panel">
          <div
            v-for="s in suggestions"
            :key="s"
            class="suggest-item"
            @click="selectSuggestion(s)"
          >
            {{ s }}
          </div>
        </div>
      </div>
    </div>

    <!-- 无搜索词时显示：热门搜索 + 搜索历史 -->
    <div v-if="!keyword && productList.length === 0" class="search-suggest-area">
      <el-card shadow="never" v-if="hotWordList.length > 0">
        <template #header><span class="card-title">🔥 热门搜索</span></template>
        <div class="keyword-tags">
          <el-tag
            v-for="(word, idx) in hotWordList"
            :key="word"
            class="keyword-tag"
            :type="idx < 3 ? 'danger' : 'info'"
            @click="searchHot(word)"
          >
            {{ word }}
          </el-tag>
        </div>
      </el-card>

      <el-card shadow="never" v-if="historyList.length > 0" style="margin-top: 16px">
        <template #header>
          <div class="card-header-with-action">
            <span class="card-title">📋 搜索历史</span>
            <el-button link type="danger" @click="handleClearHistory">清空</el-button>
          </div>
        </template>
        <div class="keyword-tags">
          <el-tag
            v-for="item in historyList"
            :key="item.id"
            class="keyword-tag"
            @click="searchHot(item.keyword)"
          >
            {{ item.keyword }}
          </el-tag>
        </div>
      </el-card>
    </div>

    <!-- 搜索结果 -->
    <div v-if="keyword || productList.length > 0" v-loading="loading">
      <div class="result-header">
        <span class="result-count">
          搜索「{{ keyword }}」共找到 {{ total }} 件商品
        </span>
        <el-select v-model="selectedSort" @change="doSearch" size="small" style="width: 130px">
          <el-option v-for="opt in sortOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
        </el-select>
      </div>

      <!-- 筛选区 -->
      <div class="filter-bar">
        <div class="filter-item">
          <span class="filter-label">分类:</span>
          <el-select
            v-model="selectedCategoryId"
            placeholder="全部分类"
            clearable
            size="small"
            style="width: 180px"
            @change="doSearch"
          >
            <el-option
              v-for="cat in flatCategories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </div>
        <div class="filter-item">
          <span class="filter-label">价格:</span>
          <el-input-number
            v-model="minPrice"
            :min="0"
            :controls="false"
            placeholder="最低"
            size="small"
            style="width: 110px"
            @change="doSearch"
          />
          <span class="filter-sep">-</span>
          <el-input-number
            v-model="maxPrice"
            :min="0"
            :controls="false"
            placeholder="最高"
            size="small"
            style="width: 110px"
            @change="doSearch"
          />
        </div>
        <el-button v-if="selectedCategoryId || minPrice || maxPrice" size="small" @click="resetFilters">
          重置筛选
        </el-button>
      </div>

      <el-row :gutter="16">
        <el-col :xs="12" :sm="8" :md="6" v-for="item in productList" :key="item.productId">
          <ProductCard :product="item" />
        </el-col>
      </el-row>

      <el-empty v-if="!loading && productList.length === 0" description="未找到相关商品" />

      <div class="pagination-wrapper" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          layout="prev, pager, next, jumper"
          background
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.search-container {
  max-width: 1200px;
  margin: 0 auto;

  .search-header {
    margin-bottom: var(--space-xl);

    .search-box {
      position: relative;
      max-width: 600px;
      margin: 0 auto;

      .suggest-panel {
        position: absolute;
        top: 100%;
        left: 0;
        right: 0;
        background: var(--color-canvas-light);
        border: 1px solid var(--color-hairline-light);
        border-radius: var(--rounded-md);
        box-shadow: var(--shadow-sm);
        z-index: 100;

        .suggest-item {
          padding: 10px var(--space-lg);
          cursor: pointer;
          font-size: var(--font-size-caption);
          color: var(--color-ink);

          &:hover {
            background-color: var(--color-canvas-cream);
            color: var(--color-shade-60);
          }
        }
      }
    }
  }

  .search-suggest-area {
    max-width: 600px;
    margin: 0 auto;

    .card-title {
      font-size: var(--font-size-body-md);
      font-weight: 500;
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    }

    .card-header-with-action {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .keyword-tags {
      display: flex;
      flex-wrap: wrap;
      gap: var(--space-md);

      .keyword-tag {
        cursor: pointer;
      }
    }
  }

  .result-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: var(--space-lg);

    .result-count {
      font-size: var(--font-size-caption);
      color: var(--color-shade-50);
    }
  }

  .filter-bar {
    display: flex;
    align-items: center;
    gap: var(--space-lg);
    padding: var(--space-md) var(--space-lg);
    background: var(--color-canvas-light);
    border-radius: var(--rounded-md);
    margin-bottom: var(--space-lg);
    flex-wrap: wrap;

    .filter-item {
      display: flex;
      align-items: center;
      gap: var(--space-sm);
    }

    .filter-label {
      font-size: var(--font-size-caption);
      color: var(--color-shade-60);
      white-space: nowrap;
    }

    .filter-sep {
      color: var(--color-shade-40);
      margin: 0 2px;
    }
  }

  .pagination-wrapper {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
