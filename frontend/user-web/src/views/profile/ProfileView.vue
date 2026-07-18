<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import {
  ShoppingBag,
  Location,
  Star,
  Ticket,
  Coin,
  ChatDotSquare,
  Clock,
  Setting,
} from '@element-plus/icons-vue'

const userStore = useUserStore()
const router = useRouter()

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

/** 快捷功能入口配置 */
const shortcuts = [
  { icon: ShoppingBag, label: '我的订单', path: '/orders' },
  { icon: Location, label: '收货地址', path: '/addresses' },
  { icon: Star, label: '我的收藏', path: '/favorites' },
  { icon: Ticket, label: '我的优惠券', path: '/coupons' },
  { icon: Coin, label: '积分商城', path: '/points/mall' },
  { icon: ChatDotSquare, label: '评价记录', path: '/reviews' },
  { icon: Clock, label: '浏览历史', path: '/history' },
  { icon: Setting, label: '设置', path: '/profile' },
]

function navigateTo(path: string) {
  router.push(path)
}

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

    <!-- 快捷功能入口 -->
    <el-card shadow="never" style="margin-top: 16px">
      <template #header>
        <span class="card-title">快捷功能</span>
      </template>
      <el-row :gutter="16">
        <el-col
          v-for="item in shortcuts"
          :key="item.path"
          :xs="12"
          :sm="12"
          :md="6"
          :lg="6"
        >
          <div class="shortcut-item" @click="navigateTo(item.path)">
            <el-card shadow="hover" class="shortcut-card" :body-style="{ padding: '16px', textAlign: 'center' }">
              <el-icon :size="28" class="shortcut-icon"><component :is="item.icon" /></el-icon>
              <div class="shortcut-label">{{ item.label }}</div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 编辑信息弹窗 -->
    <el-dialog v-model="profileDialogVisible" title="编辑个人信息" width="480px">
      <el-form ref="profileFormRef" :model="profileForm" :rules="profileRules" label-width="80px">
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-input v-model="profileForm.avatar" placeholder="请输入头像链接地址" />
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
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .profile-card {
    display: flex;
    align-items: center;
    gap: var(--space-xxl);
    padding: var(--space-xl) 0;

    .profile-info {
      flex: 1;

      h3 {
        margin: 0 0 var(--space-md) 0;
        font-size: var(--font-size-heading-md);
        color: var(--color-ink);
        font-family: var(--font-display, 'Helvetica Neue', sans-serif);
        font-weight: 330;
      }

      .info-item {
        margin: 6px 0;
        color: var(--color-shade-50);
        font-size: var(--font-size-caption);
        display: flex;
        align-items: center;
        gap: var(--space-xs);
      }
    }

    .profile-actions {
      display: flex;
      flex-direction: column;
      gap: var(--space-md);
    }
  }

  .shortcut-item {
    cursor: pointer;
    margin-bottom: 16px;

    .shortcut-card {
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
      }

      .shortcut-icon {
        color: var(--el-color-primary);
        margin-bottom: 8px;
      }

      .shortcut-label {
        font-size: 13px;
        color: var(--color-shade-50);
        margin-top: 4px;
      }
    }
  }
}
</style>
