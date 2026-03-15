<template>
  <header class="navbar">
    <div class="navbar-content">
      <!-- Logo 区域 -->
      <div class="logo-section" @click="handleLogoClick">
        <div class="logo-icon-wrap">
          <img src="@/assets/image/cqupt.svg" alt="Logo" class="logo-img">
        </div>
        <div class="logo-text-wrap">
          <span class="logo-title">就业信息中心</span>
          <span class="logo-subtitle">CQUPT Employment</span>
        </div>
      </div>

      <!-- 导航菜单 -->
      <nav class="nav-menu">
        <ul class="menu-list">
          <li
            v-for="item in menuItems"
            :key="item.path"
            :class="{ 'menu-item-active': isActive(item.path) }"
            class="menu-item"
            @click="handleMenuClick(item.path)"
          >
            <el-icon class="menu-icon"><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
            <span v-if="isActive(item.path)" class="active-dot"></span>
          </li>
        </ul>
      </nav>

      <!-- 右侧留白（UserMenu在top-bar里） -->
      <div class="nav-right">
        <div class="nav-badge">
          <el-icon><OfficeBuilding /></el-icon>
          <span>重庆邮电大学</span>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { getFrontMenuItems } from '@/config/navigation';
import { OfficeBuilding } from '@element-plus/icons-vue';

export default {
  name: 'Navbar',
  components: { OfficeBuilding },
  props: {
    user: {
      type: Object,
      default: () => ({})
    }
  },
  computed: {
    menuItems() {
      return getFrontMenuItems(this.user);
    }
  },
  methods: {
    isActive(path) {
      if (path === '/front/home') {
        return this.$route.path === path;
      }
      return this.$route.path.includes(path);
    },
    handleLogoClick() {
      this.$router.push('/front/home');
    },
    handleMenuClick(path) {
      this.$router.push(path);
    }
  }
}
</script>

<style scoped>
/* ── 导航栏主体 ── */
.navbar {
  background: var(--nav-bg);
  box-shadow: var(--shadow-nav);
  position: sticky;
  top: 0;
  z-index: var(--z-sticky);
  border-bottom: 1px solid var(--nav-border);
  /* 毛玻璃效果 */
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
}

.navbar-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 var(--spacing-xl);
  height: 68px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
}

/* ── Logo 区域 ── */
.logo-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  cursor: pointer;
  flex-shrink: 0;
  transition: opacity var(--transition-base);
  user-select: none;
}

.logo-section:hover { opacity: 0.9; }

.logo-icon-wrap {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-base);
  backdrop-filter: blur(4px);
}

.logo-section:hover .logo-icon-wrap {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(20, 184, 166, 0.4);
  box-shadow: 0 0 12px rgba(15, 118, 110, 0.3);
}

.logo-img {
  width: 24px;
  height: 24px;
  object-fit: contain;
  filter: brightness(0) invert(1);
  opacity: 0.9;
}

.logo-text-wrap {
  display: flex;
  flex-direction: column;
  gap: 1px;
}

.logo-title {
  color: var(--nav-text-active);
  font-weight: var(--font-weight-bold);
  font-size: var(--font-size-md);
  letter-spacing: 0.5px;
  line-height: 1.2;
}

.logo-subtitle {
  color: rgba(255, 255, 255, 0.4);
  font-size: 10px;
  letter-spacing: 1px;
  text-transform: uppercase;
  line-height: 1;
}

/* ── 导航菜单 ── */
.nav-menu {
  flex: 1;
  display: flex;
  justify-content: center;
  margin: 0 var(--spacing-xl);
}

.menu-list {
  list-style: none;
  display: flex;
  gap: var(--spacing-xs);
  margin: 0;
  padding: 0;
}

.menu-item {
  position: relative;
  padding: 9px var(--spacing-md);
  cursor: pointer;
  color: var(--nav-text);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  border-radius: var(--radius-base);
  transition: all var(--transition-cubic);
  display: flex;
  align-items: center;
  gap: 7px;
  letter-spacing: 0.3px;
  user-select: none;
}

.menu-icon {
  font-size: 15px;
  transition: transform var(--transition-cubic);
  flex-shrink: 0;
}

.menu-item:hover {
  color: var(--nav-text-active);
  background: var(--nav-item-hover);
}

.menu-item:hover .menu-icon {
  transform: scale(1.15);
}

.nav-badge .el-icon {
  font-size: 12px;
}

.menu-item-active {
  color: var(--nav-text-active) !important;
  background: var(--nav-item-active) !important;
  box-shadow: var(--nav-item-active-shadow), inset 0 1px 0 rgba(255, 255, 255, 0.12) !important;
  font-weight: var(--font-weight-semibold) !important;
}

.menu-item-active:hover {
  background: rgba(15, 118, 110, 0.65) !important;
}

/* 激活状态的小圆点指示器 */
.active-dot {
  position: absolute;
  bottom: 4px;
  left: 50%;
  transform: translateX(-50%);
  width: 4px;
  height: 4px;
  background: var(--primary-lighter);
  border-radius: 50%;
  box-shadow: 0 0 6px var(--primary-lighter);
}

/* ── 右侧徽章 ── */
.nav-right {
  flex-shrink: 0;
  display: flex;
  align-items: center;
}

.nav-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 5px 12px;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: var(--radius-full);
  color: rgba(255, 255, 255, 0.5);
  font-size: var(--font-size-xs);
  letter-spacing: 0.5px;
  transition: all var(--transition-base);
}

.nav-badge:hover {
  background: rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.75);
}

.nav-badge i {
  font-size: 12px;
}

/* ── 响应式 ── */
@media (max-width: 900px) {
  .navbar-content {
    padding: 0 var(--spacing-md);
  }
  .logo-subtitle { display: none; }
  .nav-menu { margin: 0 var(--spacing-md); }
  .nav-badge { display: none; }
}

@media (max-width: 640px) {
  .logo-title { display: none; }
  .menu-item span:not(.active-dot) { display: none; }
  .menu-item { padding: 9px 12px; }
  .menu-icon { font-size: 16px; }
}
</style>
