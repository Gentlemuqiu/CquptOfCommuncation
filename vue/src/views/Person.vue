<template>
  <div class="page-container">
    <div class="person-container">
      <el-card class="info-card" :body-style="{ padding: '30px' }">
        <div class="card-header">
          <h2 class="profile-title">个人信息</h2>
        </div>
        
        <el-form 
          ref="form" 
          :model="form" 
          :rules="rules" 
          label-width="80px"
          class="profile-form"
        >
        <div class="avatar-wrapper">
    <el-upload
      class="avatar-uploader"
      action="http://localhost:8080/files/upload"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :on-error="handleAvatarError"
      :before-upload="beforeAvatarUpload"
    >
      <div class="avatar-container" :class="{ 'no-avatar': !form.avatar }">
        <img 
          v-if="form.avatar" 
          :src="form.avatar" 
          class="avatar" 
          alt="用户头像"
        />
        <template v-else>
          <el-icon class="avatar-uploader-icon">
            <Plus />
          </el-icon>
          <div class="avatar-tip">点击上传头像</div>
        </template>
      </div>
    </el-upload>
  </div>

          <!-- 表单部分 -->
          <div class="form-section">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" disabled></el-input>
            </el-form-item>
            
            <el-form-item label="昵称" prop="nickName">
              <el-input 
                v-model="form.nickName" 
                placeholder="请输入昵称"
                clearable
              ></el-input>
            </el-form-item>
            
            <el-form-item label="手机号" prop="phone">
              <el-input 
                v-model="form.phone" 
                placeholder="请输入手机号"
                clearable
              ></el-input>
            </el-form-item>
            
            <el-form-item label="年龄" prop="age">
              <el-input-number 
                v-model="form.age" 
                :min="0" 
                :max="150" 
                controls-position="right"
                placeholder="请输入年龄"
              ></el-input-number>
            </el-form-item>
            
            <el-form-item label="性别">
              <el-radio-group v-model="form.sex">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
                <el-radio label="其他">其他</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item label="地址" prop="address">
              <el-input
                v-model="form.address"
                type="textarea"
                placeholder="请输入地址"
                :rows="2"
                clearable
              ></el-input>
            </el-form-item>
            
            <!-- 简化的标签选择 -->
            <el-form-item label="个性标签" prop="tagsArr">
              <el-select
                v-model="form.tagsArr"
                multiple
                clearable
                placeholder="选择标签（最多3个）"
                style="width: 100%"
                @change="handleTagsChange"
              >
                <el-option
                  v-for="tag in availableTags"
                  :key="tag"
                  :label="tag"
                  :value="tag"
                  :disabled="isTagDisabled(tag)"
                ></el-option>
              </el-select>
            </el-form-item>
          </div>

          <!-- 提交按钮 -->
          <div class="button-container">
            <el-button 
              type="primary" 
              @click="updateForm" 
              :loading="loading"
              class="submit-button"
            >
              保存修改
            </el-button>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import { Plus } from '@element-plus/icons-vue'
import request from "@/utils/request"

