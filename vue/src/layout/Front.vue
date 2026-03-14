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
      title="系统公告"
      v-model="dialogVisible"
      width="520px"
      class="notice-dialog"
    >
      <div class="notice-content">
        <el-collapse v-model="active" accordion>
          <el-collapse-item
            :title="item.name"
            :name="index + ''"
            v-for="(item, index) in notice"
            :key="item.id"
          >
            <div class="notice-item-content">{{ item.content }}</div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import Navbar from "@/components/layout/Navbar.vue";
import SearchBar from "@/components/layout/SearchBar.vue";
import UserMenu from "@/components/layout/UserMenu.vue";

export default {
  name: "FrontLayout",
  components: { Navbar, SearchBar, UserMenu },
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
  background: rgba(255, 255, 255, 0.95);
  border-bottom: 1px solid var(--border-lighter);
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  position: sticky;
  top: 68px;
  z-index: calc(var(--z-sticky) - 1);
}

.top-bar-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 10px var(--spacing-xl);
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

/* ── 公告弹框 ── */
.notice-dialog :deep(.el-dialog__header) {
  background: var(--primary-gradient);
  padding: var(--spacing-lg) var(--spacing-xl);
  border-radius: var(--radius-xl) var(--radius-xl) 0 0;
  margin-right: 0;
  border-bottom: none;
}

.notice-dialog :deep(.el-dialog__title) {
  color: #fff;
  font-weight: var(--font-weight-semibold);
  font-size: var(--font-size-lg);
}

.notice-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: rgba(255, 255, 255, 0.85);
}

.notice-dialog :deep(.el-dialog__headerbtn:hover .el-dialog__close) {
  color: #fff;
}

.notice-content { padding: var(--spacing-sm) 0; }

.notice-item-content {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  line-height: var(--line-height-relaxed);
  padding: var(--spacing-sm) 0;
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
