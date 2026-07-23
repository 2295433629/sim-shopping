<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="forbidden-page">
    <div class="content">
      <div class="error-code">403</div>
      <p class="error-text">抱歉，您没有权限访问该页面</p>
      <el-button type="primary" @click="router.push('/dashboard')">返回首页</el-button>
      <el-button @click="router.back()">返回上一页</el-button>
    </div>
  </div>
</template>

<style scoped lang="scss">
.forbidden-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-canvas, #f5f5f5);

  .content {
    text-align: center;

    .error-code {
      font-size: 120px;
      font-weight: 700;
      color: var(--color-shade-20, #e6a23c);
      line-height: 1;
    }

    .error-text {
      font-size: 18px;
      color: var(--color-shade-50, #999);
      margin: 20px 0 30px;
    }
  }
}
</style>
