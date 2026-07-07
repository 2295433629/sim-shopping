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
} from '@/api/modules/product'
import type { ProductCardVO, SearchHistoryItem } from '@/types/product'
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

const sortOptions = [
  { label: '综合', value: 'comprehensive' },
  { label: '价格升', value: 'price_asc' },
  { label: '价格降', value: 'price_desc' },
  { label: '销量', value: 'sales' },
]
const selectedSort = ref('comprehensive')

let suggestTimer: ReturnType<typeof setTimeout> | null = null

onMounted(() => {
  loadHotKeywords()
  loadHistory()
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
    historyList.value = res.list
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
    margin-bottom: 20px;

    .search-box {
      position: relative;
      max-width: 600px;
      margin: 0 auto;

      .suggest-panel {
        position: absolute;
        top: 100%;
        left: 0;
        right: 0;
        background: #fff;
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        z-index: 100;

        .suggest-item {
          padding: 10px 16px;
          cursor: pointer;
          font-size: 14px;
          color: #333;

          &:hover {
            background-color: #f5f7fa;
            color: #409eff;
          }
        }
      }
    }
  }

  .search-suggest-area {
    max-width: 600px;
    margin: 0 auto;

    .card-title {
      font-size: 16px;
      font-weight: bold;
    }

    .card-header-with-action {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .keyword-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;

      .keyword-tag {
        cursor: pointer;
      }
    }
  }

  .result-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .result-count {
      font-size: 14px;
      color: #666;
    }
  }

  .pagination-wrapper {
    margin-top: 24px;
    display: flex;
    justify-content: center;
  }
}
</style>
