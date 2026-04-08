<template>
  <div class="login-container">
    <div class="login-left">
      <div class="brand-content">
        <div class="brand-icon">
          <svg viewBox="0 0 120 120" width="120" height="120">
            <defs>
              <linearGradient id="iconGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#FFD100;stop-opacity:1" />
                <stop offset="100%" style="stop-color:#FFA500;stop-opacity:1" />
              </linearGradient>
            </defs>
            <rect x="20" y="30" width="80" height="70" rx="6" fill="url(#iconGradient)" opacity="0.2"/>
            <rect x="25" y="35" width="70" height="60" rx="4" fill="none" stroke="url(#iconGradient)" stroke-width="3"/>
            <line x1="35" y1="50" x2="85" y2="50" stroke="url(#iconGradient)" stroke-width="3" stroke-linecap="round"/>
            <line x1="35" y1="65" x2="85" y2="65" stroke="url(#iconGradient)" stroke-width="3" stroke-linecap="round"/>
            <line x1="35" y1="80" x2="70" y2="80" stroke="url(#iconGradient)" stroke-width="3" stroke-linecap="round"/>
          </svg>
        </div>
        <h1 class="brand-title">智能仓库管理系统</h1>
        <p class="brand-subtitle">Intelligent Warehouse Management System</p>
        <div class="brand-features">
          <div class="feature-item">
            <span class="feature-icon">📦</span>
            <span>智能库存管理</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">📊</span>
            <span>数据可视化分析</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">🚀</span>
            <span>高效出入库流程</span>
          </div>
        </div>
      </div>
    </div>
    <div class="login-right">
      <div class="login-content">
        <h2 class="login-title">欢迎登录</h2>
        <p class="login-subtitle">请输入您的账号信息</p>

        <div class="login-form">
          <n-form ref="formRef" :model="formData" :rules="rules" :show-label="false">
            <n-form-item path="username">
              <n-input
                v-model:value="formData.username"
                placeholder="用户名"
                size="large"
                @keyup.enter="handleLogin"
              >
                <template #prefix>
                  <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
                    <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                  </svg>
                </template>
              </n-input>
            </n-form-item>

            <n-form-item path="password">
              <n-input
                v-model:value="formData.password"
                type="password"
                placeholder="密码"
                size="large"
                show-password-on="click"
                @keyup.enter="handleLogin"
              >
                <template #prefix>
                  <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
                    <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zM9 6c0-1.66 1.34-3 3-3s3 1.34 3 3v2H9V6zm9 14H6V10h12v10zm-6-3c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2z"/>
                  </svg>
                </template>
              </n-input>
            </n-form-item>

            <n-button
              type="primary"
              size="large"
              block
              @click="handleLogin"
              :loading="loading"
              class="login-button"
            >
              登录
            </n-button>
          </n-form>

          <div class="login-footer">
            <span class="hint">默认账号: admin / 123456</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, NForm, NFormItem, NInput, NButton } from 'naive-ui'
import { login } from '../api/user'
import { useUserStore } from '../store/user'

const router = useRouter()
const message = useMessage()
const userStore = useUserStore()

const formData = ref({
  username: 'admin',
  password: '123456'
})

const loading = ref(false)

const rules = {
  username: { required: true, message: '请输入用户名', trigger: 'blur' },
  password: { required: true, message: '请输入密码', trigger: 'blur' }
}

const handleLogin = async () => {
  loading.value = true
  try {
    const res = await login(formData.value)
    userStore.setUser(res.user)
    userStore.setToken(res.token)
    localStorage.setItem('user', JSON.stringify(res.user))
    message.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    message.error('登录失败')
  } finally {
    loading.value = false
  }
}

window.$message = message
</script>

<style scoped>
.login-container {
  display: flex;
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: -20%;
  left: -10%;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(255, 209, 0, 0.15) 0%, transparent 70%);
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
}

.login-container::after {
  content: '';
  position: absolute;
  bottom: -15%;
  right: -10%;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(255, 209, 0, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  animation: float 8s ease-in-out infinite reverse;
}

@keyframes float {
  0%, 100% { transform: translateY(0) translateX(0); }
  50% { transform: translateY(-20px) translateX(20px); }
}

.login-left {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  position: relative;
  z-index: 1;
}

.brand-content {
  text-align: center;
  color: white;
}

.brand-icon {
  margin: 0 auto 32px;
  animation: pulse 3s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

.brand-title {
  font-size: 48px;
  font-weight: 700;
  margin: 0 0 16px 0;
  letter-spacing: 2px;
  text-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.brand-subtitle {
  font-size: 18px;
  opacity: 0.95;
  margin: 0 0 48px 0;
  font-weight: 300;
  letter-spacing: 1px;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-width: 400px;
  margin: 0 auto;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateX(8px);
}

.feature-icon {
  font-size: 28px;
}

.login-right {
  width: 520px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  box-shadow: -8px 0 32px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 2;
}

.login-content {
  width: 100%;
  max-width: 400px;
  padding: 48px 40px;
}

.login-title {
  font-size: 32px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 8px 0;
  letter-spacing: -0.5px;
}

.login-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0 0 40px 0;
  font-weight: 400;
}

.login-form :deep(.n-form-item) {
  margin-bottom: 24px;
}

.login-form :deep(.n-input) {
  border-radius: 12px;
  background-color: #f8f9fa;
  border: 2px solid transparent;
  transition: all 0.3s ease;
}

.login-form :deep(.n-input:hover) {
  background-color: #f0f1f3;
  border-color: #FFD100;
}

.login-form :deep(.n-input.n-input--focus) {
  background-color: #fff;
  border-color: #FFD100;
  box-shadow: 0 0 0 4px rgba(255, 209, 0, 0.1);
}

.login-form :deep(.n-input__input) {
  font-size: 15px;
}

.login-form :deep(.n-input__prefix) {
  color: #999;
  margin-right: 12px;
}

.login-button {
  margin-top: 12px;
  height: 52px;
  border-radius: 12px;
  font-size: 17px;
  font-weight: 600;
  background: linear-gradient(135deg, #FFD100 0%, #FFA500 100%);
  border: none;
  box-shadow: 0 6px 20px rgba(255, 209, 0, 0.3);
  transition: all 0.3s ease;
  color: #1a1a1a;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(255, 209, 0, 0.4);
}

.login-button:active {
  transform: translateY(0);
}

.login-footer {
  margin-top: 32px;
  text-align: center;
}

.hint {
  font-size: 13px;
  color: #999;
  font-weight: 400;
}

@media (max-width: 1024px) {
  .login-left {
    display: none;
  }

  .login-right {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .login-content {
    padding: 36px 28px;
  }

  .login-title {
    font-size: 28px;
  }
}
</style>
