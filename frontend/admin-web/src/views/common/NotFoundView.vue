<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面不存在</p>
    <el-button type="primary" @click="$router.push('/dashboard')">返回首页</el-button>
  </div>
</template>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  text-align: center;
}
.not-found h1 {
  font-size: 80px;
  color: #ccc;
  margin: 0;
}
.not-found p {
  font-size: 18px;
  color: #666;
  margin: 16px 0 24px;
}
</style>
