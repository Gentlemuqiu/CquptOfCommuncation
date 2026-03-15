<template>
  <div class="home-container">

    <!-- ════════════════ 英雄横幅 ════════════════ -->
    <div class="hero-banner">
      <div class="hero-inner">
        <div class="hero-tag">
          <el-icon><School /></el-icon>
          重庆邮电大学
        </div>
        <h1 class="hero-title">就业信息中心</h1>
        <p class="hero-desc">汇聚校园招聘 · 实习机会 · 求职指南，助力你迈出职业第一步</p>

        <!-- 统计与分类 -->
        <div class="hero-stats-wrap">
          <div class="hero-stats">
            <div class="stat-item">
              <el-icon class="stat-icon"><Document /></el-icon>
              <span class="stat-num">{{ totalAll }}</span>
              <span class="stat-label">条信息</span>
            </div>
            <div class="stat-item">
              <el-icon class="stat-icon"><Grid /></el-icon>
              <span class="stat-num">{{ areas.length }}</span>
              <span class="stat-label">大分类</span>
            </div>
          </div>
          <div class="hero-tags">
            <span v-for="item in areas" :key="item.key" class="hero-tag-item">
              <span class="hero-tag-emoji">{{ item.icon }}</span>
              {{ item.label }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- ════════════════ 轮播图 ════════════════ -->
    <div class="section-block carousel-block">
      <el-carousel height="400px" indicator-position="inside" arrow="always" :interval="4000">
        <el-carousel-item v-for="item in imgs" :key="item.img">
          <a :href="item.link" target="_blank" rel="noopener">
            <div class="carousel-wrap">
              <img :src="item.img" :alt="item.label" class="carousel-img" />
              <div class="carousel-overlay">
                <div class="carousel-badge">外部链接</div>
                <span class="carousel-label">{{ item.label }}</span>
                <p class="carousel-hint">
                  <el-icon><Link /></el-icon>
                  点击访问官网 →
                </p>
              </div>
            </div>
          </a>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- ════════════════ 分类选择 ════════════════ -->
    <div class="section-block category-block">
      <div class="section-header">
        <div class="header-left">
          <div class="section-accent"></div>
          <div>
            <h2 class="section-title">信息分类</h2>
            <p class="section-sub">选择你感兴趣的类别快速浏览</p>
          </div>
        </div>
      </div>

      <div class="category-grid">
        <button
          v-for="item in areas"
          :key="item.key"
          :class="['cat-card', { active: item.key === activeArea }]"
          @click="loadArea(1, item.key)"
        >
          <span class="cat-emoji">{{ item.icon }}</span>
          <span class="cat-name">{{ item.label }}</span>
          <div class="cat-check" v-if="item.key === activeArea">
            <el-icon><Check /></el-icon>
          </div>
        </button>
      </div>
    </div>

    <!-- ════════════════ 信息列表 ════════════════ -->
    <div class="section-block list-block">
      <div class="section-header">
        <div class="header-left">
          <div class="section-accent"></div>
          <div>
            <h2 class="section-title">
              <span class="area-dot">{{ currentAreaEmoji }}</span>
              {{ currentAreaLabel }}
            </h2>
            <p class="section-sub">精选就业信息，助力职业发展</p>
          </div>
        </div>
        <div class="header-right" v-if="total > 0">
          <div class="count-badge">
            <el-icon><Document /></el-icon>
            共 {{ total }} 条
          </div>
        </div>
      </div>

      <!-- 骨架屏 -->
      <div v-if="listLoading" class="skeleton-grid">
        <div v-for="n in skeletonCount" :key="n" class="skeleton-card">
          <div class="sk-img"></div>
          <div class="sk-body">
            <div class="sk-line sk-t1"></div>
            <div class="sk-line sk-t2"></div>
            <div class="sk-line sk-t3"></div>
          </div>
        </div>
      </div>

      <!-- 卡片网格 -->
      <div v-else-if="tableDataArea.length > 0" class="info-grid">
        <div
          v-for="item in tableDataArea"
          :key="item.id"
          class="info-card"
          @click="$router.push('/front/detail?id=' + item.id)"
        >
          <!-- 封面图 -->
          <div class="card-img-wrap">
            <el-image :src="item.img" fit="cover" class="card-img">
              <template #error>
                <div class="img-fallback">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>

            <!-- 分类徽章 -->
            <div class="card-area-badge">{{ item.area }}</div>

            <!-- 热门徽章 -->
            <div class="hot-badge" v-if="(item.zan || 0) >= 3">
              🔥 热门
            </div>

            <!-- 悬浮遮罩 -->
            <div class="card-hover-mask">
              <div class="mask-view-btn">
                <el-icon><View /></el-icon>
                查看详情
              </div>
            </div>
          </div>

          <!-- 正文 -->
          <div class="card-body">
            <h3 class="card-name" :title="item.name">{{ item.name }}</h3>
            <p class="card-desc">{{ item.description }}</p>
            <div class="card-footer">
              <span class="card-date">
                <el-icon><Calendar /></el-icon>
                {{ item.date }}
              </span>
              <span class="card-zan" :class="{ active: (item.zan || 0) > 0 }">
                <el-icon><Sunny /></el-icon>
                {{ item.zan || 0 }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <div class="empty-illus">
          <div class="empty-circle"></div>
          <el-icon class="empty-icon"><Document /></el-icon>
        </div>
        <p class="empty-title">该分类暂无信息</p>
        <p class="empty-hint">换个分类看看，或发布第一条信息吧</p>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrap" v-if="total > pageSize">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[8, 12, 24]"
          :page-size="pageSize"
          layout="prev, pager, next, sizes, total"
          :total="total"
          background
        />
      </div>
    </div>

  </div>
</template>

<script>
import { getMoviePage, getMoviePageByArea } from '@/api/movie'
import { INFO_AREAS } from '@/config/navigation'
import {
  School, Document, Grid,
  Link, Check, Picture, View, Calendar, Sunny
} from '@element-plus/icons-vue'

export default {
  name: "Home",
  components: {
    School, Document, Grid,
    Link, Check, Picture, View, Calendar, Sunny
  },
  data() {
    return {
      imgs: [
        { img: require("@/assets/image/bg2.png"), link: 'https://www.cqupt.edu.cn/', label: '重庆邮电大学官网' },
        { img: require("@/assets/image/bg1.png"), link: 'https://software.cqupt.edu.cn/', label: '软件工程学院' },
        { img: require("@/assets/image/bg3.png"), link: 'https://zjc.cqupt.edu.cn/', label: '就业指导中心' }
      ],
      areas: INFO_AREAS,
      tableDataArea: [],
      pageNum: 1,
      pageSize: 12,
      total: 0,
      totalAll: 0,  // 全站信息总数，用于英雄区展示
      activeArea: INFO_AREAS[0].key,
      listLoading: false
    };
  },
  computed: {
    currentAreaLabel() {
      const found = this.areas.find(a => a.key === this.activeArea);
      return found ? found.label : '';
    },
    currentAreaEmoji() {
      const found = this.areas.find(a => a.key === this.activeArea);
      return found ? found.icon : '';
    },
    skeletonCount() {
      // 骨架屏最多显示 8 个，避免撑出白色大空白
      return Math.min(this.pageSize, 8);
    }
  },
  created() {
    this.loadTotalAll();
    this.loadArea(1, this.activeArea);
  },
  methods: {
    loadTotalAll() {
      getMoviePage({ pageNum: 1, pageSize: 1 }).then(res => {
        this.totalAll = res.data.total != null ? res.data.total : 0
      })
    },
    loadArea(pageNum, area) {
      this.activeArea = area
      this.pageNum = pageNum
      this.listLoading = true
      getMoviePageByArea({ pageNum, pageSize: this.pageSize, area })
        .then(res => {
          this.tableDataArea = res.data.records || []
          this.total = res.data.total || 0
        })
        .finally(() => { this.listLoading = false })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.loadArea(1, this.activeArea);
    },
    handleCurrentChange(pageNum) {
      this.loadArea(pageNum, this.activeArea);
    },
  }
};
</script>

<style scoped>
/* ════════════════════════════════════════
   页面基础
════════════════════════════════════════ */
.home-container {
  padding-bottom: var(--spacing-2xl);
}

/* ════════════════════════════════════════
   英雄横幅 - 清爽现代风格
════════════════════════════════════════ */
.hero-banner {
  background: linear-gradient(160deg, #f0fdfa 0%, #ecfeff 35%, #f8fafc 100%);
  border-radius: var(--radius-xl);
  margin-bottom: var(--spacing-lg);
  position: relative;
  overflow: hidden;
  padding: var(--spacing-2xl);
  border: 1px solid var(--border-lighter);
  box-shadow: 0 1px 3px rgba(15, 118, 110, 0.06);
}

.hero-inner {
  position: relative;
  z-index: 1;
  max-width: 720px;
  margin: 0 auto;
  text-align: center;
}

.hero-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 16px;
  background: rgba(15, 118, 110, 0.1);
  border: 1px solid rgba(15, 118, 110, 0.2);
  border-radius: var(--radius-full);
  color: var(--primary-dark);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  letter-spacing: 0.5px;
  margin-bottom: var(--spacing-md);
}

.hero-title {
  font-size: var(--font-size-4xl);
  font-weight: var(--font-weight-extrabold);
  margin: 0 0 var(--spacing-base);
  line-height: 1.15;
  color: var(--text-primary);
  letter-spacing: -0.5px;
}

.hero-title::after {
  content: '';
  display: block;
  width: 48px;
  height: 4px;
  margin: var(--spacing-sm) auto 0;
  background: var(--primary-gradient);
  border-radius: var(--radius-full);
}

.hero-desc {
  font-size: var(--font-size-md);
  color: var(--text-secondary);
  margin: 0 0 var(--spacing-xl);
  line-height: var(--line-height-relaxed);
}

/* 统计与分类 */
.hero-stats-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-lg);
}

