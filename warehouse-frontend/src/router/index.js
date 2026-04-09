import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../components/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: '/material',
        name: 'Material',
        component: () => import('../views/Material.vue')
      },
      {
        path: '/supplier',
        name: 'Supplier',
        component: () => import('../views/Supplier.vue')
      },
      {
        path: '/location',
        name: 'Location',
        component: () => import('../views/Location.vue')
      },
      {
        path: '/inbound',
        name: 'Inbound',
        component: () => import('../views/Inbound.vue')
      },
      {
        path: '/outbound',
        name: 'Outbound',
        component: () => import('../views/Outbound.vue')
      },
      {
        path: '/inventory',
        name: 'Inventory',
        component: () => import('../views/Inventory.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 路由守卫 - 校验token存在性及基础有效性
 */
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  let tokenValid = false

  if (token) {
    try {
      // 解析JWT payload检查是否过期
      const payload = JSON.parse(atob(token.split('.')[1]))
      tokenValid = payload.exp * 1000 > Date.now()
    } catch {
      tokenValid = false
    }
  }

  if (to.path !== '/login' && !tokenValid) {
    // token无效或过期，清除并跳转登录
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    next('/login')
  } else if (to.path === '/login' && tokenValid) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