export default {
  name: "Person",
  components: {
    Plus
  },
  data() {
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
        tagsArr: [],
        tags: ''
      },
      availableTags: ['喜剧', '悬疑', '爱情', '动作', '动画', '犯罪', '惊悚', '冒险', '历史', '战争', '纪录片', '科幻'],
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
        ],
      },
      loading: false
    }
  },
  created() {
    this.initializeData()
  },
  methods: {
    async initializeData() {
      await this.loadUserInfo()
      if (this.form.id && (!this.form.tagsArr || this.form.tagsArr.length === 0)) {
        await this.fetchUserTags()
      }
    },
    
    async loadUserInfo() {
      try {
        const userStr = sessionStorage.getItem("user")
        if (userStr) {
          const userData = JSON.parse(userStr)
          this.form = {
            ...userData,
            tagsArr: userData.tags ? userData.tags.split(',').filter(Boolean) : []
          }
        }
      } catch (e) {
        console.error('Parse user info error:', e)
        this.$message.error('加载用户信息失败')
      }
    },

    async fetchUserTags() {
      try {
        const response = await request.get('/user/tags')
        if (response.code === '0' && response.data) {
          const tags = response.data.split(',').filter(Boolean)
          if (tags.length > 0) {
            this.form.tagsArr = tags.slice(0, 3)
            this.form.tags = this.form.tagsArr.join(',')
          }
        }
      } catch (error) {
        console.error('Fetch tags error:', error)
        this.$message.warning('标签加载失败，您可以重新选择标签')
      }
    },

    handleTagsChange(tags) {
      if (tags.length > 3) {
        this.form.tagsArr = tags.slice(0, 3)
        this.$message.warning('最多只能选择3个标签')
      }
      this.form.tags = this.form.tagsArr.join(',')
    },

    isTagDisabled(tag) {
      return this.form.tagsArr.length >= 3 && !this.form.tagsArr.includes(tag)
    },

    async updateForm() {
      try {
        const valid = await this.$refs.form.validate()
        if (!valid) {
          this.$message.warning('请完善必填信息')
          return
        }

        this.loading = true
        const formData = {
          ...this.form,
          tags: this.form.tagsArr.join(',')
        }

        const response = await request.put('/user', formData)
        if (response.code === '0') {
          this.$message.success('保存成功！')
          this.updateLocalStorage(formData)
        } else {
          throw new Error(response.msg || '保存失败')
        }
      } catch (error) {
        console.error('Update user error:', error)
        this.$message.error(error.message || '保存失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    updateLocalStorage(userData) {
      try {
        sessionStorage.setItem('user', JSON.stringify(userData))
      } catch (e) {
        console.error('Update storage error:', e)
        this.$message.error('更新本地存储失败')
      }
    },

    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith("image/") 
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isImage) {
        this.$message.error('上传头像图片只能是图片格式!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传头像图片大小不能超过 10MB!')
        return false
      }
      
      return isImage && isLt10M
    },

    handleAvatarSuccess(res) {
      if (res.code === '0') {
        this.form.avatar = res.data
        this.$message.success('头像上传成功！')
        this.updateLocalStorage(this.form)
      } else {
        this.$message.error(res.msg || '上传失败')
      }
    },

    handleAvatarError(err) {
      console.error('Upload error:', err)
      this.$message.error('头像上传失败，请检查网络连接')
    }
  }
}
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 40px 20px;
  background-color: #f5f7fa;
}

.person-container {
  width: 100%;
  max-width: 600px;
}

.info-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.card-header {
  margin-bottom: 30px;
  text-align: center;
}

.profile-title {
  color: #303133;
  font-size: 24px;
  font-weight: 500;
  margin: 0;
}

.profile-form {
  margin-top: 20px;
}

.avatar-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 40px;
}

.avatar-container {
  width: 120px;
  height: 120px;
  overflow: hidden;
  border-radius: 50%;
  margin: 0 auto;
  cursor: pointer;
  border: 2px dashed #d9d9d9;
  transition: all 0.3s;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.no-avatar {
  background: #f5f7fa;
}

.avatar-container:hover {
  border-color: #409EFF;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.avatar-tip {
  font-size: 12px;
  color: #666;
  margin-top: 8px;
  text-align: center;
}

.no-avatar .avatar-tip {
  position: absolute;
  bottom: 10px;
}
.form-section {
  max-width: 500px;
  margin: 0 auto;
}

.form-section :deep(.el-form-item) {
  margin-bottom: 22px;
}

.button-container {
  text-align: center;
  margin-top: 40px;
}

.submit-button {
  width: 180px;
  height: 40px;
  font-size: 16px;
}

/* Element UI 组件样式优化 */
:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-input__inner) {
  height: 40px;
  line-height: 40px;
}

:deep(.el-radio) {
  margin-right: 30px;
}

:deep(.el-select) {
  width: 100%;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .page-container {
    padding: 20px 10px;
  }

  .person-container {
    max-width: 100%;
  }

  .profile-form {
    padding: 0;
  }
}
</style>