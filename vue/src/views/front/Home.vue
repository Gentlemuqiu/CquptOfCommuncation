<template>
  <div class="home-container">
    <!-- 英雄横幅 -->
    <div class="hero-banner">
      <div class="hero-content">
        <div class="hero-text">
          <div class="hero-tag">🎓 重庆邮电大学</div>
          <h1 class="hero-title">就业信息中心</h1>
          <p class="hero-desc">汇聚校园招聘 · 实习机会 · 求职指南，助力你迈出职业第一步</p>
          <div class="hero-stats">
            <div class="stat-item">
              <span class="stat-num">{{ total }}</span>
              <span class="stat-label">条信息</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-num">{{ areas.length }}</span>
              <span class="stat-label">大分类</span>
            </div>
          </div>
        </div>
        <div class="hero-features">
          <div class="feature-chip">
            <i class="el-icon-document"></i>
            <span>信息发布</span>
          </div>
          <div class="feature-chip">
            <i class="el-icon-chat-line-round"></i>
            <span>互动评论</span>
          </div>
          <div class="feature-chip">
            <i class="el-icon-star-on"></i>
            <span>收藏管理</span>
          </div>
          <div class="feature-chip">
            <i class="el-icon-search"></i>
            <span>全文检索</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 轮播图 -->
    <div class="section-block">
      <el-carousel
        height="420px"
        indicator-position="inside"
        arrow="always"
        :interval="4000"
      >
        <el-carousel-item v-for="(item, index) in imgs" :key="item.img">
          <a :href="item.link" target="_blank" rel="noopener">
            <div class="carousel-wrap">
              <img :src="item.img" :alt="item.label" class="carousel-img">
              <div class="carousel-overlay">
                <span class="carousel-label">{{ item.label }}</span>
                <p class="carousel-hint">点击了解更多 →</p>
              </div>
            </div>
          </a>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 分类筛选 -->
    <div class="section-block category-block">
      <div class="block-header">
        <h2 class="block-title">
          <i class="el-icon-menu"></i> 信息分类
        </h2>
        <span class="block-desc">选择你感兴趣的类别快速筛选</span>
      </div>
      <div class="category-tabs">
        <button
          v-for="item in areas"
          :key="item.key"
          :class="['cat-tab', { active: item.key === activeArea }]"
          @click="loadArea(1, item.key)"
        >
          <span class="cat-icon">{{ item.icon }}</span>
          <span class="cat-label">{{ item.label }}</span>
        </button>
      </div>
    </div>

    <!-- 信息列表 -->
    <div class="section-block list-block">
      <div class="block-header">
        <h2 class="block-title">
          <i class="el-icon-s-order"></i>
          {{ currentAreaLabel }} · 信息列表
        </h2>
        <span class="result-count" v-if="total > 0">共 {{ total }} 条</span>
      </div>

      <!-- 卡片网格 -->
      <div v-if="tableDataArea.length > 0" class="info-grid">
        <div
          v-for="item in tableDataArea"
          :key="item.id"
          class="info-card"
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
                <i class="el-icon-time"></i>
                {{ item.date }}
              </span>
              <div class="card-actions">
                <span class="action-item">
                  <i class="el-icon-thumb"></i> {{ item.zan || 0 }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <i class="el-icon-document empty-icon"></i>
        <p>该分类暂无信息</p>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrap" v-if="total > pageSize">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[8, 12, 24]"
          :page-size="pageSize"
          layout="prev, pager, next, sizes"
          :total="total"
        />
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

const AREAS = [
  { key: '校园招聘', label: '校园招聘', icon: '🏫' },
  { key: '实习信息', label: '实习信息', icon: '💼' },
  { key: '社会招聘', label: '社会招聘', icon: '🏢' },
  { key: '求职技巧', label: '求职技巧', icon: '💡' },
  { key: '行业动态', label: '行业动态', icon: '📊' },
];

export default {
  name: "Home",
  data() {
    return {
      imgs: [
        { img: require("@/assets/image/bg2.png"), link: 'https://www.cqupt.edu.cn/', label: '重庆邮电大学官网' },
        { img: require("@/assets/image/bg1.png"), link: 'https://software.cqupt.edu.cn/', label: '软件工程学院' },
        { img: require("@/assets/image/bg3.png"), link: 'https://zjc.cqupt.edu.cn/', label: '就业指导中心' }
      ],
      areas: AREAS,
      tableDataArea: [],
      pageNum: 1,
      pageSize: 12,
      total: 0,
      activeArea: AREAS[0].key
    };
  },
  computed: {
    currentAreaLabel() {
      const found = this.areas.find(a => a.key === this.activeArea);
      return found ? found.label : '';
    }
  },
  created() {
    this.loadArea(1, this.activeArea);
  },
  methods: {
    loadArea(pageNum, area) {
      this.activeArea = area;
      this.pageNum = pageNum;
      request.get("/movie/page/area", {
        params: { pageNum, pageSize: this.pageSize, area }
      }).then(res => {
        this.tableDataArea = res.data.records || [];
        this.total = res.data.total || 0;
      });
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.loadArea(1, this.activeArea);
    },
    handleCurrentChange(pageNum) {
      this.loadArea(pageNum, this.activeArea);
    }
  }
};
</script>

<style scoped>
.home-container {
  background: transparent;
  padding-bottom: var(--spacing-xl);
}

/* ── 英雄横幅 ── */
.hero-banner {
  background: var(--nav-bg);
  margin: 0 calc(-1 * var(--spacing-lg));
  padding: var(--spacing-2xl) var(--spacing-xl);
  position: relative;
  overflow: hidden;
  border-radius: var(--radius-xl);
  margin-bottom: var(--spacing-xl);
}

.hero-banner::before {
  content: '';
  position: absolute;
  top: -100px;
  right: -100px;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(15, 118, 110, 0.2) 0%, transparent 70%);
  pointer-events: none;
}

