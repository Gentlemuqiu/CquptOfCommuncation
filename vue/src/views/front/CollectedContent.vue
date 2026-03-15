<template>
  <div class="page-bg">
    <div class="collect-wrapper">

      <!-- ── 顶部 Hero 横幅 ── -->
      <div class="hero-banner">
        <div class="hero-left">
          <div class="hero-icon-wrap">
            <el-icon class="hero-icon"><Collection /></el-icon>
          </div>
          <div class="hero-text">
            <h1>我的收藏</h1>
            <p>收藏的就业信息，随时查看回顾</p>
          </div>
        </div>
        <div class="hero-right" v-if="total > 0">
          <div class="stat-badge">
            <span class="stat-num">{{ total }}</span>
            <span class="stat-label">条收藏</span>
          </div>
        </div>
      </div>

      <!-- ── 内容区 ── -->
      <div class="content-card-wrap">

        <!-- 加载骨架屏 -->
        <div v-if="loading" class="skeleton-grid">
          <div v-for="n in pageSize" :key="n" class="skeleton-item">
            <div class="sk-img"></div>
            <div class="sk-body">
              <div class="sk-line sk-title"></div>
              <div class="sk-line sk-sub"></div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else-if="tableData.length === 0" class="empty-state">
          <div class="empty-illus">
            <div class="empty-circle"></div>
            <el-icon class="empty-icon"><Collection /></el-icon>
          </div>
          <h3 class="empty-title">暂无收藏内容</h3>
          <p class="empty-hint">去首页浏览信息，点击 ⭐ 即可收藏到这里</p>
          <el-button type="primary" class="go-btn" @click="$router.push('/front/home')">
            <el-icon><House /></el-icon>
            去浏览信息
          </el-button>
        </div>

        <!-- 收藏网格 -->
        <div v-else>
          <div class="content-grid">
            <div
              v-for="item in tableData"
              :key="item.id"
              class="collect-card"
              @click="$router.push(item.link)"
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
                <!-- 收藏标记角标 -->
                <div class="collect-badge">
                  <el-icon><StarFilled /></el-icon>
                </div>
                <!-- 悬浮遮罩 -->
                <div class="card-hover-mask">
                  <div class="mask-btn">
                    <el-icon><View /></el-icon>
                    查看详情
                  </div>
                </div>
              </div>

              <!-- 内容 -->
              <div class="card-body">
                <h3 class="card-name" :title="item.name">{{ item.name }}</h3>
                <div class="card-footer">
                  <el-popconfirm
                    title="确定取消收藏吗？"
                    confirm-button-text="取消收藏"
                    cancel-button-text="再想想"
                    @confirm.stop="handleDelete(item.id)"
                  >
                    <template #reference>
                      <el-button
                        class="uncollect-btn"
                        size="small"
                        @click.stop
                      >
                        <el-icon><StarFilled /></el-icon>
                        已收藏
                      </el-button>
                    </template>
                  </el-popconfirm>
                  <span class="view-hint">点击查看 →</span>
                </div>
              </div>
            </div>
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
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { Collection, House, Picture, StarFilled, View } from '@element-plus/icons-vue'

