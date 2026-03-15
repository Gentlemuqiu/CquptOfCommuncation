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
          <h1 class="auth-title">找回密码</h1>
          <p class="auth-subtitle">验证账号信息，重置你的登录密码</p>
        </div>

        <!-- 步骤进度条 -->
        <div class="steps-bar">
          <div class="step" :class="{ active: step >= 1, done: step > 1 }">
            <div class="step-dot">
              <el-icon v-if="step > 1"><Check /></el-icon>
              <span v-else>1</span>
            </div>
            <span class="step-label">验证身份</span>
          </div>
          <div class="step-line" :class="{ active: step > 1 }"></div>
          <div class="step" :class="{ active: step >= 2 }">
            <div class="step-dot">
              <span>2</span>
            </div>
            <span class="step-label">重置密码</span>
          </div>
        </div>

        <!-- Step 1: 验证身份 -->
        <el-form
          v-if="step === 1"
          ref="verifyForm"
          :model="verifyData"
          :rules="verifyRules"
          size="large"
          class="auth-form"
        >
          <el-form-item prop="username">
            <el-input
              v-model="verifyData.username"
              placeholder="请输入用户名"
              class="auth-input"
              clearable
            >
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item prop="phone">
            <el-input
              v-model="verifyData.phone"
              placeholder="请输入注册时的手机号"
              class="auth-input"
              clearable
              maxlength="11"
            >
              <template #prefix><el-icon><Phone /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              class="auth-submit-btn"
              :loading="loading"
              @click="handleVerify"
            >
              <span v-if="!loading">验证账号</span>
              <span v-else>验证中...</span>
            </el-button>
          </el-form-item>
        </el-form>

        <!-- Step 2: 重置密码 -->
        <el-form
          v-else
          ref="resetForm"
          :model="resetData"
          :rules="resetRules"
          size="large"
          class="auth-form"
        >
          <div class="verified-tip">
            <el-icon class="tip-icon"><CircleCheckFilled /></el-icon>
            身份验证通过，请设置新密码
          </div>

          <el-form-item prop="password">
            <el-input
              v-model="resetData.password"
              placeholder="请输入新密码（至少 6 位）"
              show-password
              class="auth-input"
            >
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item prop="confirm">
            <el-input
              v-model="resetData.confirm"
              placeholder="再次确认新密码"
              show-password
              class="auth-input"
              @keyup.enter="handleReset"
            >
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              class="auth-submit-btn"
              :loading="loading"
              @click="handleReset"
            >
              <span v-if="!loading">确认重置</span>
              <span v-else>重置中...</span>
            </el-button>
          </el-form-item>
        </el-form>

        <div class="auth-footer">
          <el-link type="primary" @click="$router.push('/login')" :underline="false" class="footer-link">
            ← 返回登录
          </el-link>
          <span class="footer-divider">|</span>
          <el-link type="primary" @click="$router.push('/register')" :underline="false" class="footer-link">
            立即注册
          </el-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { resetPassVerify, resetPass } from '@/api/user'
import { User, Phone, Lock, Check, CircleCheckFilled } from '@element-plus/icons-vue';

export default {
  name: "FindPassword",
  components: { User, Phone, Lock, Check, CircleCheckFilled },
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.resetData.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };
    return {
      step: 1,
      loading: false,
      verifyData: { username: '', phone: '' },
      resetData: { password: '', confirm: '' },
      verifyRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
        ]
      },
      resetRules: {
        password: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
        ],
        confirm: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    async handleVerify() {
      const valid = await this.$refs.verifyForm.validate().catch(() => false);
      if (!valid) return;
      this.loading = true;
      try {
        const res = await resetPassVerify(this.verifyData);
        if (res.code === '0') {
          this.$message.success('身份验证通过');
          this.step = 2;
        } else {
          this.$message.error(res.msg || '验证失败，请检查用户名和手机号');
        }
      } catch {
        this.$message.error('网络错误，请稍后重试');
      } finally {
        this.loading = false;
      }
    },

    async handleReset() {
      const valid = await this.$refs.resetForm.validate().catch(() => false);
      if (!valid) return;
      this.loading = true;
      try {
        const res = await resetPass({
          username: this.verifyData.username,
          phone: this.verifyData.phone,
          password: this.resetData.password
        });
        if (res.code === '0') {
          this.$message.success('密码重置成功，请重新登录');
          this.$router.push('/login');
        } else {
          this.$message.error(res.msg || '重置失败');
        }
      } catch {
        this.$message.error('网络错误，请稍后重试');
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
/* ── 背景装饰球 ── */
.bg-orb-1 {
  width: 480px; height: 480px;
  background: radial-gradient(circle, var(--primary-lighter) 0%, transparent 70%);
  top: -120px; left: -120px;
  animation: float-orb 20s ease-in-out infinite;
}
.bg-orb-2 {
  width: 400px; height: 400px;
  background: radial-gradient(circle, #3b82f6 0%, transparent 70%);
  bottom: -100px; right: -100px;
  animation: float-orb 26s ease-in-out infinite reverse;
}
.bg-orb-3 {
  width: 280px; height: 280px;
  background: radial-gradient(circle, #8b5cf6 0%, transparent 70%);
  top: 50%; right: 15%;
  animation: float-orb 18s ease-in-out infinite 4s;
}

/* ── 步骤进度条 ── */
.steps-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0;
  margin-bottom: var(--spacing-xl);
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.step-dot {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  background: var(--bg-tertiary);
  color: var(--text-disabled);
  border: 2px solid var(--border-light);
  transition: all var(--transition-base);
}

.step.active .step-dot {
  background: var(--primary-gradient);
  color: #fff;
  border-color: transparent;
  box-shadow: var(--shadow-primary);
}

.step.done .step-dot {
  background: var(--success-color);
  color: #fff;
  border-color: transparent;
}

.step-label {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
  white-space: nowrap;
  transition: color var(--transition-base);
}

.step.active .step-label,
.step.done .step-label {
  color: var(--primary-color);
  font-weight: var(--font-weight-semibold);
}

.step-line {
  flex: 1;
  height: 2px;
  background: var(--border-light);
  margin: 0 var(--spacing-sm);
  margin-bottom: 22px;
  min-width: 40px;
  transition: background var(--transition-base);
}

.step-line.active {
  background: var(--primary-lighter);
}

/* ── 验证通过提示 ── */
.verified-tip {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  background: var(--success-light);
  border: 1px solid rgba(16, 185, 129, 0.25);
  border-radius: var(--radius-base);
  padding: var(--spacing-base) var(--spacing-md);
  margin-bottom: var(--spacing-lg);
  font-size: var(--font-size-sm);
  color: var(--success-color);
  font-weight: var(--font-weight-medium);
}

.tip-icon {
  font-size: 18px;
  flex-shrink: 0;
}
</style>
