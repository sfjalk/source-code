<template>
  <div class="admin-dashboard">
    <!-- 顶部标题栏 -->
    <div class="dashboard-header">
      <div class="header-left">
        <div class="header-title">
          <h1>后台 / 仪表盘</h1>
        </div>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshData">刷新</el-button>
      </div>
    </div>

    <!-- 主要指标卡片 - 6个卡片 -->
    <div class="metrics-grid">
      <div class="metric-card" v-for="metric in metrics" :key="metric.id" :class="metric.type">
        <div class="metric-icon" :style="{ background: metric.gradient }">
          <i :class="metric.icon"></i>
        </div>
        <div class="metric-content">
          <div class="metric-label">{{ metric.label }}</div>
          <div class="metric-value">{{ metric.value }}</div>
          <div class="metric-trend" :class="metric.trend.type">
            <i :class="metric.trend.icon"></i>
            <span>{{ metric.trend.value }}%</span>
            <span class="trend-text">较上周</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="dashboard-main">
      <!-- 左侧图表区域 -->
      <div class="chart-section">
        <div class="chart-header">
          <div class="chart-title">数据图</div>
          <div class="chart-controls">
            <el-select v-model="chartRange" placeholder="期限" size="small" class="time-select">
              <el-option label="最近365天" value="365days"></el-option>
              <el-option label="最近30天" value="30days"></el-option>
              <el-option label="最近7天" value="7days"></el-option>
            </el-select>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              size="small"
              :picker-options="pickerOptions"
              class="date-picker"
              @change="handleDateRangeChange"
            ></el-date-picker>
          </div>
        </div>
        <div class="chart-container">
          <div ref="trendChart" class="chart-area"></div>
        </div>
        <div class="chart-legend">
          <div class="legend-item">
            <span class="legend-color" style="background: #409EFF;"></span>
            <span>商品上架数量</span>
          </div>
          <div class="legend-item">
            <span class="legend-color" style="background: #67C23A;"></span>
            <span>用户活跃度</span>
          </div>
        </div>
      </div>

      <!-- 右侧商品走马灯 -->
      <div class="sidebar-section">
        <div class="sidebar-header">
          <div class="sidebar-title">最新上架商品</div>
        </div>
        
        <!-- Element UI 走马灯组件 -->
        <el-carousel 
          v-if="latestProducts.length > 0"
          :interval="4000" 
          height="320px" 
          indicator-position="outside"
          arrow="always"
          class="product-carousel"
        >
          <!-- 每页显示2个商品 -->
          <el-carousel-item 
            v-for="(page, pageIndex) in productPages" 
            :key="pageIndex"
          >
            <div class="carousel-page">
              <div 
                class="product-item" 
                v-for="product in page" 
                :key="product.id"
                @click="viewProduct(product)"
              >
                <div class="product-image">
                  <img :src="product.image" :alt="product.name">
                  <div class="product-tags">
                    <el-tag 
                      v-if="product.canBargain" 
                      type="warning" 
                      size="mini"
                      class="bargain-tag"
                    >
                      砍价
                    </el-tag>
                    <el-tag 
                      v-if="product.isNew" 
                      type="success" 
                      size="mini"
                      class="new-tag"
                    >
                      新品
                    </el-tag>
                  </div>
                </div>
                <div class="product-info">
                  <div class="product-brand">{{ product.brand }}</div>
                  <div class="product-name">{{ product.name }}</div>
                  <div class="product-model">{{ product.model }}</div>
                  <div class="product-price">
                    <span class="price-symbol">¥</span>
                    <span class="price-value">{{ product.price }}</span>
                  </div>
                  <div class="product-stats">
                    <div class="wants-count">
                      <i class="el-icon-star-off"></i>
                      <span>{{ product.wants }}人想要</span>
                    </div>
                    <div class="view-count">
                      <i class="el-icon-view"></i>
                      <span>{{ product.views }}次浏览</span>
                    </div>
                  </div>
                  <div class="product-time">
                    <i class="el-icon-time"></i>
                    <span>{{ product.time }}</span>
                  </div>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
        
        <!-- 如果没有商品时的占位 -->
        <div v-else class="empty-products">
          <i class="el-icon-goods"></i>
          <p>暂无上架商品</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'AdminDashboard',
  data() {
    return {
      // 图表相关
      chartRange: '365days',
      dateRange: [],
      trendChart: null,
      
      // 日期选择器快捷选项
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      
      // 指标数据 - 6个卡片
      metrics: [
        {
          id: 1,
          type: 'user',
          label: '用户数',
          value: 13,
          icon: 'el-icon-user',
          gradient: 'linear-gradient(135deg, #409EFF, #337ecc)',
          trend: { type: 'up', value: 12, icon: 'el-icon-top' }
        },
        {
          id: 2,
          type: 'product',
          label: '收录商品',
          value: 18,
          icon: 'el-icon-goods',
          gradient: 'linear-gradient(135deg, #67C23A, #529b2e)',
          trend: { type: 'up', value: 8, icon: 'el-icon-top' }
        },
        {
          id: 3,
          type: 'order',
          label: '订单数',
          value: 1,
          icon: 'el-icon-s-order',
          gradient: 'linear-gradient(135deg, #E6A23C, #c68a34)',
          trend: { type: 'up', value: 5, icon: 'el-icon-top' }
        },
        {
          id: 4,
          type: 'message',
          label: '消息通知',
          value: 5,
          icon: 'el-icon-message',
          gradient: 'linear-gradient(135deg, #F56C6C, #c45656)',
          trend: { type: 'up', value: 25, icon: 'el-icon-top' }
        },
        {
          id: 5,
          type: 'interaction',
          label: '互动数据',
          value: 22,
          icon: 'el-icon-chat-line-round',
          gradient: 'linear-gradient(135deg, #337ecc, #2a6495)',
          trend: { type: 'up', value: 15, icon: 'el-icon-top' }
        },
        {
          id: 6,
          type: 'comment',
          label: '商品评论',
          value: 4,
          icon: 'el-icon-chat-dot-round',
          gradient: 'linear-gradient(135deg, #529b2e, #3f7a23)',
          trend: { type: 'up', value: 8, icon: 'el-icon-top' }
        }
      ],
      
      // 最新商品数据
      latestProducts: [
        {
          id: 1,
          name: 'vivo X100 测试商品',
          brand: 'vivo | 聚时联合研发',
          model: 'X100',
          price: '1399',
          wants: 0,
          views: 156,
          time: '2024-01-15',
          canBargain: true,
          isNew: true,
          image: 'https://via.placeholder.com/280x180/FF6B6B/FFFFFF?text=Vivo+X100'
        },
        {
          id: 2,
          name: '智能手表1',
          brand: '智能穿戴',
          model: 'Watch Pro',
          price: '222',
          wants: 0,
          views: 89,
          time: '2024-01-14',
          canBargain: true,
          isNew: true,
          image: 'https://via.placeholder.com/280x180/4ECDC4/FFFFFF?text=Smart+Watch'
        },
        {
          id: 3,
          name: '无线蓝牙耳机',
          brand: '音频设备',
          model: 'Air Pro',
          price: '299',
          wants: 12,
          views: 234,
          time: '2024-01-13',
          canBargain: false,
          isNew: false,
          image: 'https://via.placeholder.com/280x180/FFE66D/FFFFFF?text=AirPods'
        },
        {
          id: 4,
          name: '笔记本电脑',
          brand: 'ThinkPad',
          model: 'X1 Carbon',
          price: '5999',
          wants: 8,
          views: 345,
          time: '2024-01-12',
          canBargain: true,
          isNew: false,
          image: 'https://via.placeholder.com/280x180/1A535C/FFFFFF?text=ThinkPad'
        },
        {
          id: 5,
          name: '运动相机',
          brand: 'GoPro',
          model: 'Hero 11',
          price: '899',
          wants: 3,
          views: 123,
          time: '2024-01-11',
          canBargain: false,
          isNew: true,
          image: 'https://via.placeholder.com/280x180/4ECDC4/FFFFFF?text=GoPro'
        },
        {
          id: 6,
          name: '游戏主机',
          brand: 'Sony',
          model: 'PlayStation 5',
          price: '3899',
          wants: 45,
          views: 567,
          time: '2024-01-10',
          canBargain: true,
          isNew: false,
          image: 'https://via.placeholder.com/280x180/FF6B6B/FFFFFF?text=PS5'
        }
      ]
    }
  },
  
  computed: {
    // 将商品分页，每页2个
    productPages() {
      const pages = []
      const itemsPerPage = 2 // 每页显示2个商品
      
      for (let i = 0; i < this.latestProducts.length; i += itemsPerPage) {
        pages.push(this.latestProducts.slice(i, i + itemsPerPage))
      }
      
      return pages
    }
  },
  
  mounted() {
    this.initChart()
    this.fetchDashboardData()
  },
  
  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  
  methods: {
    // 初始化图表
    initChart() {
      const chartDom = this.$refs.trendChart
      if (!chartDom) return
      
      this.trendChart = echarts.init(chartDom)
      this.updateChart()
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },
    
    // 更新图表数据
    updateChart() {
      if (!this.trendChart) return
      
      // 根据选择的时间范围生成数据
      let days = 30
      if (this.chartRange === '365days') days = 365
      else if (this.chartRange === '7days') days = 7
      
      // 如果选择了自定义日期范围
      if (this.dateRange && this.dateRange.length === 2) {
        const startDate = new Date(this.dateRange[0])
        const endDate = new Date(this.dateRange[1])
        const diffTime = Math.abs(endDate - startDate)
        days = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      }
      
      // 生成模拟数据
      const dates = []
      const productCounts = []
      const userActivity = []
      
      for (let i = days; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        
        // 格式化为"月/日"
        const month = date.getMonth() + 1
        const day = date.getDate()
        dates.push(`${month}/${day}`)
        
        // 商品上架数量（模拟）
        const baseProductCount = 50
        const productVariance = Math.sin(i * 0.3) * 20
        const productRandom = Math.random() * 15
        productCounts.push(Math.round(baseProductCount + productVariance + productRandom))
        
        // 用户活跃度（模拟）
        const baseUserActivity = 30
        const userVariance = Math.cos(i * 0.2) * 15
        const userRandom = Math.random() * 10
        userActivity.push(Math.round(baseUserActivity + userVariance + userRandom))
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
          },
          formatter: function(params) {
            let result = `${params[0].axisValue}<br/>`
            params.forEach(item => {
              const color = item.color
              const marker = `<span style="display:inline-block;margin-right:5px;border-radius:50%;width:10px;height:10px;background-color:${color};"></span>`
              result += `${marker} ${item.seriesName}: ${item.value}<br/>`
            })
            return result
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '12%',
          top: '10%',
          containLabel: true
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
            data: productCounts
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
            data: userActivity
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },
    
    // 刷新数据
    refreshData() {
      this.$message({
        message: '正在刷新数据...',
        type: 'info'
      })
      
      // 模拟API请求延迟
      setTimeout(() => {
        // 更新指标数据
        this.metrics.forEach(metric => {
          const change = Math.floor(Math.random() * 10) - 3 // -3到+6的随机变化
          const newValue = Math.max(0, metric.value + change)
          
          // 随机趋势
          const trendValue = Math.floor(Math.random() * 30)
          const trendType = Math.random() > 0.3 ? 'up' : 'down'
          
          metric.value = newValue
          metric.trend = {
            type: trendType,
            value: trendValue,
            icon: trendType === 'up' ? 'el-icon-top' : 'el-icon-bottom'
          }
        })
        
        // 更新图表
        this.updateChart()
        
        this.$message({
          message: '数据刷新成功',
          type: 'success',
          duration: 1000
        })
      }, 800)
    },
    
    // 查看商品详情
    viewProduct(product) {
      this.$message({
        message: `查看商品: ${product.name}`,
        type: 'info'
      })
      // 实际应用中这里可以跳转到商品详情页
      // this.$router.push(`/product/${product.id}`)
    },
    
    // 处理日期范围变化
    handleDateRangeChange() {
      if (this.dateRange && this.dateRange.length === 2) {
        this.updateChart()
      }
    },
    
    // 处理窗口大小变化
    handleResize() {
      if (this.trendChart) {
        this.trendChart.resize()
      }
    },
    
    // 获取仪表盘数据
    fetchDashboardData() {
      // 这里应该调用后端API获取数据
      // 暂时使用模拟数据
      this.$axios.get('/api/dashboard/data')
        .then(response => {
          if (response.data.code === 200) {
            // 更新数据
            const data = response.data.data
            this.metrics = data.metrics
            this.latestProducts = data.latestProducts
            this.updateChart()
          }
        })
        .catch(error => {
          console.error('获取仪表盘数据失败:', error)
          this.$message.error('加载数据失败')
        })
    }
  },
  
  watch: {
    chartRange() {
      this.updateChart()
    }
  }
}
</script>

