<template>
  <div class="page-bg">
    <div class="works-wrapper">

      <!-- ── Hero 横幅 ── -->
      <div class="hero-banner">
        <div class="hero-left">
          <div class="author-avatar-ring">
            <el-avatar :size="64" class="author-avatar">
              {{ authorInitial }}
            </el-avatar>
          </div>
          <div class="hero-text">
            <h1>{{ username }} 的作品</h1>
            <p>浏览 TA 发布的全部就业信息</p>
          </div>
        </div>
        <div class="hero-right">
          <div class="stat-badge" v-if="works.length > 0">
            <span class="stat-num">{{ works.length }}</span>
            <span class="stat-label">条作品</span>
          </div>
          <el-button class="back-btn" @click="$router.back()">
            <el-icon><ArrowLeft /></el-icon>
            返回
          </el-button>
        </div>
      </div>

      <!-- ── 内容区 ── -->
      <div class="content-wrap">

        <!-- 加载中 -->
        <div v-if="loading" class="skeleton-grid">
          <div v-for="n in 6" :key="n" class="skeleton-card">
            <div class="sk-img"></div>
            <div class="sk-body">
              <div class="sk-line sk-t1"></div>
              <div class="sk-line sk-t2"></div>
              <div class="sk-line sk-t3"></div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else-if="works.length === 0" class="empty-state">
          <div class="empty-illus">
            <div class="empty-circle"></div>
            <el-icon class="empty-icon"><Document /></el-icon>
          </div>
          <h3 class="empty-title">该作者暂无发布内容</h3>
          <p class="empty-hint">TA 还没有发布任何就业信息，敬请期待</p>
          <el-button type="primary" class="go-btn" @click="$router.push('/front/home')">
            <el-icon><House /></el-icon>
            去浏览首页
          </el-button>
        </div>

        <!-- 作品卡片网格 -->
        <div v-else class="works-grid">
          <div
            v-for="item in works"
            :key="item.id"
            class="work-card"
            @click="goDetail(item.id)"
          >
            <!-- 封面图 -->
            <div class="card-img-wrap">
              <el-image :src="item.img" fit="cover" class="card-img" lazy>
                <template #error>
                  <div class="img-fallback">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <!-- 分类徽章 -->
              <div class="area-badge">{{ item.area }}</div>
              <!-- 热门徽章 -->
              <div class="hot-badge" v-if="(item.zan || 0) >= 3">🔥 热门</div>
              <!-- 悬浮遮罩 -->
              <div class="hover-mask">
                <el-icon><View /></el-icon>
                查看详情
              </div>
            </div>

            <!-- 卡片正文 -->
            <div class="card-body">
              <h3 class="card-title" :title="item.name">{{ item.name }}</h3>
              <p class="card-desc">{{ item.description || '暂无简介' }}</p>
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

      </div>
    </div>
  </div>
</template>

<script>
import { getUserPage } from '@/api/user'
import { getMovieByUser } from '@/api/movie'
import {
  ArrowLeft, Document, House, Picture,
  View, Calendar, Sunny
} from '@element-plus/icons-vue'

