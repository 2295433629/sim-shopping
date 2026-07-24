<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'
import ChangePasswordDialog from '@/components/ChangePasswordDialog.vue'
import { getMenus } from '@/api/modules/system'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const changePasswordRef = ref<InstanceType<typeof ChangePasswordDialog>>()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)

interface MenuNavItem {
  index: string
  title: string
  icon: string
}

interface MenuGroup {
  title: string
  items: MenuNavItem[]
}

const fallbackMenuGroups: MenuGroup[] = [
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
      { index: '/scheduler', title: '定时任务', icon: 'Timer' },
    ],
  },
]

const menuGroups = ref<MenuGroup[]>([...fallbackMenuGroups])

async function loadMenus() {
  try {
    const data = await getMenus()
    if (data && data.length > 0) {
      // 按 parentId 为 0 的作为根菜单
      const rootMenus = data.filter(m => m.parentId === 0).sort((a, b) => a.sortOrder - b.sortOrder)
      const groups = rootMenus
        .map(root => ({
          title: root.menuName,
          items: (root.children || [])
            .filter(c => c.visible !== 0 && c.menuType === 'MENU')
            .sort((a, b) => a.sortOrder - b.sortOrder)
            .map(c => ({
              index: c.path || '',
              title: c.menuName,
              icon: c.icon || 'Document',
            })),
        }))
        .filter(g => g.items.length > 0)
      if (groups.length > 0) {
        menuGroups.value = groups
      }
    }
    // fallback: data 为空时保留 menuGroups 已有的 fallbackMenuGroups
  } catch {
    // fallback to hardcoded
  }
}

onMounted(() => {
  loadMenus()
})

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

function handleCommand(cmd: string) {
  if (cmd === 'logout') {
    handleLogout()
  } else if (cmd === 'changePassword') {
    changePasswordRef.value?.open()
  }
}

function toggleFullscreen() {
  if (document.fullscreenElement) {
    document.exitFullscreen()
  } else {
    document.documentElement.requestFullscreen()
  }
}
</script>

<template>
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="aside-header">
        <span v-if="!isCollapse" class="aside-title">管理后台</span>
        <span v-else class="aside-title-mini">M</span>
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

          <template v-for="(group, gi) in menuGroups" :key="group.title || gi">
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
            <el-breadcrumb-item v-if="route.meta.parentTitle" :to="{ path: route.path.split('/').slice(0, -1).join('/') || route.path }">
              {{ route.meta.parentTitle }}
            </el-breadcrumb-item>
            <el-breadcrumb-item>{{ route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-tooltip content="全屏" placement="bottom">
            <el-icon class="header-action" @click="toggleFullscreen">
              <FullScreen />
            </el-icon>
          </el-tooltip>
          <el-dropdown @command="handleCommand">
            <span class="user-name">
              <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                {{ (userStore.userInfo?.nickname || userStore.userInfo?.username || 'A')[0] }}
              </el-avatar>
              <span class="name-text">{{ userStore.userInfo?.nickname || userStore.userInfo?.username || 'Admin' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>

  <!-- 修改密码弹窗 -->
  <ChangePasswordDialog ref="changePasswordRef" />
</template>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: var(--color-canvas-night);
  transition: width var(--transition-normal);
  overflow: hidden;
  display: flex;
  flex-direction: column;

  .aside-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--color-on-dark);
    border-bottom: 1px solid var(--color-hairline-dark);
    flex-shrink: 0;

    .aside-title {
      font-size: var(--font-size-heading-sm);
      font-weight: 600;
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
      white-space: nowrap;
    }

    .aside-title-mini {
      font-size: var(--font-size-heading-lg);
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
  background-color: var(--color-canvas-light);
  box-shadow: var(--shadow-sm);
  padding: 0 var(--space-xl);
  height: 60px;

  .header-left {
    display: flex;
    align-items: center;
    gap: var(--space-lg);

    .collapse-btn {
      font-size: var(--font-size-heading-sm);
      cursor: pointer;
      color: var(--color-ink);

      &:hover {
        color: var(--color-shade-50);
      }
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: var(--space-xl);

    .header-action {
      font-size: var(--font-size-heading-sm);
      cursor: pointer;
      color: var(--color-shade-50);
      transition: color 0.2s;
      &:hover { color: var(--color-ink); }
    }

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

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