<style scoped lang="scss">
.admin-dashboard {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
  
  .dashboard-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 30px;
    
    .header-left {
      .header-title {
        h1 {
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
    }
    
    .header-right {
      .el-button {
        font-weight: 500;
      }
    }
  }
  
  .metrics-grid {
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 20px;
    margin-bottom: 30px;
    
    .metric-card {
      background: white;
      border-radius: 12px;
      padding: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      border: 1px solid #ebeef5;
      transition: all 0.3s ease;
      cursor: pointer;
      
      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
      }
      
      .metric-icon {
        width: 50px;
        height: 50px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 15px;
        
        i {
          font-size: 24px;
          color: white;
        }
      }
      
      .metric-content {
        .metric-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 8px;
        }
        
        .metric-value {
          font-size: 28px;
          font-weight: 700;
          color: #303133;
          margin-bottom: 4px;
        }
        
        .metric-trend {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 12px;
          
          i {
            font-size: 12px;
          }
          
          &.up {
            color: #67c23a;
          }
          
          &.down {
            color: #f56c6c;
          }
          
          .trend-text {
            color: #909399;
            margin-left: 4px;
          }
        }
      }
    }
  }
  
  .dashboard-main {
    display: flex;
    gap: 20px;
    margin-bottom: 30px;
    
    .chart-section {
      flex: 1;
      background: white;
      border-radius: 12px;
      padding: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      border: 1px solid #ebeef5;
      
      .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        
        .chart-title {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
        
        .chart-controls {
          display: flex;
          gap: 10px;
          
          .time-select {
            width: 120px;
          }
          
          .date-picker {
            width: 240px;
          }
        }
      }
      
      .chart-container {
        height: 300px;
        
        .chart-area {
          width: 100%;
          height: 100%;
        }
      }
      
      .chart-legend {
        margin-top: 20px;
        padding-top: 16px;
        border-top: 1px solid #ebeef5;
        display: flex;
        justify-content: center;
        gap: 30px;
        
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
    
    .sidebar-section {
      width: 380px;
      background: white;
      border-radius: 12px;
      padding: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      border: 1px solid #ebeef5;
      
      .sidebar-header {
        margin-bottom: 20px;
        
        .sidebar-title {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }
      
      // Element UI Carousel 自定义样式
      .product-carousel {
        ::v-deep .el-carousel__container {
          height: 320px !important;
        }
        
        ::v-deep .el-carousel__indicators--outside {
          margin-top: 15px;
        }
        
        ::v-deep .el-carousel__indicator {
          padding: 5px 3px;
          
          .el-carousel__button {
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background-color: #c0c4cc;
            opacity: 0.6;
            transition: all 0.3s;
            
            &:hover {
              background-color: #409EFF;
            }
          }
          
          &.is-active .el-carousel__button {
            width: 20px;
            border-radius: 4px;
            background-color: #409EFF;
            opacity: 1;
          }
        }
        
        ::v-deep .el-carousel__arrow {
          background-color: rgba(64, 158, 255, 0.8);
          color: white;
          
          &:hover {
            background-color: #409EFF;
          }
          
          i {
            font-size: 18px;
          }
        }
        
        .carousel-page {
          display: flex;
          flex-direction: column;
          gap: 15px;
          padding: 5px;
          
          .product-item {
            display: flex;
            padding: 12px;
            border-radius: 8px;
            border: 1px solid #ebeef5;
            background: #fafafa;
            cursor: pointer;
            transition: all 0.3s;
            
            &:hover {
              transform: translateY(-3px);
              box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
              border-color: #409EFF;
            }
            
            .product-image {
              position: relative;
              width: 80px;
              height: 80px;
              margin-right: 12px;
              flex-shrink: 0;
              
              img {
                width: 100%;
                height: 100%;
                border-radius: 8px;
                object-fit: cover;
                border: 1px solid #e4e7ed;
              }
              
              .product-tags {
                position: absolute;
                top: -8px;
                right: -8px;
                display: flex;
                flex-direction: column;
                gap: 5px;
                
                .bargain-tag, .new-tag {
                  font-size: 10px;
                  padding: 2px 6px;
                  height: auto;
                  line-height: 1.2;
                }
              }
            }
            
            .product-info {
              flex: 1;
              min-width: 0;
              
              .product-brand {
                font-size: 12px;
                color: #909399;
                margin-bottom: 4px;
              }
              
              .product-name {
                font-size: 16px;
                font-weight: 600;
                color: #303133;
                margin-bottom: 4px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
              }
              
              .product-model {
                font-size: 14px;
                color: #606266;
                margin-bottom: 8px;
              }
              
              .product-price {
                margin-bottom: 8px;
                
                .price-symbol {
                  font-size: 14px;
                  color: #f56c6c;
                }
                
                .price-value {
                  font-size: 20px;
                  font-weight: 700;
                  color: #f56c6c;
                }
              }
              
              .product-stats {
                display: flex;
                gap: 15px;
                margin-bottom: 8px;
                
                .wants-count, .view-count {
                  display: flex;
                  align-items: center;
                  gap: 4px;
                  font-size: 12px;
                  color: #909399;
                  
                  i {
                    font-size: 12px;
                  }
                }
              }
              
              .product-time {
                display: flex;
                align-items: center;
                gap: 4px;
                font-size: 12px;
                color: #c0c4cc;
                
                i {
                  font-size: 12px;
                }
              }
            }
          }
        }
      }
      
      .empty-products {
        text-align: center;
        padding: 60px 20px;
        
        i {
          font-size: 48px;
          color: #c0c4cc;
          margin-bottom: 15px;
        }
        
        p {
          color: #909399;
          margin-bottom: 20px;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 1400px) {
  .metrics-grid {
    grid-template-columns: repeat(3, 1fr) !important;
  }
  
  .dashboard-main {
    flex-direction: column;
    
    .sidebar-section {
      width: 100% !important;
      
      .product-carousel ::v-deep .el-carousel__container {
        height: 300px !important;
      }
      
      .carousel-page {
        flex-direction: row !important;
        justify-content: space-between;
        
        .product-item {
          width: 48% !important;
        }
      }
    }
  }
}

@media (max-width: 992px) {
  .metrics-grid {
    grid-template-columns: repeat(2, 1fr) !important;
  }
  
  .chart-header .chart-controls {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px !important;
    
    .time-select, .date-picker {
      width: 100% !important;
    }
  }
}

@media (max-width: 768px) {
  .dashboard-header {
    flex-direction: column;
    gap: 16px;
  }
  
  .metrics-grid {
    grid-template-columns: 1fr !important;
  }
  
  .dashboard-main .sidebar-section {
    .carousel-page {
      flex-direction: column !important;
      
      .product-item {
        width: 100% !important;
      }
    }
  }
}
</style>