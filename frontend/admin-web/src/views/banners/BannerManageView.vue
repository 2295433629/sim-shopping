<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getAdminBanners, createBanner, updateBanner, deleteBanner, type BannerItem } from '@/api/modules/banner'

const loading = ref(false)
const bannerList = ref<BannerItem[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('创建Banner')
const formRef = ref<FormInstance>()
const submitLoading = ref(false)

const defaultForm = {
  title: '',
  imageUrl: '',
  linkUrl: '',
  sortOrder: 0,
  startTime: '',
  endTime: '',
  status: 'ACTIVE',
}

const form = ref({ ...defaultForm })
const editingId = ref<number | null>(null)

const rules: FormRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传图片', trigger: 'change' }],
  linkUrl: [{ required: true, message: '请输入链接地址', trigger: 'blur' }],
  sortOrder: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
}

const statusOptions = [
  { label: '启用', value: 'ACTIVE' },
  { label: '禁用', value: 'INACTIVE' },
]

onMounted(() => {
  loadBanners()
})

async function loadBanners() {
  loading.value = true
  try {
    const data = await getAdminBanners()
    if (Array.isArray(data)) {
      bannerList.value = data as BannerItem[]
    } else if (data && typeof data === 'object' && 'list' in data) {
      bannerList.value = ((data as Record<string, unknown>).list as BannerItem[]) || []
    } else {
      bannerList.value = []
    }
  } catch {
    bannerList.value = []
  } finally {
    loading.value = false
  }
}

function handleCreate() {
  dialogTitle.value = '创建Banner'
  editingId.value = null
  form.value = { ...defaultForm }
  dialogVisible.value = true
}

function handleEdit(row: BannerItem) {
  dialogTitle.value = '编辑Banner'
  editingId.value = row.id
  form.value = {
    title: row.title,
    imageUrl: row.imageUrl,
    linkUrl: row.linkUrl,
    sortOrder: row.sortOrder,
    startTime: row.startTime,
    endTime: row.endTime,
    status: row.status,
  }
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (editingId.value) {
        await updateBanner(editingId.value, form.value)
        ElMessage.success('更新成功')
      } else {
        await createBanner(form.value)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadBanners()
    } catch {
      // 错误已由拦截器处理
    } finally {
      submitLoading.value = false
    }
  })
}

async function handleDelete(row: BannerItem) {
  try {
    await ElMessageBox.confirm('确定要删除该Banner吗？删除后不可恢复。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteBanner(row.id)
    ElMessage.success('删除成功')
    loadBanners()
  } catch {
    // 用户取消
  }
}

function handleDialogClose() {
  formRef.value?.resetFields()
}
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <div class="card-header">
        <span class="card-title">Banner管理</span>
        <el-button type="primary" @click="handleCreate">创建Banner</el-button>
      </div>
    </template>

    <el-table :data="bannerList" v-loading="loading" style="width: 100%" stripe>
      <el-table-column label="ID" prop="id" width="70" align="center" />
      <el-table-column label="标题" prop="title" min-width="150" show-overflow-tooltip />
      <el-table-column label="图片" width="120" align="center">
        <template #default="{ row }">
          <el-image
            :src="row.imageUrl"
            :preview-src-list="[row.imageUrl]"
            fit="cover"
            style="width: 80px; height: 45px; border-radius: 4px"
            preview-teleported
          />
        </template>
      </el-table-column>
      <el-table-column label="链接地址" prop="linkUrl" min-width="150" show-overflow-tooltip />
      <el-table-column label="排序" prop="sortOrder" width="80" align="center" />
      <el-table-column label="开始时间" prop="startTime" width="180" />
      <el-table-column label="结束时间" prop="endTime" width="180" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'" size="small">
            {{ row.status === 'ACTIVE' ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!loading && bannerList.length === 0" description="暂无Banner" />
  </el-card>

  <!-- 创建/编辑对话框 -->
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="600px"
    destroy-on-close
    @close="handleDialogClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入Banner标题" />
      </el-form-item>
      <el-form-item label="图片地址" prop="imageUrl">
        <el-input v-model="form.imageUrl" placeholder="请输入图片URL地址" />
      </el-form-item>
      <el-form-item label="链接地址" prop="linkUrl">
        <el-input v-model="form.linkUrl" placeholder="请输入点击跳转链接" />
      </el-form-item>
      <el-form-item label="排序" prop="sortOrder">
        <el-input-number v-model="form.sortOrder" :min="0" controls-position="right" />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker
          v-model="form.startTime"
          type="datetime"
          placeholder="选择开始时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker
          v-model="form.endTime"
          type="datetime"
          placeholder="选择结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-size: var(--font-size-body-md);
  font-weight: 600;
}
</style>
