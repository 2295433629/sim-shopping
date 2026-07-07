<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  getAddressListApi,
  addAddressApi,
  updateAddressApi,
  deleteAddressApi,
  setDefaultAddressApi,
} from '@/api/modules/address'
import type { AddressInfo } from '@/types/common'

const loading = ref(false)
const addressList = ref<AddressInfo[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const editingId = ref<number | null>(null)

const addressForm = reactive({
  receiverName: '',
  receiverPhone: '',
  province: '',
  city: '',
  district: '',
  detailAddress: '',
  isDefault: 0,
})

const rules = reactive<FormRules>({
  receiverName: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
  receiverPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' },
  ],
  province: [{ required: true, message: '请输入省份', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  district: [{ required: true, message: '请输入区/县', trigger: 'blur' }],
  detailAddress: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
})

async function fetchAddresses() {
  loading.value = true
  try {
    const res = await getAddressListApi()
    addressList.value = res.list || []
  } catch {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

function resetForm() {
  addressForm.receiverName = ''
  addressForm.receiverPhone = ''
  addressForm.province = ''
  addressForm.city = ''
  addressForm.district = ''
  addressForm.detailAddress = ''
  addressForm.isDefault = 0
  editingId.value = null
}

function handleAdd() {
  resetForm()
  dialogTitle.value = '新增地址'
  dialogVisible.value = true
}

function handleEdit(row: AddressInfo) {
  resetForm()
  dialogTitle.value = '编辑地址'
  editingId.value = row.id
  addressForm.receiverName = row.receiverName
  addressForm.receiverPhone = row.receiverPhone
  addressForm.province = row.province
  addressForm.city = row.city
  addressForm.district = row.district
  addressForm.detailAddress = row.detailAddress
  addressForm.isDefault = row.isDefault
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const data = { ...addressForm }
      if (editingId.value) {
        await updateAddressApi(editingId.value, data)
        ElMessage.success('修改成功')
      } else {
        await addAddressApi(data)
        ElMessage.success('添加成功')
      }
      dialogVisible.value = false
      fetchAddresses()
    } catch {
      // 错误已在拦截器中处理
    }
  })
}

async function handleDelete(row: AddressInfo) {
  try {
    await ElMessageBox.confirm('确定要删除该地址吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteAddressApi(row.id)
    ElMessage.success('删除成功')
    fetchAddresses()
  } catch {
    // 用户取消或错误
  }
}

async function handleSetDefault(row: AddressInfo) {
  try {
    await setDefaultAddressApi(row.id)
    ElMessage.success('已设为默认地址')
    fetchAddresses()
  } catch {
    // 错误已在拦截器中处理
  }
}

onMounted(() => {
  fetchAddresses()
})
</script>

<template>
  <div class="address-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">收货地址管理</span>
          <el-button type="primary" @click="handleAdd">新增地址</el-button>
        </div>
      </template>

      <el-table :data="addressList" v-loading="loading" style="width: 100%">
        <el-table-column label="收货人" prop="receiverName" width="100" />
        <el-table-column label="手机号" prop="receiverPhone" width="130" />
        <el-table-column label="地址">
          <template #default="{ row }">
            {{ row.province }} {{ row.city }} {{ row.district }} {{ row.detailAddress }}
          </template>
        </el-table-column>
        <el-table-column label="默认" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.isDefault === 1" type="success">默认</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleSetDefault(row)" v-if="row.isDefault !== 1">设默认</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="addressForm" :rules="rules" label-width="100px">
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="receiverPhone">
          <el-input v-model="addressForm.receiverPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="addressForm.province" placeholder="请输入省份" />
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="addressForm.city" placeholder="请输入城市" />
        </el-form-item>
        <el-form-item label="区/县" prop="district">
          <el-input v-model="addressForm.district" placeholder="请输入区/县" />
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress">
          <el-input v-model="addressForm.detailAddress" type="textarea" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="设为默认" prop="isDefault">
          <el-switch v-model="addressForm.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.address-container {
  max-width: 900px;
  margin: 0 auto;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .card-title {
      font-size: 16px;
      font-weight: bold;
    }
  }
}
</style>
