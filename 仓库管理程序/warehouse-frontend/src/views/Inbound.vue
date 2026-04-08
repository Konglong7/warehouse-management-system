<template>
  <div>
    <n-button type="primary" @click="showModal = true" style="margin-bottom: 16px">新增入库</n-button>
    <n-data-table :columns="columns" :data="dataList" :pagination="{ pageSize: 10 }" />

    <n-modal v-model:show="showModal" preset="dialog" title="入库信息">
      <n-form :model="formData">
        <n-form-item label="物料">
          <n-select v-model:value="formData.materialId" :options="materialOptions" />
        </n-form-item>
        <n-form-item label="仓位">
          <n-select v-model:value="formData.locationId" :options="locationOptions" />
        </n-form-item>
        <n-form-item label="数量">
          <n-input-number v-model:value="formData.quantity" style="width: 100%" />
        </n-form-item>
        <n-form-item label="操作人">
          <n-input v-model:value="formData.operator" />
        </n-form-item>
        <n-form-item label="备注">
          <n-input v-model:value="formData.remark" type="textarea" />
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
import { getInboundList, addInbound } from '../api/inbound'
import { getMaterialList } from '../api/material'
import { getLocationList } from '../api/location'

const message = useMessage()
const showModal = ref(false)
const dataList = ref([])
const formData = ref({ quantity: 1, operator: JSON.parse(localStorage.getItem('user') || '{}').username })
const materialOptions = ref([])
const locationOptions = ref([])

const columns = [
  { title: '入库单号', key: 'inboundNo' },
  { title: '物料ID', key: 'materialId' },
  { title: '仓位ID', key: 'locationId' },
  { title: '数量', key: 'quantity' },
  { title: '操作人', key: 'operator' },
  { title: '入库时间', key: 'inboundTime' }
]

const loadData = async () => {
  dataList.value = await getInboundList()
  const materials = await getMaterialList()
  materialOptions.value = materials.map(m => ({ label: m.materialName, value: m.id }))
  const locations = await getLocationList()
  locationOptions.value = locations.map(l => ({ label: l.locationName, value: l.id }))
}

const handleSave = async () => {
  try {
    await addInbound(formData.value)
    message.success('入库成功')
    showModal.value = false
    loadData()
  } catch (error) {
    message.error('入库失败')
  }
}

onMounted(() => loadData())
</script>
