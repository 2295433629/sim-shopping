<template>
  <div class="product-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Products</span>
          <el-button type="primary" :icon="Plus" @click="router.push('/products/new')">New Product</el-button>
        </div>
      </template>
      <div class="filter-bar">
        <el-select v-model="filter.status" placeholder="Status" clearable style="width:120px" @change="loadList">
          <el-option label="All" value="" />
          <el-option label="Draft" value="DRAFT" />
          <el-option label="Published" value="PUBLISHED" />
          <el-option label="Offline" value="OFFLINE" />
        </el-select>
        <el-input v-model="filter.keyword" placeholder="Search" clearable style="width:200px" @keyup.enter="loadList" />
        <el-button type="primary" @click="loadList">Search</el-button>
      </div>
      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="name" label="Name" min-width="200" />
        <el-table-column prop="price" label="Price" width="100">
          <template #default="{ row }">{{ formatPrice(row.price) }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="Stock" width="80" />
        <el-table-column prop="sales" label="Sales" width="80" />
        <el-table-column prop="status" label="Status" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'PUBLISHED' ? 'success' : row.status === 'DRAFT' ? 'info' : 'warning'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="280">
          <template #default="{ row }">
            <el-button size="small" @click="router.push('/products/' + row.productId + '/edit')">Edit</el-button>
            <el-button v-if="row.status === 'DRAFT'" size="small" type="success" @click="handlePublish(row)">Publish</el-button>
            <el-button v-if="row.status === 'PUBLISHED'" size="small" type="warning" @click="handleOffline(row)">Offline</el-button>
            <el-button v-if="row.status === 'DRAFT'" size="small" type="danger" @click="handleDelete(row)">Delete</el-button>
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

function formatPrice(val: number) { return val != null ? val.toFixed(2) : '0.00' }

const loadList = async () => {
  loading.value = true
  try {
    const res = await getProducts({ page: page.value, size: size.value, status: filter.status, keyword: filter.keyword })
    list.value = res.data.list || []
    total.value = res.data.total || 0
  } catch (e: any) { ElMessage.error('Load failed') }
  loading.value = false
}

const handlePublish = async (row: any) => {
  try { await publishProduct(row.productId); ElMessage.success('Published'); loadList() } catch { ElMessage.error('Failed') }
}

const handleOffline = async (row: any) => {
  try { await offlineProduct(row.productId); ElMessage.success('Offline'); loadList() } catch { ElMessage.error('Failed') }
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('Delete product "' + row.name + '"?', 'Confirm', { type: 'warning' })
    await deleteProduct(row.productId); ElMessage.success('Deleted'); loadList()
  } catch (e: any) { if (e !== 'cancel') ElMessage.error('Delete failed') }
}

onMounted(loadList)
</script>

<style scoped>
.product-list { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.filter-bar { display: flex; gap: 10px; margin-bottom: 16px; }
</style>