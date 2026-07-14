<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getConsumptionRanking, getSignInRanking, type RankingPeriod, type RankingItem } from '@/api/modules/ranking'
type MainTab = 'consumption' | 'signIn'

const mainTab = ref<MainTab>('consumption')
const subTab = ref<RankingPeriod>('week')
const loading = ref(false)
const list = ref<RankingItem[]>([])

const mainTabOptions = [
  { label: '消费榜', value: 'consumption' as MainTab },
  { label: '签到榜', value: 'signIn' as MainTab },
]

const subTabOptions = [
  { label: '本周', value: 'week' as RankingPeriod },
  { label: '本月', value: 'month' as RankingPeriod },
  { label: '总榜', value: 'total' as RankingPeriod },
]

const rankColors: Record<number, string> = {
  1: '#f7ba2a',
  2: '#909399',
  3: '#cd7f32',
}

const rankBgColors: Record<number, string> = {
  1: 'linear-gradient(135deg, #fffbeb 0%, #fef3c7 100%)',
  2: 'linear-gradient(135deg, #f9fafb 0%, #f3f4f6 100%)',
  3: 'linear-gradient(135deg, #fff7ed 0%, #ffedd5 100%)',
}

watch([mainTab, subTab], () => {
  loadRanking()
})

onMounted(() => {
  loadRanking()
})

async function loadRanking() {
  loading.value = true
  try {
    let result
    if (mainTab.value === 'consumption') {
      result = await getConsumptionRanking(subTab.value)
    } else {
      result = await getSignInRanking(subTab.value)
    }
    list.value = result.list || []
  } catch {
    list.value = []
    ElMessage.error('加载排行榜失败')
  } finally {
    loading.value = false
  }
}

function formatValue(item: RankingItem) {
  if (mainTab.value === 'consumption') {
    return `¥${item.value.toFixed(2)}`
  }
  return `${item.value} 天`
}
</script>

<template>
  <div class="ranking-container">
    <el-card shadow="never" v-loading="loading">
      <template #header>
        <div class="card-header">
          <span class="card-title">排行榜</span>
        </div>
      </template>

      <!-- 主 Tab -->
      <div class="main-tabs">
        <div
          v-for="opt in mainTabOptions"
          :key="opt.value"
          class="main-tab-item"
          :class="{ active: mainTab === opt.value }"
          @click="mainTab = opt.value"
        >
          {{ opt.label }}
        </div>
      </div>

      <!-- 子 Tab -->
      <div class="sub-tabs">
        <el-radio-group v-model="subTab" size="small">
          <el-radio-button
            v-for="opt in subTabOptions"
            :key="opt.value"
            :label="opt.value"
            :value="opt.value"
          >
            {{ opt.label }}
          </el-radio-button>
        </el-radio-group>
      </div>

      <!-- 前三名展示 -->
      <div v-if="list.length > 0" class="top-three">
        <div
          v-for="rank in [2, 1, 3]"
          :key="rank"
          class="top-item"
          :class="`top-${rank}`"
          :style="{ background: rankBgColors[rank] }"
        >
          <div class="top-rank">
            <el-icon :size="rank === 1 ? 36 : 28" :color="rankColors[rank]">
              <Trophy />
            </el-icon>
            <span class="rank-num" :style="{ color: rankColors[rank] }">{{ rank }}</span>
          </div>
          <el-avatar :size="rank === 1 ? 72 : 56" :src="list[rank - 1]?.avatar">
            {{ (list[rank - 1]?.nickname || 'U')[0] }}
          </el-avatar>
          <div class="top-nickname">{{ list[rank - 1]?.nickname || '-' }}</div>
          <div class="top-value">{{ list[rank - 1] ? formatValue(list[rank - 1]) : '-' }}</div>
        </div>
      </div>

      <!-- 列表展示 -->
      <div v-if="list.length > 0" class="ranking-list">
        <div
          v-for="item in list"
          :key="item.userId"
          class="ranking-item"
          :class="{ 'top-bg': item.rank <= 3 }"
        >
          <div class="rank-number">
            <span v-if="item.rank <= 3" class="rank-badge" :style="{ backgroundColor: rankColors[item.rank] }">
              {{ item.rank }}
            </span>
            <span v-else class="rank-text">{{ item.rank }}</span>
          </div>
          <el-avatar :size="40" :src="item.avatar">
            {{ (item.nickname || 'U')[0] }}
          </el-avatar>
          <div class="user-info">
            <div class="nickname">{{ item.nickname }}</div>
          </div>
          <div class="value">{{ formatValue(item) }}</div>
        </div>
      </div>

      <el-empty v-if="!loading && list.length === 0" description="暂无数据" />
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.ranking-container {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
}

.main-tabs {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 20px;

  .main-tab-item {
    padding: 10px 32px;
    border-radius: 24px;
    font-size: 15px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s;
    background-color: #f5f5f5;
    color: #666;

    &:hover {
      background-color: #e8e8e8;
    }

    &.active {
      background-color: #409eff;
      color: #fff;
    }
  }
}

.sub-tabs {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.top-three {
  display: flex;
  justify-content: center;
  align-items: flex-end;
  gap: 16px;
  margin-bottom: 32px;
  padding: 20px 0;

  .top-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px 16px;
    border-radius: 16px;
    min-width: 140px;
    transition: transform 0.3s;

    &:hover {
      transform: translateY(-4px);
    }
  }

  .top-1 {
    order: 2;
    padding: 28px 20px;
    min-width: 160px;
  }

  .top-2 {
    order: 1;
  }

  .top-3 {
    order: 3;
  }

  .top-rank {
    display: flex;
    align-items: center;
    gap: 4px;
    margin-bottom: 8px;

    .rank-num {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .top-nickname {
    margin-top: 10px;
    font-size: 15px;
    font-weight: 600;
    color: #333;
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .top-value {
    margin-top: 6px;
    font-size: 14px;
    color: #666;
    font-weight: 500;
  }
}

.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 20px;
  border-radius: 10px;
  background-color: #fafafa;
  transition: background-color 0.2s;

  &:hover {
    background-color: #f0f0f0;
  }

  &.top-bg {
    background-color: #fffbeb;
  }
}

.rank-number {
  width: 36px;
  text-align: center;
  flex-shrink: 0;

  .rank-badge {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 28px;
    height: 28px;
    border-radius: 50%;
    color: #fff;
    font-size: 14px;
    font-weight: bold;
  }

  .rank-text {
    font-size: 15px;
    color: #999;
    font-weight: 500;
  }
}

.user-info {
  flex: 1;
  min-width: 0;

  .nickname {
    font-size: 14px;
    color: #333;
    font-weight: 500;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.value {
  font-size: 15px;
  font-weight: bold;
  color: #f56c6c;
  flex-shrink: 0;
}
</style>
