<template>
  <div class="user-section">
    <!-- 通知图标 -->
    <el-badge :value="noticeCount" :hidden="noticeCount === 0" class="notice-badge">
      <el-button
        circle
        :icon="Bell"
        class="notice-btn"
        @click="handleNoticeClick"
      />
    </el-badge>

    <!-- 登录按钮 -->
    <el-button
      v-if="!user.id"
      @click="handleLogin"
      type="primary"
      class="login-btn"
    >
      登录
    </el-button>

    <!-- 用户下拉菜单 -->
    <el-dropdown v-else trigger="click" class="user-dropdown">
      <div class="user-info">
        <el-avatar
          :size="38"
          :src="user.avatar || defaultAvatar"
          @error="handleAvatarError"
          class="user-avatar"
        >
          {{ user.nickName ? user.nickName[0].toUpperCase() : 'U' }}
        </el-avatar>
        <span class="user-name">{{ user.nickName || user.username }}</span>
        <el-icon class="dropdown-arrow"><ArrowDown /></el-icon>
      </div>

      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item
            v-for="item in menuItems"
            :key="item.path"
            @click="handleMenuClick(item.path)"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            {{ item.label }}
          </el-dropdown-item>
          <el-dropdown-item divided @click="handleLogout">
            <el-icon><SwitchButton /></el-icon> 退出登录
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script>
import { getUserDropdownItems } from '@/config/navigation';
import { Bell, ArrowDown, SwitchButton } from '@element-plus/icons-vue';

export default {
  name: 'UserMenu',
  components: { Bell, ArrowDown, SwitchButton },
  props: {
    user: {
      type: Object,
      default: () => ({})
    },
    noticeCount: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      Bell,
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  computed: {
    menuItems() {
      return getUserDropdownItems(this.user);
    }
  },
  methods: {
    handleNoticeClick() {
      this.$emit('show-notice');
    },
    handleLogin() {
      this.$router.push('/login');
    },
    handleMenuClick(path) {
      this.$router.push(path);
    },
    handleAvatarError(e) {
      e.target.src = this.defaultAvatar;
    },
    handleLogout() {
      sessionStorage.removeItem("user");
      window.dispatchEvent(new Event('userUpdate'));
      this.$message.success('已退出登录');
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
.user-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  flex-shrink: 0;
}

.notice-badge {
  cursor: pointer;
}

.notice-btn {
  border: none;
  background: transparent;
  color: var(--text-secondary);
  font-size: var(--font-size-xl);
  transition: all var(--transition-base);
  width: 40px;
  height: 40px;
}

.notice-btn:hover {
  color: var(--primary-color);
  background-color: #ecfeff;
}

.login-btn {
  border-radius: var(--radius-full);
  padding: 10px var(--spacing-lg);
  font-weight: var(--font-weight-medium);
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  padding: 6px var(--spacing-base);
  border-radius: var(--radius-full);
  transition: all var(--transition-base);
}

.user-info:hover {
  background-color: var(--bg-hover);
}

.user-avatar {
  border: 2px solid var(--border-light);
  transition: all var(--transition-base);
}

.user-info:hover .user-avatar {
  border-color: var(--primary-color);
}

.user-name {
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  font-weight: var(--font-weight-medium);
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

:deep(.el-badge__content) {
  background-color: var(--danger-color);
  border: 2px solid #fff;
  font-weight: var(--font-weight-semibold);
}

:deep(.el-dropdown-menu__item) {
  padding: 12px 20px;
  transition: all var(--transition-base);
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: var(--bg-hover);
  color: var(--primary-color);
}

.dropdown-arrow {
  font-size: 11px;
  color: var(--text-tertiary);
  transition: transform var(--transition-base);
}

.user-info:hover .dropdown-arrow {
  transform: rotate(180deg);
}

:deep(.el-dropdown-menu__item .el-icon) {
  margin-right: var(--spacing-sm);
  color: var(--text-secondary);
  width: 16px;
  text-align: center;
}

:deep(.el-dropdown-menu__item:hover .el-icon) {
  color: var(--primary-color);
}

@media (max-width: 768px) {
  .user-name {
    display: none;
  }

  .dropdown-arrow {
    display: none;
  }
}
</style>
