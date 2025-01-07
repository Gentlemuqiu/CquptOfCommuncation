<template>
  <div style="background-color: #eee; min-height: 100vh">
    <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex; background-color: #fff">
      <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex; background-color: #fff">
        <img src="../assets/image/cqupt.svg" alt="Logo" style="width: 40px; height: 40px; margin-top: 5px;">
        <div style="color:#1D6849; font-weight: bold; font-size: 25px; cursor: pointer" @click="$router.push('/front/home')">CQUPT交流论坛</div>
      </div>

      <div style="width: 300px">
        <ul class="menu-box">
          <li :class="{'active': $route.path === '/front/home'}" @click="$router.push('/front/home')">首页</li>
        </ul>
      </div>

      <div style="flex: 1">
        <el-input 
          style="width: 300px; margin-left: 100px" 
          v-model="name" 
          clearable 
          placeholder="请输入搜索内容"
          @keyup.enter="search" 
        />
        <el-button style="margin-left: 5px" type="primary" @click="search">搜索</el-button>
      </div>

      <div style="width: 130px; display: flex; align-items: center; justify-content: flex-end;">
        <span @click="showNotice" style="margin-right: 10px; color: orangered; cursor: pointer; display: flex; align-items: center;">
          <i class="el-icon-bell" style="font-size:18px; margin-right: 5px;"></i> {{ count }}
        </span>
        <el-button 
          v-if="!user.id" 
          @click="$router.push('/login')"
          type="primary"
          size="small"
        >登录</el-button>

        <!-- 已登录状态 -->
        <el-dropdown v-else trigger="click">
          <div class="user-info">
            <el-avatar 
              :size="32"
              :src="user.avatar || defaultAvatar"
              @error="handleAvatarError"
            >
              {{ user.nickName ? user.nickName[0].toUpperCase() : 'U' }}
            </el-avatar>
            <i class="el-icon-arrow-down el-icon--right"></i>
          </div>

          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="$router.push('/sayings')" v-if="user.role === 1">
                <i class="el-icon-setting"></i> 后台管理
              </el-dropdown-item>
              <el-dropdown-item @click="$router.push('/front/littleSaying')" v-if="user.role === 2">
                <i class="el-icon-setting"></i> 新增帖子
              </el-dropdown-item>
              <el-dropdown-item @click="$router.push('/front/person')">
                <i class="el-icon-user"></i> 个人信息
              </el-dropdown-item>
              <el-dropdown-item @click="$router.push('/front/collect')">
                <i class="el-icon-star-off"></i> 我收藏的评论
              </el-dropdown-item>
              <el-dropdown-item @click="$router.push('/front/collectedContent')">
                <i class="el-icon-collection"></i> 我收藏的内容
              </el-dropdown-item>
              <el-dropdown-item @click="$router.push('/front/subscribePerson')">
                <i class="el-icon-coordinate"></i> 我关注的人
              </el-dropdown-item>
              <el-dropdown-item divided @click="$router.push('/login')">
                <i class="el-icon-switch-button"></i> 退出论坛
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <el-row>
      <el-col :span="18" :offset="3">
        <router-view />
      </el-col>
    </el-row>

    <!-- 公告弹框 -->
    <el-dialog title="公告" v-model="dialogVisible" width="40%">
      <div style="padding: 0 20px">
        <el-collapse v-model="active">
          <el-collapse-item :title="item.name" :name="index+''" v-for="(item, index) in notice" :key="item.id">
            <div style="font-size: 12px; color: #888">
              {{ item.content }}
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-dialog>
  </div>
</template>

<script>import request from "@/utils/request";

export default {
  name: "Layout",
  data() {
    return {
      user: {},
      name: '',
      count: 1,
      notice: [],
      dialogVisible: false,
      active: '0'
    }
  },
  created() {
    this.initUserInfo();
    this.initNotice();
    // 添加事件监听器
    window.addEventListener('storage', this.handleStorageChange);
    // 添加自定义事件监听器
    window.addEventListener('userUpdate', this.handleUserUpdate);
  },
  destroyed() {
    // 清理事件监听器
    window.removeEventListener('storage', this.handleStorageChange);
    window.removeEventListener('userUpdate', this.handleUserUpdate);
  },
  methods: {
    initUserInfo() {
      let str = sessionStorage.getItem("user") || "{}";
      this.user = JSON.parse(str);
    },
    initNotice() {
      request.get('/notice').then(res => {
        this.notice = res.data.filter(v => v.status === '已发布');
        this.count = this.notice.length;
      });
    },
    handleStorageChange(e) {
      if (e.key === 'user') {
        this.initUserInfo();
      }
    },
    handleUserUpdate(e) {
      // 处理自定义事件
      this.initUserInfo();
    },
    showNotice() {
      this.dialogVisible = true;
    },
    search() {
      this.$router.push({
        path: '/front/search',
        query: {
          name: this.name.trim()
        }
      });
    },
    handleAvatarError(e) {
      e.target.src = this.defaultAvatar;
    },
    handleClose(done) {
      done();
    }
  },
  // 添加watch来监听sessionStorage的变化
  watch: {
    '$route': {
      handler() {
        this.initUserInfo();
      },
      immediate: true
    }
  }
}
</script>

<style scoped>
.menu-box {
  list-style: none;
  display: flex;
  margin-left: 70px;
}
.menu-box li {
  padding: 0 20px;
  text-align: center;
  cursor: pointer;
  color: #666;
}
.menu-box li:hover {
  color: dodgerblue;
}
.active {
  background-color: dodgerblue;
  color: white !important;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 0 5px;
  border-radius: 15px;
  transition: all 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.el-dropdown-menu__item i {
  margin-right: 8px;
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