.hero-stats {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-base);
  flex-wrap: wrap;
  justify-content: center;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-full);
  padding: 10px 20px;
  box-shadow: var(--shadow-xs);
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
}

.stat-item:hover {
  border-color: rgba(15, 118, 110, 0.35);
  box-shadow: 0 2px 12px rgba(15, 118, 110, 0.12);
}

.stat-icon {
  color: var(--primary-lighter);
  font-size: 16px;
  flex-shrink: 0;
}

.stat-num {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-bold);
  color: var(--primary-dark);
}

.stat-label {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
}

/* 分类标签行 */
.hero-tags {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: var(--spacing-sm);
}

.hero-tag-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 5px 12px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-full);
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
  transition: background var(--transition-fast), color var(--transition-fast), border-color var(--transition-fast);
}

.hero-tag-item:hover {
  background: var(--primary-gradient-soft);
  color: var(--primary-dark);
  border-color: rgba(15, 118, 110, 0.25);
}

.hero-tag-emoji {
  font-size: 12px;
  line-height: 1;
}

/* ════════════════════════════════════════
   通用区块
════════════════════════════════════════ */
.section-block {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl) var(--spacing-2xl);
  margin-bottom: var(--spacing-lg);
  box-shadow: var(--shadow-base);
  border: 1px solid var(--border-lighter);
  transition: box-shadow var(--transition-base);
}

