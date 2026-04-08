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

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