export default {
  name: "AuthorWorks",
  components: { ArrowLeft, Document, House, Picture, View, Calendar, Sunny },
  data() {
    return {
      username: '',           // 用于页面标题展示（如「xxx 的作品」）
      userId: null,           // 关注的人的 id / 作者的 id，传给接口 movie/user/{userid}
      works: [],
      loading: false
    };
  },
  computed: {
    authorInitial() {
      return this.username ? this.username[0].toUpperCase() : 'U';
    }
  },
  watch: {
    '$route.query': {
      handler(q) {
        this.username = q.username || '';
        this.userId = q.userId != null ? Number(q.userId) : (q.user_id != null ? Number(q.user_id) : null);
        if (this.userId != null) {
          this.loadWorks();
        } else if (this.username) {
          this.fetchUserIdThenLoad();
        } else {
          this.works = [];
        }
      },
      immediate: false
    }
  },
  created() {
    const q = this.$route.query;
    this.username = q.username || '';
    this.userId = q.userId != null ? Number(q.userId) : (q.user_id != null ? Number(q.user_id) : null);
    if (this.userId != null) {
      this.loadWorks();
    } else if (this.username) {
      this.fetchUserIdThenLoad();
    }
  },
  methods: {
    /** 仅有 username 时先根据用户名查出「关注的人的 id」，再请求 movie/user/{userid} 拉作品 */
    fetchUserIdThenLoad() {
      this.loading = true
      getUserPage({ username: this.username, pageNum: 1, pageSize: 1 })
        .then(res => {
          const list = (res.data && res.data.records) ? res.data.records : (Array.isArray(res.data) ? res.data : [])
          const user = list[0]
          if (user && user.id != null) {
            this.userId = Number(user.id)
            this.loadWorks()
          } else {
            this.works = []
          }
        })
        .catch(() => { this.works = [] })
        .finally(() => { this.loading = false })
    },
    loadWorks() {
      if (this.userId == null) return
      this.loading = true
      getMovieByUser(this.userId)
        .then(res => {
          if (res.code !== '0' && res.code !== 0) {
            this.works = []
            return
          }
          const d = res.data
          if (Array.isArray(d)) {
            this.works = d
          } else if (d && Array.isArray(d.records)) {
            this.works = d.records
          } else {
            this.works = []
          }
        })
        .catch(() => { this.works = [] })
        .finally(() => {
          this.loading = false
        });
    },
    goDetail(id) {
      this.$router.push('/front/detail?id=' + id);
    }
  }
};
</script>

<style scoped>
/* ════════════════════════════════════════
   页面背景
════════════════════════════════════════ */
.page-bg {
  min-height: calc(100vh - 64px);
  background: var(--bg-page);
  padding: var(--spacing-xl);
}

.works-wrapper {
  max-width: 1100px;
  margin: 0 auto;
}

/* ════════════════════════════════════════
   Hero 横幅
════════════════════════════════════════ */
.hero-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--primary-gradient);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl) var(--spacing-2xl);
  margin-bottom: var(--spacing-xl);
  box-shadow: var(--shadow-primary);
  position: relative;
  overflow: hidden;
}

.hero-banner::before {
  content: '';
  position: absolute;
  top: -50px; right: -50px;
  width: 180px; height: 180px;
  border-radius: 50%;
  background: rgba(255,255,255,0.06);
}

.hero-banner::after {
  content: '';
  position: absolute;
  bottom: -70px; right: 100px;
  width: 240px; height: 240px;
  border-radius: 50%;
  background: rgba(255,255,255,0.04);
}

.hero-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  position: relative;
  z-index: 1;
}

.author-avatar-ring {
  padding: 3px;
  border-radius: 50%;
  background: rgba(255,255,255,0.3);
  display: inline-flex;
  flex-shrink: 0;
}

.author-avatar {
  background: rgba(255,255,255,0.2) !important;
  color: #fff !important;
  font-weight: var(--font-weight-bold) !important;
  font-size: var(--font-size-2xl) !important;
  border: 3px solid rgba(255,255,255,0.4) !important;
}

.hero-text h1 {
  margin: 0 0 5px;
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: #fff;
}

.hero-text p {
  margin: 0;
  font-size: var(--font-size-sm);
  color: rgba(255,255,255,0.75);
}

.hero-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  position: relative;
  z-index: 1;
}

.stat-badge {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: rgba(255,255,255,0.15);
  border: 1px solid rgba(255,255,255,0.25);
  border-radius: var(--radius-lg);
  padding: var(--spacing-base) var(--spacing-xl);
  backdrop-filter: blur(4px);
  min-width: 72px;
}

.stat-num {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-extrabold);
  color: #fff;
  line-height: 1;
}

.stat-label {
  font-size: var(--font-size-xs);
  color: rgba(255,255,255,0.75);
  margin-top: 4px;
}

.back-btn {
  background: rgba(255,255,255,0.15) !important;
  border: 1px solid rgba(255,255,255,0.3) !important;
  color: #fff !important;
  border-radius: var(--radius-base) !important;
  gap: 4px;
  backdrop-filter: blur(4px);
  transition: all var(--transition-base);
}

.back-btn:hover {
  background: rgba(255,255,255,0.25) !important;
}

/* ════════════════════════════════════════
   内容区
════════════════════════════════════════ */
.content-wrap {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  padding: var(--spacing-2xl);
  box-shadow: var(--shadow-base);
  min-height: 200px;
}

