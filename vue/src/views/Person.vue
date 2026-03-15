<template>
  <div class="page-container">
    <div class="profile-layout">

      <!-- ───────── 左侧：用户卡片 ───────── -->
      <div class="profile-sidebar">
        <div class="sidebar-card">
          <!-- 头像上传 -->
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :on-error="handleAvatarError"
            :before-upload="beforeAvatarUpload"
          >
            <div class="avatar-wrap">
              <img v-if="form.avatar" :src="form.avatar" class="avatar-img" alt="头像" />
              <div v-else class="avatar-placeholder">
                <el-icon class="placeholder-icon"><User /></el-icon>
              </div>
              <div class="avatar-overlay">
                <el-icon><Camera /></el-icon>
                <span>更换头像</span>
              </div>
            </div>
          </el-upload>

          <!-- 姓名 & 用户名 -->
          <div class="user-meta">
            <h3 class="display-name">{{ form.nickName || form.username || '未设置昵称' }}</h3>
            <p class="display-username">@{{ form.username }}</p>
          </div>

          <!-- 个性签名展示 -->
          <div class="bio-preview" v-if="form.bio">
            <p>{{ form.bio }}</p>
          </div>
          <div class="bio-preview empty" v-else>
            <p>暂无个性签名</p>
          </div>

          <!-- 基本信息速览 -->
          <div class="info-chips">
            <div class="chip" v-if="form.sex">
              <el-icon><Male v-if="form.sex === '男'" /><Female v-else-if="form.sex === '女'" /><User v-else /></el-icon>
              <span>{{ form.sex }}</span>
            </div>
            <div class="chip" v-if="form.age">
              <el-icon><Calendar /></el-icon>
              <span>{{ form.age }} 岁</span>
            </div>
            <div class="chip" v-if="form.address">
              <el-icon><Location /></el-icon>
              <span class="chip-text">{{ form.address }}</span>
            </div>
          </div>

          <!-- 修改密码入口 -->
          <div class="password-section">
            <el-button class="password-btn" text @click="showPasswordDialog = true">
              <el-icon><Lock /></el-icon>
              修改密码
            </el-button>
          </div>
        </div>
      </div>

      <!-- ───────── 右侧：编辑表单 ───────── -->
      <div class="profile-main">
        <div class="main-card">
          <div class="section-title">
            <div class="title-icon-wrap"><el-icon><Edit /></el-icon></div>
            <div>
              <h2>编辑个人资料</h2>
              <p>完善您的信息，让大家更了解您</p>
            </div>
          </div>

          <el-form
            ref="form"
            :model="form"
            :rules="rules"
            label-position="top"
            class="edit-form"
          >
            <!-- 基本信息 -->
            <div class="form-group-label">基本信息</div>
            <div class="form-row">
              <el-form-item label="用户名" class="form-col">
                <el-input v-model="form.username" disabled>
                  <template #prefix><el-icon><User /></el-icon></template>
                </el-input>
              </el-form-item>

              <el-form-item label="昵称" prop="nickName" class="form-col">
                <el-input
                  v-model="form.nickName"
                  placeholder="请输入昵称"
                  clearable
                >
                  <template #prefix><el-icon><Postcard /></el-icon></template>
                </el-input>
              </el-form-item>
            </div>

            <div class="form-row">
              <el-form-item label="年龄" prop="age" class="form-col">
                <el-input-number
                  v-model="form.age"
                  :min="1"
                  :max="150"
                  controls-position="right"
                  placeholder="请输入年龄"
                  style="width: 100%"
                />
              </el-form-item>

              <el-form-item label="性别" class="form-col">
                <el-radio-group v-model="form.sex" class="sex-group">
                  <el-radio-button label="男">
                    <el-icon><Male /></el-icon> 男
                  </el-radio-button>
                  <el-radio-button label="女">
                    <el-icon><Female /></el-icon> 女
                  </el-radio-button>
                  <el-radio-button label="其他">其他</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </div>

            <!-- 联系方式 -->
            <div class="form-group-label">联系方式</div>
            <el-form-item label="手机号" prop="phone">
              <el-input
                v-model="form.phone"
                placeholder="请输入手机号"
                clearable
                maxlength="11"
              >
                <template #prefix><el-icon><Phone /></el-icon></template>
              </el-input>
            </el-form-item>

            <el-form-item label="所在地区" prop="address">
              <el-input
                v-model="form.address"
                placeholder="请输入所在城市或地区"
                clearable
              >
                <template #prefix><el-icon><Location /></el-icon></template>
              </el-input>
            </el-form-item>

            <!-- 个性签名 -->
            <div class="form-group-label">个人简介</div>
            <el-form-item label="个性签名">
              <el-input
                v-model="form.bio"
                type="textarea"
                placeholder="介绍一下自己吧～（最多100字）"
                :rows="3"
                maxlength="100"
                show-word-limit
              />
            </el-form-item>

            <!-- 操作按钮 -->
            <div class="form-actions">
              <el-button @click="resetForm" class="reset-btn">重置</el-button>
              <el-button
                type="primary"
                @click="updateForm"
                :loading="loading"
                class="save-btn"
              >
                <el-icon><Check /></el-icon>
                保存修改
              </el-button>
            </div>
          </el-form>
        </div>
      </div>
    </div>

    <!-- ───────── 修改密码弹窗 ───────── -->
    <el-dialog
      v-model="showPasswordDialog"
      title="修改密码"
      width="420px"
      class="password-dialog"
      :close-on-click-modal="false"
    >
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-position="top">
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入当前密码"
            show-password
          >
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码（至少6位）"
            show-password
          >
            <template #prefix><el-icon><Key /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="再次输入新密码"
            show-password
          >
            <template #prefix><el-icon><Key /></el-icon></template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="changePassword" :loading="pwdLoading">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {
  Plus, User, Camera, Edit, Lock, Key, Check,
  Location, Phone, Calendar, Male, Female, Postcard
} from '@element-plus/icons-vue'
import request from "@/utils/request"