.section-block:hover {
  box-shadow: var(--shadow-md);
}

/* 区块标题 */
.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-xl);
  flex-wrap: wrap;
  gap: var(--spacing-base);
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
}

.section-accent {
  width: 4px;
  height: 28px;
  border-radius: var(--radius-full);
  background: var(--primary-gradient);
  flex-shrink: 0;
}

.section-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0 0 2px;
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.area-dot { font-size: 18px; }

.section-sub {
  font-size: var(--font-size-sm);
  color: var(--text-disabled);
  margin: 0;
}

.count-badge {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  background: var(--bg-tertiary);
  padding: 4px 12px;
  border-radius: var(--radius-full);
}

/* ════════════════════════════════════════
   轮播图
════════════════════════════════════════ */
.carousel-block { padding: var(--spacing-lg); }

.carousel-wrap {
  position: relative;
  width: 100%; height: 100%;
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.carousel-wrap:hover .carousel-img { transform: scale(1.03); }

.carousel-img {
  width: 100%; height: 100%;
  object-fit: cover;
  transition: transform var(--transition-slow);
  display: block;
}

.carousel-overlay {
  position: absolute;
  bottom: 0; left: 0; right: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.78) 0%, rgba(0,0,0,0.1) 60%, transparent 100%);
  padding: var(--spacing-3xl) var(--spacing-xl) var(--spacing-xl);
  color: #fff;
}

