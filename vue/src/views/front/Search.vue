<template>
  <div class="search-page">
    <!-- 搜索结果标题 -->
    <div class="search-header">
      <h2 class="search-title">
        搜索结果
        <span class="search-keyword" v-if="keyword">"{{ keyword }}"</span>
      </h2>
      <span class="result-count" v-if="tableData.length > 0">
        找到 {{ tableData.length }} 条相关信息
      </span>
    </div>

    <!-- 结果网格 -->
    <div v-if="tableData.length > 0" class="result-grid">
      <div
        v-for="item in tableData"
        :key="item.id"
        class="result-card"
        @click="$router.push('/front/detail?id=' + item.id)"
      >
        <div class="card-img-wrap">
          <el-image :src="item.img" fit="cover" class="card-img" />
          <div class="card-area-badge">{{ item.area }}</div>
        </div>
        <div class="card-body">
          <h3 class="card-name">{{ item.name }}</h3>
          <p class="card-desc">{{ item.description }}</p>
          <div class="card-footer">
            <span class="card-date">
              <i class="el-icon-time"></i> {{ item.date }}
            </span>
            <span class="card-zan">
              <i class="el-icon-thumb"></i> {{ item.zan || 0 }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty-state">
      <el-empty
        :description="keyword ? `未找到与「${keyword}」相关的信息` : '请输入关键词搜索'"
        image-size="120"
      />
      <el-button type="primary" @click="$router.push('/front/home')">
        返回首页
      </el-button>
    </div>
  </div>
</template>

<script>
import { searchMovie } from '@/api/movie'

export default {
  name: 'Search',
  data() {
    return {
      keyword: '',
      tableData: []
    }
  },
  created() {
    this.fetchData()
  },
  watch: {
    '$route.query.name': { handler: 'fetchData', immediate: true }
  },
  methods: {
    fetchData() {
      this.keyword = this.$route.query.name || ''
      if (!this.keyword) {
        this.tableData = []
        return
      }
      searchMovie({ name: this.keyword })
        .then(res => { this.tableData = res.data || [] })
        .catch(() => {
          this.tableData = []
          this.$message.error('搜索失败')
        })
    }
  }
}
</script>

<style scoped>
.search-page {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-base);
}

.search-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--border-lighter);
  flex-wrap: wrap;
  gap: var(--spacing-base);
}

.search-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.search-keyword {
  color: var(--primary-color);
}

.result-count {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  background: var(--bg-tertiary);
  padding: 3px 10px;
  border-radius: var(--radius-full);
}

/* 复用 Home.vue 的卡片样式 */
.result-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: var(--spacing-lg);
}

.result-card {
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-md);
  overflow: hidden;
  cursor: pointer;
  transition: all var(--transition-cubic);
  background: var(--bg-primary);
}

.result-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-lg);
  border-color: var(--border-primary);
}

.card-img-wrap {
  position: relative;
  height: 160px;
  overflow: hidden;
  background: var(--bg-tertiary);
}

.card-img {
  width: 100%;
  height: 100%;
  transition: transform var(--transition-slow);
}

.result-card:hover .card-img {
  transform: scale(1.08);
}

.card-area-badge {
  position: absolute;
  top: 8px;
  left: 8px;
  background: var(--primary-gradient);
  color: #fff;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  padding: 2px 8px;
  border-radius: var(--radius-full);
}

.card-body {
  padding: var(--spacing-md);
}

.card-name {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-sm);
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  transition: color var(--transition-base);
}

.result-card:hover .card-name { color: var(--primary-color); }

.card-desc {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  margin: 0 0 var(--spacing-base);
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
  padding-top: var(--spacing-sm);
  border-top: 1px solid var(--border-lighter);
}

.card-date, .card-zan {
  display: flex;
  align-items: center;
  gap: 4px;
}

.empty-state {
  text-align: center;
  padding: var(--spacing-4xl);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-lg);
}
</style>
