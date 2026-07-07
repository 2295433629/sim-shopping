<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyReviews, deleteReview, type ReviewItem } from '@/api/modules/review'

const loading = ref(false)
const reviews = ref<ReviewItem[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
  loadReviews()
})

watch([page], () => {
  loadReviews()
})

async function loadReviews() {
  loading.value = true
  try {
    const data = await getMyReviews({ page: page.value, size: pageSize.value }) as any
    reviews.value = data.list || []
    total.value = data.total || 0
  } catch {
    reviews.value = []
  } finally {
    loading.value = false
  }
}

async function handleDelete(review: ReviewItem) {
  try {
    await ElMessageBox.confirm('确定要删除这条评价吗？删除后不可恢复', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteReview(review.id)
    ElMessage.success('评价已删除')
    loadReviews()
  } catch {
    // cancelled or error
  }
}

function handlePageChange(p: number) {
  page.value = p
}
</script>

<template>
  <div class="my-reviews-page">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">我的评价</span>
      </template>

      <div v-loading="loading">
        <div v-for="review in reviews" :key="review.id" class="review-card">
          <div class="review-header">
            <span class="product-name">{{ review.productName }}</span>
            <span class="review-time">{{ review.createdAt }}</span>
          </div>

          <div class="review-body">
            <el-rate v-model="review.rating" disabled />
            <div class="review-content">{{ review.content }}</div>
            <div class="review-images" v-if="review.images && review.images.length > 0">
              <el-image
                v-for="(img, idx) in review.images"
                :key="idx"
                :src="img"
                fit="cover"
                class="review-image"
                :preview-src-list="review.images"
                :initial-index="idx"
              />
            </div>
          </div>

          <div class="merchant-reply" v-if="review.merchantReply">
            <div class="reply-label">商家回复：</div>
            <div class="reply-content">{{ review.merchantReply }}</div>
            <div class="reply-time">{{ review.merchantRepliedAt }}</div>
          </div>

          <div class="review-actions">
            <el-button type="danger" text size="small" @click="handleDelete(review)">删除评价</el-button>
          </div>
        </div>

        <el-empty v-if="!loading && reviews.length === 0" description="暂无评价" />

        <div v-if="total > pageSize" class="pagination-wrap">
          <el-pagination
            v-model:current-page="page"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.my-reviews-page {
  max-width: 900px;
  margin: 0 auto;

  .card-title {
    font-size: 16px;
    font-weight: bold;
  }

  .review-card {
    border: 1px solid #ebeef5;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 16px;

    .review-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;

      .product-name {
        font-size: 15px;
        font-weight: 600;
        color: #303133;
      }

      .review-time {
        font-size: 12px;
        color: #909399;
      }
    }

    .review-body {
      .review-content {
        margin-top: 8px;
        font-size: 14px;
        color: #606266;
        line-height: 1.6;
      }

      .review-images {
        display: flex;
        gap: 8px;
        margin-top: 12px;
        flex-wrap: wrap;

        .review-image {
          width: 80px;
          height: 80px;
          border-radius: 4px;
          flex-shrink: 0;
        }
      }
    }

    .merchant-reply {
      background: #f5f7fa;
      border-radius: 6px;
      padding: 12px;
      margin-top: 12px;

      .reply-label {
        font-size: 13px;
        font-weight: 600;
        color: #409eff;
        margin-bottom: 4px;
      }

      .reply-content {
        font-size: 13px;
        color: #606266;
        line-height: 1.5;
      }

      .reply-time {
        font-size: 12px;
        color: #909399;
        margin-top: 4px;
        text-align: right;
      }
    }

    .review-actions {
      margin-top: 12px;
      display: flex;
      justify-content: flex-end;
    }
  }

  .pagination-wrap {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>
