<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <n-message-provider>
      <router-view />
    </n-message-provider>
  </n-config-provider>
</template>

<script setup>
import { NConfigProvider, NMessageProvider } from 'naive-ui'

const themeOverrides = {
  common: {
    primaryColor: '#FFD100',
    primaryColorHover: '#FFE44D',
    primaryColorPressed: '#E6BC00',
    primaryColorSuppl: '#FFA500'
  },
  Button: {
    textColorPrimary: '#1a1a1a',
    textColorHoverPrimary: '#1a1a1a',
    textColorPressedPrimary: '#1a1a1a',
    textColorFocusPrimary: '#1a1a1a',
    borderRadiusMedium: '8px',
    borderRadiusLarge: '10px'
  },
  Input: {
    borderRadius: '8px'
  },
  Card: {
    borderRadius: '12px'
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

#app {
  width: 100%;
  height: 100vh;
}
</style>
