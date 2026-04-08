<template>
  <div class="page-container">
    <n-card :bordered="false" class="page-card">
      <n-space justify="space-between" style="margin-bottom: 20px">
        <n-space>
          <n-input
            v-model:value="searchText"
            placeholder="搜索物料名称或编码"
            style="width: 320px"
            size="large"
            @keyup.enter="handleSearch"
            clearable
          >
            <template #prefix>
              <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
                <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
              </svg>
            </template>
          </n-input>
          <n-button type="primary" size="large" @click="handleSearch">搜索</n-button>
        </n-space>
        <n-button type="primary" size="large" @click="showModal = true">
          <template #icon>
            <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
              <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
            </svg>
          </template>
          新增物料
        </n-button>
      </n-space>

      <n-data-table
        :columns="columns"
        :data="filteredData"
        :pagination="pagination"
        :bordered="false"
        :single-line="false"
        striped
      />
    </n-card>

    <n-modal v-model:show="showModal" preset="dialog" title="物料信息" style="width: 600px">
      <n-form :model="formData" label-placement="left" label-width="100px">
        <n-form-item label="物料编码">
          <n-input v-model:value="formData.materialCode" placeholder="请输入物料编码" />
        </n-form-item>
        <n-form-item label="物料名称">
          <n-input v-model:value="formData.materialName" placeholder="请输入物料名称" />
        </n-form-item>
        <n-form-item label="分类">
          <n-input v-model:value="formData.category" placeholder="请输入分类" />
        </n-form-item>
        <n-form-item label="单位">
          <n-input v-model:value="formData.unit" placeholder="请输入单位" />
        </n-form-item>
        <n-form-item label="预警库存">
          <n-input-number v-model:value="formData.warningStock" style="width: 100%" :min="0" />
        </n-form-item>
        <n-form-item label="当前库存">
          <n-input-number v-model:value="formData.currentStock" style="width: 100%" :min="0" />
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
import { ref, computed, h, onMounted } from 'vue'
import { NSpace, NInput, NButton, NDataTable, NModal, NForm, NFormItem, NInputNumber, NCard, useMessage } from 'naive-ui'
import { getMaterialList, addMaterial, updateMaterial, deleteMaterial } from '../api/material'

const message = useMessage()
const searchText = ref('')
const searchQuery = ref('')
const showModal = ref(false)
const dataList = ref([])
const formData = ref({
  materialCode: '',
  materialName: '',
  category: '',
  unit: '',
  warningStock: 10,
  currentStock: 0
})

const columns = [
  { title: '物料编码', key: 'materialCode', width: 120 },
  { title: '物料名称', key: 'materialName', ellipsis: { tooltip: true } },
  { title: '分类', key: 'category', width: 100 },
  { title: '单位', key: 'unit', width: 80 },
  { title: '当前库存', key: 'currentStock', width: 100 },
  { title: '预警库存', key: 'warningStock', width: 100 },
  {
    title: '操作',
    key: 'actions',
    width: 160,
    render: (row) => {
      return h(NSpace, { size: 'small' }, {
        default: () => [
          h(NButton, { size: 'small', type: 'primary', secondary: true, onClick: () => handleEdit(row) }, { default: () => '编辑' }),
          h(NButton, { size: 'small', type: 'error', secondary: true, onClick: () => handleDelete(row.id) }, { default: () => '删除' })
        ]
      })
    }
  }
]

const pagination = { pageSize: 10 }

const filteredData = computed(() => {
  if (!searchQuery.value) return dataList.value
  return dataList.value.filter(item =>
    item.materialName.includes(searchQuery.value) ||
    item.materialCode.includes(searchQuery.value)
  )
})

const handleSearch = () => {
  searchQuery.value = searchText.value
}

const loadData = async () => {
  dataList.value = await getMaterialList()
}

const handleEdit = (row) => {
  formData.value = { ...row }
  showModal.value = true
}

const handleSave = async () => {
  try {
    if (formData.value.id) {
      await updateMaterial(formData.value.id, formData.value)
    } else {
      await addMaterial(formData.value)
    }
    message.success('保存成功')
    showModal.value = false
    formData.value = {
      materialCode: '',
      materialName: '',
      category: '',
      unit: '',
      warningStock: 10,
      currentStock: 0
    }
    loadData()
  } catch (error) {
    message.error('保存失败')
  }
}

const handleDelete = async (id) => {
  try {
    await deleteMaterial(id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  width: 100%;
}

.page-card {
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.page-card :deep(.n-card__content) {
  padding: 24px;
}

.page-card :deep(.n-data-table) {
  font-size: 14px;
}

.page-card :deep(.n-data-table-th) {
  font-weight: 600;
  background-color: #fafafa;
}

.page-card :deep(.n-data-table-td) {
  padding: 16px 12px;
}

.page-card :deep(.n-input) {
  border-radius: 8px;
}

.page-card :deep(.n-button) {
  border-radius: 8px;
  font-weight: 500;
}
</style>
