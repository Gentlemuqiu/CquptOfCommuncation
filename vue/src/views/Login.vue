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
              v-model="form.username"
              placeholder="请输入用户名"
              class="auth-input"
              clearable
            >
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              placeholder="请输入密码"
              show-password
              class="auth-input"
              @keyup.enter="login"
            >
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
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
import { login } from '@/api/user'
import { clearUser, setUser } from '@/utils/auth'
import { User, Lock } from '@element-plus/icons-vue'

export default {
  name: 'Login',
  components: { User, Lock },
  data() {
    return {
      form: { username: '', password: '' },
      loading: false,
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
      },
    }
  },
  created() {
    clearUser()
  },
  methods: {
    login() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const res = await login(this.form)
          if (res.code === '0') {
            this.$message.success('登录成功')
            setUser(res.data)
            this.$router.push(res.data.role !== 1 ? '/front/home' : '/')
          } else {
            this.$message.error(res.msg)
          }
        } catch {
          this.$message.error('网络错误，请稍后再试')
        } finally {
          this.loading = false
        }
      })
    },
  },
}
</script>

<style scoped>
/* ── 背景装饰球（Login 专属位置） ── */
.bg-orb-1 {
  width: 500px; height: 500px;
  background: radial-gradient(circle, var(--primary-lighter) 0%, transparent 70%);
  top: -150px; right: -100px;
  animation: float-orb 22s ease-in-out infinite;
}
.bg-orb-2 {
  width: 400px; height: 400px;
  background: radial-gradient(circle, #3b82f6 0%, transparent 70%);
  bottom: -100px; left: -100px;
  animation: float-orb 28s ease-in-out infinite reverse;
}
.bg-orb-3 {
  width: 250px; height: 250px;
  background: radial-gradient(circle, #8b5cf6 0%, transparent 70%);
  top: 40%; right: 30%;
  animation: float-orb 16s ease-in-out infinite 3s;
}
</style>
