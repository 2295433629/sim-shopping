<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getActiveActivities, type Activity } from '@/api/modules/activity'

const router = useRouter()
const loading = ref(false)
const activityList = ref<Activity[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(12)

onMounted(() => {
  loadActivities()
})

async function loadActivities() {
  loading.value = true
  try {
    const res = await getActiveActivities({ page: page.value, size: size.value })
    activityList.value = res.list || []
    total.value = res.total || 0
  } catch {
    activityList.value = []
    total.value = 0
    ElMessage.error('加载专题活动失败')
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
  loadActivities()
}

function goToDetail(activityId: number) {
  router.push(`/activities/${activityId}`)
}

function formatTimeRange(startTime: string, endTime: string): string {
  const start = new Date(startTime)
  const end = new Date(endTime)
  const format = (d: Date) => `${d.getMonth() + 1}月${d.getDate()}日 ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
  return `${format(start)} - ${format(end)}`
}
</script>

<template>
  <div class="activity-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">
            <el-icon><Star /></el-icon>
            专题活动
          </span>
          <span class="card-subtitle">精选专题，好物汇聚</span>
        </div>
      </template>

      <div v-loading="loading" class="activity-list">
        <el-row :gutter="16">
          <el-col v-for="item in activityList" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6">
            <div class="activity-card" @click="goToDetail(item.id)">
              <div class="activity-image">
                <el-image :src="item.bannerImage" fit="cover" class="banner-img">
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon :size="40" color="#ccc"><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="activity-badge">专题</div>
              </div>
              <div class="activity-body">
                <div class="activity-name">{{ item.activityName }}</div>
                <div class="activity-desc" v-if="item.description">{{ item.description }}</div>
                <div class="activity-time">
                  <el-icon><Clock /></el-icon>
                  <span>{{ formatTimeRange(item.startTime, item.endTime) }}</span>
                </div>
                <div class="activity-meta">
                  <span class="product-count">{{ item.productCount }} 件商品</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-empty v-if="!loading && activityList.length === 0" description="暂无进行中的专题活动" />
      </div>

      <div class="pagination-wrapper" v-if="total > 0">
        <el-pagination
          v-model:current-page="page"
          :page-size="size"
          :total="total"
          layout="prev, pager, next, total"
          background
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.activity-container {
  max-width: 1200px;
  margin: 0 auto;

  .card-header {
    display: flex;
    align-items: center;
    gap: var(--space-md);

    .card-title {
      font-size: var(--font-size-heading-sm);
      font-weight: 500;
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
      color: var(--color-warning);
      display: flex;
      align-items: center;
      gap: var(--space-sm);
    }

    .card-subtitle {
      font-size: var(--font-size-micro);
      color: var(--color-shade-40);
    }
  }

  .activity-list {
    min-height: 400px;
  }

  .activity-card {
    background: var(--color-canvas-light);
    border-radius: var(--rounded-lg);
    overflow: hidden;
    border: 1px solid var(--color-hairline-light);
    margin-bottom: var(--space-lg);
    cursor: pointer;
    transition: box-shadow var(--transition-normal), transform var(--transition-normal);

    &:hover {
      box-shadow: var(--shadow-lg);
      transform: translateY(-2px);
    }

    .activity-image {
      position: relative;
      height: 180px;
      background-color: var(--color-canvas-cream);

      .banner-img {
        width: 100%;
        height: 100%;
      }

      .image-placeholder {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .activity-badge {
        position: absolute;
        top: var(--space-sm);
        left: var(--space-sm);
        background: linear-gradient(135deg, #f5c842, var(--color-warning));
        color: #fff;
        font-size: var(--font-size-eyebrow);
        font-weight: 500;
        padding: var(--space-xs) 10px;
        border-radius: var(--rounded-md);
      }
    }

    .activity-body {
      padding: var(--space-md);

      .activity-name {
        font-size: var(--font-size-heading-sm);
        font-weight: 500;
        color: var(--color-ink);
        margin-bottom: 6px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .activity-desc {
        font-size: var(--font-size-micro);
        color: var(--color-shade-50);
        margin-bottom: var(--space-sm);
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        line-height: 1.4;
      }

      .activity-time {
        display: flex;
        align-items: center;
        gap: 6px;
        color: var(--color-shade-40);
        font-size: var(--font-size-eyebrow);
        margin-bottom: 6px;
      }

      .activity-meta {
        display: flex;
        align-items: center;
        justify-content: space-between;

        .product-count {
          font-size: var(--font-size-eyebrow);
          color: var(--color-info);
          background-color: #ecf5ff;
          padding: 2px var(--space-sm);
          border-radius: var(--rounded-md);
        }
      }
    }
  }

  .pagination-wrapper {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
