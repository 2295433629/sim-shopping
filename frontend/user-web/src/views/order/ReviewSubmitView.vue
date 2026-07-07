<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { UploadFile } from 'element-plus'
import { submitReview } from '@/api/modules/review'
import { getOrderDetail } from '@/api/modules/order'

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

onMounted(async () => {
  loading.value = true
  try {
    const orderData: any = await getOrderDetail(orderNo)
    orderIdRef.value = orderData.orderId || orderData.id || 0
  } catch {
    ElMessage.error('订单信息获取失败')
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
  } catch (e: any) {
    ElMessage.error(e.message || '评价提交失败')
  } finally {
    submitting.value = false
  }
}

function goBack() {
  router.back()
}

function handleImageUpload(file: UploadFile) {
  if (file.url) {
    imageUrls.value.push(file.url)
  }
}

function handleImageRemove(_file: UploadFile, _fileList: UploadFile[]) {
  imageUrls.value = imageUrls.value.slice(0, -1)
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
            :auto-upload="false"
            :limit="5"
            :on-change="handleImageUpload"
            :on-remove="handleImageRemove"
            accept="image/*"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">最多上传5张图片</div>
        </div>

        <div class="form-actions">
          <el-button @click="goBack">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">提交评价</el-button>
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
  gap: 12px;
  margin-bottom: 16px;

  h3 {
    margin: 0;
  }
}

.review-form {
  padding: 8px 0;

  .form-section {
    margin-bottom: 24px;

    .form-label {
      display: block;
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 8px;
    }
  }

  .upload-tip {
    font-size: 12px;
    color: #909399;
    margin-top: 8px;
  }

  .form-actions {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin-top: 32px;
  }
}
</style>
