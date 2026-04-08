<template>
  <div>
    <n-button type="primary" @click="handleAdd" style="margin-bottom: 16px">新增仓位</n-button>
    <n-data-table :columns="columns" :data="dataList" :pagination="{ pageSize: 10 }" />

    <n-modal v-model:show="showModal" preset="dialog" title="仓位信息">
      <n-form :model="formData" :rules="rules" label-placement="left" label-width="100px">
        <n-form-item label="仓位编码" path="locationCode">
          <n-input v-model:value="formData.locationCode" placeholder="请输入仓位编码" />
        </n-form-item>
        <n-form-item label="仓位名称" path="locationName">
          <n-input v-model:value="formData.locationName" placeholder="请输入仓位名称" />
        </n-form-item>
        <n-form-item label="所属仓库" path="warehouse">
          <n-input v-model:value="formData.warehouse" placeholder="请输入所属仓库" />
        </n-form-item>
        <n-form-item label="容量" path="capacity">
          <n-input-number v-model:value="formData.capacity" style="width: 100%" :min="1" placeholder="请输入容量" />
        </n-form-item>
        <n-form-item label="状态" path="status">
          <n-select v-model:value="formData.status" :options="statusOptions" placeholder="请选择状态" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-space>
          <n-button @click="showModal = false">取消</n-button>
          <n-button type="primary" @click="handleSave">保存</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, h, onMounted } from 'vue'
import { NButton, NDataTable, NModal, NForm, NFormItem, NInput, NInputNumber, NSelect, NSpace, NTag, useMessage } from 'naive-ui'
import { getLocationList, addLocation, updateLocation, deleteLocation } from '../api/location'

const message = useMessage()
const showModal = ref(false)
const dataList = ref([])
const formData = ref({})

// 表单验证规则
const rules = {
  locationCode: { required: true, message: '请输入仓位编码', trigger: 'blur' },
  locationName: { required: true, message: '请输入仓位名称', trigger: 'blur' },
  warehouse: { required: true, message: '请输入所属仓库', trigger: 'blur' },
  capacity: { required: true, type: 'number', message: '请输入容量', trigger: 'blur' },
  status: { required: true, message: '请选择状态', trigger: 'blur' }
}

const statusOptions = [
  { label: '可用', value: 'available' },
  { label: '占用', value: 'occupied' },
  { label: '禁用', value: 'disabled' }
]

// 状态显示映射
const statusMap = {
  'available': { text: '可用', type: 'success' },
  'occupied': { text: '占用', type: 'warning' },
  'disabled': { text: '禁用', type: 'error' }
}

const columns = [
  { title: '仓位编码', key: 'locationCode', width: 120 },
  { title: '仓位名称', key: 'locationName', ellipsis: { tooltip: true } },
  { title: '所属仓库', key: 'warehouse', width: 120 },
  { title: '容量', key: 'capacity', width: 80 },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) => {
      const status = statusMap[row.status] || { text: row.status, type: 'default' }
      return h(NTag, { type: status.type, size: 'small' }, { default: () => status.text })
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    render: (row) => h(NSpace, null, {
      default: () => [
        h(NButton, { size: 'small', type: 'primary', secondary: true, onClick: () => { formData.value = { ...row }; showModal.value = true } }, { default: () => '编辑' }),
        h(NButton, { size: 'small', type: 'error', secondary: true, onClick: () => handleDelete(row.id) }, { default: () => '删除' })
      ]
    })
  }
]

const loadData = async () => {
  dataList.value = await getLocationList()
}

const handleAdd = () => {
  formData.value = { status: 'available', capacity: 100 }
  showModal.value = true
}

const handleSave = async () => {
  // 表单验证
  if (!formData.value.locationCode) {
    message.error('请输入仓位编码')
    return
  }
  if (!formData.value.locationName) {
    message.error('请输入仓位名称')
    return
  }
  if (!formData.value.warehouse) {
    message.error('请输入所属仓库')
    return
  }
  if (!formData.value.capacity || formData.value.capacity < 1) {
    message.error('请输入有效容量')
    return
  }
  if (!formData.value.status) {
    message.error('请选择状态')
    return
  }

  try {
    if (formData.value.id) {
      await updateLocation(formData.value.id, formData.value)
    } else {
      await addLocation(formData.value)
    }
    message.success('保存成功')
    showModal.value = false
    loadData()
  } catch (error) {
    message.error(error.message || '保存失败')
  }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除该仓位吗？')) {
    return
  }
  try {
    await deleteLocation(id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error(error.message || '删除失败')
  }
}

onMounted(() => loadData())
</script>
