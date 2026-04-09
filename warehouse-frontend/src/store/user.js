import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 从localStorage恢复状态，避免刷新丢失
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  const token = ref(localStorage.getItem('token') || '')

  const setUser = (userData) => {
    user.value = userData
    localStorage.setItem('user', JSON.stringify(userData))
  }

  const setToken = (tokenValue) => {
    token.value = tokenValue
    localStorage.setItem('token', tokenValue)
  }

  const logout = () => {
    user.value = null
    token.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  /**
   * 检查token是否存在且未过期（基础校验）
   * @returns {boolean} token是否有效
   */
  const isTokenValid = () => {
    if (!token.value) return false
    try {
      // JWT格式：header.payload.signature，解析payload检查过期时间
      const payload = JSON.parse(atob(token.value.split('.')[1]))
      return payload.exp * 1000 > Date.now()
    } catch {
      return false
    }
  }

  return {
    user,
    token,
    setUser,
    setToken,
    logout,
    isTokenValid
  }
})