.hero-banner::after {
  content: '';
  position: absolute;
  bottom: -60px;
  left: -60px;
  width: 280px;
  height: 280px;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.12) 0%, transparent 70%);
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--spacing-2xl);
  max-width: 960px;
  margin: 0 auto;
}

.hero-text { flex: 1; }

.hero-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: var(--radius-full);
  color: rgba(255, 255, 255, 0.7);
  font-size: var(--font-size-xs);
  letter-spacing: 0.5px;
  margin-bottom: var(--spacing-base);
}

.hero-title {
  font-size: var(--font-size-4xl);
  font-weight: var(--font-weight-extrabold);
  color: #fff;
  margin: 0 0 var(--spacing-base);
  line-height: 1.15;
  background: linear-gradient(135deg, #fff 0%, rgba(20, 184, 166, 0.85) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -0.5px;
}

.hero-desc {
  font-size: var(--font-size-md);
  color: rgba(255, 255, 255, 0.6);
  margin: 0 0 var(--spacing-lg);
  line-height: var(--line-height-relaxed);
  max-width: 440px;
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.stat-item {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.stat-num {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: var(--primary-lighter);
}

.stat-label {
  font-size: var(--font-size-sm);
  color: rgba(255, 255, 255, 0.5);
}

.stat-divider {
  width: 1px;
  height: 24px;
  background: rgba(255, 255, 255, 0.15);
}

.hero-features {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
  flex-shrink: 0;
}

.feature-chip {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  background: rgba(255, 255, 255, 0.07);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: var(--radius-base);
  color: rgba(255, 255, 255, 0.75);
  font-size: var(--font-size-sm);
  transition: all var(--transition-base);
  cursor: default;
}

.feature-chip:hover {
  background: rgba(255, 255, 255, 0.13);
  color: #fff;
}

/* ── 通用区块 ── */
.section-block {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  margin-bottom: var(--spacing-lg);
  box-shadow: var(--shadow-base);
  border: 1px solid var(--border-lighter);
}

.block-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-xl);
  flex-wrap: wrap;
  gap: var(--spacing-base);
}

.block-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.block-title i {
  color: var(--primary-color);
}

.block-desc {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
}

.result-count {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  background: var(--bg-tertiary);
  padding: 3px 10px;
  border-radius: var(--radius-full);
}

/* ── 轮播图 ── */
.carousel-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: var(--radius-base);
  overflow: hidden;
}

.carousel-wrap:hover .carousel-img {
  transform: scale(1.03);
}

.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-slow);
  display: block;
}

.carousel-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.75) 0%, transparent 100%);
  padding: var(--spacing-3xl) var(--spacing-xl) var(--spacing-xl);
  color: #fff;
}

