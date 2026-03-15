<template>
  <aside class="admin-aside">
    <!-- 侧边栏标题 -->
    <div class="aside-header">
      <el-icon class="aside-header-icon"><Menu /></el-icon>
      <span>功能导航</span>
    </div>

    <!-- 导航菜单 -->
    <el-menu :default-active="path" router class="aside-menu" unique-opened>
      <!-- 系统管理分组 -->
      <el-sub-menu index="system" v-if="systemItems.length">
        <template #title>
          <el-icon><Setting /></el-icon>
          <span>系统管理</span>
        </template>
        <el-menu-item
          v-for="item in systemItems"
          :key="item.path"
          :index="item.path"
        >
          <el-icon><component :is="getIcon(item.icon)" /></el-icon>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-sub-menu>

      <!-- 普通菜单项 -->
      <el-menu-item
        v-for="item in normalItems"
        :key="item.path"
        :index="item.path"
      >
        <el-icon><component :is="getIcon(item.icon)" /></el-icon>
        <span>{{ item.label }}</span>
      </el-menu-item>
    </el-menu>
  </aside>
</template>

<script>
import { getUserById } from '@/api/user'
import { getCurrentUser } from '@/utils/auth'
import { ADMIN_SIDEBAR_ITEMS } from '@/config/navigation'
import {
  Menu, Setting, UserFilled, Document,
  Warning, ChatDotRound, Bell
} from '@element-plus/icons-vue'

const ICON_MAP = { UserFilled, Document, Warning, ChatDotRound, Bell }

export default {
  name: "AdminAside",
  components: { Menu, Setting, UserFilled, Document, Warning, ChatDotRound, Bell },
  data() {
    return {
      user: {},
      path: this.$route.path
    }
  },
  computed: {
    filteredItems() {
      return ADMIN_SIDEBAR_ITEMS.filter(item => {
        if (!item.requireRole) return true
        return item.requireRole === this.user.role
      })
    },
    systemItems() {
      return this.filteredItems.filter(item => item.group === '系统管理')
    },
    normalItems() {
      return this.filteredItems.filter(item => item.group !== '系统管理')
    }
  },
  created() {
    this.user = getCurrentUser()
    if (this.user.id) {
      getUserById(this.user.id).then(res => {
        if (res.code === '0') this.user = res.data
      })
    }
  },
  methods: {
    getIcon(name) {
      return ICON_MAP[name] || Document;
    }
  }
};
</script>

<style scoped>
.admin-aside {
  width: 240px;
  flex-shrink: 0;
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  align-self: flex-start;
  position: sticky;
  top: calc(64px + var(--spacing-xl));
}

.aside-header {
  background: var(--primary-gradient);
  padding: var(--spacing-lg) var(--spacing-xl);
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  color: rgba(255, 255, 255, 0.98);
  font-weight: var(--font-weight-semibold);
  font-size: var(--font-size-base);
  letter-spacing: 0.03em;
}

.aside-header-icon {
  font-size: 18px;
  opacity: 0.95;
}

.aside-menu {
  border-right: none !important;
  padding: var(--spacing-md) var(--spacing-sm);
}

.aside-menu :deep(.el-menu-item),
.aside-menu :deep(.el-sub-menu__title) {
  height: 48px;
  line-height: 48px;
  border-radius: var(--radius-base);
  margin: 2px 0;
  padding: 0 var(--spacing-base) !important;
  transition: all var(--transition-cubic) !important;
  font-size: var(--font-size-base) !important;
  color: var(--text-secondary) !important;
}

.aside-menu :deep(.el-menu-item .el-icon),
.aside-menu :deep(.el-sub-menu__title .el-icon) {
  margin-right: var(--spacing-base);
  font-size: 18px;
  transition: color var(--transition-fast), transform var(--transition-fast);
}

.aside-menu :deep(.el-menu-item:hover),
.aside-menu :deep(.el-sub-menu__title:hover) {
  background: var(--bg-hover) !important;
  color: var(--primary-color) !important;
}

.aside-menu :deep(.el-menu-item:hover .el-icon),
.aside-menu :deep(.el-sub-menu__title:hover .el-icon) {
  color: var(--primary-color);
}

.aside-menu :deep(.el-menu-item.is-active) {
  background: var(--primary-gradient-soft) !important;
  color: var(--primary-color) !important;
  font-weight: var(--font-weight-semibold) !important;
  box-shadow: inset 3px 0 0 var(--primary-color);
}

.aside-menu :deep(.el-menu-item.is-active .el-icon) {
  color: var(--primary-color) !important;
}

.aside-menu :deep(.el-sub-menu .el-menu) {
  background: var(--bg-secondary) !important;
  padding: var(--spacing-xs) 0;
}

.aside-menu :deep(.el-sub-menu .el-menu-item) {
  height: 42px !important;
  line-height: 42px !important;
  padding-left: 52px !important;
  font-size: var(--font-size-sm) !important;
}

@media (max-width: 900px) {
  .admin-aside {
    width: 100%;
    position: static;
  }
}
</style>
