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
const notifications = ref<NotificationItem[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const unreadCount = ref(0)

onMounted(() => {
  loadNotifications()
  loadUnreadCount()
})

watch([page], () => {
  loadNotifications()
})

async function loadNotifications() {
  loading.value = true
  try {
    const data = await getNotifications({ page: page.value, size: pageSize.value }) as any
    notifications.value = data.list || []
    total.value = data.total || 0
  } catch {
    notifications.value = []
  } finally {
    loading.value = false
  }
}

async function loadUnreadCount() {
  try {
    const data = await getUnreadCount() as any
    unreadCount.value = (data as any).unreadCount || 0
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
    loadNotifications()
  } catch {
    // cancelled or error
  }
}

function handlePageChange(p: number) {
  page.value = p
}

const typeMap: Record<string, string> = {
  ORDER: '订单消息',
  PAYMENT: '支付消息',
  SHIPMENT: '物流消息',
  PROMOTION: '促销消息',
  SYSTEM: '系统消息',
}

const typeTagMap: Record<string, string> = {
  ORDER: 'primary',
  PAYMENT: 'success',
  SHIPMENT: 'warning',
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
              <el-tag :type="(typeTagMap[notification.notificationType] as any) || 'info'" size="small">
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
      gap: 12px;
    }

    .card-title {
      font-size: 16px;
      font-weight: bold;
    }
  }

  .notification-item {
    display: flex;
    align-items: flex-start;
    gap: 12px;
    padding: 16px;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    margin-bottom: 12px;
    transition: background-color 0.2s;

    &:hover {
      background: #fafafa;
    }

    &.unread {
      background: #f0f7ff;
      border-color: #d0e3ff;
    }

    .notification-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: #409eff;
      flex-shrink: 0;
      margin-top: 6px;
    }

    .notification-content {
      flex: 1;

      .notification-header {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 6px;

        .notification-title {
          font-size: 14px;
          font-weight: 600;
          color: #303133;
        }

        .notification-time {
          margin-left: auto;
          font-size: 12px;
          color: #909399;
          flex-shrink: 0;
        }
      }

      .notification-body {
        font-size: 14px;
        color: #606266;
        line-height: 1.6;
      }
    }

    .notification-actions {
      display: flex;
      gap: 4px;
      flex-shrink: 0;
    }
  }

  .pagination-wrap {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>
