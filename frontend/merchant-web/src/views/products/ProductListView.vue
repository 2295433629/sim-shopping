<template>
  <div class="product-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商品列表</span>
          <el-button type="primary" :icon="Plus" @click="router.push('/products/new')">新建商品</el-button>
        </div>
      </template>
      <div class="filter-bar">
        <el-select v-model="filter.status" placeholder="状态" clearable style="width:120px" @change="loadList">
          <el-option label="全部" value="" />
          <el-option label="草稿" value="DRAFT" />
          <el-option label="已发布" value="PUBLISHED" />
          <el-option label="已下架" value="OFFLINE" />
        </el-select>
        <el-input v-model="filter.keyword" placeholder="搜索" clearable style="width:200px" @keyup.enter="loadList" />
        <el-button type="primary" @click="loadList">搜索</el-button>
      </div>
      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="name" label="名称" min-width="200" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">{{ formatPrice(row.price) }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="sales" label="销量" width="80" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'PUBLISHED' ? 'success' : row.status === 'DRAFT' ? 'info' : 'warning'">{{ statusMap[row.status] || row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button size="small" @click="router.push('/products/' + row.productId + '/edit')">编辑</el-button>
            <el-button v-if="row.status === 'DRAFT'" size="small" type="success" @click="handlePublish(row)">发布</el-button>
            <el-button v-if="row.status === 'PUBLISHED'" size="small" type="warning" @click="handleOffline(row)">下架</el-button>
            <el-button v-if="row.status === 'DRAFT'" size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" :page-size="size" :total="total" layout="prev, pager, next, total" @current-change="loadList" style="margin-top:16px;justify-content:flex-end" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProducts, publishProduct, offlineProduct, deleteProduct } from '@/api/modules/product'

const router = useRouter()
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const filter = reactive({ status: '', keyword: '' })

const statusMap: Record<string, string> = {
  DRAFT: '草稿',
  PUBLISHED: '已发布',
  OFFLINE: '已下架',
}

function formatPrice(val: number) { return val != null ? val.toFixed(2) : '0.00' }

const loadList = async () => {
  loading.value = true
  try {
    const res = await getProducts({ page: page.value, size: size.value, status: filter.status, keyword: filter.keyword })
    list.value = res.list || []
    total.value = res.total || 0
  } catch (e: any) { ElMessage.error(e?.message || '加载失败') }
  loading.value = false
}

const handlePublish = async (row: any) => {
  try { await publishProduct(row.productId); ElMessage.success('发布成功'); loadList() } catch { ElMessage.error('操作失败') }
}

const handleOffline = async (row: any) => {
  try { await offlineProduct(row.productId); ElMessage.success('已下架'); loadList() } catch { ElMessage.error('操作失败') }
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定删除商品「' + row.name + '」吗？', '确认', { type: 'warning' })
    await deleteProduct(row.productId); ElMessage.success('删除成功'); loadList()
  } catch (e: any) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(loadList)
</script>

<style scoped lang="scss">
.product-list {
  padding: var(--space-xl);

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    span {
      font-family: var(--font-display, 'Helvetica Neue', sans-serif);
      font-weight: 330;
      font-size: var(--font-size-heading-lg);
      color: var(--color-ink);
    }
  }

  .filter-bar {
    display: flex;
    gap: var(--space-md);
    margin-bottom: var(--space-lg);
  }

  .amount {
    color: var(--color-price);
    font-weight: 600;
  }

  .pagination-wrap {
    margin-top: var(--space-xl);
    display: flex;
    justify-content: flex-end;
  }
}
</style>