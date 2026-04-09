<template>
  <n-layout has-sider style="height: 100vh">
    <n-layout-sider bordered :width="240">
      <div class="logo">
        <h2>仓库管理系统</h2>
      </div>
      <n-menu
        :value="activeKey"
        :options="menuOptions"
        @update:value="handleMenuSelect"
      />
    </n-layout-sider>
    <n-layout>
      <n-layout-header bordered style="height: 64px; padding: 0 24px; display: flex; align-items: center; justify-content: space-between;">
        <h3>{{ currentTitle }}</h3>
        <n-dropdown :options="userOptions" @select="handleUserAction">
          <n-button text>
            {{ userInfo?.nickname || userInfo?.username }}
          </n-button>
        </n-dropdown>
      </n-layout-header>
      <n-layout-content content-style="padding: 24px;">
        <router-view />
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script setup>
import { ref, computed, h, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { NLayout, NLayoutSider, NLayoutHeader, NLayoutContent, NMenu, NButton, NDropdown, NIcon } from 'naive-ui'
import { useUserStore } from '../store/user'
import {
  GridOutline as DashboardOutline,
  CubeOutline,
  BusinessOutline,
  LocationOutline,
  ArrowDownCircleOutline,
  ArrowUpCircleOutline,
  ArchiveOutline
} from '@vicons/ionicons5'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const userInfo = computed(() => userStore.user || JSON.parse(localStorage.getItem('user') || '{}'))

const activeKey = computed(() => route.path)

const renderIcon = (icon) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const menuOptions = [
  {
    label: '数据看板',
    key: '/dashboard',
    icon: renderIcon(DashboardOutline)
  },
  {
    label: '物料管理',
    key: '/material',
    icon: renderIcon(CubeOutline)
  },
  {
    label: '供应商管理',
    key: '/supplier',
    icon: renderIcon(BusinessOutline)
  },
  {
    label: '仓位管理',
    key: '/location',
    icon: renderIcon(LocationOutline)
  },
  {
    label: '入库管理',
    key: '/inbound',
    icon: renderIcon(ArrowDownCircleOutline)
  },
  {
    label: '出库管理',
    key: '/outbound',
    icon: renderIcon(ArrowUpCircleOutline)
  },
  {
    label: '库存查询',
    key: '/inventory',
    icon: renderIcon(ArchiveOutline)
  }
]

const currentTitle = computed(() => {
  const item = menuOptions.find(m => m.key === route.path)
  return item?.label || '仓库管理系统'
})

const userOptions = [
  { label: '退出登录', key: 'logout' }
]

const handleMenuSelect = (key) => {
  router.push(key)
}

const handleUserAction = (key) => {
  if (key === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #e0e0e6;
  background: linear-gradient(135deg, #FFD100 0%, #FFA500 100%);
}

.logo h2 {
  color: #1a1a1a;
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 1px;
}
</style>
