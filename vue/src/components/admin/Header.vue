<template>
  <header class="admin-header">
    <!-- 左侧：logo + 标题 -->
    <div class="header-brand">
      <div class="brand-icon">
        <i class="el-icon-s-grid"></i>
      </div>
      <div class="brand-text">
        <h1 class="header-title">后台管理</h1>
        <span class="header-subtitle">内容与用户运营中心</span>
      </div>
    </div>

    <div class="header-spacer"></div>

    <!-- 返回主页 -->
    <el-button
      @click="$router.push('/front/home')"
      type="primary"
      size="small"
      class="back-btn"
    >
      <i class="el-icon-house"></i>
      返回主页
    </el-button>

    <!-- 分割线 -->
    <div class="header-divider"></div>

    <!-- 用户下拉 -->
    <el-dropdown trigger="click" placement="bottom-end">
      <div class="user-info">
        <el-avatar
          :src="user.avatar || defaultAvatar"
          :size="32"
          @error="handleAvatarError"
          class="user-avatar"
        />
        <span class="user-name">{{ user.nickName || user.username || '管理员' }}</span>
        <i class="el-icon-arrow-down arrow-icon"></i>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="$router.push('/person')">
            <i class="el-icon-user"></i> 个人信息
          </el-dropdown-item>
          <el-dropdown-item divided @click="handleLogout">
            <i class="el-icon-switch-button"></i> 退出系统
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </header>
</template>

<script>
export default {
  name: "AdminHeader",
  data() {
    return {
      user: {},
      defaultAvatar: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
    };
  },
  created() {
    const str = sessionStorage.getItem("user") || "{}";
    this.user = JSON.parse(str);
  },
  methods: {
    handleAvatarError(e) {
      e.target.src = this.defaultAvatar;
    },
    handleLogout() {
      sessionStorage.removeItem("user");
      this.$message.success('已退出系统');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.admin-header {
  height: 60px;
  padding: 0 var(--spacing-xl);
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  background: rgba(255, 255, 255, 0.96);
  border-bottom: 1px solid var(--border-light);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  position: sticky;
  top: 0;
  z-index: var(--z-sticky);
  box-shadow: var(--shadow-sm);
}

/* ── 品牌区域 ── */
.header-brand {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  flex-shrink: 0;
}

.brand-icon {
  width: 36px;
  height: 36px;
  background: var(--primary-gradient);
  border-radius: var(--radius-base);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 3px 8px rgba(15, 118, 110, 0.3);
}

.brand-icon i {
  font-size: 18px;
  color: #fff;
}

.brand-text {
  display: flex;
  flex-direction: column;
  gap: 1px;
}

.header-title {
  margin: 0;
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  line-height: 1.2;
}

.header-subtitle {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
  line-height: 1;
}

.header-spacer { flex: 1; }

/* ── 返回按钮 ── */
.back-btn {
  border-radius: var(--radius-sm) !important;
  font-size: var(--font-size-sm) !important;
  padding: 6px 14px !important;
}

/* ── 分割线 ── */
.header-divider {
  width: 1px;
  height: 24px;
  background: var(--border-light);
}

/* ── 用户信息 ── */
.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: 5px var(--spacing-base);
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all var(--transition-base);
  flex-shrink: 0;
}

.user-info:hover {
  background: var(--bg-hover);
}

.user-avatar {
  border: 1.5px solid var(--border-light);
  transition: border-color var(--transition-base);
}

.user-info:hover .user-avatar {
  border-color: var(--primary-color);
}

.user-name {
  max-width: 110px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--text-primary);
}

.arrow-icon {
  font-size: 11px;
  color: var(--text-tertiary);
  transition: transform var(--transition-base);
}

.user-info:hover .arrow-icon {
  transform: rotate(180deg);
}

/* ── 响应式 ── */
@media (max-width: 768px) {
  .admin-header {
    padding: 0 var(--spacing-md);
    gap: var(--spacing-sm);
  }

  .header-subtitle,
  .user-name,
  .header-divider {
    display: none;
  }

  .brand-text .header-title {
    font-size: var(--font-size-base);
  }
}
</style>
