<template>
  <div class="bg-container">
    <div class="login-container">
      <div class="login-title">欢迎登录 CQUPT 交流论坛</div>
      <el-form ref="form" :model="form" size="normal" :rules="rules">
        <!-- 用户名输入框 -->
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user-solid" v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <!-- 密码输入框 -->
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="form.password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <!-- 角色选择 -->
        <el-form-item>
          <el-radio-group v-model="form.role">
            <el-radio :label="1">管理员</el-radio>
            <el-radio :label="2">普通用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button :loading="loading" class="login-button" type="primary" @click="login">登 录</el-button>
        </el-form-item>
        <!-- 链接跳转 -->
        <el-form-item class="link-buttons">
          <el-button type="text" @click="$router.push('/pass')">忘记密码</el-button>
          <el-button type="text" @click="$router.push('/register')">前往注册 &gt;&gt;</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Login",
  data() {
    return {
      form: { username: "", password: "", role: 1 }, // 初始化登录表单
      loading: false, // 登录按钮加载状态
      rules: {
        username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  created() {
    sessionStorage.removeItem("user"); // 清除缓存用户信息
  },
  methods: {
    async login() {
      // 表单校验
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.loading = true; // 开启加载状态
          try {
            const res = await request.post("/user/login", this.form);
            if (res.code === "0") {
              this.$message.success("登录成功");
              sessionStorage.setItem("user", JSON.stringify(res.data)); // 缓存用户信息
              const redirectPath = res.data.role !== 1 ? "/front/home" : "/";
              this.$router.push(redirectPath);
            } else {
              this.$message.error(res.msg);
            }
          } catch (error) {
            this.$message.error("网络错误，请稍后再试");
          } finally {
            this.loading = false; // 关闭加载状态
          }
        }
      });
    },
  },
};
</script>

<style scoped>
/* 背景容器 */
.bg-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  width: 100%;
  height: 100vh;
  background: url("../assets/image/bg.png") no-repeat center center;
  background-size: cover; /* 背景图片全屏填充 */
  background-color: LightYellow; /* 背景颜色 */
}

/* 登录容器 */
.login-container {
  width: 400px;
  background-color: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
.login-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  color: dodgerblue;
  margin-bottom: 20px;
}

/* 登录按钮样式 */
.login-button {
  width: 100%;
}

/* 链接按钮容器 */
.link-buttons {
  text-align: center;
}
</style>