export default {
  name: "CollectedContent",
  components: { Collection, House, Picture, StarFilled, View },
  data() {
    return {
      loading: false,
      pageNum: 1,
      pageSize: 12,
      total: 0,
      tableData: [],
      user: JSON.parse(sessionStorage.getItem("user") || "{}")
    };
  },
  created() { this.load(); },
  methods: {
    load() {
      this.loading = true;
      request.get("/collectMovie/page", {
        params: { pageNum: this.pageNum, pageSize: this.pageSize, userId: this.user.id }
      }).then(res => {
        this.loading = false;
        if (res.code === '0') {
          this.tableData = res.data.records || [];
          this.total = res.data.total || 0;
        }
      }).catch(() => { this.loading = false; });
    },
    handleDelete(id) {
      request.delete(`/collectMovie/${id}`).then(res => {
        if (res.code === '0') { this.$message.success('已取消收藏'); this.load(); }
        else this.$message.error(res.msg);
      });
    },
    handleSizeChange(ps) { this.pageSize = ps; this.pageNum = 1; this.load(); },
    handleCurrentChange(pn) { this.pageNum = pn; this.load(); },
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

.collect-wrapper {
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
   内容卡片容器
════════════════════════════════════════ */
.content-card-wrap {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  padding: var(--spacing-2xl);
  box-shadow: var(--shadow-base);
  min-height: 300px;
}

/* ════════════════════════════════════════
   骨架屏
════════════════════════════════════════ */
.skeleton-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: var(--spacing-lg);
}

.skeleton-item {
  border-radius: var(--radius-md);
  overflow: hidden;
  border: 1px solid var(--border-lighter);
}

.sk-img {
  height: 150px;
  background: linear-gradient(90deg, var(--bg-tertiary) 25%, var(--bg-secondary) 50%, var(--bg-tertiary) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.sk-body { padding: var(--spacing-base) var(--spacing-md); }

.sk-line {
  border-radius: var(--radius-full);
  background: linear-gradient(90deg, var(--bg-tertiary) 25%, var(--bg-secondary) 50%, var(--bg-tertiary) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.sk-title { height: 14px; margin-bottom: 8px; }
.sk-sub   { height: 10px; width: 60%; }

@keyframes shimmer {
  0%   { background-position: 200% 0; }
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
   收藏卡片网格
════════════════════════════════════════ */
.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(210px, 1fr));
  gap: var(--spacing-lg);
}

.collect-card {
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-lg);
  overflow: hidden;
  cursor: pointer;
  transition: all var(--transition-cubic);
  background: var(--bg-primary);
}

.collect-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-lg);
  border-color: var(--border-primary);
}

/* 图片区 */
.card-img-wrap {
  height: 150px;
  overflow: hidden;
  background: var(--bg-tertiary);
  position: relative;
}

.card-img {
  width: 100%;
  height: 100%;
  transition: transform var(--transition-slow);
  display: block;
}

.collect-card:hover .card-img {
  transform: scale(1.08);
}

.img-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-tertiary);
  color: var(--text-disabled);
  font-size: 32px;
}

/* 收藏角标 */
.collect-badge {
  position: absolute;
  top: 10px; right: 10px;
  width: 28px; height: 28px;
  background: rgba(245, 158, 11, 0.92);
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  color: #fff;
  font-size: 13px;
  box-shadow: 0 2px 8px rgba(245, 158, 11, 0.4);
}

/* 悬浮遮罩 */
.card-hover-mask {
  position: absolute;
  inset: 0;
  background: rgba(15, 118, 110, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity var(--transition-base);
  backdrop-filter: blur(2px);
}

.collect-card:hover .card-hover-mask {
  opacity: 1;
}

.mask-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #fff;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  background: rgba(255,255,255,0.18);
  border: 1px solid rgba(255,255,255,0.3);
  border-radius: var(--radius-full);
  padding: 8px 18px;
  letter-spacing: 0.02em;
}

/* 内容区 */
.card-body {
  padding: var(--spacing-base) var(--spacing-md);
}

.card-name {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-base);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--transition-base);
  line-height: var(--line-height-snug);
}

.collect-card:hover .card-name {
  color: var(--primary-color);
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.uncollect-btn {
  font-size: var(--font-size-xs) !important;
  color: var(--warning-color) !important;
  border-color: rgba(245, 158, 11, 0.35) !important;
  background: rgba(245, 158, 11, 0.06) !important;
  border-radius: var(--radius-full) !important;
  padding: 3px 10px !important;
  gap: 3px;
  transition: all var(--transition-fast);
}

.uncollect-btn:hover {
  color: var(--danger-color) !important;
  border-color: var(--danger-color) !important;
  background: var(--danger-light) !important;
}

.view-hint {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
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
@media (max-width: 768px) {
  .page-bg {
    padding: var(--spacing-md);
  }

  .hero-banner {
    flex-direction: column;
    gap: var(--spacing-lg);
    padding: var(--spacing-lg);
    text-align: center;
  }

  .hero-left { flex-direction: column; }

  .content-card-wrap {
    padding: var(--spacing-lg);
  }

  .content-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: var(--spacing-base);
  }
}
</style>