/* ════════════════════════════════════════
   骨架屏
════════════════════════════════════════ */
.skeleton-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: var(--spacing-lg);
}

.skeleton-card {
  border-radius: var(--radius-lg);
  overflow: hidden;
  background: var(--bg-secondary);
  border: 1px solid var(--border-lighter);
}

.sk-img {
  width: 100%;
  height: 140px;
  background: linear-gradient(90deg, var(--bg-tertiary) 25%, var(--bg-secondary) 50%, var(--bg-tertiary) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.sk-body { padding: var(--spacing-base); display: flex; flex-direction: column; gap: var(--spacing-sm); }

.sk-line {
  height: 12px;
  border-radius: var(--radius-full);
  background: linear-gradient(90deg, var(--bg-tertiary) 25%, var(--bg-secondary) 50%, var(--bg-tertiary) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.sk-t1 { width: 80%; }
.sk-t2 { width: 60%; }
.sk-t3 { width: 40%; }

@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* ════════════════════════════════════════
   空状态
════════════════════════════════════════ */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-4xl) var(--spacing-xl);
  text-align: center;
}

.empty-illus {
  position: relative;
  width: 100px; height: 100px;
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
  inset: 0;
  margin: auto;
  width: 40px; height: 40px;
  font-size: 40px;
  color: var(--primary-light);
}

.empty-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--text-secondary);
  margin: 0 0 var(--spacing-sm);
}

.empty-hint {
  font-size: var(--font-size-sm);
  color: var(--text-disabled);
  margin: 0 0 var(--spacing-xl);
  max-width: 320px;
  line-height: var(--line-height-relaxed);
}

.go-btn {
  background: var(--primary-gradient);
  border: none;
  border-radius: var(--radius-base);
  padding: 10px 28px;
  font-weight: var(--font-weight-semibold);
  box-shadow: var(--shadow-primary);
  gap: 6px;
  transition: all var(--transition-base);
}

.go-btn:hover {
  box-shadow: var(--shadow-primary-lg);
  transform: translateY(-1px);
}

/* ════════════════════════════════════════
   作品卡片网格
════════════════════════════════════════ */
.works-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: var(--spacing-lg);
}

.work-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-lg);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: all var(--transition-cubic);
}

.work-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-lg);
  border-color: var(--border-primary);
}

/* 封面图 */
.card-img-wrap {
  position: relative;
  overflow: hidden;
  height: 160px;
  background: var(--bg-tertiary);
}

.card-img {
  width: 100%;
  height: 100%;
  display: block;
  transition: transform var(--transition-slow);
}

.work-card:hover .card-img {
  transform: scale(1.06);
}

.img-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-disabled);
  font-size: 36px;
}

/* 分类徽章 */
.area-badge {
  position: absolute;
  top: 8px; left: 8px;
  background: var(--primary-gradient);
  color: #fff;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  padding: 2px 8px;
  border-radius: var(--radius-full);
}

/* 热门徽章 */
.hot-badge {
  position: absolute;
  bottom: 8px; right: 8px;
  background: linear-gradient(135deg, #f59e0b, #ef4444);
  color: #fff;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  padding: 2px 8px;
  border-radius: var(--radius-full);
}

/* 悬浮遮罩 */
.hover-mask {
  position: absolute;
  inset: 0;
  background: rgba(15,118,110,0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  color: #fff;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  opacity: 0;
  transition: opacity var(--transition-base);
}

.work-card:hover .hover-mask {
  opacity: 1;
}

/* 卡片正文 */
.card-body {
  padding: var(--spacing-base) var(--spacing-md);
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: var(--spacing-xs);
}

.card-title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin: 0;
  line-height: var(--line-height-snug);
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  transition: color var(--transition-base);
}

.work-card:hover .card-title {
  color: var(--primary-color);
}

.card-desc {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  margin: 0;
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
   响应式
════════════════════════════════════════ */
@media (max-width: 768px) {
  .page-bg { padding: var(--spacing-md); }

  .hero-banner {
    flex-direction: column;
    gap: var(--spacing-lg);
    padding: var(--spacing-lg);
    text-align: center;
  }

  .hero-left { flex-direction: column; }

  .hero-right {
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
  }

  .content-wrap { padding: var(--spacing-lg); }

  .works-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: var(--spacing-base);
  }
}
</style>
