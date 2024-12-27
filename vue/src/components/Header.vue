<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex; align-items: center; padding: 0 20px;">
    <!-- 左侧“后台管理”文本 -->
    <div style="font-weight: bold; color: dodgerblue; font-size: 18px;">后台管理</div>

    <!-- 返回主页按钮 -->
    <el-button 
      style="margin-left: 20px; height: 30px; line-height: 30px; padding: 0 15px 0 0; display: flex; align-items: center; justify-content: center;" 
      @click="$router.push('/front/home')" 
      type="primary" 
      icon="el-icon-house"
    >
      返回主页
    </el-button>

    <!-- 空白区域，用于将右侧内容推到右边 -->
    <div style="flex: 1;"></div>

    <!-- 用户信息（头像 + 下拉菜单） -->
    <el-dropdown trigger="click" placement="bottom-end">
      <div style="display: flex; align-items: center; cursor: pointer; gap: 10px;">
        <!-- 用户头像 -->
        <el-avatar 
          :src="user.avatar || defaultAvatar" 
          size="32" 
          @error="handleAvatarError"
        ></el-avatar>
        <!-- 下拉箭头 -->
        <i class="el-icon-arrow-down el-icon--right"></i>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <!-- 个人信息菜单项 -->
          <el-dropdown-item @click="$router.push('/person')">
            <i class="el-icon-user"></i> 个人信息
          </el-dropdown-item>
          <!-- 退出系统菜单项 -->
          <el-dropdown-item @click="$router.push('/login')">
            <i class="el-icon-switch-button"></i> 退出系统
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {
      user: {}, // 用户信息
      defaultAvatar: "http://ali-img.kaiyanapp.com/578707a1c3a46cbb8bd6018466739d0e.jpeg?imageMogr2/quality/60/format/jpg" // 默认头像
    };
  },
  created() {
    // 获取用户信息
    let str = sessionStorage.getItem("user") || "{}";
    this.user = JSON.parse(str);
  },
  methods: {
    // 如果头像加载失败，切换到默认头像
    handleAvatarError(e) {
      e.target.src = this.defaultAvatar;
    }
  }
};
</script>

<style scoped>
/* 用户下拉菜单样式调整 */
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 0 ;
  border-radius: 15px;
  transition: all 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}


:deep(.el-badge__content) {
  background-color: #F56C6C;
}

:deep(.el-avatar) {
  background-color: #409EFF;
  color: #fff;
}

.notice-icon {
  display: flex;
  align-items: center;
  margin-right: 10px;
  font-size: 18px;
  color: #F56C6C;
  cursor: pointer;
}
</style>
