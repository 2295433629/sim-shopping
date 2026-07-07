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
        <h1 class="logo" @click="router.push('/home')">🛒 模拟商城</h1>
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
}

.layout-header {
  display: flex;
  align-items: center;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 0 24px;
  height: 64px;
  gap: 24px;

  .header-left {
    .logo {
      font-size: 20px;
      cursor: pointer;
      white-space: nowrap;
      margin: 0;
      color: #409eff;
    }
  }

  .header-search {
    flex: 1;
    max-width: 500px;
    margin: 0 auto;
  }

  .header-right {
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
  flex: 1;
  background-color: #f5f5f5;
  padding: 20px;
}

.layout-footer {
  background-color: #333;
  color: #999;
  text-align: center;
  line-height: 60px;
  height: 60px;

  .footer-desc {
    font-size: 13px;
  }
}
</style>
