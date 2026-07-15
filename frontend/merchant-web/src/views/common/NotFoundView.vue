<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面不存在</p>
    <el-button type="primary" @click="$router.push('/dashboard')">返回首页</el-button>
  </div>
</template>

<style scoped lang="scss">
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  text-align: center;

  h1 {
    font-family: var(--font-display, 'Helvetica Neue', sans-serif);
    font-weight: 330;
    font-size: 80px;
    color: var(--color-shade-30);
    margin: 0;
    line-height: 1;
  }

  p {
    font-size: var(--font-size-body-lg);
    color: var(--color-shade-50);
    margin: var(--space-lg) 0 var(--space-xl);
  }
}
</style>
