<template>
  <div class="layout-container">
    <!-- 深色主导航栏 -->
    <Navbar :user="user" />

    <!-- 搜索 & 用户操作栏 -->
    <div class="top-bar">
      <div class="top-bar-content">
        <SearchBar />
        <UserMenu
          :user="user"
          :notice-count="count"
          @show-notice="showNotice"
        />
      </div>
    </div>

    <!-- 主内容区 -->
    <main class="main-content">
      <div class="content-wrapper">
        <router-view />
      </div>
    </main>

    <!-- 公告弹框 -->
    <el-dialog
      v-model="dialogVisible"
      width="540px"
      class="notice-dialog"
      :show-close="false"
      align-center
    >
      <!-- 自定义 header -->
      <template #header>
        <div class="notice-dialog-header">
          <div class="notice-header-left">
            <div class="notice-bell-wrap">
              <el-icon class="notice-bell"><Bell /></el-icon>
              <span class="bell-dot" v-if="count > 0"></span>
            </div>
            <div class="notice-header-text">
              <span class="notice-title">系统公告</span>
              <span class="notice-count-tag">{{ notice.length }} 条</span>
            </div>
          </div>
          <button class="notice-close-btn" @click="dialogVisible = false">
            <el-icon><Close /></el-icon>
          </button>
        </div>
      </template>

      <!-- 公告列表 -->
      <div class="notice-body">
        <!-- 空状态 -->
        <div v-if="notice.length === 0" class="notice-empty">
          <el-icon class="notice-empty-icon"><Bell /></el-icon>
          <p>暂无系统公告</p>
        </div>

        <!-- 公告卡片 -->
        <div
          v-for="(item, index) in notice"
          :key="item.id"
          class="notice-card"
          :class="{ expanded: active === String(index) }"
        >
          <div class="notice-card-hd" @click="active = active === String(index) ? '' : String(index)">
            <div class="notice-index">{{ index + 1 }}</div>
            <div class="notice-card-main">
              <span class="notice-card-title">{{ item.name }}</span>
              <span class="notice-card-time" v-if="item.time">
                <el-icon><Clock /></el-icon>
                {{ item.time }}
              </span>
            </div>
            <el-icon class="notice-toggle" :class="{ rotated: active === String(index) }">
              <ArrowDown />
            </el-icon>
          </div>
          <div class="notice-card-body" v-show="active === String(index)">
            <p class="notice-card-content">{{ item.content }}</p>
          </div>
        </div>
      </div>

      <!-- 底部 -->
      <template #footer>
        <div class="notice-footer">
          <span class="notice-footer-tip">
            <el-icon><InfoFilled /></el-icon>
            以上公告由管理员发布
          </span>
          <el-button type="primary" class="notice-confirm-btn" @click="dialogVisible = false">
            我知道了
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import Navbar from "@/components/layout/Navbar.vue";
import SearchBar from "@/components/layout/SearchBar.vue";
import UserMenu from "@/components/layout/UserMenu.vue";
import { Bell, Close, Clock, ArrowDown, InfoFilled } from '@element-plus/icons-vue'

export default {
  name: "FrontLayout",
  components: { Navbar, SearchBar, UserMenu, Bell, Close, Clock, ArrowDown, InfoFilled },
  data() {
    return {
      user: {},
      count: 0,
      notice: [],
      dialogVisible: false,
      active: '0'
    };
  },
  created() {
    this.initUserInfo();
    this.initNotice();
    window.addEventListener('storage', this.handleStorageChange);
    window.addEventListener('userUpdate', this.handleUserUpdate);
  },
  beforeUnmount() {
    window.removeEventListener('storage', this.handleStorageChange);
    window.removeEventListener('userUpdate', this.handleUserUpdate);
  },
  watch: {
    '$route': {
      handler() { this.initUserInfo(); },
      immediate: true
    }
  },
  methods: {
    initUserInfo() {
      const userStr = sessionStorage.getItem("user") || "{}";
      this.user = JSON.parse(userStr);
    },
    initNotice() {
      request.get('/notice').then(res => {
        this.notice = res.data.filter(v => v.status === '已发布');
        this.count = this.notice.length;
      }).catch(() => { this.count = 0; });
    },
    handleStorageChange(e) {
      if (e.key === 'user') this.initUserInfo();
    },
    handleUserUpdate() { this.initUserInfo(); },
    showNotice() { this.dialogVisible = true; }
  }
};
</script>

<style scoped>
/* ── 整体容器 ── */
.layout-container {
  min-height: 100vh;
  background: var(--bg-page);
  background-attachment: fixed;
}

/* ── 次级操作栏（搜索 + 用户菜单） ── */
.top-bar {
  background: #ffffff;
  border-bottom: 1px solid var(--border-lighter);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 68px;
  z-index: calc(var(--z-sticky) - 1);
}

.top-bar-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 8px var(--spacing-xl);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--spacing-lg);
}

/* ── 主内容区 ── */
/* 注意：不对 router-view 子元素做通用包裹，避免重复边框 */
.main-content {
  padding: var(--spacing-xl) 0 var(--spacing-3xl);
  min-height: calc(100vh - 122px);
}

.content-wrapper {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
}

/* ── 公告弹框容器 ── */
.notice-dialog :deep(.el-dialog) {
  border-radius: var(--radius-xl);
  overflow: hidden;
  padding: 0;
}

.notice-dialog :deep(.el-dialog__header) {
  padding: 0;
  margin: 0;
}

.notice-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.notice-dialog :deep(.el-dialog__footer) {
  padding: 0;
}

