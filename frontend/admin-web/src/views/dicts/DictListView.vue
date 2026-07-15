<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  getDictTypeListApi,
  createDictTypeApi,
  getDictItemListApi,
  createDictItemApi,
  updateDictItemApi,
  deleteDictItemApi,
} from '@/api/modules/dict'
import type { DictType, DictItem } from '@/types/common'

// 字典类型
const typeLoading = ref(false)
const dictTypeList = ref<DictType[]>([])
const selectedTypeId = ref<number | null>(null)
const typeDialogVisible = ref(false)
const typeFormRef = ref<FormInstance>()
const typeForm = reactive({
  dictName: '',
  dictType: '',
  remark: '',
})
const typeRules = reactive<FormRules>({
  dictName: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
  dictType: [{ required: true, message: '请输入字典类型', trigger: 'blur' }],
})

// 字典项
const itemLoading = ref(false)
const dictItemList = ref<DictItem[]>([])
const itemDialogVisible = ref(false)
const itemDialogTitle = ref('')
const itemFormRef = ref<FormInstance>()
const editingItemId = ref<number | null>(null)
const itemForm = reactive({
  itemLabel: '',
  itemValue: '',
  sort: 0,
  status: 1,
  remark: '',
})
const itemRules = reactive<FormRules>({
  itemLabel: [{ required: true, message: '请输入字典项标签', trigger: 'blur' }],
  itemValue: [{ required: true, message: '请输入字典项值', trigger: 'blur' }],
})

async function fetchDictTypes() {
  typeLoading.value = true
  try {
    const res = await getDictTypeListApi()
    dictTypeList.value = res || []
    if (dictTypeList.value.length > 0 && !selectedTypeId.value) {
      selectedTypeId.value = dictTypeList.value[0].id
    }
  } catch {
    // 错误已在拦截器中处理
  } finally {
    typeLoading.value = false
  }
}

async function fetchDictItems() {
  if (!selectedTypeId.value) {
    dictItemList.value = []
    return
  }
  itemLoading.value = true
  try {
    const res = await getDictItemListApi(selectedTypeId.value)
    dictItemList.value = res || []
  } catch {
    // 错误已在拦截器中处理
  } finally {
    itemLoading.value = false
  }
}

function handleSelectType(type: DictType) {
  selectedTypeId.value = type.id
}

function handleAddType() {
  typeForm.dictName = ''
  typeForm.dictType = ''
  typeForm.remark = ''
  typeDialogVisible.value = true
}

async function handleSubmitType() {
  if (!typeFormRef.value) return
  await typeFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      await createDictTypeApi({
        dictName: typeForm.dictName,
        dictType: typeForm.dictType,
        remark: typeForm.remark,
      })
      ElMessage.success('创建成功')
      typeDialogVisible.value = false
      fetchDictTypes()
    } catch {
      // 错误已在拦截器中处理
    }
  })
}

function handleAddItem() {
  itemForm.itemLabel = ''
  itemForm.itemValue = ''
  itemForm.sort = 0
  itemForm.status = 1
  itemForm.remark = ''
  editingItemId.value = null
  itemDialogTitle.value = '新增字典项'
  itemDialogVisible.value = true
}

function handleEditItem(row: DictItem) {
  itemForm.itemLabel = row.itemLabel
  itemForm.itemValue = row.itemValue
  itemForm.sort = row.sort ?? 0
  itemForm.status = row.status ?? 1
  itemForm.remark = row.remark || ''
  editingItemId.value = row.id
  itemDialogTitle.value = '编辑字典项'
  itemDialogVisible.value = true
}

async function handleSubmitItem() {
  if (!itemFormRef.value || !selectedTypeId.value) return
  const typeId = selectedTypeId.value
  await itemFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const data = {
        itemLabel: itemForm.itemLabel,
        itemValue: itemForm.itemValue,
        sort: itemForm.sort,
        status: itemForm.status,
        remark: itemForm.remark,
      }
      if (editingItemId.value) {
        await updateDictItemApi(typeId, editingItemId.value, data)
        ElMessage.success('修改成功')
      } else {
        await createDictItemApi(typeId, data)
        ElMessage.success('新增成功')
      }
      itemDialogVisible.value = false
      fetchDictItems()
    } catch {
      // 错误已在拦截器中处理
    }
  })
}

async function handleDeleteItem(row: DictItem) {
  if (!selectedTypeId.value) return
  try {
    await ElMessageBox.confirm('确定要删除该字典项吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteDictItemApi(selectedTypeId.value, row.id)
    ElMessage.success('删除成功')
    fetchDictItems()
  } catch {
    // 用户取消或错误
  }
}

watch(selectedTypeId, () => {
  fetchDictItems()
})

onMounted(() => {
  fetchDictTypes()
})
</script>

<template>
  <div class="dict-container">
    <el-row :gutter="16">
      <!-- 左侧：字典类型列表 -->
      <el-col :span="8">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">字典类型</span>
              <el-button type="primary" size="small" @click="handleAddType">新增</el-button>
            </div>
          </template>
          <el-table
            :data="dictTypeList"
            v-loading="typeLoading"
            highlight-current-row
            @row-click="handleSelectType"
            style="width: 100%"
          >
            <el-table-column label="名称" prop="dictName" />
            <el-table-column label="类型" prop="dictType" width="120" />
          </el-table>
        </el-card>
      </el-col>

      <!-- 右侧：字典项列表 -->
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                字典项
                <span v-if="selectedTypeId" class="selected-type">（当前类型 ID: {{ selectedTypeId }}）</span>
              </span>
              <el-button type="primary" size="small" :disabled="!selectedTypeId" @click="handleAddItem">新增</el-button>
            </div>
          </template>
          <el-table :data="dictItemList" v-loading="itemLoading" style="width: 100%">
            <el-table-column label="标签" prop="itemLabel" width="150" />
            <el-table-column label="值" prop="itemValue" width="150" />
            <el-table-column label="排序" prop="sort" width="80" />
            <el-table-column label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                  {{ row.status === 1 ? '正常' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="备注" prop="remark" min-width="120" />
            <el-table-column label="操作" width="150" align="center">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleEditItem(row)">编辑</el-button>
                <el-button link type="danger" @click="handleDeleteItem(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!itemLoading && dictItemList.length === 0" description="请选择字典类型或暂无字典项" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 字典类型新增弹窗 -->
    <el-dialog v-model="typeDialogVisible" title="新增字典类型" width="440px">
      <el-form ref="typeFormRef" :model="typeForm" :rules="typeRules" label-width="100px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="typeForm.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="typeForm.dictType" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="typeForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitType">确定</el-button>
      </template>
    </el-dialog>

    <!-- 字典项新增/编辑弹窗 -->
    <el-dialog v-model="itemDialogVisible" :title="itemDialogTitle" width="440px">
      <el-form ref="itemFormRef" :model="itemForm" :rules="itemRules" label-width="100px">
        <el-form-item label="标签" prop="itemLabel">
          <el-input v-model="itemForm.itemLabel" placeholder="请输入字典项标签" />
        </el-form-item>
        <el-form-item label="值" prop="itemValue">
          <el-input v-model="itemForm.itemValue" placeholder="请输入字典项值" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="itemForm.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="itemForm.status">
            <el-radio :value="1">正常</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="itemForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="itemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitItem">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.dict-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .card-title {
      font-size: var(--font-size-body-md);
      font-weight: 600;

      .selected-type {
        font-size: var(--font-size-micro);
        font-weight: 400;
        color: var(--color-shade-40);
      }
    }
  }
}
</style>