.carousel-badge {
  display: inline-block;
  font-size: 11px;
  font-weight: var(--font-weight-semibold);
  background: rgba(255,255,255,0.18);
  border: 1px solid rgba(255,255,255,0.25);
  border-radius: var(--radius-full);
  padding: 2px 10px;
  margin-bottom: 8px;
  letter-spacing: 0.05em;
}

.carousel-label {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  display: block;
  margin-bottom: 6px;
  text-shadow: 0 2px 8px rgba(0,0,0,0.3);
}

.carousel-hint {
  margin: 0;
  font-size: var(--font-size-sm);
  opacity: 0.7;
  display: flex;
  align-items: center;
  gap: 5px;
}

:deep(.el-carousel__arrow) {
  background: rgba(255,255,255,0.92);
  color: var(--primary-dark);
  width: 40px; height: 40px;
  box-shadow: var(--shadow-md);
  transition: all var(--transition-cubic);
}

:deep(.el-carousel__arrow:hover) {
  background: #fff;
  transform: scale(1.1);
}

:deep(.el-carousel__indicator .el-carousel__button) {
  width: 8px; height: 8px;
  border-radius: var(--radius-full);
  background: rgba(255,255,255,0.55);
}

:deep(.el-carousel__indicator.is-active .el-carousel__button) {
  width: 28px;
  border-radius: 4px;
  background: var(--primary-lighter);
}

/* ════════════════════════════════════════
   分类卡片
════════════════════════════════════════ */
.category-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: var(--spacing-base);
}

.cat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-lg) var(--spacing-base);
  background: var(--bg-secondary);
  border: 1.5px solid var(--border-light);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-cubic);
  outline: none;
  position: relative;
  overflow: hidden;
}

.cat-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: var(--primary-gradient);
  opacity: 0;
  transition: opacity var(--transition-base);
}

.cat-card:hover {
  border-color: var(--primary-lighter);
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(15,118,110,0.15);
}

.cat-card:hover::before { opacity: 0.06; }

.cat-card.active {
  border-color: var(--primary-color);
  background: var(--primary-gradient-soft);
  box-shadow: 0 4px 16px rgba(15,118,110,0.2);
  transform: translateY(-3px);
}

.cat-card.active::before { opacity: 0.08; }

.cat-emoji {
  font-size: 28px;
  line-height: 1;
  position: relative;
  z-index: 1;
  transition: transform var(--transition-spring);
}

.cat-card:hover .cat-emoji,
.cat-card.active .cat-emoji {
  transform: scale(1.15);
}

.cat-name {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--text-secondary);
  position: relative;
  z-index: 1;
  transition: color var(--transition-base);
}

.cat-card:hover .cat-name { color: var(--primary-dark); }
.cat-card.active .cat-name { color: var(--primary-dark); }

.cat-check {
  position: absolute;
  top: 8px; right: 8px;
  width: 18px; height: 18px;
  border-radius: 50%;
  background: var(--primary-gradient);
  display: flex; align-items: center; justify-content: center;
  color: #fff;
  font-size: 10px;
  z-index: 1;
}

/* ════════════════════════════════════════
   骨架屏
════════════════════════════════════════ */
.skeleton-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: var(--spacing-lg);
}

.skeleton-card {
  border-radius: var(--radius-md);
  overflow: hidden;
  border: 1px solid var(--border-lighter);
}

