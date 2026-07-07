<script setup lang="ts">
import { ref } from 'vue'

const loading = ref(false)
const userList = ref<unknown[]>([])
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <span class="card-title">用户管理</span>
    </template>
    <el-table :data="userList" v-loading="loading" style="width: 100%">
      <el-table-column label="ID" prop="userId" width="80" />
      <el-table-column label="用户名" prop="username" width="150" />
      <el-table-column label="昵称" prop="nickname" width="150" />
      <el-table-column label="手机号" prop="phone" width="130" />
      <el-table-column label="角色" prop="role" width="100" />
      <el-table-column label="状态" prop="status" width="100" />
      <el-table-column label="注册时间" prop="createdAt" width="180" />
      <el-table-column label="操作" width="150" align="center">
        <template #default>
          <el-button link type="warning">禁用</el-button>
          <el-button link type="success">启用</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && userList.length === 0" description="暂无用户" />
  </el-card>
</template>

<style scoped lang="scss">
.card-title {
  font-size: 16px;
  font-weight: bold;
}
</style>