export default {
  name: "Person",
  components: {
    Plus, User, Camera, Edit, Lock, Key, Check,
    Location, Phone, Calendar, Male, Female, Postcard
  },
  data() {
    const validateConfirmPwd = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      form: {
        id: '',
        username: '',
        nickName: '',
        phone: '',
        age: '',
        sex: '',
        address: '',
        avatar: '',
        bio: ''
      },
      originalForm: {},
      rules: {
        nickName: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        age: [
          { type: 'number', message: '年龄必须为数字', trigger: 'blur' }
        ]
      },
      uploadUrl: (process.env.VUE_APP_BASEURL || '/api') + '/files/upload',
      loading: false,
      showPasswordDialog: false,
      pwdLoading: false,
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: validateConfirmPwd, trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      try {
        const userStr = sessionStorage.getItem("user")
        if (userStr) {
          const userData = JSON.parse(userStr)
          this.form = {
            id: userData.id || '',
            username: userData.username || '',
            nickName: userData.nickName || '',
            phone: userData.phone || '',
            age: userData.age || '',
            sex: userData.sex || '',
            address: userData.address || '',
            avatar: userData.avatar || '',
            bio: userData.bio || ''
          }
          this.originalForm = { ...this.form }
        }
      } catch (e) {
        console.error('Parse user info error:', e)
        this.$message.error('加载用户信息失败')
      }
    },

    resetForm() {
      this.form = { ...this.originalForm }
      this.$refs.form.clearValidate()
    },

    async updateForm() {
      try {
        const valid = await this.$refs.form.validate()
        if (!valid) {
          this.$message.warning('请完善必填信息')
          return
        }
        this.loading = true
        const response = await request.put('/user', { ...this.form })
        if (response.code === '0') {
          this.$message.success('保存成功！')
          this.originalForm = { ...this.form }
          sessionStorage.setItem('user', JSON.stringify(this.form))
        } else {
          throw new Error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error(error.message || '保存失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    async changePassword() {
      try {
        const valid = await this.$refs.passwordFormRef.validate()
        if (!valid) return
        this.pwdLoading = true
        const response = await request.put('/user/password', {
          id: this.form.id,
          oldPassword: this.passwordForm.oldPassword,
          newPassword: this.passwordForm.newPassword
        })
        if (response.code === '0') {
          this.$message.success('密码修改成功，请重新登录')
          this.showPasswordDialog = false
          this.passwordForm = { oldPassword: '', newPassword: '', confirmPassword: '' }
        } else {
          throw new Error(response.msg || '密码修改失败')
        }
      } catch (error) {
        this.$message.error(error.message || '密码修改失败')
      } finally {
        this.pwdLoading = false
      }
    },

    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith("image/")
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isImage) { this.$message.error('只能上传图片格式！'); return false }
      if (!isLt10M) { this.$message.error('图片大小不能超过 10MB！'); return false }
      return true
    },

    handleAvatarSuccess(res) {
      if (res.code === '0') {
        this.form.avatar = res.data
        this.$message.success('头像上传成功！')
        sessionStorage.setItem('user', JSON.stringify(this.form))
      } else {
        this.$message.error(res.msg || '上传失败')
      }
    },

    handleAvatarError() {
      this.$message.error('头像上传失败，请检查网络连接')
    }
  }
}
</script>

