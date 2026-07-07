<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getMerchantReviews, replyReview, type MerchantReviewItem } from '@/api/modules/review'

const loading = ref(false)
const reviewList = ref<MerchantReviewItem[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 回复相关
const replyMap = ref<Record<number, string>>({})
const replyLoadingMap = ref<Record<number, boolean>>({})

onMounted(() => {
  loadReviews()
})

watch(page, () => {
  loadReviews()
})

async function loadReviews() {
  loading.value = true
  try {
    const data = await getMerchantReviews({ page: page.value, size: pageSize.value })
    reviewList.value = (data as any).list || []
    total.value = (data as any).total || 0
  } catch {
    reviewList.value = []
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
}

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
              :key="idx"
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
    font-size: 16px;
    font-weight: bold;
  }

  .review-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .review-item {
    padding: 16px;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    background: #fff;

    &:hover {
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    }
  }

  .review-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 8px;

    .review-user-info {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .review-username {
      font-size: 14px;
      font-weight: 600;
      color: #333;
    }

    .review-time {
      font-size: 12px;
      color: #999;
    }
  }

  .review-product {
    font-size: 13px;
    color: #666;
    margin-bottom: 6px;

    .product-label {
      color: #999;
    }
  }

  .review-content {
    font-size: 14px;
    color: #333;
    line-height: 1.6;
    margin-bottom: 8px;
  }

  .review-images {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    margin-bottom: 12px;

    .review-image {
      width: 80px;
      height: 80px;
      border-radius: 6px;
      cursor: pointer;
    }

    .image-placeholder {
      width: 80px;
      height: 80px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f5f5;
      border-radius: 6px;
      font-size: 12px;
      color: #999;
    }
  }

  .review-reply-section {
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px dashed #ebeef5;

    .merchant-reply {
      background: #f9f9f9;
      padding: 10px 12px;
      border-radius: 6px;
      font-size: 13px;

      .reply-label {
        color: #409eff;
        font-weight: 500;
      }

      .reply-content {
        color: #333;
      }
    }

    .reply-input-wrap {
      display: flex;
      flex-direction: column;
      gap: 8px;

      .reply-btn {
        align-self: flex-end;
      }
    }
  }

  .pagination-wrap {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>
