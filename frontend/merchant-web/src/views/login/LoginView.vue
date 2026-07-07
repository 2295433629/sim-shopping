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

const loginForm = reactive({
  username: '',
  password: '',
})

const rules = reactive<FormRules>({
  username: [{ required: true, message: '请输入商家账号', trigger: 'blur' }],
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
      await userStore.fetchUserInfo()
      // 检查是否为商家账号
      const role = userStore.userInfo?.role
      if (role && role !== 'MERCHANT' && role !== 'ADMIN') {
        ElMessage.error('非商家账号，无权登录')
        await userStore.logout()
        return
      }
      ElMessage.success('登录成功')
      const redirect = (route.query.redirect as string) || '/home'
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
        <h2 class="login-title">商家管理平台 - 登录</h2>
      </template>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        label-width="80px"
        @submit.prevent="handleLogin"
      >
        <el-form-item label="账号" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入商家账号" prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" prefix-icon="Lock" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a237e 0%, #283593 100%);
}

.login-card {
  width: 420px;
  max-width: 90vw;

  .login-title {
    text-align: center;
    margin: 0;
    font-size: 20px;
    color: #333;
  }
}
</style>
