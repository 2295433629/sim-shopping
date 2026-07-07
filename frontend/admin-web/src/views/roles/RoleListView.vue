<script setup lang="ts">
import { ref } from 'vue'

const loading = ref(false)
const roleList = ref<unknown[]>([])
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <div class="card-header">
        <span class="card-title">角色管理</span>
        <el-button type="primary">新增角色</el-button>
      </div>
    </template>
    <el-table :data="roleList" v-loading="loading" style="width: 100%">
      <el-table-column label="ID" prop="roleId" width="80" />
      <el-table-column label="角色名称" prop="roleName" width="200" />
      <el-table-column label="角色编码" prop="roleCode" width="200" />
      <el-table-column label="描述" prop="remark" min-width="200" />
      <el-table-column label="操作" width="250" align="center">
        <template #default>
          <el-button link type="primary">编辑</el-button>
          <el-button link type="primary">分配权限</el-button>
          <el-button link type="danger">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && roleList.length === 0" description="暂无角色" />
  </el-card>
</template>

<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .card-title {
    font-size: 16px;
    font-weight: bold;
  }
}
</style>
