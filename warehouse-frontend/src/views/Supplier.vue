<template>
  <div>
    <n-button type="primary" @click="handleAdd" style="margin-bottom: 16px">新增供应商</n-button>
    <n-data-table :columns="columns" :data="dataList" :pagination="{ pageSize: 10 }" />

    <n-modal v-model:show="showModal" preset="dialog" title="供应商信息">
      <n-form :model="formData" :rules="rules" label-placement="left" label-width="100px">
        <n-form-item label="供应商编码" path="supplierCode">
          <n-input v-model:value="formData.supplierCode" placeholder="请输入供应商编码" />
        </n-form-item>
        <n-form-item label="供应商名称" path="supplierName">
          <n-input v-model:value="formData.supplierName" placeholder="请输入供应商名称" />
        </n-form-item>
        <n-form-item label="联系人" path="contact">
          <n-input v-model:value="formData.contact" placeholder="请输入联系人" />
        </n-form-item>
        <n-form-item label="联系电话" path="phone">
          <n-input v-model:value="formData.phone" placeholder="请输入联系电话" />
        </n-form-item>
        <n-form-item label="地址">
          <n-input v-model:value="formData.address" placeholder="请输入地址（可选）" />
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

// 表单验证规则
const rules = {
  supplierCode: { required: true, message: '请输入供应商编码', trigger: 'blur' },
  supplierName: { required: true, message: '请输入供应商名称', trigger: 'blur' },
  contact: { required: true, message: '请输入联系人', trigger: 'blur' },
  phone: { required: true, message: '请输入联系电话', trigger: 'blur' }
}

const columns = [
  { title: '供应商编码', key: 'supplierCode', width: 120 },
  { title: '供应商名称', key: 'supplierName', ellipsis: { tooltip: true } },
  { title: '联系人', key: 'contact', width: 100 },
  { title: '联系电话', key: 'phone', width: 130 },
  { title: '地址', key: 'address', ellipsis: { tooltip: true } },
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
  dataList.value = await getSupplierList()
}

const handleAdd = () => {
  formData.value = {}
  showModal.value = true
}

const handleSave = async () => {
  // 表单验证
  if (!formData.value.supplierCode) {
    message.error('请输入供应商编码')
    return
  }
  if (!formData.value.supplierName) {
    message.error('请输入供应商名称')
    return
  }
  if (!formData.value.contact) {
    message.error('请输入联系人')
    return
  }
  if (!formData.value.phone) {
    message.error('请输入联系电话')
    return
  }

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
    message.error(error.message || '保存失败')
  }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除该供应商吗？')) {
    return
  }
  try {
    await deleteSupplier(id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error(error.message || '删除失败')
  }
}

onMounted(() => loadData())
</script>