/* ── 自定义 header ── */
.notice-dialog-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-lg) var(--spacing-xl);
  background: var(--primary-gradient);
  position: relative;
  overflow: hidden;
}

.notice-dialog-header::before {
  content: '';
  position: absolute;
  top: -30px; right: -30px;
  width: 120px; height: 120px;
  border-radius: 50%;
  background: rgba(255,255,255,0.07);
}

.notice-dialog-header::after {
  content: '';
  position: absolute;
  bottom: -40px; left: 20px;
  width: 90px; height: 90px;
  border-radius: 50%;
  background: rgba(255,255,255,0.05);
}

.notice-header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  position: relative;
  z-index: 1;
}

.notice-bell-wrap {
  position: relative;
  width: 44px; height: 44px;
  border-radius: var(--radius-base);
  background: rgba(255,255,255,0.15);
  border: 1px solid rgba(255,255,255,0.2);
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}

.notice-bell { font-size: 22px; color: #fff; }

.bell-dot {
  position: absolute;
  top: 6px; right: 6px;
  width: 8px; height: 8px;
  border-radius: 50%;
  background: #fbbf24;
  border: 2px solid rgba(15,118,110,0.5);
  box-shadow: 0 0 6px rgba(251,191,36,0.6);
}

.notice-header-text {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.notice-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: #fff;
  letter-spacing: 0.02em;
}

.notice-count-tag {
  font-size: var(--font-size-xs);
  color: rgba(255,255,255,0.7);
  background: rgba(255,255,255,0.15);
  border: 1px solid rgba(255,255,255,0.2);
  border-radius: var(--radius-full);
  padding: 2px 9px;
  font-weight: var(--font-weight-medium);
}

.notice-close-btn {
  position: relative;
  z-index: 1;
  width: 32px; height: 32px;
  border-radius: 50%;
  background: rgba(255,255,255,0.12);
  border: 1px solid rgba(255,255,255,0.18);
  display: flex; align-items: center; justify-content: center;
  cursor: pointer;
  color: rgba(255,255,255,0.85);
  font-size: 15px;
  transition: all var(--transition-fast);
}

.notice-close-btn:hover {
  background: rgba(255,255,255,0.22);
  color: #fff;
}

/* ── 公告正文区 ── */
.notice-body {
  padding: var(--spacing-lg);
  max-height: 420px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.notice-body::-webkit-scrollbar { width: 4px; }
.notice-body::-webkit-scrollbar-track { background: transparent; }
.notice-body::-webkit-scrollbar-thumb {
  background: var(--border-base);
  border-radius: var(--radius-full);
}

/* 空状态 */
.notice-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-3xl);
  color: var(--text-disabled);
  text-align: center;
}

.notice-empty-icon {
  font-size: 42px;
  margin-bottom: var(--spacing-base);
  opacity: 0.35;
}

.notice-empty p {
  font-size: var(--font-size-md);
  margin: 0;
}

/* 公告卡片 */
.notice-card {
  border-radius: var(--radius-lg);
  border: 1.5px solid var(--border-light);
  background: var(--bg-primary);
  overflow: hidden;
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
}

.notice-card:hover {
  border-color: var(--border-primary);
  box-shadow: var(--shadow-sm);
}

.notice-card.expanded {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(15,118,110,0.08);
}

.notice-card-hd {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  padding: var(--spacing-base) var(--spacing-lg);
  cursor: pointer;
  user-select: none;
  transition: background var(--transition-fast);
}

.notice-card-hd:hover {
  background: var(--bg-secondary);
}

.notice-card.expanded .notice-card-hd {
  background: var(--primary-gradient-soft);
  border-bottom: 1px solid var(--border-primary);
}

.notice-index {
  width: 26px; height: 26px;
  border-radius: 50%;
  background: var(--primary-gradient);
  color: #fff;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-bold);
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 2px 6px rgba(15,118,110,0.3);
}

.notice-card-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.notice-card-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  line-height: var(--line-height-snug);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notice-card.expanded .notice-card-title {
  color: var(--primary-dark);
}

.notice-card-time {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

.notice-toggle {
  font-size: 14px;
  color: var(--text-disabled);
  flex-shrink: 0;
  transition: transform var(--transition-base);
}

.notice-toggle.rotated { transform: rotate(180deg); color: var(--primary-color); }

.notice-card-body {
  padding: var(--spacing-base) var(--spacing-lg) var(--spacing-lg);
  background: var(--bg-secondary);
}

.notice-card-content {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  line-height: var(--line-height-relaxed);
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
}

/* ── 底部 ── */
.notice-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-base) var(--spacing-xl);
  border-top: 1px solid var(--border-lighter);
  background: var(--bg-secondary);
}

.notice-footer-tip {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

.notice-footer-tip .el-icon { color: var(--info-color); font-size: 13px; }

.notice-confirm-btn {
  background: var(--primary-gradient);
  border: none;
  border-radius: var(--radius-full);
  padding: 7px 24px;
  font-weight: var(--font-weight-semibold);
  box-shadow: 0 2px 8px rgba(15,118,110,0.25);
  transition: all var(--transition-base);
}

.notice-confirm-btn:hover {
  box-shadow: var(--shadow-primary);
  transform: translateY(-1px);
}

/* ── 响应式 ── */
@media (max-width: 768px) {
  .top-bar { top: 68px; }

  .top-bar-content {
    flex-direction: column;
    padding: var(--spacing-base);
    gap: var(--spacing-base);
  }

  .main-content {
    padding: var(--spacing-md) 0 var(--spacing-xl);
  }

  .content-wrapper {
    padding: 0 var(--spacing-sm);
  }
}
</style>
