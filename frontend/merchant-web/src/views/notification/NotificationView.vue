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
    unreadCount.value = (await getUnreadCount()) as any
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
      font-size: 16px;
      font-weight: bold;
    }

    .header-actions {
      display: flex;
      align-items: center;
      gap: 16px;

      .unread-text {
        font-size: 13px;
        color: #666;
      }
    }
  }

  .notification-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .notification-item {
    display: flex;
    align-items: flex-start;
    padding: 14px 16px;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    background: #fff;
    transition: box-shadow 0.2s;

    &:hover {
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    }

    &.unread {
      background: #fafbfc;
      border-left: 3px solid #409eff;
    }
  }

  .notification-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: #409eff;
    margin-top: 6px;
    margin-right: 12px;
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
      gap: 8px;
    }

    .notification-title {
      font-size: 14px;
      font-weight: 600;
      color: #333;
    }

    .notification-time {
      font-size: 12px;
      color: #999;
      flex-shrink: 0;
    }
  }

  .notification-content {
    font-size: 13px;
    color: #666;
    line-height: 1.5;
    margin-bottom: 8px;
  }

  .notification-actions {
    display: flex;
    gap: 8px;
  }

  .pagination-wrap {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>
