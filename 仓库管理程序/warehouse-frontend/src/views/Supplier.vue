<template>
  <div>
    <n-button type="primary" @click="showModal = true" style="margin-bottom: 16px">新增供应商</n-button>
    <n-data-table :columns="columns" :data="dataList" :pagination="{ pageSize: 10 }" />

    <n-modal v-model:show="showModal" preset="dialog" title="供应商信息">
      <n-form :model="formData">
        <n-form-item label="供应商编码">
          <n-input v-model:value="formData.supplierCode" />
        </n-form-item>
        <n-form-item label="供应商名称">
          <n-input v-model:value="formData.supplierName" />
        </n-form-item>
        <n-form-item label="联系人">
          <n-input v-model:value="formData.contact" />
        </n-form-item>
        <n-form-item label="联系电话">
          <n-input v-model:value="formData.phone" />
        </n-form-item>
        <n-form-item label="地址">
          <n-input v-model:value="formData.address" />
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
import { NButton, NDataTable, NModal, NForm, NFormItem, NInput, NSpace, useMessage } from 'naive-ui'
import { getSupplierList, addSupplier, updateSupplier, deleteSupplier } from '../api/supplier'

const message = useMessage()
const showModal = ref(false)
const dataList = ref([])
const formData = ref({})

const columns = [
  { title: '供应商编码', key: 'supplierCode' },
  { title: '供应商名称', key: 'supplierName' },
  { title: '联系人', key: 'contact' },
  { title: '联系电话', key: 'phone' },
  { title: '地址', key: 'address' },
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
  dataList.value = await getSupplierList()
}

const handleSave = async () => {
  try {
    if (formData.value.id) {
      await updateSupplier(formData.value.id, formData.value)
    } else {
      await addSupplier(formData.value)
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
    await deleteSupplier(id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

onMounted(() => loadData())
</script>
