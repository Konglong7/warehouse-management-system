<template>
  <div>
    <n-button type="primary" @click="handleAdd" style="margin-bottom: 16px">新增出库</n-button>
    <n-data-table :columns="columns" :data="dataList" :pagination="{ pageSize: 10 }" />

    <n-modal v-model:show="showModal" preset="dialog" title="出库信息">
      <n-form :model="formData" :rules="rules" label-placement="left" label-width="100px">
        <n-form-item label="物料" path="materialId">
          <n-select v-model:value="formData.materialId" :options="materialOptions" placeholder="请选择物料" />
        </n-form-item>
        <n-form-item label="仓位" path="locationId">
          <n-select v-model:value="formData.locationId" :options="locationOptions" placeholder="请选择仓位" />
        </n-form-item>
        <n-form-item label="数量" path="quantity">
          <n-input-number v-model:value="formData.quantity" style="width: 100%" :min="1" placeholder="请输入数量" />
        </n-form-item>
        <n-form-item label="操作人" path="operator">
          <n-input v-model:value="formData.operator" placeholder="请输入操作人" />
        </n-form-item>
        <n-form-item label="备注">
          <n-input v-model:value="formData.remark" type="textarea" placeholder="请输入备注（可选）" />
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
import { ref, onMounted } from 'vue'
import { NButton, NDataTable, NModal, NForm, NFormItem, NInput, NInputNumber, NSelect, NSpace, useMessage } from 'naive-ui'
import { getOutboundList, addOutbound } from '../api/outbound'
import { getMaterialList } from '../api/material'
import { getLocationList } from '../api/location'

const message = useMessage()
const showModal = ref(false)
const dataList = ref([])
const formData = ref({
  quantity: 1,
  operator: JSON.parse(localStorage.getItem('user') || '{}').username || ''
})

// 物料和仓位选项
const materialOptions = ref([])
const locationOptions = ref([])

// 表单验证规则
const rules = {
  materialId: { required: true, message: '请选择物料', trigger: 'blur' },
  quantity: { required: true, type: 'number', message: '请输入有效数量', trigger: 'blur' },
  operator: { required: true, message: '请输入操作人', trigger: 'blur' }
}

// 物料ID到名称的映射
const materialMap = ref({})
const locationMap = ref({})

const columns = [
  { title: '出库单号', key: 'outboundNo', width: 180 },
  {
    title: '物料',
    key: 'materialId',
    width: 120,
    render: (row) => materialMap.value[row.materialId] || row.materialId
  },
  {
    title: '仓位',
    key: 'locationId',
    width: 120,
    render: (row) => locationMap.value[row.locationId] || row.locationId
  },
  { title: '数量', key: 'quantity', width: 80 },
  { title: '操作人', key: 'operator', width: 100 },
  {
    title: '出库时间',
    key: 'outboundTime',
    width: 160,
    render: (row) => row.outboundTime ? new Date(row.outboundTime).toLocaleString() : '-'
  },
  { title: '备注', key: 'remark', ellipsis: { tooltip: true } }
]

const loadData = async () => {
  // 加载出库记录
  dataList.value = await getOutboundList()
  // 加载物料列表
  const materials = await getMaterialList()
  materialOptions.value = materials.map(m => ({ label: m.materialName, value: m.id }))
  materialMap.value = {}
  materials.forEach(m => { materialMap.value[m.id] = m.materialName })
  // 加载仓位列表
  const locations = await getLocationList()
  locationOptions.value = locations.map(l => ({ label: l.locationName, value: l.id }))
  locationMap.value = {}
  locations.forEach(l => { locationMap.value[l.id] = l.locationName })
}

const handleAdd = () => {
  formData.value = {
    quantity: 1,
    operator: JSON.parse(localStorage.getItem('user') || '{}').username || ''
  }
  showModal.value = true
}

const handleSave = async () => {
  // 表单验证
  if (!formData.value.materialId) {
    message.error('请选择物料')
    return
  }
  if (!formData.value.quantity || formData.value.quantity < 1) {
    message.error('请输入有效数量')
    return
  }
  if (!formData.value.operator) {
    message.error('请输入操作人')
    return
  }

  try {
    await addOutbound(formData.value)
    message.success('出库成功')
    showModal.value = false
    loadData()
  } catch (error) {
    message.error(error.message || '出库失败')
  }
}

onMounted(() => loadData())
</script>
