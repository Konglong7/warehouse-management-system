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
        <n-tag type="error" size="large" :bordered="false" class="warning-tag">
          <template #icon>
            <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
              <path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z"/>
            </svg>
          </template>
          预警物料: {{ warningCount }}
        </n-tag>
      </n-space>

      <n-data-table
        :columns="columns"
        :data="filteredData"
        :pagination="{ pageSize: 10 }"
        :row-props="rowProps"
        :bordered="false"
        :single-line="false"
        striped
      />
    </n-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, h } from 'vue'
import { NSpace, NInput, NButton, NTag, NDataTable, NCard } from 'naive-ui'
import { getMaterialList } from '../api/material'

const searchText = ref('')
const searchQuery = ref('')
const dataList = ref([])

const columns = [
  { title: '物料编码', key: 'materialCode', width: 120 },
  { title: '物料名称', key: 'materialName', ellipsis: { tooltip: true } },
  { title: '分类', key: 'category', width: 100 },
  { title: '单位', key: 'unit', width: 80 },
  { title: '当前库存', key: 'currentStock', width: 100 },
  { title: '预警库存', key: 'warningStock', width: 100 },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) => {
      if (row.currentStock <= row.warningStock) {
        return h(NTag, { type: 'error', size: 'small', round: true }, { default: () => '⚠️ 预警' })
      } else if (row.currentStock <= row.warningStock * 1.5) {
        return h(NTag, { type: 'warning', size: 'small', round: true }, { default: () => '正常' })
      } else {
        return h(NTag, { type: 'success', size: 'small', round: true }, { default: () => '✓ 充足' })
      }
    }
  }
]

const rowProps = (row) => {
  if (row.currentStock <= row.warningStock) {
    return {
      style: 'background-color: #fff1f0; font-weight: 500;'
    }
  }
  return {}
}

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

const warningCount = computed(() => {
  return dataList.value.filter(item => item.currentStock <= item.warningStock).length
})

const loadData = async () => {
  dataList.value = await getMaterialList()
}

onMounted(() => loadData())
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

.warning-tag {
  padding: 8px 16px;
  font-size: 15px;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(245, 34, 45, 0.2);
}
</style>
