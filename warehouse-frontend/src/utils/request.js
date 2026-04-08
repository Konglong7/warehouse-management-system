import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      // 使用Bearer格式传递JWT令牌
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res.data
    } else {
      window.$message?.error(res.message || '请求失败')
      // 401未授权，跳转登录
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    // 处理HTTP错误状态
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        window.$message?.error('登录已过期，请重新登录')
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        window.location.href = '/login'
      } else if (status === 403) {
        window.$message?.error('没有权限执行此操作')
      } else if (status === 404) {
        window.$message?.error('请求的资源不存在')
      } else if (status === 500) {
        window.$message?.error('服务器内部错误')
      } else {
        window.$message?.error(error.message || '网络错误')
      }
    } else {
      window.$message?.error('网络连接失败，请检查网络')
    }
    return Promise.reject(error)
  }
)

export default request
