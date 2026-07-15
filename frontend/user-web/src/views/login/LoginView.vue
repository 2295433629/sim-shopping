<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)
const isRegister = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  nickname: '',
})

const rules = computed<FormRules>(() => ({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' },
  ],
  nickname: isRegister.value
    ? [{ required: true, message: '请输入昵称', trigger: 'blur' }]
    : [],
}))

async function handleSubmit() {
  if (!loginFormRef.value) return
  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
  } catch {
    return
  }
  loading.value = true
  try {
    if (isRegister.value) {
      await userStore.register(loginForm.username, loginForm.password, loginForm.nickname)
      ElMessage.success('注册成功')
      const redirect = (route.query.redirect as string) || '/home'
      router.push(redirect)
    } else {
      await userStore.login(loginForm.username, loginForm.password)
      await userStore.fetchUserInfo()
      ElMessage.success('登录成功')
      const redirect = (route.query.redirect as string) || '/home'
      router.push(redirect)
    }
  } catch {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

function toggleMode() {
  isRegister.value = !isRegister.value
  loginFormRef.value?.resetFields()
}
</script>

<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2 class="login-title">{{ isRegister ? '用户注册' : '用户登录' }}</h2>
      </template>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        label-width="80px"
        @submit.prevent="handleSubmit"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item v-if="isRegister" label="昵称" prop="nickname">
          <el-input v-model="loginForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit" style="width: 100%">
            {{ isRegister ? '注册' : '登录' }}
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-link type="primary" @click="toggleMode">
            {{ isRegister ? '已有账号？去登录' : '没有账号？去注册' }}
          </el-link>
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
  background-color: var(--color-canvas-night);
}

.login-card {
  width: 420px;
  max-width: 90vw;

  .login-title {
    text-align: center;
    margin: 0;
    font-size: var(--font-size-heading-md);
    color: var(--color-ink);
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
  }
}
</style>
