<template>
  <div class="auth-page">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="bg-orb bg-orb-1"></div>
      <div class="bg-orb bg-orb-2"></div>
      <div class="bg-orb bg-orb-3"></div>
    </div>

    <div class="auth-wrapper">
      <div class="auth-card">
        <!-- 顶部 Logo -->
        <div class="auth-header">
          <div class="logo-wrap">
            <img src="../assets/image/cqupt.svg" alt="Logo" class="auth-logo">
          </div>
          <h1 class="auth-title">创建账号</h1>
          <p class="auth-subtitle">加入就业信息中心，开启职业新旅程</p>
        </div>

        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          size="large"
          class="auth-form"
          label-position="top"
        >
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="用户名（3-15 个字符）"
              class="auth-input"
              clearable
            >
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item prop="phone">
            <el-input
              v-model="form.phone"
              placeholder="手机号"
              class="auth-input"
              clearable
            >
              <template #prefix><el-icon><Phone /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              placeholder="密码（至少 6 位）"
              show-password
              class="auth-input"
            >
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item prop="confirm">
            <el-input
              v-model="form.confirm"
              placeholder="再次输入密码"
              show-password
              class="auth-input"
              @keyup.enter="register"
            >
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button
              :loading="loading"
              type="primary"
              class="auth-submit-btn"
              @click="register"
              size="large"
            >
              <span v-if="!loading">立即注册</span>
              <span v-else>注册中...</span>
            </el-button>
          </el-form-item>
        </el-form>

        <div class="auth-footer">
          <span class="footer-hint">已有账号？</span>
          <el-link type="primary" @click="$router.push('/login')" :underline="false" class="footer-link">
            前往登录 →
          </el-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { register as registerUser } from '@/api/user'
import { User, Phone, Lock } from '@element-plus/icons-vue';

export default {
  name: "Register",
  components: { User, Phone, Lock },
  data() {
    return {
      form: {
        username: "",
        phone: "",
        password: "",
        confirm: ""
      },
      loading: false,
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 15, message: '用户名长度应在 3~15 个字符之间', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
        ],
        confirm: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.form.password) {
                callback(new Error('两次输入的密码不一致'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ]
      }
    };
  },
  methods: {
    async register() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return;
        this.loading = true;
        try {
          const res = await registerUser(this.form);
          if (res.code === '0') {
            this.$message.success("注册成功，欢迎加入！");
            this.$router.push("/login");
          } else {
            this.$message.error(res.msg);
          }
        } catch {
          this.$message.error("网络错误，请稍后再试");
        } finally {
          this.loading = false;
        }
      });
    }
  }
};
</script>

<style scoped>
/* 注册页卡片稍宽 */
.auth-wrapper { max-width: 440px; }

/* ── 背景装饰球（Register 专属位置） ── */
.bg-orb-1 {
  width: 500px; height: 500px;
  background: radial-gradient(circle, var(--primary-lighter) 0%, transparent 70%);
  top: -150px; left: -150px;
  animation: float-orb 20s ease-in-out infinite;
}
.bg-orb-2 {
  width: 400px; height: 400px;
  background: radial-gradient(circle, #3b82f6 0%, transparent 70%);
  bottom: -100px; right: -100px;
  animation: float-orb 25s ease-in-out infinite reverse;
}
.bg-orb-3 {
  width: 300px; height: 300px;
  background: radial-gradient(circle, #8b5cf6 0%, transparent 70%);
  top: 50%; left: 60%;
  transform: translate(-50%, -50%);
  animation: float-orb 18s ease-in-out infinite 5s;
}
</style>