<style scoped>
/* ════════════════════════════════════════
   页面容器
════════════════════════════════════════ */
.page-container {
  min-height: calc(100vh - 64px);
  padding: var(--spacing-xl) var(--spacing-xl);
  background: var(--bg-page);
}

/* ════════════════════════════════════════
   左右分栏布局
════════════════════════════════════════ */
.profile-layout {
  display: flex;
  gap: var(--spacing-xl);
  max-width: 1000px;
  margin: 0 auto;
  align-items: flex-start;
}

/* ════════════════════════════════════════
   左侧用户卡片
════════════════════════════════════════ */
.profile-sidebar {
  flex: 0 0 260px;
  width: 260px;
}

.sidebar-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-base);
  padding: var(--spacing-2xl) var(--spacing-lg);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  position: sticky;
  top: 80px;
  border: 1px solid var(--border-lighter);
  transition: box-shadow var(--transition-base);
}

.sidebar-card:hover {
  box-shadow: var(--shadow-md);
}

/* 头像 */
.avatar-uploader {
  display: flex;
  justify-content: center;
  margin-bottom: var(--spacing-lg);
}

.avatar-wrap {
  position: relative;
  width: 96px;
  height: 96px;
  border-radius: 50%;
  cursor: pointer;
  overflow: hidden;
  box-shadow: 0 0 0 4px var(--bg-secondary), 0 0 0 6px var(--border-light);
  transition: box-shadow var(--transition-base);
}

.avatar-wrap:hover {
  box-shadow: 0 0 0 4px var(--bg-secondary), 0 0 0 6px var(--primary-lighter);
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  background: var(--primary-gradient-soft);
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-icon {
  font-size: 40px;
  color: var(--primary-light);
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  background: rgba(15, 118, 110, 0.72);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: #fff;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  opacity: 0;
  transition: opacity var(--transition-base);
  border-radius: 50%;
}

.avatar-overlay .el-icon {
  font-size: 18px;
}

.avatar-wrap:hover .avatar-overlay {
  opacity: 1;
}

/* 用户名 */
.user-meta {
  margin-bottom: var(--spacing-base);
}

.display-name {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0 0 4px;
  line-height: var(--line-height-tight);
}

.display-username {
  font-size: var(--font-size-sm);
  color: var(--text-disabled);
  margin: 0;
}

/* 签名 */
.bio-preview {
  background: var(--bg-secondary);
  border-radius: var(--radius-base);
  padding: var(--spacing-sm) var(--spacing-base);
  margin-bottom: var(--spacing-md);
  width: 100%;
  box-sizing: border-box;
}

.bio-preview p {
  margin: 0;
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  line-height: var(--line-height-relaxed);
  word-break: break-all;
}

.bio-preview.empty p {
  color: var(--text-disabled);
  font-style: italic;
}

/* 信息小标签 */
.info-chips {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
  width: 100%;
  margin-bottom: var(--spacing-lg);
}

.chip {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  background: var(--bg-tertiary);
  border-radius: var(--radius-full);
  padding: 5px 12px;
}

.chip .el-icon {
  color: var(--primary-light);
  font-size: 14px;
  flex-shrink: 0;
}

.chip-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 150px;
}

/* 修改密码 */
.password-section {
  width: 100%;
  padding-top: var(--spacing-md);
  border-top: 1px solid var(--border-lighter);
}

