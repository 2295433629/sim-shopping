<template>
  <div class="product-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Products</span>
          <div>
            <el-select v-model="filter.status" placeholder="Status" clearable style="width:120px" @change="loadList">
              <el-option label="All" value="" />
              <el-option label="Published" value="PUBLISHED" />
              <el-option label="Draft" value="DRAFT" />
              <el-option label="Offline" value="OFFLINE" />
            </el-select>
            <el-input v-model="filter.keyword" placeholder="Search" clearable style="width:200px" @keyup.enter="loadList" />
            <el-button type="primary" @click="loadList">Search</el-button>
          </div>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="productId" label="ID" width="60" />
        <el-table-column prop="name" label="Name" min-width="200" />
        <el-table-column prop="shopName" label="Shop" width="120" />
        <el-table-column prop="price" label="Price" width="100">
          <template #default="{ row }">{{ formatPrice(row.price) }}</template>
        </el-table-column>
        <el-table-column prop="sales" label="Sales" width="80" />
        <el-table-column prop="status" label="Status" width="90">
          <template #default="{ row }"><el-tag :type="row.status === 'PUBLISHED' ? 'success' : row.status === 'DRAFT' ? 'info' : 'warning'">{{ row.status }}</el-tag></template>
        </el-table-column>
        <el-table-column label="Actions" width="150">
          <template #default="{ row }">
            <el-button v-if="row.status === 'PUBLISHED'" size="small" type="danger" @click="handleForceOffline(row)">Force Offline</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" :page-size="size" :total="total" layout="prev, pager, next, total" @current-change="loadList" style="margin-top:16px;justify-content:flex-end" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProducts, forceOfflineProduct } from '@/api/modules/product'

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
    list.value = res.list || []
    total.value = res.total || 0
  } catch { ElMessage.error('Load failed') }
  loading.value = false
}

const handleForceOffline = async (row: any) => {
  try { const { value } = await ElMessageBox.prompt('Offline reason', 'Force Offline', { inputType: 'textarea' }); await forceOfflineProduct(row.productId, value); ElMessage.success('Offline'); loadList() } catch (e: any) { if (e !== 'cancel') ElMessage.error('Failed') }
}
onMounted(loadList)
</script>

<style scoped>
.product-manage { padding: var(--space-xl); }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>