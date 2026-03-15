<template>
  <header class="admin-header">
    <!-- 左侧：logo + 标题 -->
    <div class="header-brand">
      <div class="brand-icon">
        <el-icon><Grid /></el-icon>
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
      <el-icon><House /></el-icon>
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
        <el-icon class="arrow-icon"><ArrowDown /></el-icon>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="$router.push('/front/person')">
            <el-icon><User /></el-icon> 个人信息
          </el-dropdown-item>
          <el-dropdown-item divided @click="handleLogout">
            <el-icon><SwitchButton /></el-icon> 退出系统
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </header>
</template>

<script>
import { Grid, House, ArrowDown, User, SwitchButton } from '@element-plus/icons-vue'
import { getCurrentUser, clearUser } from '@/utils/auth'

export default {
  name: 'AdminHeader',
  components: { Grid, House, ArrowDown, User, SwitchButton },
  data() {
    return {
      user: {},
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  created() {
    this.user = getCurrentUser()
  },
  methods: {
    handleAvatarError(e) {
      e.target.src = this.defaultAvatar
    },
    handleLogout() {
      clearUser()
      this.$message.success('已退出系统')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.admin-header {
  height: 64px;
  padding: 0 var(--spacing-2xl);
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-lighter);
  position: sticky;
  top: 0;
  z-index: var(--z-sticky);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
}

.admin-header::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 3px;
  background: var(--primary-gradient);
  opacity: 0.9;
}

/* ── 品牌区域 ── */
.header-brand {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  flex-shrink: 0;
}

.brand-icon {
  width: 40px;
  height: 40px;
  background: var(--primary-gradient);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(15, 118, 110, 0.35);
  color: #fff;
  font-size: 20px;
}

.brand-text {
  display: flex;
  flex-direction: column;
  gap: 1px;
}

.header-title {
  margin: 0;
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  line-height: 1.25;
  letter-spacing: -0.02em;
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
  gap: 4px;
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

/* ── 下拉菜单图标 ── */
:deep(.el-dropdown-menu__item .el-icon) {
  margin-right: var(--spacing-sm);
  color: var(--text-secondary);
}

/* ── 响应式 ── */
@media (max-width: 768px) {
  .admin-header {
    padding: 0 var(--spacing-md);
    gap: var(--spacing-sm);
  }
  .header-subtitle, .user-name, .header-divider { display: none; }
  .brand-text .header-title { font-size: var(--font-size-base); }
}
</style>
