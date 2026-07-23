<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

// 三端地址：生产环境同域名不同路径，开发环境不同端口
const baseUrl = import.meta.env.VITE_APP_BASE_URL || ''
const userWebUrl = `${window.location.origin}${baseUrl}/user-web/`
const merchantWebUrl = `${window.location.origin}${baseUrl}/merchant/`

const loginForm = reactive({
  username: '',
  password: '',
})

const rules = reactive<FormRules>({
  username: [{ required: true, message: '请输入管理员账号', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' },
  ],
})

async function handleLogin() {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      await userStore.login(loginForm.username, loginForm.password)
      ElMessage.success('登录成功')
      const redirect = (route.query.redirect as string) || '/dashboard'
      router.push(redirect)
    } catch {
      // 错误已在拦截器中处理
    } finally {
      loading.value = false
    }
  })
}
</script>

<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2 class="login-title">系统管理后台 - 登录</h2>
      </template>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        label-width="80px"
        @submit.prevent="handleLogin"
      >
        <el-form-item label="账号" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入管理员账号" prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" prefix-icon="Lock" @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-links">
        <a :href="userWebUrl" target="_blank">用户端</a>
        <span class="login-links-divider">|</span>
        <a :href="merchantWebUrl" target="_blank">商家端</a>
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--color-canvas-night);
}

.login-card {
  width: 420px;
  max-width: 90vw;

  .login-title {
    text-align: center;
    margin: 0;
    font-size: var(--font-size-heading-md);
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    color: var(--color-ink);
    font-weight: 600;
  }
}

.login-links {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
  font-size: 13px;
  color: var(--color-ink-subtle);

  a {
    color: var(--color-primary, #409eff);
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }

  .login-links-divider {
    color: var(--color-border, #dcdfe6);
  }
}
</style>