.password-btn {
  width: 100%;
  color: var(--text-tertiary) !important;
  font-size: var(--font-size-sm);
  gap: 6px;
  border-radius: var(--radius-base);
  transition: all var(--transition-fast);
}

.password-btn:hover {
  color: var(--primary-light) !important;
  background: var(--primary-gradient-soft) !important;
}

/* ════════════════════════════════════════
   右侧编辑表单
════════════════════════════════════════ */
.profile-main {
  flex: 1;
  min-width: 0;
}

.main-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-base);
  padding: var(--spacing-2xl);
  border: 1px solid var(--border-lighter);
  transition: box-shadow var(--transition-base);
}

.main-card:hover {
  box-shadow: var(--shadow-md);
}

/* 标题区 */
.section-title {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  margin-bottom: var(--spacing-2xl);
  padding-bottom: var(--spacing-lg);
  border-bottom: 1px solid var(--border-lighter);
}

.title-icon-wrap {
  width: 44px;
  height: 44px;
  border-radius: var(--radius-base);
  background: var(--primary-gradient-soft);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.title-icon-wrap .el-icon {
  font-size: 20px;
  color: var(--primary-color);
}

.section-title h2 {
  margin: 0 0 4px;
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  line-height: var(--line-height-tight);
}

.section-title p {
  margin: 0;
  font-size: var(--font-size-sm);
  color: var(--text-disabled);
}

/* 表单组标题 */
.form-group-label {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  color: var(--primary-color);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin: var(--spacing-lg) 0 var(--spacing-base);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.form-group-label::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, var(--border-light) 0%, transparent 100%);
}

/* 表单两列 */
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--spacing-md);
}

.edit-form :deep(.el-form-item__label) {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--text-secondary);
  margin-bottom: 4px;
  line-height: 1.4;
  padding-bottom: 4px;
}

.edit-form :deep(.el-input__wrapper) {
  border-radius: var(--radius-base);
  transition: box-shadow var(--transition-fast), border-color var(--transition-fast);
}

.edit-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--primary-lighter) inset;
}

.edit-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--primary-color) inset !important;
}

.edit-form :deep(.el-textarea__inner) {
  border-radius: var(--radius-base);
}

/* 性别单选按钮组 */
.sex-group :deep(.el-radio-button__inner) {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-sm);
}

/* 操作按钮 */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-md);
  margin-top: var(--spacing-2xl);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-lighter);
}

.reset-btn {
  min-width: 100px;
  border-radius: var(--radius-base);
  color: var(--text-tertiary);
  border-color: var(--border-base);
}

.save-btn {
  min-width: 140px;
  height: 42px;
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  border-radius: var(--radius-base);
  background: var(--primary-gradient);
  border: none;
  letter-spacing: 0.5px;
  box-shadow: var(--shadow-primary);
  transition: all var(--transition-base);
}

.save-btn:hover {
  box-shadow: var(--shadow-primary-lg);
  transform: translateY(-1px);
}

/* ════════════════════════════════════════
   密码弹窗
════════════════════════════════════════ */
:deep(.password-dialog .el-dialog__header) {
  padding: var(--spacing-lg) var(--spacing-xl) var(--spacing-base);
  border-bottom: 1px solid var(--border-lighter);
}

:deep(.password-dialog .el-dialog__body) {
  padding: var(--spacing-xl);
}

:deep(.password-dialog .el-form-item__label) {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--text-secondary);
}

/* ════════════════════════════════════════
   响应式
════════════════════════════════════════ */
@media screen and (max-width: 860px) {
  .page-container {
    padding: var(--spacing-md);
  }

  .profile-layout {
    flex-direction: column;
  }

  .profile-sidebar {
    flex: none;
    width: 100%;
  }

  .sidebar-card {
    position: static;
    flex-direction: row;
    flex-wrap: wrap;
    gap: var(--spacing-md);
    text-align: left;
    padding: var(--spacing-lg);
  }

  .avatar-uploader {
    margin-bottom: 0;
  }

  .user-meta {
    flex: 1;
    margin-bottom: 0;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .form-row {
    grid-template-columns: 1fr;
  }
}

@media screen and (max-width: 480px) {
  .main-card {
    padding: var(--spacing-lg);
  }
}
</style>
