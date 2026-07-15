<script setup lang="ts">
import { ref } from 'vue'

const loading = ref(false)
const menuList = ref<unknown[]>([])
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <div class="card-header">
        <span class="card-title">菜单管理</span>
        <el-button type="primary">新增菜单</el-button>
      </div>
    </template>
    <el-table :data="menuList" v-loading="loading" row-key="id" style="width: 100%">
      <el-table-column label="菜单名称" prop="menuName" min-width="200" />
      <el-table-column label="路径" prop="path" width="200" />
      <el-table-column label="图标" prop="icon" width="100" />
      <el-table-column label="排序" prop="sort" width="80" />
      <el-table-column label="操作" width="200" align="center">
        <template #default>
          <el-button link type="primary">编辑</el-button>
          <el-button link type="primary">新增子菜单</el-button>
          <el-button link type="danger">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && menuList.length === 0" description="暂无菜单" />
  </el-card>
</template>

<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .card-title {
    font-size: var(--font-size-body-md);
    font-weight: 600;
  }
}
</style>
