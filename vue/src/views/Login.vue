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
          <h1 class="auth-title">欢迎登录</h1>
          <p class="auth-subtitle">就业信息中心 · 助力职业发展</p>
        </div>

        <el-form
          ref="form"
          :model="form"
          size="large"
          :rules="rules"
          class="auth-form"
        >
          <el-form-item prop="username">
            <el-input
              prefix-icon="el-icon-user-solid"
              v-model="form.username"
              placeholder="请输入用户名"
              class="auth-input"
              clearable
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              prefix-icon="el-icon-lock"
              v-model="form.password"
              placeholder="请输入密码"
              show-password
              class="auth-input"
              @keyup.enter="login"
            />
          </el-form-item>

          <el-form-item class="role-group">
            <el-radio-group v-model="form.role" class="role-radio">
              <el-radio :value="1" border class="role-item">
                <i class="el-icon-s-tools"></i> 管理员
              </el-radio>
              <el-radio :value="2" border class="role-item">
                <i class="el-icon-user"></i> 普通用户
              </el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item>
            <el-button
              :loading="loading"
              class="auth-submit-btn"
              type="primary"
              @click="login"
              size="large"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登录中...</span>
            </el-button>
          </el-form-item>
        </el-form>

        <div class="auth-footer">
          <el-link type="primary" @click="$router.push('/pass')" :underline="false" class="footer-link">
            忘记密码？
          </el-link>
          <span class="footer-divider">|</span>
          <el-link type="primary" @click="$router.push('/register')" :underline="false" class="footer-link">
            立即注册 →
          </el-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Login",
  data() {
    return {
      form: { username: "", password: "", role: 1 },
      loading: false,
      rules: {
        username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  created() {
    sessionStorage.removeItem("user");
  },
  methods: {
    async login() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return;
        this.loading = true;
        try {
          const res = await request.post("/user/login", this.form);
          if (res.code === "0") {
            this.$message.success("登录成功");
            sessionStorage.setItem("user", JSON.stringify(res.data));
            this.$router.push(res.data.role !== 1 ? "/front/home" : "/");
          } else {
            this.$message.error(res.msg);
          }
        } catch {
          this.$message.error("网络错误，请稍后再试");
        } finally {
          this.loading = false;
        }
      });
    },
  },
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
  right: -100px;
  animation: float-orb 22s ease-in-out infinite;
}

.bg-orb-2 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, #3b82f6 0%, transparent 70%);
  bottom: -100px;
  left: -100px;
  animation: float-orb 28s ease-in-out infinite reverse;
}

.bg-orb-3 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, #8b5cf6 0%, transparent 70%);
  top: 40%;
  right: 30%;
  animation: float-orb 16s ease-in-out infinite 3s;
}

@keyframes float-orb {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(-25px, 20px) scale(1.04); }
  66% { transform: translate(20px, -25px) scale(0.96); }
}

/* ── 卡片容器 ── */
.auth-wrapper {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
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

/* 角色选择 */
.role-group { margin-bottom: var(--spacing-md); }

.role-radio {
  width: 100%;
  display: flex;
  gap: var(--spacing-base);
}

.role-item {
  flex: 1;
  margin: 0 !important;
  justify-content: center;
}

.role-radio :deep(.el-radio) {
  border-radius: var(--radius-base) !important;
  border: 1.5px solid var(--border-light) !important;
  transition: all var(--transition-base) !important;
  height: 42px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.role-radio :deep(.el-radio.is-checked) {
  border-color: var(--primary-color) !important;
  background: var(--primary-gradient-soft) !important;
}

.role-radio :deep(.el-radio__input.is-checked .el-radio__inner) {
  background-color: var(--primary-color) !important;
  border-color: var(--primary-color) !important;
}

.role-radio :deep(.el-radio__input.is-checked + .el-radio__label) {
  color: var(--primary-color) !important;
  font-weight: var(--font-weight-semibold) !important;
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
  letter-spacing: 1px;
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
  gap: var(--spacing-md);
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-lighter);
}

.footer-link {
  font-size: var(--font-size-sm) !important;
  font-weight: var(--font-weight-medium) !important;
  color: var(--primary-color) !important;
}

.footer-divider {
  color: var(--border-base);
  font-size: var(--font-size-sm);
}
</style>
