<template>
  <div class="product-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商品管理</span>
          <div>
            <el-select v-model="filter.status" placeholder="状态" clearable style="width:120px" @change="loadList">
              <el-option label="全部" value="" />
              <el-option label="已上架" value="PUBLISHED" />
              <el-option label="草稿" value="DRAFT" />
              <el-option label="已下架" value="OFFLINE" />
            </el-select>
            <el-input v-model="filter.keyword" placeholder="搜索商品" clearable style="width:200px" @keyup.enter="loadList" />
            <el-button type="primary" @click="loadList">搜索</el-button>
          </div>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="productId" label="ID" width="60" />
        <el-table-column prop="name" label="商品名称" min-width="200" />
        <el-table-column prop="shopName" label="所属店铺" width="120" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">{{ formatPrice(row.price) }}</template>
        </el-table-column>
        <el-table-column prop="sales" label="销量" width="80" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'PUBLISHED' ? 'success' : row.status === 'DRAFT' ? 'info' : 'warning'">
              {{ row.status === 'PUBLISHED' ? '已上架' : row.status === 'DRAFT' ? '草稿' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openDetail(row)">查看</el-button>
            <el-button v-if="row.status === 'PUBLISHED'" size="small" type="danger" @click="handleForceOffline(row)">强制下架</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" :page-size="size" :total="total" layout="prev, pager, next, total" @current-change="loadList" style="margin-top:16px;justify-content:flex-end" />
    </el-card>

    <!-- 商品详情弹窗 -->
    <el-dialog v-model="detailVisible" title="商品详情" width="700px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="商品ID">{{ detail.productId }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detail.status === 'PUBLISHED' ? 'success' : detail.status === 'DRAFT' ? 'info' : 'warning'">
            {{ detail.status === 'PUBLISHED' ? '已上架' : detail.status === 'DRAFT' ? '草稿' : '已下架' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="商品名称" :span="2">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="副标题" :span="2">{{ detail.subtitle || '-' }}</el-descriptions-item>
        <el-descriptions-item label="价格">{{ formatPrice(detail.price) }}</el-descriptions-item>
        <el-descriptions-item label="原价">{{ formatPrice(detail.originalPrice) }}</el-descriptions-item>
        <el-descriptions-item label="库存">{{ detail.stock }}</el-descriptions-item>
        <el-descriptions-item label="销量">{{ detail.sales }}</el-descriptions-item>
        <el-descriptions-item label="浏览量">{{ detail.viewCount }}</el-descriptions-item>
        <el-descriptions-item label="所属店铺">{{ detail.shopName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ detail.categoryName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="评分">{{ detail.reviewSummary?.avgRating }}</el-descriptions-item>
        <el-descriptions-item label="评价数">{{ detail.reviewSummary?.reviewCount }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProducts, getProductDetail, forceOfflineProduct } from '@/api/modules/product'
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const filter = reactive({ status: '', keyword: '' })

const detailVisible = ref(false)
const detail = ref<any>(null)

function formatPrice(val: number) { return val != null ? '¥' + Number(val).toFixed(2) : '¥0.00' }

const loadList = async () => {
  loading.value = true
  try {
    const res = await getProducts({ page: page.value, size: size.value, status: filter.status, keyword: filter.keyword })
    list.value = res.list || []
    total.value = res.total || 0
  } catch { ElMessage.error('加载失败') }
  loading.value = false
}

interface ProductRow {
  productId: number
  name: string
  shopName?: string
  price: number
  sales?: number
  status: string
  [key: string]: unknown
}

const openDetail = async (row: ProductRow) => {
  try {
    const res = await getProductDetail(row.productId)
    detail.value = res
    detailVisible.value = true
  } catch {
    ElMessage.error('获取详情失败')
  }
}

const handleForceOffline = async (row: ProductRow) => {
  try {
    await ElMessageBox.confirm('确认强制下架该商品？', '强制下架', { type: 'warning' })
    await forceOfflineProduct(row.productId)
    ElMessage.success('已下架')
    loadList()
  } catch (e: unknown) {
    if (!(typeof e === 'string' && e === 'cancel')) ElMessage.error('操作失败')
  }
}
onMounted(loadList)
</script>

<style scoped>
.product-manage { padding: var(--space-xl); }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
