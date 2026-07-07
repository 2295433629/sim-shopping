<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'

const userStore = useUserStore()

const profileFormRef = ref<FormInstance>()
const passwordFormRef = ref<FormInstance>()
const profileDialogVisible = ref(false)
const passwordDialogVisible = ref(false)
const profileLoading = ref(false)
const passwordLoading = ref(false)

const profileForm = reactive({
  nickname: userStore.userInfo?.nickname || '',
  avatar: userStore.userInfo?.avatar || '',
  gender: userStore.userInfo?.gender ?? 0,
  phone: userStore.userInfo?.phone || userStore.userInfo?.mobile || '',
  email: userStore.userInfo?.email || '',
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const profileRules = reactive<FormRules>({
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
})

const passwordRules = reactive<FormRules>({
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
})

function openProfileDialog() {
  profileForm.nickname = userStore.userInfo?.nickname || ''
  profileForm.avatar = userStore.userInfo?.avatar || ''
  profileForm.gender = userStore.userInfo?.gender ?? 0
  profileForm.phone = userStore.userInfo?.phone || userStore.userInfo?.mobile || ''
  profileForm.email = userStore.userInfo?.email || ''
  profileDialogVisible.value = true
}

async function handleUpdateProfile() {
  if (!profileFormRef.value) return
  await profileFormRef.value.validate(async (valid) => {
    if (!valid) return
    profileLoading.value = true
    try {
      await userStore.updateProfile({
        nickname: profileForm.nickname,
        avatar: profileForm.avatar,
        gender: profileForm.gender,
        phone: profileForm.phone,
        email: profileForm.email,
      })
      ElMessage.success('更新成功')
      profileDialogVisible.value = false
    } catch {
      // 错误已在拦截器中处理
    } finally {
      profileLoading.value = false
    }
  })
}

async function handleChangePassword() {
  if (!passwordFormRef.value) return
  await passwordFormRef.value.validate(async (valid) => {
    if (!valid) return
    passwordLoading.value = true
    try {
      const { changePasswordApi } = await import('@/api/modules/auth')
      await changePasswordApi(passwordForm.oldPassword, passwordForm.newPassword)
      ElMessage.success('密码修改成功')
      passwordDialogVisible.value = false
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    } catch {
      // 错误已在拦截器中处理
    } finally {
      passwordLoading.value = false
    }
  })
}
</script>

<template>
  <div class="profile-container">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">个人信息</span>
      </template>

      <!-- 个人信息卡片 -->
      <div class="profile-card">
        <el-avatar :size="80" :src="userStore.userInfo?.avatar">
          {{ (userStore.userInfo?.nickname || userStore.userInfo?.username || 'U')[0] }}
        </el-avatar>
        <div class="profile-info">
          <h3>{{ userStore.userInfo?.nickname || '未设置昵称' }}</h3>
          <p class="info-item"><el-icon><User /></el-icon> 用户名：{{ userStore.userInfo?.username }}</p>
          <p class="info-item"><el-icon><Phone /></el-icon> 手机：{{ userStore.userInfo?.phone || userStore.userInfo?.mobile || '未绑定' }}</p>
          <p class="info-item"><el-icon><Message /></el-icon> 邮箱：{{ userStore.userInfo?.email || '未绑定' }}</p>
          <p class="info-item"><el-icon><Medal /></el-icon> 积分：{{ userStore.userInfo?.points ?? 0 }}</p>
        </div>
        <div class="profile-actions">
          <el-button type="primary" @click="openProfileDialog">编辑信息</el-button>
          <el-button @click="passwordDialogVisible = true">修改密码</el-button>
        </div>
      </div>
    </el-card>

    <!-- 编辑信息弹窗 -->
    <el-dialog v-model="profileDialogVisible" title="编辑个人信息" width="480px">
      <el-form ref="profileFormRef" :model="profileForm" :rules="profileRules" label-width="80px">
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-input v-model="profileForm.avatar" placeholder="请输入头像URL" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="profileForm.gender">
            <el-radio :value="0">保密</el-radio>
            <el-radio :value="1">男</el-radio>
            <el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="profileDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="profileLoading" @click="handleUpdateProfile">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="passwordDialogVisible" title="修改密码" width="440px">
      <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="100px">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入旧密码" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="passwordLoading" @click="handleChangePassword">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.profile-container {
  max-width: 800px;
  margin: 0 auto;

  .card-title {
    font-size: 16px;
    font-weight: bold;
  }

  .profile-card {
    display: flex;
    align-items: center;
    gap: 32px;
    padding: 20px 0;

    .profile-info {
      flex: 1;

      h3 {
        margin: 0 0 12px 0;
        font-size: 20px;
        color: #333;
      }

      .info-item {
        margin: 6px 0;
        color: #666;
        font-size: 14px;
        display: flex;
        align-items: center;
        gap: 6px;
      }
    }

    .profile-actions {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }
  }
}
</style>
