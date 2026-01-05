<template>
  <div class="dashboard-container">
    <!-- 顶部标题栏 -->
    <div class="dashboard-header">
      <div class="header-left">
        <h1 class="page-title">后台 / 仪表盘</h1>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshData">刷新</el-button>
        <el-button type="success" icon="el-icon-download" @click="exportData">导出报表</el-button>
      </div>
    </div>

    <!-- 主要数据卡片区域 -->
    <div class="dashboard-main">
      <!-- 左侧：数据卡片 -->
      <div class="data-cards">
        <!-- 第一行：4个主要指标 -->
        <div class="card-row">
          <el-card class="metric-card" shadow="hover">
            <div class="card-content">
              <div class="card-icon" style="background: linear-gradient(135deg, #409EFF, #337ecc);">
                <i class="el-icon-user"></i>
              </div>
              <div class="card-info">
                <div class="card-label">用户数</div>
                <div class="card-value">{{ metrics.users || 13 }}</div>
                <div class="card-trend">
                  <span class="trend-up">↑ 12%</span>
                  <span class="trend-text">较上周</span>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="metric-card" shadow="hover">
            <div class="card-content">
              <div class="card-icon" style="background: linear-gradient(135deg, #67C23A, #529b2e);">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-label">收录商品</div>
                <div class="card-value">{{ metrics.products || 18 }}</div>
                <div class="card-trend">
                  <span class="trend-up">↑ 8%</span>
                  <span class="trend-text">较上周</span>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="metric-card" shadow="hover">
            <div class="card-content">
              <div class="card-icon" style="background: linear-gradient(135deg, #E6A23C, #c68a34);">
                <i class="el-icon-s-order"></i>
              </div>
              <div class="card-info">
                <div class="card-label">订单数</div>
                <div class="card-value">{{ metrics.orders || 5 }}</div>
                <div class="card-trend">
                  <span class="trend-down">↓ 3%</span>
                  <span class="trend-text">较上周</span>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="metric-card" shadow="hover">
            <div class="card-content">
              <div class="card-icon" style="background: linear-gradient(135deg, #F56C6C, #c45656);">
                <i class="el-icon-message"></i>
              </div>
              <div class="card-info">
                <div class="card-label">消息通知</div>
                <div class="card-value">{{ metrics.messages || 1 }}</div>
                <div class="card-trend">
                  <span class="trend-up">↑ 25%</span>
                  <span class="trend-text">较上周</span>
                </div>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 第二行：3个次要指标 -->
        <div class="card-row">
          <el-card class="metric-card secondary" shadow="hover">
            <div class="card-content">
              <div class="card-icon" style="background: linear-gradient(135deg, #909399, #73767a);">
                <i class="el-icon-shopping-cart-2"></i>
              </div>
              <div class="card-info">
                <div class="card-label">砍价</div>
                <div class="card-value">{{ metrics.bargains || 1 }}</div>
                <div class="card-trend">
                  <span class="trend-up">↑ 5%</span>
                  <span class="trend-text">较上周</span>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="metric-card secondary" shadow="hover">
            <div class="card-content">
              <div class="card-icon" style="background: linear-gradient(135deg, #337ecc, #2a6495);">
                <i class="el-icon-chat-line-round"></i>
              </div>
              <div class="card-info">
                <div class="card-label">互动数据</div>
                <div class="card-value">{{ metrics.interactions || 22 }}</div>
                <div class="card-trend">
                  <span class="trend-up">↑ 15%</span>
                  <span class="treptext">较上周</span>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="metric-card secondary" shadow="hover">
            <div class="card-content">
              <div class="card-icon" style="background: linear-gradient(135deg, #529b2e, #3f7a23);">
                <i class="el-icon-chat-dot-round"></i>
              </div>
              <div class="card-info">
                <div class="card-label">商品评论</div>
                <div class="card-value">{{ metrics.comments || 4 }}</div>
                <div class="card-trend">
                  <span class="trend-up">↑ 8%</span>
                  <span class="trend-text">较上周</span>
                </div>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 商品上架趋势图表 -->
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="chart-header">
              <span class="chart-title">商品上架趋势</span>
              <div class="chart-controls">
                <el-select v-model="chartRange" placeholder="期限" size="small" style="width: 120px; margin-right: 10px;">
                  <el-option label="最近7天" value="7days"></el-option>
                  <el-option label="最近30天" value="30days"></el-option>
                  <el-option label="最近365天" value="365days"></el-option>
                </el-select>
                <el-date-picker
                  v-model="dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="small"
                  style="width: 240px;"
                >
                </el-date-picker>
              </div>
            </div>
          </template>
          <div id="trend-chart" class="chart-content"></div>
          <div class="chart-footer">
            <div class="chart-legend">
              <span class="legend-item">
                <span class="legend-color" style="background-color: #409EFF;"></span>
                <span>商品上架数量</span>
              </span>
              <span class="legend-item">
                <span class="legend-color" style="background-color: #67C23A;"></span>
                <span>用户活跃度</span>
              </span>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧：最新上架商品 -->
      <div class="right-sidebar">
        <el-card class="sidebar-card" shadow="hover">
          <template #header>
            <div class="sidebar-header">
              <span class="sidebar-title">最新上架商品</span>
              <el-button type="text" @click="viewAllProducts">查看全部</el-button>
            </div>
          </template>
          
          <div class="product-list">
            <div v-for="(product, index) in latestProducts" :key="index" class="product-item">
              <div class="product-image">
                <img :src="product.image" alt="商品图片">
                <el-tag v-if="product.canBargain" type="warning" size="mini" class="bargain-tag">砍价</el-tag>
              </div>
              <div class="product-info">
                <div class="product-name">{{ product.name }}</div>
                <div class="product-price">¥{{ product.price }}</div>
                <div class="product-meta">
                  <span class="wants-count">
                    <i class="el-icon-star-off"></i>
                    {{ product.wants || 0 }}人想要
                  </span>
                  <span class="update-time">{{ product.time }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 快速操作 -->
        <el-card class="sidebar-card quick-actions" shadow="hover">
          <template #header>
            <span class="sidebar-title">快捷操作</span>
          </template>
          <div class="actions-grid">
            <div class="action-item" @click="handleAction('add')">
              <div class="action-icon" style="background-color: #409EFF;">
                <i class="el-icon-circle-plus"></i>
              </div>
              <div class="action-label">添加商品</div>
            </div>
            <div class="action-item" @click="handleAction('order')">
              <div class="action-icon" style="background-color: #67C23A;">
                <i class="el-icon-s-order"></i>
              </div>
              <div class="action-label">订单管理</div>
            </div>
            <div class="action-item" @click="handleAction('user')">
              <div class="action-icon" style="background-color: #E6A23C;">
                <i class="el-icon-user"></i>
              </div>
              <div class="action-label">用户管理</div>
            </div>
            <div class="action-item" @click="handleAction('message')">
              <div class="action-icon" style="background-color: #F56C6C;">
                <i class="el-icon-message"></i>
              </div>
              <div class="action-label">消息中心</div>
            </div>
            <div class="action-item" @click="handleAction('setting')">
              <div class="action-icon" style="background-color: #909399;">
                <i class="el-icon-setting"></i>
              </div>
              <div class="action-label">系统设置</div>
            </div>
            <div class="action-item" @click="handleAction('stat')">
              <div class="action-icon" style="background-color: #337ecc;">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="action-label">数据统计</div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      metrics: {
        users: 13,
        products: 18,
        orders: 5,
        messages: 1,
        bargains: 1,
        interactions: 22,
        comments: 4
      },
      chartRange: '365days',
      dateRange: [],
      latestProducts: [
        {
          id: 1,
          name: 'vivo X100 测试商品',
          price: '1399',
          image: 'https://via.placeholder.com/60x60/409EFF/ffffff?text=V',
          wants: 0,
          time: '2024-01-15',
          canBargain: true
        },
        {
          id: 2,
          name: '智能手表1',
          price: '222',
          image: 'https://via.placeholder.com/60x60/67C23A/ffffff?text=S',
          wants: 0,
          time: '2024-01-14',
          canBargain: true
        },
        {
          id: 3,
          name: '无线蓝牙耳机',
          price: '299',
          image: 'https://via.placeholder.com/60x60/E6A23C/ffffff?text=E',
          wants: 12,
          time: '2024-01-13',
          canBargain: false
        },
        {
          id: 4,
          name: '笔记本电脑',
          price: '5999',
          image: 'https://via.placeholder.com/60x60/F56C6C/ffffff?text=L',
          wants: 8,
          time: '2024-01-12',
          canBargain: true
        },
        {
          id: 5,
          name: '运动相机',
          price: '899',
          image: 'https://via.placeholder.com/60x60/909399/ffffff?text=C',
          wants: 3,
          time: '2024-01-11',
          canBargain: false
        }
      ],
      trendChart: null
    }
  },
  mounted() {
    this.initChart()
    this.loadDashboardData()
  },
  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    // 初始化图表
    initChart() {
      const chartDom = document.getElementById('trend-chart')
      if (!chartDom) return
      
      this.trendChart = echarts.init(chartDom)
      this.updateChart()
    },
    
    // 更新图表数据
    updateChart() {
      if (!this.trendChart) return
      
      // 模拟数据 - 实际应该从API获取
      const dates = []
      const values = []
      const interactions = []
      
      for (let i = 0; i < 30; i++) {
        dates.push(`1月${i + 1}日`)
        values.push(Math.floor(Math.random() * 100) + 50)
        interactions.push(Math.floor(Math.random() * 80) + 30)
      }
      
      const option = {
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          borderColor: '#dcdfe6',
          borderWidth: 1,
          textStyle: {
            color: '#606266'
          },
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#409EFF'
            }
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '12%',
          top: '10%',
          containLabel: true
        },
        legend: {
          show: false
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: dates,
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#909399',
            fontSize: 12
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#909399',
            fontSize: 12
          },
          splitLine: {
            lineStyle: {
              color: '#ebeef5',
              type: 'dashed'
            }
          }
        },
        series: [
          {
            name: '商品上架数量',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              width: 3,
              color: '#409EFF'
            },
            itemStyle: {
              color: '#409EFF',
              borderColor: '#fff',
              borderWidth: 2
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
              ])
            },
            data: values
          },
          {
            name: '用户活跃度',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              width: 3,
              color: '#67C23A'
            },
            itemStyle: {
              color: '#67C23A',
              borderColor: '#fff',
              borderWidth: 2
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
                { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }
              ])
            },
            data: interactions
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },
    
    // 加载仪表盘数据
    loadDashboardData() {
      // 模拟API调用
      this.$axios.get('/dashboard/data').then(res => {
        const { data } = res
        if (data.code === 200) {
          this.metrics = data.data.metrics
          this.latestProducts = data.data.latestProducts
          this.updateChart()
        }
      }).catch(error => {
        console.error('加载仪表盘数据失败:', error)
      })
    },
    
    // 刷新数据
    refreshData() {
      this.$message.success('数据刷新中...')
      this.loadDashboardData()
      setTimeout(() => {
        this.$message.success('数据已刷新')
      }, 1000)
    },
    
    // 导出数据
    exportData() {
      this.$message.info('导出功能开发中...')
    },
    
    // 查看全部商品
    viewAllProducts() {
      this.$router.push('/products')
    },
    
    // 处理快捷操作
    handleAction(type) {
      const actions = {
        add: { path: '/products/add', name: '添加商品' },
        order: { path: '/orders', name: '订单管理' },
        user: { path: '/users', name: '用户管理' },
        message: { path: '/messages', name: '消息中心' },
        setting: { path: '/settings', name: '系统设置' },
        stat: { path: '/statistics', name: '数据统计' }
      }
      
      if (actions[type]) {
        this.$router.push(actions[type].path)
        this.$message.success(`跳转到${actions[type].name}`)
      }
    }
  },
  watch: {
    chartRange() {
      this.updateChart()
    },
    dateRange() {
      this.updateChart()
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
  
  .dashboard-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 24px;
    
    .header-left {
      .page-title {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 600;
        color: #303133;
      }
      
      .header-subtitle {
        font-size: 14px;
        color: #909399;
      }
    }
    
    .header-right {
      display: flex;
      gap: 12px;
    }
  }
  
  .dashboard-main {
    display: flex;
    gap: 20px;
    
    .data-cards {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 20px;
      
      .card-row {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 20px;
        
        &.secondary {
          grid-template-columns: repeat(3, 1fr);
        }
      }
      
      .metric-card {
        border: none;
        border-radius: 12px;
        transition: transform 0.3s ease, box-shadow 0.3s ease;
        
        &:hover {
          transform: translateY(-4px);
          box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12) !important;
        }
        
        .card-content {
          display: flex;
          align-items: center;
          gap: 16px;
          
          .card-icon {
            width: 60px;
            height: 60px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            
            i {
              font-size: 28px;
              color: white;
            }
          }
          
          .card-info {
            flex: 1;
            
            .card-label {
              font-size: 14px;
              color: #909399;
              margin-bottom: 8px;
            }
            
            .card-value {
              font-size: 28px;
              font-weight: 700;
              color: #303133;
              margin-bottom: 4px;
            }
            
            .card-trend {
              font-size: 12px;
              
              .trend-up {
                color: #67c23a;
              }
              
              .trend-down {
                color: #f56c6c;
              }
              
              .trend-text {
                color: #909399;
                margin-left: 4px;
              }
            }
          }
        }
        
        &.secondary {
          .card-icon {
            width: 50px;
            height: 50px;
            
            i {
              font-size: 24px;
            }
          }
          
          .card-value {
            font-size: 24px;
          }
        }
      }
      
      .chart-card {
        border: none;
        border-radius: 12px;
        
        .chart-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          
          .chart-title {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
          }
        }
        
        .chart-content {
          height: 300px;
        }
        
        .chart-footer {
          margin-top: 20px;
          padding-top: 16px;
          border-top: 1px solid #ebeef5;
          
          .chart-legend {
            display: flex;
            justify-content: center;
            gap: 24px;
            
            .legend-item {
              display: flex;
              align-items: center;
              gap: 8px;
              font-size: 14px;
              color: #606266;
              
              .legend-color {
                width: 12px;
                height: 12px;
                border-radius: 2px;
              }
            }
          }
        }
      }
    }
    
    .right-sidebar {
      width: 380px;
      display: flex;
      flex-direction: column;
      gap: 20px;
      
      .sidebar-card {
        border: none;
        border-radius: 12px;
        
        .sidebar-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          
          .sidebar-title {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
          }
        }
        
        .product-list {
          .product-item {
            display: flex;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid #ebeef5;
            cursor: pointer;
            transition: background-color 0.3s;
            
            &:last-child {
              border-bottom: none;
            }
            
            &:hover {
              background-color: #f8f9fa;
            }
            
            .product-image {
              position: relative;
              width: 60px;
              height: 60px;
              margin-right: 12px;
              
              img {
                width: 100%;
                height: 100%;
                border-radius: 8px;
                object-fit: cover;
              }
              
              .bargain-tag {
                position: absolute;
                top: -6px;
                right: -6px;
                font-size: 10px;
                padding: 2px 6px;
              }
            }
            
            .product-info {
              flex: 1;
              
              .product-name {
                font-size: 14px;
                font-weight: 500;
                color: #303133;
                margin-bottom: 4px;
                line-height: 1.4;
              }
              
              .product-price {
                font-size: 18px;
                font-weight: 700;
                color: #f56c6c;
                margin-bottom: 4px;
              }
              
              .product-meta {
                display: flex;
                justify-content: space-between;
                align-items: center;
                font-size: 12px;
                color: #909399;
                
                .wants-count {
                  display: flex;
                  align-items: center;
                  gap: 4px;
                }
              }
            }
          }
        }
        
        &.quick-actions {
          .actions-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 16px;
            
            .action-item {
              text-align: center;
              padding: 16px 8px;
              border-radius: 8px;
              cursor: pointer;
              transition: all 0.3s;
              
              &:hover {
                background-color: #f5f7fa;
                transform: translateY(-2px);
              }
              
              .action-icon {
                width: 48px;
                height: 48px;
                border-radius: 12px;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 12px;
                
                i {
                  font-size: 24px;
                  color: white;
                }
              }
              
              .action-label {
                font-size: 12px;
                color: #606266;
                line-height: 1.4;
              }
            }
          }
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 1400px) {
  .dashboard-main {
    flex-direction: column;
    
    .right-sidebar {
      width: 100%;
    }
  }
}

@media (max-width: 992px) {
  .dashboard-main .data-cards .card-row {
    grid-template-columns: repeat(2, 1fr) !important;
  }
}

@media (max-width: 768px) {
  .dashboard-main .data-cards .card-row {
    grid-template-columns: 1fr !important;
  }
  
  .dashboard-header {
    flex-direction: column;
    gap: 16px;
    
    .header-right {
      width: 100%;
      justify-content: flex-end;
    }
  }
}
</style>