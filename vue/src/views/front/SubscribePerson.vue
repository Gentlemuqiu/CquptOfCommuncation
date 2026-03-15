<template>
  <div class="page-bg">
    <div class="subscribe-wrapper">

      <!-- ── Hero 横幅 ── -->
      <div class="hero-banner">
        <div class="hero-left">
          <div class="hero-icon-wrap">
            <el-icon class="hero-icon"><User /></el-icon>
          </div>
          <div class="hero-text">
            <h1>我关注的人</h1>
            <p>关注感兴趣的作者，随时查看 TA 发布的就业信息</p>
          </div>
        </div>
        <div class="hero-right" v-if="table.length > 0">
          <div class="stat-badge">
            <span class="stat-num">{{ table.length }}</span>
            <span class="stat-label">位关注</span>
          </div>
        </div>
      </div>

      <!-- ── 内容区 ── -->
      <div class="content-wrap">

        <!-- 空状态 -->
        <div v-if="table.length === 0" class="empty-state">
          <div class="empty-illus">
            <div class="empty-circle"></div>
            <el-icon class="empty-icon"><User /></el-icon>
          </div>
          <h3 class="empty-title">还没有关注任何人</h3>
          <p class="empty-hint">在帖子详情页点击「关注」即可关注作者，这里将显示他们的主页入口</p>
          <el-button type="primary" class="go-btn" @click="$router.push('/front/home')">
            <el-icon><House /></el-icon>
            去浏览帖子
          </el-button>
        </div>

        <!-- 关注列表 -->
        <div v-else class="follow-grid">
          <div
            v-for="row in table"
            :key="row.id"
            class="follow-card"
          >
            <!-- 卡片顶部装饰 -->
            <div class="card-top-bar"></div>

            <!-- 头像区 -->
            <div class="avatar-section">
              <div class="avatar-ring">
                <el-avatar :size="64" class="follow-avatar">
                  {{ row.commentUser ? row.commentUser[0].toUpperCase() : 'U' }}
                </el-avatar>
              </div>
              <div class="following-tag">
                <el-icon><Check /></el-icon>
                已关注
              </div>
            </div>

            <!-- 用户信息 -->
            <div class="user-info">
              <h3 class="user-name">{{ row.commentUser }}</h3>
            </div>

            <!-- 操作按钮 -->
            <div class="card-actions">
              <el-button
                type="primary"
                class="view-btn"
                @click="viewWorks(row.commentUser)"
              >
                <el-icon><Picture /></el-icon>
                查看作品
              </el-button>
              <el-popconfirm
                title="确定取消关注吗？"
                confirm-button-text="取消关注"
                cancel-button-text="再想想"
                @confirm="cancel(row)"
              >
                <template #reference>
                  <el-button class="cancel-btn">
                    <el-icon><Remove /></el-icon>
                    取消关注
                  </el-button>
                </template>
              </el-popconfirm>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { getCurrentUser } from '@/utils/auth'
import { getFollowListByUser, unfollowAuthor } from '@/api/lookAuthor'
import { User, House, Check, Remove, Picture } from '@element-plus/icons-vue'

export default {
  name: 'SubscribePerson',
  components: { User, House, Check, Remove, Picture },
  data() {
    return {
      user: {},
      table: []
    }
  },
  created() {
    this.user = getCurrentUser()
    this.load()
  },
  methods: {
    load() {
      if (!this.user.username) {
        this.table = []
        return
      }
      getFollowListByUser(this.user.username).then(res => {
        this.table = res.data || []
      })
    },
    viewWorks(username) {
      this.$router.push({ path: '/front/authorWorks', query: { username } })
    },
    cancel(row) {
      unfollowAuthor(row.user, row.commentUser).then(() => {
        this.$message.success('已取消关注')
        this.load()
      })
    }
  }
}
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

.subscribe-wrapper {
  max-width: 1000px;
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

.hero-icon-wrap {
  width: 56px; height: 56px;
  border-radius: var(--radius-lg);
  background: rgba(255,255,255,0.15);
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
  border: 1px solid rgba(255,255,255,0.2);
  backdrop-filter: blur(4px);
}

.hero-icon { font-size: 26px; color: #fff; }

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

.hero-right { position: relative; z-index: 1; }

.stat-badge {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: rgba(255,255,255,0.15);
  border: 1px solid rgba(255,255,255,0.25);
  border-radius: var(--radius-lg);
  padding: var(--spacing-base) var(--spacing-xl);
  backdrop-filter: blur(4px);
  min-width: 80px;
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
  max-width: 340px;
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
   关注卡片网格（优化布局）
════════════════════════════════════════ */
.follow-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: var(--spacing-xl);
}

.follow-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  transition: all var(--transition-cubic);
  text-align: center;
}

.follow-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--border-primary);
}

/* 顶部装饰条 */
.card-top-bar {
  width: 100%;
  height: 4px;
  background: var(--primary-gradient);
  flex-shrink: 0;
}

/* 头像区 */
.avatar-section {
  position: relative;
  padding: var(--spacing-xl) var(--spacing-lg) var(--spacing-sm);
}

.avatar-ring {
  padding: 3px;
  border-radius: 50%;
  background: var(--primary-gradient);
  display: inline-flex;
}

.follow-avatar {
  background: var(--primary-gradient) !important;
  color: #fff !important;
  font-weight: var(--font-weight-bold) !important;
  font-size: var(--font-size-2xl) !important;
  border: 3px solid var(--bg-primary) !important;
}

.following-tag {
  position: absolute;
  bottom: 2px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--success-color);
  color: #fff;
  font-size: 10px;
  font-weight: var(--font-weight-semibold);
  padding: 2px 8px;
  border-radius: var(--radius-full);
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 2px;
  box-shadow: 0 2px 6px rgba(16, 185, 129, 0.4);
}

/* 用户信息 */
.user-info {
  padding: 0 var(--spacing-lg);
  flex: 1;
}

.user-name {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0;
  line-height: 1.3;
}

/* 操作按钮 */
.card-actions {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
  padding: var(--spacing-lg);
  width: 100%;
  box-sizing: border-box;
}

.view-btn {
  width: 100%;
  background: var(--primary-gradient);
  border: none;
  border-radius: var(--radius-base);
  font-weight: var(--font-weight-semibold);
  box-shadow: 0 2px 8px rgba(15,118,110,0.25);
  gap: 4px;
  transition: all var(--transition-base);
}

.view-btn:hover {
  box-shadow: var(--shadow-primary);
  transform: translateY(-1px);
}

.cancel-btn {
  width: 100%;
  border-radius: var(--radius-base);
  color: var(--text-tertiary) !important;
  border-color: var(--border-base) !important;
  gap: 4px;
  font-size: var(--font-size-sm) !important;
  transition: all var(--transition-fast);
}

.cancel-btn:hover {
  color: var(--danger-color) !important;
  border-color: var(--danger-color) !important;
  background: var(--danger-light) !important;
}

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

  .content-wrap { padding: var(--spacing-lg); }

  .follow-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: var(--spacing-base);
  }
}
</style>
