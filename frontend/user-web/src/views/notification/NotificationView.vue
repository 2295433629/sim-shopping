<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getNotifications,
  getUnreadCount,
  markAsRead,
  markAllAsRead,
  deleteNotification,
  type NotificationItem,
} from '@/api/modules/notification'
import { usePagination } from '@/composables/usePagination'

const unreadCount = ref(0)

const { loading, page, pageSize, total, list: notifications, loadList, handlePageChange } = usePagination<NotificationItem>({
  fetchFn: (page, pageSize) => getNotifications({ pageNum: page, pageSize }),
})

onMounted(() => {
  loadUnreadCount()
})

async function loadUnreadCount() {
  try {
    const data = await getUnreadCount()
    unreadCount.value = data.unreadCount || 0
  } catch {
    unreadCount.value = 0
  }
}

async function handleMarkRead(notification: NotificationItem) {
  if (notification.isRead === 1) return
  try {
    await markAsRead(notification.id)
    notification.isRead = 1
    notification.readAt = new Date().toISOString()
    unreadCount.value = Math.max(0, unreadCount.value - 1)
    ElMessage.success('已标记为已读')
  } catch {
    ElMessage.error('操作失败')
  }
}

async function handleMarkAllRead() {
  try {
    await markAllAsRead()
    notifications.value.forEach((n) => {
      n.isRead = 1
      if (!n.readAt) n.readAt = new Date().toISOString()
    })
    unreadCount.value = 0
    ElMessage.success('已全部标记为已读')
  } catch {
    ElMessage.error('操作失败')
  }
}

async function handleDelete(notification: NotificationItem) {
  try {
    await ElMessageBox.confirm('确定要删除这条消息吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteNotification(notification.id)
    if (notification.isRead === 0) {
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    }
    ElMessage.success('消息已删除')
    loadList()
  } catch {
    // cancelled or error
  }
}

const typeMap: Record<string, string> = {
  ORDER: '订单消息',
  PAYMENT: '支付消息',
  SHIPMENT: '发货消息',
  LOGISTICS: '物流消息',
  REVIEW: '评价消息',
  PROMOTION: '促销消息',
  SYSTEM: '系统消息',
}

const typeTagMap: Record<string, 'primary' | 'success' | 'warning' | 'info' | 'danger'> = {
  ORDER: 'primary',
  PAYMENT: 'success',
  SHIPMENT: 'warning',
  LOGISTICS: 'warning',
  REVIEW: 'info',
  PROMOTION: 'danger',
  SYSTEM: 'info',
}
</script>

<template>
  <div class="notification-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <span class="card-title">消息中心</span>
            <el-badge :value="unreadCount" :hidden="unreadCount === 0" type="danger" />
          </div>
          <el-button
            v-if="unreadCount > 0"
            type="primary"
            text
            size="small"
            @click="handleMarkAllRead"
          >
            全部已读
          </el-button>
        </div>
      </template>

      <div v-loading="loading">
        <div
          v-for="notification in notifications"
          :key="notification.id"
          class="notification-item"
          :class="{ unread: notification.isRead === 0 }"
        >
          <div class="notification-dot" v-if="notification.isRead === 0"></div>
          <div class="notification-content">
            <div class="notification-header">
              <el-tag :type="typeTagMap[notification.notificationType] || 'info'" size="small">
                {{ typeMap[notification.notificationType] || notification.notificationType }}
              </el-tag>
              <span class="notification-title">{{ notification.title }}</span>
              <span class="notification-time">{{ notification.createdAt }}</span>
            </div>
            <div class="notification-body">{{ notification.content }}</div>
          </div>
          <div class="notification-actions">
            <el-button
              v-if="notification.isRead === 0"
              type="primary"
              text
              size="small"
              @click="handleMarkRead(notification)"
            >
              标记已读
            </el-button>
            <el-button
              type="danger"
              text
              size="small"
              @click="handleDelete(notification)"
            >
              删除
            </el-button>
          </div>
        </div>

        <el-empty v-if="!loading && notifications.length === 0" description="暂无消息" />

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
.notification-page {
  max-width: 900px;
  margin: 0 auto;

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .header-left {
      display: flex;
      align-items: center;
      gap: var(--space-md);
    }

    .card-title {
      font-size: var(--font-size-body-md);
      font-weight: 500;
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    }
  }

  .notification-item {
    display: flex;
    align-items: flex-start;
    gap: var(--space-md);
    padding: var(--space-lg);
    border: 1px solid var(--color-hairline-light);
    border-radius: var(--rounded-lg);
    margin-bottom: var(--space-md);
    transition: background-color var(--transition-fast);

    &:hover {
      background: var(--color-canvas-cream);
    }

    &.unread {
      background: var(--color-aloe-10);
      border-color: var(--color-pistachio-10);
    }

    .notification-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: var(--color-ink);
      flex-shrink: 0;
      margin-top: 6px;
    }

    .notification-content {
      flex: 1;

      .notification-header {
        display: flex;
        align-items: center;
        gap: var(--space-sm);
        margin-bottom: 6px;

        .notification-title {
          font-size: var(--font-size-caption);
          font-weight: 500;
          color: var(--color-ink);
        }

        .notification-time {
          margin-left: auto;
          font-size: var(--font-size-eyebrow);
          color: var(--color-shade-40);
          flex-shrink: 0;
        }
      }

      .notification-body {
        font-size: var(--font-size-caption);
        color: var(--color-shade-50);
        line-height: 1.6;
      }
    }

    .notification-actions {
      display: flex;
      gap: var(--space-xs);
      flex-shrink: 0;
    }
  }

  .pagination-wrap {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: center;
  }
}
</style>
