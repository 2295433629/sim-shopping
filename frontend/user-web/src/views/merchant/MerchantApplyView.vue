<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { applyMerchant, getMerchantInfo } from '@/api/modules/merchant'
import type { MerchantInfo } from '@/api/modules/merchant'
import { Check, Clock } from '@element-plus/icons-vue'

const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)
const submitting = ref(false)
const merchantInfo = ref<MerchantInfo | null>(null)

const form = reactive({
  merchantName: '',
  contactPhone: '',
  contactEmail: '',
  businessLicense: '',
})

const rules = reactive<FormRules>({
  merchantName: [{ required: true, message: '请输入商家名称', trigger: 'blur' }],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' },
  ],
  contactEmail: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
  ],
})

/** 当前步骤：0=填写信息, 1=提交审核, 2=开通店铺 */
const currentStep = computed(() => {
  if (!merchantInfo.value) return 0
  if (merchantInfo.value.status === 'PENDING') return 1
  if (merchantInfo.value.status === 'ACTIVE') return 2
  return 0
})

async function fetchMerchantInfo() {
  loading.value = true
  try {
    const res = await getMerchantInfo()
    merchantInfo.value = res as unknown as MerchantInfo
  } catch {
    // 接口返回 404 表示尚未申请过
    merchantInfo.value = null
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    const valid = await formRef.value.validate()
    if (!valid) return
  } catch {
    return
  }
  submitting.value = true
  try {
    await applyMerchant({
      merchantName: form.merchantName,
      contactPhone: form.contactPhone,
      contactEmail: form.contactEmail || undefined,
      businessLicense: form.businessLicense || undefined,
    })
    ElMessage.success('申请提交成功，请等待管理员审核')
    await fetchMerchantInfo()
  } catch {
    // 错误已在拦截器中处理
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchMerchantInfo()
})
</script>

<template>
  <div class="merchant-apply-container">
    <h2 class="page-title">申请成为商家</h2>

    <!-- 入驻流程步骤条 -->
    <el-steps :active="currentStep" finish-status="success" align-center class="steps-bar">
      <el-step title="填写信息" description="完善商家入驻资料" />
      <el-step title="提交审核" description="等待管理员审核" />
      <el-step title="开通店铺" description="审核通过即可开店" />
    </el-steps>

    <div v-loading="loading" class="content-area">
      <!-- 尚未申请 / 被拒绝后重新申请 -->
      <template v-if="!merchantInfo || merchantInfo.status === 'REJECTED'">
        <!-- 被拒绝提示 -->
        <el-alert
          v-if="merchantInfo?.status === 'REJECTED'"
          :title="'申请已被拒绝：' + (merchantInfo.rejectReason || '未提供原因')"
          type="error"
          show-icon
          :closable="false"
          style="margin-bottom: 20px"
        />

        <el-card shadow="never">
          <template #header>
            <span class="card-title">商家入驻申请</span>
          </template>
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="100px"
            style="max-width: 560px"
          >
            <el-form-item label="商家名称" prop="merchantName">
              <el-input v-model="form.merchantName" placeholder="请输入商家名称" maxlength="50" show-word-limit />
            </el-form-item>
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" maxlength="11" />
            </el-form-item>
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="form.contactEmail" placeholder="选填，方便接收审核通知" />
            </el-form-item>
            <el-form-item label="营业执照号" prop="businessLicense">
              <el-input v-model="form.businessLicense" placeholder="个体工商户可暂不填写" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="submitting" @click="handleSubmit">
                提交申请
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </template>

      <!-- 审核中 -->
      <el-card v-else-if="merchantInfo.status === 'PENDING'" shadow="never" class="status-card">
        <div class="status-content">
          <el-icon :size="48" class="status-icon pending"><Clock /></el-icon>
          <h3>审核中</h3>
          <p>您的入驻申请正在审核中，请耐心等待管理员审核。</p>
          <p class="status-sub">
            商家名称：{{ merchantInfo.merchantName }}<br />
            联系电话：{{ merchantInfo.contactPhone }}
          </p>
        </div>
      </el-card>

      <!-- 已通过 -->
      <el-card v-else-if="merchantInfo.status === 'ACTIVE'" shadow="never" class="status-card">
        <div class="status-content">
          <el-icon :size="48" class="status-icon success"><Check /></el-icon>
          <h3>您已是入驻商家</h3>
          <p>恭喜！您的入驻申请已通过审核。</p>
          <p v-if="merchantInfo.shopName" class="status-sub">
            店铺名称：{{ merchantInfo.shopName }}
          </p>
          <el-button type="primary" style="margin-top: 16px" @click="router.push('/')">
            前往商家端
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped lang="scss">
.merchant-apply-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px 16px;

  .page-title {
    text-align: center;
    font-size: var(--font-size-heading-md);
    color: var(--color-ink);
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
    margin-bottom: 32px;
  }

  .steps-bar {
    margin-bottom: 32px;
  }

  .content-area {
    min-height: 200px;
  }

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 500;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
  }

  .status-card {
    .status-content {
      text-align: center;
      padding: 32px 0;

      h3 {
        margin: 16px 0 8px;
        font-size: var(--font-size-heading-sm);
        color: var(--color-ink);
        font-family: var(--font-display, 'Helvetica Neue', sans-serif);
        font-weight: 500;
      }

      p {
        color: var(--color-shade-50);
        font-size: var(--font-size-body-sm);
      }

      .status-sub {
        margin-top: 12px;
        font-size: var(--font-size-caption);
        color: var(--color-shade-70);
      }
    }

    .status-icon {
      &.pending {
        color: var(--el-color-warning);
      }

      &.success {
        color: var(--el-color-success);
      }
    }
  }
}
</style>