.sk-img {
  height: 190px;
  background: linear-gradient(90deg, var(--bg-tertiary) 25%, var(--bg-secondary) 50%, var(--bg-tertiary) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.6s infinite;
}

.sk-body { padding: var(--spacing-md); }

.sk-line {
  border-radius: var(--radius-full);
  background: linear-gradient(90deg, var(--bg-tertiary) 25%, var(--bg-secondary) 50%, var(--bg-tertiary) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.6s infinite;
  margin-bottom: var(--spacing-sm);
}

.sk-t1 { height: 14px; }
.sk-t2 { height: 12px; width: 80%; }
.sk-t3 { height: 10px; width: 50%; }

@keyframes shimmer {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* ════════════════════════════════════════
   信息卡片
════════════════════════════════════════ */
.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: var(--spacing-lg);
}

.info-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-lg);
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

/* 图片区 */
.card-img-wrap {
  position: relative;
  height: 190px;
  overflow: hidden;
  background: var(--bg-tertiary);
}

.card-img {
  width: 100%; height: 100%;
  transition: transform var(--transition-slow);
  display: block;
}

.info-card:hover .card-img { transform: scale(1.08); }

.img-fallback {
  width: 100%; height: 100%;
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, var(--bg-tertiary) 0%, var(--bg-secondary) 100%);
  color: var(--text-disabled);
  font-size: 36px;
}

/* 分类徽章 */
.card-area-badge {
  position: absolute;
  top: 10px; left: 10px;
  background: var(--primary-gradient);
  color: #fff;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  padding: 3px 10px;
  border-radius: var(--radius-full);
  box-shadow: 0 2px 8px rgba(15,118,110,0.3);
  z-index: 2;
}

/* 热门徽章 */
.hot-badge {
  position: absolute;
  top: 10px; right: 10px;
  background: linear-gradient(135deg, #f97316, #ef4444);
  color: #fff;
  font-size: 11px;
  font-weight: var(--font-weight-semibold);
  padding: 3px 9px;
  border-radius: var(--radius-full);
  box-shadow: 0 2px 8px rgba(239,68,68,0.3);
  z-index: 2;
}

/* 悬浮遮罩 */
.card-hover-mask {
  position: absolute;
  inset: 0;
  background: rgba(15,118,110,0.68);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity var(--transition-base);
  backdrop-filter: blur(2px);
  z-index: 3;
}

.info-card:hover .card-hover-mask { opacity: 1; }

.mask-view-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #fff;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  background: rgba(255,255,255,0.2);
  border: 1px solid rgba(255,255,255,0.35);
  border-radius: var(--radius-full);
  padding: 8px 20px;
}

/* 正文 */
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

.info-card:hover .card-name { color: var(--primary-color); }

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

.card-zan {
  display: flex;
  align-items: center;
  gap: 3px;
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
  transition: color var(--transition-fast);
}

.card-zan.active { color: var(--warning-color); }

/* ════════════════════════════════════════
   空状态
════════════════════════════════════════ */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-4xl);
  text-align: center;
}

.empty-illus {
  position: relative;
  width: 90px; height: 90px;
  margin-bottom: var(--spacing-xl);
}

.empty-circle {
  position: absolute;
  inset: 0;
  border-radius: 50%;
  background: var(--primary-gradient-soft);
  border: 2px dashed var(--primary-lighter);
}

.empty-icon {
  position: absolute;
  inset: 0; margin: auto;
  width: 36px; height: 36px;
  font-size: 36px;
  color: var(--primary-light);
}

.empty-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--text-secondary);
  margin: 0 0 var(--spacing-sm);
}

.empty-hint {
  font-size: var(--font-size-sm);
  color: var(--text-disabled);
  margin: 0;
}

/* ════════════════════════════════════════
   分页
════════════════════════════════════════ */
.pagination-wrap {
  display: flex;
  justify-content: center;
  padding-top: var(--spacing-xl);
  margin-top: var(--spacing-xl);
  border-top: 1px solid var(--border-lighter);
}

/* ════════════════════════════════════════
   响应式
════════════════════════════════════════ */
@media (max-width: 900px) {
  .category-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .hero-banner {
    padding: var(--spacing-xl) var(--spacing-md);
    border-radius: var(--radius-md);
  }

  .hero-title { font-size: var(--font-size-3xl); }

  .hero-stats { gap: var(--spacing-sm); }
  .hero-tags { gap: var(--spacing-xs); }
  .hero-tag-item { padding: 4px 10px; font-size: 11px; }

  .section-block {
    padding: var(--spacing-lg);
  }

  .category-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .info-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: var(--spacing-base);
  }
}
</style>