.carousel-label {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  display: block;
  margin-bottom: 4px;
}

.carousel-hint {
  font-size: var(--font-size-sm);
  margin: 0;
  opacity: 0.75;
}

:deep(.el-carousel__arrow) {
  background: rgba(255,255,255,0.9);
  color: var(--primary-dark);
  width: 40px;
  height: 40px;
  box-shadow: var(--shadow-md);
  transition: all var(--transition-cubic);
}

:deep(.el-carousel__arrow:hover) {
  background: #fff;
  transform: scale(1.1);
}

:deep(.el-carousel__indicator .el-carousel__button) {
  width: 8px;
  height: 8px;
  border-radius: var(--radius-full);
  background: rgba(255,255,255,0.6);
}

:deep(.el-carousel__indicator.is-active .el-carousel__button) {
  width: 24px;
  border-radius: 4px;
  background: var(--primary-lighter);
}

/* ── 分类标签页 ── */
.category-tabs {
  display: flex;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
}

.cat-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px var(--spacing-lg);
  background: var(--bg-secondary);
  border: 1.5px solid var(--border-light);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-cubic);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--text-secondary);
  outline: none;
}

.cat-tab:hover {
  border-color: var(--primary-lighter);
  background: var(--primary-gradient-soft);
  color: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(15, 118, 110, 0.15);
}

.cat-tab.active {
  background: var(--primary-gradient);
  border-color: var(--primary-color);
  color: #fff;
  box-shadow: var(--shadow-primary);
  transform: translateY(-2px);
}

.cat-icon { font-size: 17px; line-height: 1; }

/* ── 信息卡片网格 ── */
.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: var(--spacing-lg);
}

.info-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-md);
  overflow: hidden;
  cursor: pointer;
  transition: all var(--transition-cubic);
  display: flex;
  flex-direction: column;
}

.info-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-lg);
  border-color: var(--border-primary);
}

.card-img-wrap {
  position: relative;
  height: 180px;
  overflow: hidden;
  background: var(--bg-tertiary);
}

.card-img {
  width: 100%;
  height: 100%;
  transition: transform var(--transition-slow);
  display: block;
}

.info-card:hover .card-img {
  transform: scale(1.08);
}

.card-area-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: var(--primary-gradient);
  color: #fff;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  padding: 3px 10px;
  border-radius: var(--radius-full);
  box-shadow: 0 2px 8px rgba(15, 118, 110, 0.3);
}

.card-body {
  padding: var(--spacing-md);
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-name {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-sm);
  line-height: var(--line-height-snug);
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  transition: color var(--transition-base);
}

.info-card:hover .card-name {
  color: var(--primary-color);
}

.card-desc {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  margin: 0 0 var(--spacing-base);
  line-height: var(--line-height-snug);
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  flex: 1;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: var(--spacing-sm);
  border-top: 1px solid var(--border-lighter);
  margin-top: auto;
}

.card-date {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
  display: flex;
  align-items: center;
  gap: 4px;
}

.card-actions {
  display: flex;
  gap: var(--spacing-base);
}

.action-item {
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
  display: flex;
  align-items: center;
  gap: 3px;
}

/* ── 空状态 ── */
.empty-state {
  text-align: center;
  padding: var(--spacing-4xl) var(--spacing-lg);
  color: var(--text-disabled);
}

.empty-icon {
  font-size: 52px;
  display: block;
  margin-bottom: var(--spacing-md);
  opacity: 0.4;
}

.empty-state p {
  font-size: var(--font-size-md);
  margin: 0;
}

/* ── 分页 ── */
.pagination-wrap {
  display: flex;
  justify-content: center;
  padding-top: var(--spacing-xl);
  margin-top: var(--spacing-xl);
  border-top: 1px solid var(--border-lighter);
}

/* ── 响应式 ── */
@media (max-width: 768px) {
  .hero-banner {
    margin: 0;
    padding: var(--spacing-xl) var(--spacing-md);
    border-radius: var(--radius-md);
  }

  .hero-content {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-lg);
  }

  .hero-features {
    flex-direction: row;
    flex-wrap: wrap;
  }

  .hero-title {
    font-size: var(--font-size-3xl);
  }

  .info-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: var(--spacing-base);
  }
}
</style>
