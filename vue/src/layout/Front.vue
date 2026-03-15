<template>
  <div class="layout-container">
    <!-- 深色主导航栏 -->
    <Navbar :user="user" />

    <!-- 搜索 & 用户操作栏 -->
    <div class="top-bar">
      <div class="top-bar-content">
        <SearchBar />
        <UserMenu :user="user" />
      </div>
    </div>

    <!-- 主内容区 -->
    <main class="main-content">
      <div class="content-wrapper">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script>
import Navbar from '@/components/layout/Navbar.vue'
import SearchBar from '@/components/layout/SearchBar.vue'
import UserMenu from '@/components/layout/UserMenu.vue'
import { getCurrentUser } from '@/utils/auth'

export default {
  name: 'FrontLayout',
  components: { Navbar, SearchBar, UserMenu },
  data() {
    return {
      user: {}
    }
  },
  created() {
    this.initUserInfo()
    window.addEventListener('storage', this.handleStorageChange)
    window.addEventListener('userUpdate', this.handleUserUpdate)
  },
  beforeUnmount() {
    window.removeEventListener('storage', this.handleStorageChange)
    window.removeEventListener('userUpdate', this.handleUserUpdate)
  },
  watch: {
    $route: {
      handler() { this.initUserInfo() },
      immediate: true
    }
  },
  methods: {
    initUserInfo() {
      this.user = getCurrentUser()
    },
    handleStorageChange(e) {
      if (e.key === 'user') this.initUserInfo()
    },
    handleUserUpdate() { this.initUserInfo() }
  }
}
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
