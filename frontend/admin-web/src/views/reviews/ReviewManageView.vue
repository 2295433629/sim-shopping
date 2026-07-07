<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminReviews, hideReview, showReview, deleteReview, type AdminReviewItem } from '@/api/modules/review'

const loading = ref(false)
const reviewList = ref<AdminReviewItem[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const keyword = ref('')

onMounted(() => {
  loadReviews()
})

watch(page, () => {
  loadReviews()
})

async function loadReviews() {
  loading.value = true
  try {
    const params: Record<string, unknown> = { page: page.value, size: pageSize.value }
    if (keyword.value.trim()) params.keyword = keyword.value.trim()
    const data = await getAdminReviews(params as any)
    reviewList.value = (data as any).list || []
    total.value = (data as any).total || 0
  } catch {
    reviewList.value = []
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  page.value = 1
  loadReviews()
}

function handleReset() {
  keyword.value = ''
  page.value = 1
  loadReviews()
}

function handlePageChange(p: number) {
  page.value = p
}

async function handleToggleStatus(row: AdminReviewItem) {
  const isHidden = row.status === 'HIDDEN'
  const action = isHidden ? '显示' : '隐藏'
  try {
    await ElMessageBox.confirm(`确定要${action}该评价吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    if (isHidden) {
      await showReview(row.id)
    } else {
      await hideReview(row.id)
    }
    ElMessage.success(`${action}成功`)
    loadReviews()
  } catch {
    // 用户取消
  }
}

async function handleDelete(row: AdminReviewItem) {
  try {
    await ElMessageBox.confirm('确定要删除该评价吗？删除后不可恢复。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteReview(row.id)
    ElMessage.success('删除成功')
    loadReviews()
  } catch {
    // 用户取消
  }
}
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <span class="card-title">评价管理</span>
    </template>

    <!-- 搜索栏 -->
    <div class="filter-bar">
      <el-input
        v-model="keyword"
        placeholder="搜索商品名/用户名"
        clearable
        style="width: 250px"
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <!-- 评价列表 -->
    <el-table :data="reviewList" v-loading="loading" style="width: 100%; margin-top: 16px" stripe>
      <el-table-column label="ID" prop="id" width="70" align="center" />
      <el-table-column label="商品名" prop="productName" min-width="150" show-overflow-tooltip />
      <el-table-column label="用户名" prop="username" width="120" />
      <el-table-column label="评分" width="160" align="center">
        <template #default="{ row }">
          <el-rate :model-value="row.rating" disabled :size="'small'" />
        </template>
      </el-table-column>
      <el-table-column label="评价内容" prop="content" min-width="200" show-overflow-tooltip />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 'VISIBLE' ? 'success' : 'danger'" size="small">
            {{ row.status === 'VISIBLE' ? '可见' : '已隐藏' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createdAt" width="180" />
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template #default="{ row }">
          <el-button
            link
            :type="row.status === 'VISIBLE' ? 'warning' : 'success'"
            @click="handleToggleStatus(row)"
          >
            {{ row.status === 'VISIBLE' ? '隐藏' : '显示' }}
          </el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!loading && reviewList.length === 0" description="暂无评价" />

    <div v-if="total > pageSize" class="pagination-wrap">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.card-title {
  font-size: 16px;
  font-weight: bold;
}

.filter-bar {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.pagination-wrap {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
