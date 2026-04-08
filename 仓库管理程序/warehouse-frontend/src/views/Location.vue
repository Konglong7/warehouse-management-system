<template>
  <div>
    <n-button type="primary" @click="showModal = true" style="margin-bottom: 16px">新增仓位</n-button>
    <n-data-table :columns="columns" :data="dataList" :pagination="{ pageSize: 10 }" />

    <n-modal v-model:show="showModal" preset="dialog" title="仓位信息">
      <n-form :model="formData">
        <n-form-item label="仓位编码">
          <n-input v-model:value="formData.locationCode" />
        </n-form-item>
        <n-form-item label="仓位名称">
          <n-input v-model:value="formData.locationName" />
        </n-form-item>
        <n-form-item label="所属仓库">
          <n-input v-model:value="formData.warehouse" />
        </n-form-item>
        <n-form-item label="容量">
          <n-input-number v-model:value="formData.capacity" style="width: 100%" />
        </n-form-item>
        <n-form-item label="状态">
          <n-select v-model:value="formData.status" :options="statusOptions" />
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
import { NButton, NDataTable, NModal, NForm, NFormItem, NInput, NInputNumber, NSelect, NSpace, useMessage } from 'naive-ui'
import { getLocationList, addLocation, updateLocation, deleteLocation } from '../api/location'

const message = useMessage()
const showModal = ref(false)
const dataList = ref([])
const formData = ref({})

const statusOptions = [
  { label: '可用', value: 'available' },
  { label: '占用', value: 'occupied' },
  { label: '禁用', value: 'disabled' }
]

const columns = [
  { title: '仓位编码', key: 'locationCode' },
  { title: '仓位名称', key: 'locationName' },
  { title: '所属仓库', key: 'warehouse' },
  { title: '容量', key: 'capacity' },
  { title: '状态', key: 'status' },
  {
    title: '操作',
    key: 'actions',
    render: (row) => h(NSpace, null, {
      default: () => [
        h(NButton, { size: 'small', onClick: () => { formData.value = { ...row }; showModal.value = true } }, { default: () => '编辑' }),
        h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row.id) }, { default: () => '删除' })
      ]
    })
  }
]

const loadData = async () => {
  dataList.value = await getLocationList()
}

const handleSave = async () => {
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
    message.error('保存失败')
  }
}

const handleDelete = async (id) => {
  try {
    await deleteLocation(id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

onMounted(() => loadData())
</script>
