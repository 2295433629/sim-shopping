<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getNotifications,
  getUnreadCount,
  markAsRead,
  markAllAsRead,
  deleteNotification,
  type NotificationItem,
} from '@/api/modules/notification'

const loading = ref(false)
const notificationList = ref<NotificationItem[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const unreadCount = ref(0)

onMounted(() => {
  loadNotifications()
  loadUnreadCount()
})

watch(page, () => {
  loadNotifications()
})

async function loadNotifications() {
  loading.value = true
  try {
    const data = await getNotifications({ page: page.value, size: pageSize.value })
    notificationList.value = (data as any).list || []
    total.value = (data as any).total || 0
  } catch {
    notificationList.value = []
  } finally {
    loading.value = false
  }
}

async function loadUnreadCount() {
  try {
    const res = await getUnreadCount() as any
    unreadCount.value = res?.unreadCount ?? res?.count ?? 0
  } catch {
    // handled by interceptor
  }
}

function handlePageChange(p: number) {
  page.value = p
}

async function handleMarkRead(item: NotificationItem) {
  try {
    await markAsRead(item.id)
    item.isRead = 1
    item.readAt = new Date().toISOString()
    unreadCount.value = Math.max(0, unreadCount.value - 1)
    ElMessage.success('已标记为已读')
  } catch {
    // handled by interceptor
  }
}

async function handleMarkAllRead() {
  try {
    await markAllAsRead()
    notificationList.value.forEach((item) => {
      item.isRead = 1
      item.readAt = new Date().toISOString()
    })
    unreadCount.value = 0
    ElMessage.success('已全部标记为已读')
  } catch {
    // handled by interceptor
  }
}

async function handleDelete(item: NotificationItem) {
  try {
    await ElMessageBox.confirm('确定要删除该消息吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteNotification(item.id)
    if (item.isRead === 0) {
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    }
    loadNotifications()
    ElMessage.success('删除成功')
  } catch {
    // 用户取消或请求错误
  }
}

function formatTime(time: string | undefined | null): string {
  if (!time) return '-'
  return time.replace('T', ' ').substring(0, 19)
}

function getNotificationTypeLabel(type: string): string {
  const map: Record<string, string> = {
    ORDER: '订单通知',
    SHIPMENT: '物流通知',
    SYSTEM: '系统通知',
    PROMOTION: '营销通知',
  }
  return map[type] || type
}

function getNotificationTypeTag(type: string): string {
  const map: Record<string, string> = {
    ORDER: 'primary',
    SHIPMENT: 'success',
    SYSTEM: 'info',
    PROMOTION: 'warning',
  }
  return map[type] || 'info'
}
</script>

<template>
  <div class="notification-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">消息中心</span>
          <div class="header-actions">
            <el-badge :value="unreadCount" :hidden="unreadCount === 0" :max="99">
              <span class="unread-text">{{ unreadCount }} 条未读</span>
            </el-badge>
            <el-button
              type="primary"
              size="small"
              :disabled="unreadCount === 0"
              @click="handleMarkAllRead"
            >
              全部已读
            </el-button>
          </div>
        </div>
      </template>

      <div v-loading="loading" class="notification-list">
        <div
          v-for="item in notificationList"
          :key="item.id"
          :class="['notification-item', { unread: item.isRead === 0 }]"
        >
          <div class="notification-dot" v-if="item.isRead === 0"></div>
          <div class="notification-body">
            <div class="notification-top">
              <div class="notification-title-row">
                <span class="notification-title">{{ item.title }}</span>
                <el-tag :type="getNotificationTypeTag(item.notificationType)" size="small" effect="plain">
                  {{ getNotificationTypeLabel(item.notificationType) }}
                </el-tag>
              </div>
              <span class="notification-time">{{ formatTime(item.createdAt) }}</span>
            </div>
            <div class="notification-content">{{ item.content }}</div>
            <div class="notification-actions">
              <el-button
                v-if="item.isRead === 0"
                link
                type="primary"
                size="small"
                @click="handleMarkRead(item)"
              >
                标记已读
              </el-button>
              <el-button link type="danger" size="small" @click="handleDelete(item)">
                删除
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && notificationList.length === 0" description="暂无消息" />

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
.notification-container {
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .card-title {
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
      font-weight: 330;
      font-size: var(--font-size-heading-lg);
      color: var(--color-ink);
    }

    .header-actions {
      display: flex;
      align-items: center;
      gap: var(--space-lg);

      .unread-text {
        font-size: var(--font-size-micro);
        color: var(--color-shade-50);
      }
    }
  }

  .notification-list {
    display: flex;
    flex-direction: column;
    gap: var(--space-md);
  }

  .notification-item {
    display: flex;
    align-items: flex-start;
    padding: 14px var(--space-lg);
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    background: var(--color-canvas-light);
    transition: box-shadow var(--transition-fast);

    &:hover {
      box-shadow: var(--shadow-sm);
    }

    &.unread {
      background: var(--color-canvas-cream);
      border-left: 3px solid var(--color-primary);
    }
  }

  .notification-dot {
    width: var(--space-sm);
    height: var(--space-sm);
    border-radius: 50%;
    background: var(--color-primary);
    margin-top: 6px;
    margin-right: var(--space-md);
    flex-shrink: 0;
  }

  .notification-body {
    flex: 1;
    min-width: 0;
  }

  .notification-top {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 6px;

    .notification-title-row {
      display: flex;
      align-items: center;
      gap: var(--space-sm);
    }

    .notification-title {
      font-size: var(--font-size-caption);
      font-weight: 600;
      color: var(--color-ink);
    }

    .notification-time {
      font-size: var(--font-size-eyebrow);
      color: var(--color-shade-40);
      flex-shrink: 0;
    }
  }

  .notification-content {
    font-size: var(--font-size-micro);
    color: var(--color-shade-50);
    line-height: 1.5;
    margin-bottom: var(--space-sm);
  }

  .notification-actions {
    display: flex;
    gap: var(--space-sm);
  }

  .pagination-wrap {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
