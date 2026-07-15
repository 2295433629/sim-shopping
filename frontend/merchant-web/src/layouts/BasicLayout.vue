<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)

const menuItems = [
  { index: '/home', title: '工作台', icon: 'HomeFilled' },
  { index: '/products', title: '商品管理', icon: 'Goods' },
  { index: '/orders', title: '订单管理', icon: 'List' },
  { index: '/orders/shipping', title: '待发货', icon: 'Van' },
  { index: '/reviews', title: '评价管理', icon: 'ChatDotRound' },
  { index: '/notifications', title: '消息中心', icon: 'Bell' },
  { index: '/finance', title: '财务收入', icon: 'Wallet' },
  { index: '/settings', title: '店铺设置', icon: 'Setting' },
]

function handleSelect(index: string) {
  router.push(index)
}

async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await userStore.logout()
    router.push('/login')
  } catch {
    // 用户取消
  }
}
</script>

<template>
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside :width="isCollapse ? '64px' : '210px'" class="layout-aside">
      <div class="aside-header">
        <span v-if="!isCollapse" class="aside-title">🏪 商家管理</span>
        <span v-else class="aside-title-mini">🏪</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        @select="handleSelect"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容区 -->
    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <span class="merchant-name">{{ userStore.userInfo?.shopName || userStore.userInfo?.nickname || userStore.userInfo?.username || '商家' }}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="(cmd: string) => cmd === 'logout' ? handleLogout() : router.push(cmd)">
            <span class="user-name">
              <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                {{ (userStore.userInfo?.nickname || userStore.userInfo?.username || '商')[0] }}
              </el-avatar>
              <span class="name-text">{{ userStore.userInfo?.nickname || userStore.userInfo?.username || '商家' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="/settings">店铺设置</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: var(--color-canvas-night);
  transition: width var(--transition-normal);
  overflow: hidden;

  .aside-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--color-on-dark);
    border-bottom: 1px solid var(--color-surface-elevated-dark);

    .aside-title {
      font-size: var(--font-size-heading-sm);
      font-weight: 500;
      white-space: nowrap;
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    }

    .aside-title-mini {
      font-size: 24px;
    }
  }

  :deep(.el-menu) {
    border-right: none;
    background-color: var(--color-canvas-night) !important;
  }

  :deep(.el-menu-item) {
    color: var(--color-link-cool-1) !important;

    &:hover,
    &:focus {
      background-color: var(--color-canvas-night-elevated) !important;
      color: var(--color-on-dark) !important;
    }

    &.is-active {
      color: var(--color-on-dark) !important;
      background-color: var(--color-canvas-night-elevated) !important;
      border-right: 3px solid var(--color-aloe-10);
    }
  }
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: var(--color-canvas-light);
  box-shadow: var(--shadow-sm);
  padding: 0 var(--space-xl);
  height: 60px;

  .header-left {
    display: flex;
    align-items: center;
    gap: var(--space-lg);

    .collapse-btn {
      font-size: 20px;
      cursor: pointer;
      color: var(--color-ink);
      transition: color var(--transition-fast);

      &:hover {
        color: var(--color-shade-50);
      }
    }

    .merchant-name {
      font-size: var(--font-size-body-md);
      font-weight: 500;
      color: var(--color-ink);
    }
  }

  .header-right {
    .user-name {
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: var(--space-sm);
      color: var(--color-ink);

      .name-text {
        font-size: var(--font-size-caption);
      }
    }
  }
}

.layout-main {
  background-color: var(--color-canvas-cream);
  padding: var(--space-xl);
  overflow-y: auto;
}
</style>
