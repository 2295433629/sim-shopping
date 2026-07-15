<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getBrowseHistory, clearBrowseHistory } from '@/api/modules/product'
import type { BrowseHistoryItem } from '@/types/product'

const router = useRouter()
const loading = ref(false)
const historyList = ref<BrowseHistoryItem[]>([])

onMounted(() => {
  loadHistory()
})

async function loadHistory() {
  loading.value = true
  try {
    const data = await getBrowseHistory()
    historyList.value = data || []
  } catch {
    historyList.value = []
  } finally {
    loading.value = false
  }
}

async function handleClear() {
  try {
    await ElMessageBox.confirm('确定要清空所有浏览历史吗？', '提示', { type: 'warning' })
    await clearBrowseHistory()
    ElMessage.success('已清空')
    historyList.value = []
  } catch {
    // 取消
  }
}

function goToProduct(productId: number) {
  router.push(`/products/${productId}`)
}
</script>

<template>
  <div class="history-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">浏览历史</span>
          <el-button v-if="historyList.length > 0" type="danger" text @click="handleClear">
            清空历史
          </el-button>
        </div>
      </template>

      <div v-loading="loading" class="history-list">
        <div
          v-for="item in historyList"
          :key="item.id"
          class="history-item"
          @click="goToProduct(item.productId)"
        >
          <el-image
            :src="item.productImage || ''"
            fit="cover"
            class="history-image"
          >
            <template #error>
              <div class="image-placeholder">
                <el-icon :size="24"><Picture /></el-icon>
              </div>
            </template>
          </el-image>
          <div class="history-info">
            <div class="history-name">{{ item.productName }}</div>
            <div class="history-price">¥{{ item.price ? item.price.toFixed(2) : '0.00' }}</div>
            <div class="history-time">{{ item.viewedAt }}</div>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && historyList.length === 0" description="暂无浏览历史" />
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.history-container {
  max-width: 900px;
  margin: 0 auto;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .history-list {
    display: flex;
    flex-direction: column;
    gap: var(--space-md);
  }

  .history-item {
    display: flex;
    align-items: center;
    gap: var(--space-lg);
    padding: var(--space-md);
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    cursor: pointer;
    transition: box-shadow var(--transition-fast);

    &:hover {
      box-shadow: var(--shadow-sm);
    }
  }

  .history-image {
    width: 80px;
    height: 80px;
    border-radius: var(--rounded-md);
    flex-shrink: 0;
  }

  .image-placeholder {
    width: 80px;
    height: 80px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: var(--color-canvas-cream);
    border-radius: var(--rounded-md);
    color: var(--color-shade-30);
  }

  .history-info {
    flex: 1;

    .history-name {
      font-size: var(--font-size-caption);
      color: var(--color-ink);
      margin-bottom: 6px;
    }

    .history-price {
      font-size: var(--font-size-body-md);
      color: var(--color-price);
      font-weight: 500;
      margin-bottom: var(--space-xs);
    }

    .history-time {
      font-size: var(--font-size-eyebrow);
      color: var(--color-shade-40);
    }
  }
}
</style>
