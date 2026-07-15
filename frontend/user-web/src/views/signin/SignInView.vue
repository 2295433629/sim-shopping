<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { signIn, getTodaySignInStatus, getSignInRecords, type SignInResult, type SignInRecord } from '@/api/modules/signin'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const signedToday = ref(false)
const points = ref(0)
const consecutiveDays = ref(0)
const records = ref<SignInRecord[]>([])
const page = ref(1)
const pageSize = ref(30)
const totalRecords = ref(0)

onMounted(() => {
  loadStatus()
  loadRecords()
})

async function loadStatus() {
  loading.value = true
  try {
    const data = await getTodaySignInStatus() as any
    signedToday.value = data.signed || false
    consecutiveDays.value = data.consecutiveDays || 0
    points.value = data.points || 0
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

async function loadRecords() {
  try {
    const data = await getSignInRecords({ page: page.value, size: pageSize.value }) as any
    records.value = data.list || []
    totalRecords.value = data.total || 0
  } catch {
    records.value = []
  }
}

async function handleSignIn() {
  if (signedToday.value) return
  loading.value = true
  try {
    const data = await signIn() as any
    const result = data as SignInResult
    signedToday.value = true
    consecutiveDays.value = result.consecutiveDays || consecutiveDays.value + 1
    points.value = result.points || points.value
    ElMessage.success(`签到成功！获得 ${result.points} 积分，已连续签到 ${result.consecutiveDays} 天`)
    loadRecords()
  } catch (e: any) {
    ElMessage.error(e.message || '签到失败')
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
  loadRecords()
}

/** 格式化日期用于日历显示 */
function formatDate(dateStr: string): string {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}
</script>

<template>
  <div class="signin-page">
    <el-card shadow="never" class="signin-card">
      <div class="signin-content">
        <div class="signin-info">
          <div class="streak-info">
            <div class="streak-number">{{ consecutiveDays }}</div>
            <div class="streak-label">连续签到天数</div>
          </div>
          <div class="points-info">
            <div class="points-number">{{ points }}</div>
            <div class="points-label">今日积分</div>
          </div>
        </div>

        <el-button
          type="primary"
          size="large"
          round
          :loading="loading"
          :disabled="signedToday"
          @click="handleSignIn"
          class="signin-btn"
        >
          {{ signedToday ? '今日已签到' : '立即签到' }}
        </el-button>
      </div>
    </el-card>

    <el-card shadow="never" class="records-card">
      <template #header>
        <span class="card-title">签到记录</span>
      </template>

      <div v-loading="loading">
        <div v-for="record in records" :key="record.id" class="record-item">
          <div class="record-date">{{ formatDate(record.signDate) }}</div>
          <div class="record-detail">
            <el-tag type="success" size="small">+{{ record.pointsEarned }} 积分</el-tag>
            <span class="record-streak">连续第 {{ record.consecutiveDays }} 天</span>
          </div>
        </div>

        <el-empty v-if="!loading && records.length === 0" description="暂无签到记录" />

        <div v-if="totalRecords > pageSize" class="pagination-wrap">
          <el-pagination
            v-model:current-page="page"
            :page-size="pageSize"
            :total="totalRecords"
            layout="prev, pager, next"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.signin-page {
  max-width: 800px;
  margin: 0 auto;

  .signin-card {
    margin-bottom: var(--space-lg);

    .signin-content {
      text-align: center;
      padding: var(--space-xl) 0;
    }

    .signin-info {
      display: flex;
      justify-content: center;
      gap: var(--space-huge);
      margin-bottom: var(--space-xl);

      .streak-info,
      .points-info {
        display: flex;
        flex-direction: column;
        align-items: center;
      }

      .streak-number,
      .points-number {
        font-size: var(--font-size-display-lg);
        font-weight: 330;
        color: var(--color-ink);
        line-height: 1.2;
        font-family: var(--font-display, 'Helvetica Neue', sans-serif);
      }

      .streak-label,
      .points-label {
        font-size: var(--font-size-caption);
        color: var(--color-shade-40);
        margin-top: var(--space-xs);
      }
    }

    .signin-btn {
      width: 200px;
      height: 48px;
      font-size: var(--font-size-body-md);
    }
  }

  .records-card {
    .card-title {
      font-size: var(--font-size-body-md);
      font-weight: 500;
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    }

    .record-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: var(--space-md) 0;
      border-bottom: 1px solid var(--color-hairline-light);

      &:last-child {
        border-bottom: none;
      }

      .record-date {
        font-size: var(--font-size-caption);
        color: var(--color-ink);
        font-weight: 500;
      }

      .record-detail {
        display: flex;
        align-items: center;
        gap: var(--space-md);

        .record-streak {
          font-size: var(--font-size-micro);
          color: var(--color-shade-40);
        }
      }
    }

    .pagination-wrap {
      margin-top: var(--space-xl);
      display: flex;
      justify-content: center;
    }
  }
}
</style>
