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
          <el-col v-for="item in activityList" :key="item.activityId" :xs="24" :sm="12" :md="8" :lg="6">
            <div class="activity-card" @click="goToDetail(item.activityId)">
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
                <div class="activity-name">{{ item.name }}</div>
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
    gap: 12px;

    .card-title {
      font-size: 18px;
      font-weight: bold;
      color: #e6a23c;
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .card-subtitle {
      font-size: 13px;
      color: #999;
    }
  }

  .activity-list {
    min-height: 400px;
  }

  .activity-card {
    background: #fff;
    border-radius: 8px;
    overflow: hidden;
    border: 1px solid #ebeef5;
    margin-bottom: 16px;
    cursor: pointer;
    transition: box-shadow 0.3s, transform 0.3s;

    &:hover {
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
      transform: translateY(-2px);
    }

    .activity-image {
      position: relative;
      height: 180px;
      background-color: #f5f5f5;

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
        top: 8px;
        left: 8px;
        background: linear-gradient(135deg, #f5c842, #e6a23c);
        color: #fff;
        font-size: 12px;
        font-weight: bold;
        padding: 4px 10px;
        border-radius: 4px;
      }
    }

    .activity-body {
      padding: 12px;

      .activity-name {
        font-size: 15px;
        font-weight: bold;
        color: #333;
        margin-bottom: 6px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .activity-desc {
        font-size: 13px;
        color: #666;
        margin-bottom: 8px;
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
        color: #999;
        font-size: 12px;
        margin-bottom: 6px;
      }

      .activity-meta {
        display: flex;
        align-items: center;
        justify-content: space-between;

        .product-count {
          font-size: 12px;
          color: #409eff;
          background-color: #ecf5ff;
          padding: 2px 8px;
          border-radius: 4px;
        }
      }
    }
  }

  .pagination-wrapper {
    margin-top: 24px;
    display: flex;
    justify-content: center;
  }
}
</style>
