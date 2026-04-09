<template>
  <div class="dashboard">
    <n-grid :cols="4" :x-gap="20" :y-gap="20">
      <n-gi>
        <n-card class="stat-card stat-primary" :bordered="false">
          <div class="stat-icon">📦</div>
          <div class="stat-content">
            <div class="stat-label">总库存</div>
            <div class="stat-value">{{ stats.totalStock }}</div>
          </div>
        </n-card>
      </n-gi>
      <n-gi>
        <n-card class="stat-card stat-success" :bordered="false">
          <div class="stat-icon">📥</div>
          <div class="stat-content">
            <div class="stat-label">今日入库</div>
            <div class="stat-value">{{ stats.todayInbound }}</div>
          </div>
        </n-card>
      </n-gi>
      <n-gi>
        <n-card class="stat-card stat-info" :bordered="false">
          <div class="stat-icon">📤</div>
          <div class="stat-content">
            <div class="stat-label">今日出库</div>
            <div class="stat-value">{{ stats.todayOutbound }}</div>
          </div>
        </n-card>
      </n-gi>
      <n-gi>
        <n-card class="stat-card stat-warning" :bordered="false">
          <div class="stat-icon">⚠️</div>
          <div class="stat-content">
            <div class="stat-label">预警物料</div>
            <div class="stat-value">{{ stats.warningCount }}</div>
          </div>
        </n-card>
      </n-gi>
    </n-grid>

    <n-grid :cols="2" :x-gap="20" :y-gap="20" style="margin-top: 20px">
      <n-gi>
        <n-card title="库存统计" :bordered="false" class="chart-card">
          <template #header-extra>
            <n-tag type="info" size="small">TOP 10</n-tag>
          </template>
          <div ref="stockChartRef" style="height: 320px"></div>
        </n-card>
      </n-gi>
      <n-gi>
        <n-card title="物料分类占比" :bordered="false" class="chart-card">
          <template #header-extra>
            <n-tag type="success" size="small">实时数据</n-tag>
          </template>
          <div ref="categoryChartRef" style="height: 320px"></div>
        </n-card>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { NGrid, NGi, NCard, NTag } from 'naive-ui'
import * as echarts from 'echarts'
import { getDashboardStats, getInventoryStats } from '../api/stats'

const stats = ref({
  totalStock: 0,
  todayInbound: 0,
  todayOutbound: 0,
  warningCount: 0
})

const stockChartRef = ref(null)
const categoryChartRef = ref(null)

/** ECharts实例引用，用于销毁和resize */
let stockChart = null
let categoryChart = null

const loadStats = async () => {
  const data = await getDashboardStats()
  stats.value = data
}

const loadCharts = async () => {
  const materials = await getInventoryStats()

  // 库存统计柱状图
  stockChart = echarts.init(stockChartRef.value)
  stockChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: materials.slice(0, 10).map(m => m.materialName),
      axisLabel: {
        rotate: 30,
        fontSize: 11
      }
    },
    yAxis: {
      type: 'value',
      name: '库存量'
    },
    series: [{
      data: materials.slice(0, 10).map(m => m.currentStock),
      type: 'bar',
      barWidth: '50%',
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#FFD100' },
          { offset: 1, color: '#FFA500' }
        ]),
        borderRadius: [8, 8, 0, 0]
      },
      emphasis: {
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#FFE44D' },
            { offset: 1, color: '#FFB84D' }
          ])
        }
      }
    }]
  })

  // 物料分类饼图
  const categoryMap = {}
  materials.forEach(m => {
    categoryMap[m.category] = (categoryMap[m.category] || 0) + m.currentStock
  })

  categoryChart = echarts.init(categoryChartRef.value)
  categoryChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: '10%',
      top: 'center'
    },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['35%', '50%'],
      data: Object.keys(categoryMap).map(key => ({
        name: key,
        value: categoryMap[key]
      })),
      label: {
        show: true,
        formatter: '{b}\n{d}%'
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      },
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2
      }
    }],
    color: ['#FFD100', '#FFA500', '#667eea', '#764ba2', '#f093fb', '#4facfe']
  })
}

/** 窗口resize时自适应图表 */
const handleResize = () => {
  stockChart?.resize()
  categoryChart?.resize()
}

onMounted(() => {
  loadStats()
  loadCharts()
  window.addEventListener('resize', handleResize)
})

/** 组件销毁时清理ECharts实例和事件监听，防止内存泄漏 */
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  stockChart?.dispose()
  categoryChart?.dispose()
  stockChart = null
  categoryChart = null
})
</script>

<style scoped>
.dashboard {
  width: 100%;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-card :deep(.n-card__content) {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 0;
}

.stat-icon {
  font-size: 48px;
  line-height: 1;
}

.stat-content {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  font-weight: 500;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #1a1a1a;
  line-height: 1;
}

.stat-primary {
  background: linear-gradient(135deg, #FFD100 0%, #FFA500 100%);
}

.stat-primary .stat-label,
.stat-primary .stat-value {
  color: #1a1a1a;
}

.stat-success {
  background: linear-gradient(135deg, #a8e6cf 0%, #56ab2f 100%);
}

.stat-success .stat-label,
.stat-success .stat-value {
  color: #1a4d2e;
}

.stat-info {
  background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
}

.stat-info .stat-label,
.stat-info .stat-value {
  color: #1a4d5e;
}

.stat-warning {
  background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%);
}

.stat-warning .stat-label,
.stat-warning .stat-value {
  color: #8b4513;
}

.chart-card {
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.chart-card :deep(.n-card-header) {
  padding: 20px 24px;
  font-weight: 600;
  font-size: 16px;
}

.chart-card :deep(.n-card__content) {
  padding: 0 24px 24px;
}
</style>
