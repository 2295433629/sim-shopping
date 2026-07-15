<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const searchKeyword = ref('')

function handleSearch() {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/search', query: { keyword: searchKeyword.value.trim() } })
  }
}

function handleCommand(command: string) {
  if (command === 'logout') {
    handleLogout()
  } else {
    router.push(command)
  }
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
    <!-- 顶部导航 -->
    <el-header class="layout-header">
      <div class="header-left">
        <div class="logo" @click="router.push('/home')">
          <svg class="logo-icon" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="40" height="40" rx="10" fill="url(#logoGrad)"/>
            <path d="M12 16L20 12L28 16V24L20 30L12 24V16Z" fill="white" fill-opacity="0.3"/>
            <path d="M14 18L20 15L26 18V23L20 27L14 23V18Z" fill="white"/>
            <circle cx="20" cy="20" r="3" fill="url(#logoGrad)"/>
            <path d="M11 13C11 13 13 8 20 7C27 8 29 13 29 13" stroke="white" stroke-width="2" stroke-linecap="round" fill="none"/>
            <circle cx="14" cy="12" r="1.5" fill="white"/>
            <circle cx="26" cy="12" r="1.5" fill="white"/>
            <defs>
              <linearGradient id="logoGrad" x1="0" y1="0" x2="40" y2="40">
                <stop offset="0%" stop-color="#FF6B6B"/>
                <stop offset="100%" stop-color="#FF8E53"/>
              </linearGradient>
            </defs>
          </svg>
          <span class="logo-text">
            <span class="logo-text-main">模拟商城</span>
            <span class="logo-text-sub">SIM SHOP</span>
          </span>
        </div>
      </div>
      <div class="header-search">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索商品"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
      <div class="header-right">
        <template v-if="userStore.isLoggedIn">
          <el-badge :value="0" :hidden="true" class="cart-badge">
            <el-button text @click="router.push('/cart')">
              <el-icon :size="22"><ShoppingCart /></el-icon>
            </el-button>
          </el-badge>
          <el-dropdown @command="handleCommand">
            <span class="user-name">
              <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                {{ (userStore.userInfo?.nickname || userStore.userInfo?.username || 'U')[0] }}
              </el-avatar>
              <span class="name-text">{{ userStore.userInfo?.nickname || userStore.userInfo?.username || '用户' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="/profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="/orders">我的订单</el-dropdown-item>
                <el-dropdown-item command="/favorites">我的收藏</el-dropdown-item>
                <el-dropdown-item command="/history">浏览历史</el-dropdown-item>
                <el-dropdown-item command="/coupon-center">领券中心</el-dropdown-item>
                <el-dropdown-item command="/my-coupons">我的优惠券</el-dropdown-item>
                <el-dropdown-item command="/points-mall">积分商城</el-dropdown-item>
                <el-dropdown-item command="/points-records">积分明细</el-dropdown-item>
                <el-dropdown-item command="/flash-sale">限时秒杀</el-dropdown-item>
                <el-dropdown-item command="/activities">专题活动</el-dropdown-item>
                <el-dropdown-item command="/rankings">排行榜</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" @click="router.push('/login')">登录 / 注册</el-button>
        </template>
      </div>
    </el-header>

    <!-- 内容区 -->
    <el-main class="layout-main">
      <router-view />
    </el-main>

    <!-- 底部 -->
    <el-footer class="layout-footer">
      <p class="footer-desc">模拟商城 — 提供真实购物体验的模拟平台，所有商品及交易均为模拟数据，仅供学习与体验使用</p>
    </el-footer>
  </el-container>
</template>

<style scoped lang="scss">
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--color-canvas-cream);
}

.layout-header {
  display: flex;
  align-items: center;
  background-color: var(--color-canvas-light);
  border-bottom: 1px solid var(--color-hairline-light);
  padding: 0 var(--space-xl);
  height: 72px;
  gap: var(--space-xl);
  position: sticky;
  top: 0;
  z-index: 100;
  backdrop-filter: blur(12px);
  background-color: rgba(255, 255, 255, 0.92);

  .header-left {
    .logo {
      display: flex;
      align-items: center;
      gap: 10px;
      cursor: pointer;
      white-space: nowrap;
      margin: 0;
      transition: transform var(--transition-fast);
      user-select: none;

      &:hover {
        transform: scale(1.03);

        .logo-icon {
          transform: rotate(-8deg) scale(1.05);
        }
      }

      &:active {
        transform: scale(0.98);
      }
    }

    .logo-icon {
      width: 38px;
      height: 38px;
      border-radius: 10px;
      transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
      filter: drop-shadow(0 2px 6px rgba(255, 107, 107, 0.35));
    }

    .logo-text {
      display: flex;
      flex-direction: column;
      line-height: 1.1;
    }

    .logo-text-main {
      font-size: 18px;
      font-weight: 800;
      background: linear-gradient(135deg, #FF6B6B 0%, #FF8E53 50%, #FFC857 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      letter-spacing: 1px;
    }

    .logo-text-sub {
      font-size: 9px;
      font-weight: 700;
      color: var(--color-shade-40);
      letter-spacing: 2.5px;
      text-transform: uppercase;
      margin-top: 1px;
    }
  }

  .header-search {
    flex: 1;
    max-width: 560px;
    margin: 0 auto;

    :deep(.el-input__wrapper) {
      border-radius: var(--rounded-pill) !important;
      box-shadow: 0 0 0 1px var(--color-hairline-light);
      transition: box-shadow var(--transition-normal);

      &:focus-within {
        box-shadow: 0 0 0 2px var(--color-ink);
      }
    }

    :deep(.el-input-group__append) {
      border-radius: 0 var(--rounded-pill) var(--rounded-pill) 0;
      background-color: var(--color-ink);
      color: var(--color-on-primary);
      border-color: var(--color-ink);
      padding: 0 20px;

      .el-button {
        color: var(--color-on-primary);
      }
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: var(--space-md);

    .cart-badge {
      :deep(.el-button) {
        color: var(--color-ink);
        transition: opacity var(--transition-fast);

        &:hover {
          opacity: 0.6;
        }
      }
    }

    .user-name {
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: var(--space-sm);
      color: var(--color-ink);
      transition: opacity var(--transition-fast);

      &:hover {
        opacity: 0.7;
      }

      .name-text {
        font-size: var(--font-size-caption);
        font-weight: 500;
        letter-spacing: 0.28px;
      }
    }
  }
}

.layout-main {
  flex: 1;
  background-color: var(--color-canvas-cream);
  padding: var(--space-xl);
}

.layout-footer {
  background-color: var(--color-canvas-night);
  color: var(--color-link-cool-2);
  text-align: center;
  padding: var(--space-huge) var(--space-xl);
  height: auto;

  .footer-desc {
    font-size: var(--font-size-caption);
    font-weight: 500;
    letter-spacing: 0.28px;
    max-width: 600px;
    margin: 0 auto;
    line-height: 1.8;
  }
}
</style>
