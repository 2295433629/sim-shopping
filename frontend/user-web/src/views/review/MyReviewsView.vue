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
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .review-card {
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    padding: var(--space-lg);
    margin-bottom: var(--space-lg);

    .review-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: var(--space-md);

      .product-name {
        font-size: var(--font-size-heading-sm);
        font-weight: 500;
        color: var(--color-ink);
      }

      .review-time {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-40);
      }
    }

    .review-body {
      .review-content {
        margin-top: var(--space-sm);
        font-size: var(--font-size-caption);
        color: var(--color-shade-50);
        line-height: 1.6;
      }

      .review-images {
        display: flex;
        gap: var(--space-sm);
        margin-top: var(--space-md);
        flex-wrap: wrap;

        .review-image {
          width: 80px;
          height: 80px;
          border-radius: var(--rounded-md);
          flex-shrink: 0;
        }
      }
    }

    .merchant-reply {
      background: var(--color-canvas-cream);
      border-radius: var(--rounded-md);
      padding: var(--space-md);
      margin-top: var(--space-md);

      .reply-label {
        font-size: var(--font-size-micro);
        font-weight: 500;
        color: var(--color-info);
        margin-bottom: var(--space-xs);
      }

      .reply-content {
        font-size: var(--font-size-micro);
        color: var(--color-shade-50);
        line-height: 1.5;
      }

      .reply-time {
        font-size: var(--font-size-eyebrow);
        color: var(--color-shade-40);
        margin-top: var(--space-xs);
        text-align: right;
      }
    }

    .review-actions {
      margin-top: var(--space-md);
      display: flex;
      justify-content: flex-end;
    }
  }

  .pagination-wrap {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
