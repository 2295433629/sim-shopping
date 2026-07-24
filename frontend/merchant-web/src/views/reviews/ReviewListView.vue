<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMerchantReviews, replyReview, type MerchantReviewItem } from '@/api/modules/review'
import { usePagination } from '@/composables/usePagination'
import type { PageResponse } from '@/types/common'

// 回复相关
const replyMap = ref<Record<number, string>>({})
const replyLoadingMap = ref<Record<number, boolean>>({})

const {
  loading,
  page,
  pageSize,
  total,
  list: reviewList,
  loadList: loadReviews,
  handlePageChange,
} = usePagination<MerchantReviewItem>(
  async () => {
    const data = await getMerchantReviews({ page: page.value, size: pageSize.value }) as unknown as PageResponse<MerchantReviewItem>
    return { list: data.list || [], total: data.total || 0 }
  }
)

onMounted(() => {
  loadReviews()
})

function handleReply(item: MerchantReviewItem) {
  const content = (replyMap.value[item.id] || '').trim()
  if (!content) {
    ElMessage.warning('请输入回复内容')
    return
  }
  replyLoadingMap.value[item.id] = true
  replyReview(item.id, content)
    .then(() => {
      ElMessage.success('回复成功')
      replyMap.value[item.id] = ''
      loadReviews()
    })
    .catch(() => {
      // handled by interceptor
    })
    .finally(() => {
      replyLoadingMap.value[item.id] = false
    })
}
</script>

<template>
  <div class="review-list-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">评价管理</span>
      </template>

      <div v-loading="loading" class="review-list">
        <div v-for="item in reviewList" :key="item.id" class="review-item">
          <div class="review-header">
            <div class="review-user-info">
              <span class="review-username">{{ item.username }}</span>
              <el-rate
                :model-value="item.rating"
                disabled
                show-score
                score-template="{value}分"
                size="small"
              />
            </div>
            <span class="review-time">{{ item.createdAt }}</span>
          </div>

          <div class="review-product">
            <span class="product-label">商品：</span>{{ item.productName }}
          </div>

          <div class="review-content">{{ item.content }}</div>

          <div v-if="item.images && item.images.length > 0" class="review-images">
            <el-image
              v-for="(img, idx) in item.images"
              :key="img + '-' + idx"
              :src="img"
              fit="cover"
              class="review-image"
              :preview-src-list="item.images"
              :initial-index="idx"
            >
              <template #error>
                <div class="image-placeholder">加载失败</div>
              </template>
            </el-image>
          </div>

          <!-- 商家回复区域 -->
          <div class="review-reply-section">
            <template v-if="item.merchantReply">
              <div class="merchant-reply">
                <span class="reply-label">商家回复：</span>
                <span class="reply-content">{{ item.merchantReply }}</span>
              </div>
            </template>
            <template v-else>
              <div class="reply-input-wrap">
                <el-input
                  v-model="replyMap[item.id]"
                  type="textarea"
                  :rows="2"
                  placeholder="请输入回复内容..."
                  maxlength="200"
                  show-word-limit
                />
                <el-button
                  type="primary"
                  size="small"
                  class="reply-btn"
                  :loading="replyLoadingMap[item.id]"
                  @click="handleReply(item)"
                >
                  回复
                </el-button>
              </div>
            </template>
          </div>
        </div>
      </div>

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
  </div>
</template>

<style scoped lang="scss">
.review-list-container {
  .card-title {
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
    font-size: var(--font-size-heading-lg);
    color: var(--color-ink);
  }

  .review-list {
    display: flex;
    flex-direction: column;
    gap: var(--space-lg);
  }

  .review-item {
    padding: var(--space-lg);
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    background: var(--color-canvas-light);
    transition: box-shadow var(--transition-fast);

    &:hover {
      box-shadow: var(--shadow-sm);
    }
  }

  .review-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: var(--space-sm);

    .review-user-info {
      display: flex;
      align-items: center;
      gap: var(--space-md);
    }

    .review-username {
      font-size: var(--font-size-caption);
      font-weight: 600;
      color: var(--color-ink);
    }

    .review-time {
      font-size: var(--font-size-eyebrow);
      color: var(--color-shade-40);
    }
  }

  .review-product {
    font-size: var(--font-size-micro);
    color: var(--color-shade-50);
    margin-bottom: 6px;

    .product-label {
      color: var(--color-shade-40);
    }
  }

  .review-content {
    font-size: var(--font-size-caption);
    color: var(--color-ink);
    line-height: 1.6;
    margin-bottom: var(--space-sm);
  }

  .review-images {
    display: flex;
    gap: var(--space-sm);
    flex-wrap: wrap;
    margin-bottom: var(--space-md);

    .review-image {
      width: 80px;
      height: 80px;
      border-radius: var(--rounded-md);
      cursor: pointer;
    }

    .image-placeholder {
      width: 80px;
      height: 80px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--color-canvas-cream);
      border-radius: var(--rounded-md);
      font-size: var(--font-size-eyebrow);
      color: var(--color-shade-40);
    }
  }

  .review-reply-section {
    margin-top: var(--space-md);
    padding-top: var(--space-md);
    border-top: 1px dashed var(--color-hairline-light);

    .merchant-reply {
      background: var(--color-canvas-cream);
      padding: 10px var(--space-md);
      border-radius: var(--rounded-md);
      font-size: var(--font-size-micro);

      .reply-label {
        color: var(--color-ink);
        font-weight: 500;
      }

      .reply-content {
        color: var(--color-ink);
      }
    }

    .reply-input-wrap {
      display: flex;
      flex-direction: column;
      gap: var(--space-sm);

      .reply-btn {
        align-self: flex-end;
      }
    }
  }

  .pagination-wrap {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
