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

interface MenuItem {
  index: string
  title: string
  icon: string
}

interface MenuGroup {
  title: string
  items: MenuItem[]
}

const menuGroups: MenuGroup[] = [
  {
    title: '系统管理',
    items: [
      { index: '/users', title: '用户管理', icon: 'User' },
      { index: '/merchants', title: '商家管理', icon: 'Shop' },
      { index: '/roles', title: '角色管理', icon: 'UserFilled' },
      { index: '/permissions', title: '权限管理', icon: 'Key' },
      { index: '/menus', title: '菜单管理', icon: 'Menu' },
      { index: '/dicts', title: '字典管理', icon: 'Collection' },
    ],
  },
  {
    title: '商品管理',
    items: [
      { index: '/products', title: '商品列表', icon: 'Goods' },
    ],
  },
  {
    title: '订单管理',
    items: [
      { index: '/orders', title: '订单列表', icon: 'Document' },
    ],
  },
  {
    title: '内容管理',
    items: [
      { index: '/reviews', title: '评价管理', icon: 'ChatDotRound' },
      { index: '/banners', title: 'Banner管理', icon: 'Picture' },
    ],
  },
  {
    title: '营销管理',
    items: [
      { index: '/coupons', title: '优惠券管理', icon: 'Ticket' },
      { index: '/points/products', title: '积分商品管理', icon: 'Goods' },
      { index: '/points/records', title: '积分流水管理', icon: 'Document' },
      { index: '/flash-sales', title: '秒杀活动管理', icon: 'AlarmClock' },
      { index: '/activities', title: '专题活动管理', icon: 'Star' },
    ],
  },
  {
    title: '系统监控',
    items: [
      { index: '/logs/operation', title: '操作日志', icon: 'Document' },
      { index: '/logs/login', title: '登录日志', icon: 'Tickets' },
    ],
  },
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
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="aside-header">
        <span v-if="!isCollapse" class="aside-title">⚡ 管理后台</span>
        <span v-else class="aside-title-mini">⚡</span>
      </div>
      <el-scrollbar class="aside-scrollbar">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          @select="handleSelect"
          background-color="#001529"
          text-color="rgba(255,255,255,0.65)"
          active-text-color="#fff"
        >
          <!-- 仪表盘单独项 -->
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <template #title>仪表盘</template>
          </el-menu-item>

          <template v-for="(group, gi) in menuGroups" :key="gi">
            <el-sub-menu v-if="!isCollapse" :index="`group-${gi}`">
              <template #title>
                <el-icon><component :is="group.items[0].icon" /></el-icon>
                <span>{{ group.title }}</span>
              </template>
              <el-menu-item v-for="item in group.items" :key="item.index" :index="item.index">
                <el-icon><component :is="item.icon" /></el-icon>
                <template #title>{{ item.title }}</template>
              </el-menu-item>
            </el-sub-menu>
            <template v-else>
              <el-menu-item v-for="item in group.items" :key="item.index" :index="item.index">
                <el-icon><component :is="item.icon" /></el-icon>
                <template #title>{{ item.title }}</template>
              </el-menu-item>
            </template>
          </template>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <!-- 右侧 -->
    <el-container>
      <!-- 顶部栏 -->
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="(cmd: string) => cmd === 'logout' ? handleLogout() : router.push(cmd)">
            <span class="user-name">
              <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                {{ (userStore.userInfo?.nickname || userStore.userInfo?.username || 'A')[0] }}
              </el-avatar>
              <span class="name-text">{{ userStore.userInfo?.nickname || userStore.userInfo?.username || 'Admin' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
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
  background-color: #001529;
  transition: width 0.3s;
  overflow: hidden;
  display: flex;
  flex-direction: column;

  .aside-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    border-bottom: 1px solid #1f2d3d;
    flex-shrink: 0;

    .aside-title {
      font-size: 18px;
      font-weight: bold;
      white-space: nowrap;
    }

    .aside-title-mini {
      font-size: 24px;
    }
  }

  .aside-scrollbar {
    flex: 1;

    :deep(.el-menu) {
      border-right: none;
    }
  }
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  padding: 0 20px;
  height: 60px;

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .collapse-btn {
      font-size: 20px;
      cursor: pointer;
      color: #333;

      &:hover {
        color: #409eff;
      }
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 20px;

    .user-name {
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 8px;
      color: #333;

      .name-text {
        font-size: 14px;
      }
    }
  }
}

.layout-main {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>
