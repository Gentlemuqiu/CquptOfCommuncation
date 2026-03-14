<template>
  <aside class="admin-aside">
    <!-- 侧边栏标题 -->
    <div class="aside-header">
      <div class="aside-title">
        <i class="el-icon-menu"></i>
        <span>功能导航</span>
      </div>
    </div>

    <!-- 导航菜单 -->
    <el-menu :default-active="path" router class="aside-menu" unique-opened>
      <!-- 系统管理分组 -->
      <el-sub-menu index="system" v-if="systemItems.length">
        <template #title>
          <i class="el-icon-setting"></i>
          <span>系统管理</span>
        </template>
        <el-menu-item
          v-for="item in systemItems"
          :key="item.path"
          :index="item.path"
          class="sub-menu-item"
        >
          <i :class="item.icon"></i>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-sub-menu>

      <!-- 普通菜单项 -->
      <el-menu-item
        v-for="item in normalItems"
        :key="item.path"
        :index="item.path"
        class="menu-item"
      >
        <i :class="item.icon"></i>
        <span>{{ item.label }}</span>
      </el-menu-item>
    </el-menu>
  </aside>
</template>

<script>
import request from "@/utils/request";
import { ADMIN_SIDEBAR_ITEMS } from "@/config/navigation";

export default {
  name: "AdminAside",
  data() {
    return {
      user: {},
      path: this.$route.path
    };
  },
  computed: {
    filteredItems() {
      return ADMIN_SIDEBAR_ITEMS.filter(item => {
        if (!item.requireRole) return true;
        return item.requireRole === this.user.role;
      });
    },
    systemItems() {
      return this.filteredItems.filter(item => item.group === '系统管理');
    },
    normalItems() {
      return this.filteredItems.filter(item => item.group !== '系统管理');
    }
  },
  created() {
    const userStr = sessionStorage.getItem("user") || "{}";
    this.user = JSON.parse(userStr);

    if (this.user.id) {
      request.get("/user/" + this.user.id).then(res => {
        if (res.code === '0') {
          this.user = res.data;
        }
      });
    }
  }
};
</script>

<style scoped>
.admin-aside {
  width: 220px;
  flex-shrink: 0;
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-base);
  overflow: hidden;
  align-self: flex-start;
  position: sticky;
  top: calc(60px + var(--spacing-lg));
}

/* ── 侧边栏顶部标题 ── */
.aside-header {
  background: var(--primary-gradient);
  padding: var(--spacing-md) var(--spacing-lg);
}

.aside-title {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  color: rgba(255, 255, 255, 0.95);
  font-weight: var(--font-weight-semibold);
  font-size: var(--font-size-sm);
  letter-spacing: 0.5px;
}

.aside-title i {
  font-size: 16px;
}

/* ── 菜单 ── */
.aside-menu {
  border-right: none !important;
  padding: var(--spacing-sm) 0;
}

/* 菜单项基础样式 */
.aside-menu :deep(.el-menu-item),
.aside-menu :deep(.el-sub-menu__title) {
  height: 46px;
  line-height: 46px;
  border-radius: var(--radius-sm);
  margin: 2px var(--spacing-sm);
  width: calc(100% - var(--spacing-md));
  transition: all var(--transition-cubic) !important;
  font-size: var(--font-size-base) !important;
  color: var(--text-secondary) !important;
}

.aside-menu :deep(.el-menu-item i),
.aside-menu :deep(.el-sub-menu__title i) {
  margin-right: var(--spacing-sm);
  font-size: 15px;
  transition: transform var(--transition-cubic);
}

/* 悬停状态 */
.aside-menu :deep(.el-menu-item:hover),
.aside-menu :deep(.el-sub-menu__title:hover) {
  background: var(--bg-hover) !important;
  color: var(--primary-color) !important;
}

.aside-menu :deep(.el-menu-item:hover i),
.aside-menu :deep(.el-sub-menu__title:hover i) {
  transform: scale(1.1);
}

/* 激活状态 */
.aside-menu :deep(.el-menu-item.is-active) {
  background: var(--primary-gradient-soft) !important;
  color: var(--primary-color) !important;
  font-weight: var(--font-weight-semibold) !important;
  box-shadow: inset 3px 0 0 var(--primary-color);
}

.aside-menu :deep(.el-menu-item.is-active i) {
  color: var(--primary-color) !important;
}

/* 子菜单 */
.aside-menu :deep(.el-sub-menu .el-menu) {
  background: var(--bg-secondary) !important;
  padding: var(--spacing-xs) 0;
}

.aside-menu :deep(.el-sub-menu .el-menu-item) {
  height: 40px !important;
  line-height: 40px !important;
  padding-left: 48px !important;
  font-size: var(--font-size-sm) !important;
}

/* ── 响应式 ── */
@media (max-width: 900px) {
  .admin-aside {
    width: 100%;
    position: static;
  }
}
</style>
