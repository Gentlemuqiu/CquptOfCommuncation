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
              prefix-icon="el-icon-user-solid"
              class="auth-input"
              clearable
            />
          </el-form-item>

          <el-form-item prop="phone">
            <el-input
              v-model="form.phone"
              placeholder="手机号"
              prefix-icon="el-icon-mobile-phone"
              class="auth-input"
              clearable
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              placeholder="密码（至少 6 位）"
              prefix-icon="el-icon-lock"
              show-password
              class="auth-input"
            />
          </el-form-item>

          <el-form-item prop="confirm">
            <el-input
              v-model="form.confirm"
              placeholder="再次输入密码"
              prefix-icon="el-icon-lock"
              show-password
              class="auth-input"
              @keyup.enter="register"
            />
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
import request from "@/utils/request";

export default {
  name: "Register",
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
          const res = await request.post("/user/register", this.form);
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
/* ── 整体页面 ── */
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--nav-bg);
  position: relative;
  overflow: hidden;
  padding: var(--spacing-xl) var(--spacing-md);
}

/* ── 背景装饰球 ── */
.bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.bg-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.15;
}

.bg-orb-1 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, var(--primary-lighter) 0%, transparent 70%);
  top: -150px;
  left: -150px;
  animation: float-orb 20s ease-in-out infinite;
}

.bg-orb-2 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, #3b82f6 0%, transparent 70%);
  bottom: -100px;
  right: -100px;
  animation: float-orb 25s ease-in-out infinite reverse;
}

.bg-orb-3 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, #8b5cf6 0%, transparent 70%);
  top: 50%;
  left: 60%;
  transform: translate(-50%, -50%);
  animation: float-orb 18s ease-in-out infinite 5s;
}

@keyframes float-orb {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(30px, -20px) scale(1.05); }
  66% { transform: translate(-20px, 30px) scale(0.95); }
}

/* ── 卡片容器 ── */
.auth-wrapper {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 440px;
}

.auth-card {
  background: rgba(255, 255, 255, 0.97);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border-radius: var(--radius-2xl);
  padding: var(--spacing-3xl) var(--spacing-2xl);
  box-shadow: 0 32px 80px rgba(0, 0, 0, 0.35), 0 0 0 1px rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: transform var(--transition-base);
}

.auth-card:hover {
  transform: translateY(-4px);
}

/* ── 卡片顶部 ── */
.auth-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.logo-wrap {
  width: 64px;
  height: 64px;
  background: var(--primary-gradient);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto var(--spacing-md);
  box-shadow: var(--shadow-primary);
}

.auth-logo {
  width: 36px;
  height: 36px;
  object-fit: contain;
  filter: brightness(0) invert(1);
}

.auth-title {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-bold);
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 var(--spacing-sm);
  line-height: var(--line-height-tight);
}

.auth-subtitle {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  margin: 0;
  line-height: var(--line-height-base);
}

/* ── 表单 ── */
.auth-form {
  margin-top: var(--spacing-lg);
}

.auth-input :deep(.el-input__wrapper) {
  height: 48px;
  border-radius: var(--radius-base) !important;
  border: 1.5px solid var(--border-light) !important;
  box-shadow: none !important;
  background: var(--bg-secondary) !important;
  transition: all var(--transition-base) !important;
}

.auth-input :deep(.el-input__wrapper:hover) {
  border-color: var(--primary-lighter) !important;
  background: var(--bg-primary) !important;
}

.auth-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary-color) !important;
  background: var(--bg-primary) !important;
  box-shadow: 0 0 0 3px rgba(15, 118, 110, 0.1) !important;
}

.auth-input :deep(.el-input__inner) {
  font-size: var(--font-size-base);
  color: var(--text-primary);
}

/* 提交按钮 */
.auth-submit-btn {
  width: 100%;
  height: 50px;
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  border-radius: var(--radius-base) !important;
  background: var(--primary-gradient) !important;
  border: none !important;
  letter-spacing: 0.5px;
  transition: all var(--transition-cubic) !important;
  box-shadow: var(--shadow-primary) !important;
}

.auth-submit-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: var(--shadow-primary-lg) !important;
  filter: brightness(1.06) !important;
}

.auth-submit-btn:active {
  transform: translateY(0) !important;
}

/* ── 底部链接 ── */
.auth-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-lighter);
}

.footer-hint {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
}

.footer-link {
  font-size: var(--font-size-sm) !important;
  font-weight: var(--font-weight-medium) !important;
  color: var(--primary-color) !important;
}
</style>
