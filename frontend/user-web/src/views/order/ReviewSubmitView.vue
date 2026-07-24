<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { UploadFile, UploadRequestOptions } from 'element-plus'
import { submitReview } from '@/api/modules/review'
import { getOrderDetail } from '@/api/modules/order'
import request from '@/api/request'

const route = useRoute()
const router = useRouter()
const orderNo = route.params.orderNo as string
const productId = Number(route.query.productId || route.params.productId)
const loading = ref(false)
const submitting = ref(false)
const orderIdRef = ref<number>(0)

const rating = ref(0)
const content = ref('')
const imageUrls = ref<string[]>([])
// 记录上传中状态，防止重复提交
const uploadingCount = ref(0)

onMounted(async () => {
  loading.value = true
  try {
    const orderData = await getOrderDetail(orderNo)
    orderIdRef.value = orderData.orderId || 0
  } catch (e: unknown) {
    ElMessage.error(e instanceof Error ? e.message : '订单信息获取失败')
  } finally {
    loading.value = false
  }
})

async function handleSubmit() {
  if (rating.value === 0) {
    ElMessage.warning('请选择评分')
    return
  }
  if (!content.value.trim()) {
    ElMessage.warning('请输入评价内容')
    return
  }
  if (uploadingCount.value > 0) {
    ElMessage.warning('请等待图片上传完成')
    return
  }
  submitting.value = true
  try {
    await submitReview({
      productId,
      orderId: orderIdRef.value,
      orderNo,
      rating: rating.value,
      content: content.value.trim(),
      imageUrls: imageUrls.value,
    })
    ElMessage.success('评价提交成功')
    router.push('/orders')
  } catch (e: unknown) {
    ElMessage.error(e instanceof Error ? e.message : '评价提交失败')
  } finally {
    submitting.value = false
  }
}

function goBack() {
  router.back()
}

/** 上传响应结构 */
interface UploadResponse {
  url?: string
  data?: { url?: string }
}

/** 自定义上传方法：调用后端 /api/common/upload 接口 */
async function handleHttpUpload(options: UploadRequestOptions) {
  const file = options.file
  const formData = new FormData()
  formData.append('file', file)
  uploadingCount.value++
  try {
    const res = await request.post<unknown, UploadResponse | string>('/common/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    // 后端返回的 URL 格式可能是 res.url 或 res.data?.url 或完整路径
    const url = typeof res === 'string' ? res : (res?.url || res?.data?.url || '')
    if (!url) {
      ElMessage.error('图片上传失败：未获取到返回URL')
      return
    }
    imageUrls.value.push(url)
    ElMessage.success('图片上传成功')
  } catch (e: unknown) {
    ElMessage.error(e instanceof Error ? e.message : '图片上传失败')
  } finally {
    uploadingCount.value--
  }
}

function handleImageRemove(file: UploadFile, _fileList: UploadFile[]) {
  const response = file.response as UploadResponse | undefined
  const url = response?.url || file.url || ''
  if (url) {
    imageUrls.value = imageUrls.value.filter(u => u !== url)
  }
}
</script>

<template>
  <div class="review-submit-page" v-loading="loading">
    <div class="page-header">
      <el-button @click="goBack" :icon="ArrowLeft" text>返回</el-button>
      <h3>提交评价</h3>
    </div>

    <el-card shadow="never">
      <div class="review-form">
        <div class="form-section">
          <label class="form-label">商品评分</label>
          <el-rate v-model="rating" :texts="['很差', '较差', '一般', '满意', '非常满意']" show-text />
        </div>

        <div class="form-section">
          <label class="form-label">评价内容</label>
          <el-input
            v-model="content"
            type="textarea"
            :rows="5"
            placeholder="请输入您的评价内容..."
            maxlength="500"
            show-word-limit
          />
        </div>

        <div class="form-section">
          <label class="form-label">上传图片（可选）</label>
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="true"
            :http-request="handleHttpUpload"
            :limit="5"
            :on-remove="handleImageRemove"
            accept="image/*"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">最多上传5张图片，图片将自动上传至服务器</div>
        </div>

        <div class="form-actions">
          <el-button @click="goBack">取消</el-button>
          <el-button type="primary" :loading="submitting || uploadingCount > 0" @click="handleSubmit">提交评价</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.review-submit-page {
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  margin-bottom: var(--space-lg);

  h3 {
    margin: 0;
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
  }
}

.review-form {
  padding: var(--space-sm) 0;

  .form-section {
    margin-bottom: var(--space-xl);

    .form-label {
      display: block;
      font-size: var(--font-size-caption);
      font-weight: 500;
      color: var(--color-ink);
      margin-bottom: var(--space-sm);
    }
  }

  .upload-tip {
    font-size: var(--font-size-eyebrow);
    color: var(--color-shade-40);
    margin-top: var(--space-sm);
  }

  .form-actions {
    display: flex;
    justify-content: center;
    gap: var(--space-md);
    margin-top: var(--space-xxl);
  }
}
</style>
