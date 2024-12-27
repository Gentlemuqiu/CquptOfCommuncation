<template>
  <div class="bg-container">
    <div class="image-bg"></div>
    <div class="main-container">
      <div class="title">找回密码</div>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="账号" prop="username">
          <el-input prefix-icon="el-icon-user" v-model="form.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input prefix-icon="el-icon-phone" v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button class="full-width-button primary" type="primary" @click="handleVerifyUser">验证账号</el-button>
        </el-form-item>
        <el-form-item class="link-buttons">
          <el-button type="text" @click="$router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>

  <el-dialog 
    title="修改密码" 
    v-model="dialogVisible"
    width="400px" 
    @closed="handleDialogClose"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form :model="newPasswordForm" :rules="newPasswordRules" ref="newPasswordFormRef" label-width="80px">
      <el-form-item label="新密码" prop="password">
        <el-input v-model="newPasswordForm.password" type="password" placeholder="请输入新密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="newPasswordForm.confirmPassword" type="password" placeholder="请确认新密码"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleResetPassword">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
import request from "@/utils/request";
import { ElMessage } from 'element-plus';

export default {
  name: "ResetPassword",
  data() {
    // 密码确认校验函数
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.newPasswordForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };

    return {
      // 表单数据
      form: {
        username: "",
        phone: "",
      },
      // 弹窗控制
      dialogVisible: false,
      // 新密码表单
      newPasswordForm: {
        password: "",
        confirmPassword: "",
      },
      // 表单校验规则
      rules: {
        username: [
          { required: true, message: "请输入账号", trigger: "blur" },
        ],
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "请输入有效的手机号", trigger: "blur" },
        ],
      },
      // 新密码表单校验规则
      newPasswordRules: {
        password: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          { min: 6, message: "密码长度不能小于6个字符", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "请确认新密码", trigger: "blur" },
          { validator: validateConfirmPassword, trigger: ["blur", "change"] }
        ]
      }
    };
  },
  methods: {
    // 处理验证用户
    async handleVerifyUser() {
      try {
        // 先验证表单
        await this.$refs.formRef.validate();
        
        // 显示加载状态
        const loading = this.$loading({
          lock: true,
          text: '验证中...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });

        try {
          // 调用验证接口
          const res = await request.post("/user/resetPassVerify", this.form);
          
          // 关闭加载状态
          loading.close();

          if (res.code === "0") {
            // 验证成功，显示修改密码弹窗
            this.dialogVisible = true;
            // 重置新密码表单
            this.newPasswordForm.password = "";
            this.newPasswordForm.confirmPassword = "";
            if (this.$refs.newPasswordFormRef) {
              this.$refs.newPasswordFormRef.clearValidate();
            }
          } else {
            ElMessage.error(res.msg || '验证失败');
          }
        } catch (error) {
          loading.close();
          console.error('验证请求失败:', error);
          ElMessage.error('网络错误，请重试');
        }
      } catch (error) {
        console.error('表单验证失败:', error);
      }
    },

    // 处理重置密码
    async handleResetPassword() {
      try {
        // 验证新密码表单
        await this.$refs.newPasswordFormRef.validate();
        
        // 显示加载状态
        const loading = this.$loading({
          lock: true,
          text: '正在重置密码...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });

        try {
          // 调用重置密码接口
          const data = {
            username: this.form.username,
            phone: this.form.phone,
            password: this.newPasswordForm.password
          };
          
          const res = await request.post("/user/resetPass", data);
          
          // 关闭加载状态
          loading.close();

          if (res.code === "0") {
            ElMessage.success('密码重置成功！');
            this.dialogVisible = false;
            // 重置成功后跳转到登录页
            this.$router.push('/login');
          } else {
            ElMessage.error(res.msg || '重置密码失败');
          }
        } catch (error) {
          loading.close();
          console.error('重置密码请求失败:', error);
          ElMessage.error('网络错误，请重试');
        }
      } catch (error) {
        console.error('新密码表单验证失败:', error);
      }
    },

    // 处理弹窗关闭
    handleDialogClose() {
      this.newPasswordForm.password = "";
      this.newPasswordForm.confirmPassword = "";
      if (this.$refs.newPasswordFormRef) {
        this.$refs.newPasswordFormRef.clearValidate();
      }
    }
  }
};
</script>
<style scoped>
/* 样式保持不变 */
.bg-container {
  position: relative;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  animation: fadeIn 2s ease;
}

.image-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('../assets/image/meihua.png');
  background-size: cover;
  background-position: center;
  z-index: -1;
  filter: brightness(0.6);
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.main-container {
  width: 400px;
  padding: 30px 30px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
  animation: slideIn 1s ease;
}

@keyframes slideIn {
  from {
    transform: translateY(50px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: #ff69b4;
  text-align: center;
  margin-bottom: 20px;
}

.full-width-button {
  width: 100%;
}

.primary {
  background-color: #ff69b4;
  color: white;
  border-color: #ff69b4;
}

.primary:hover {
  background-color: #e856a7;
  border-color: #e856a7;
}

.link-buttons {
  text-align: center;
}

.link-buttons .el-button {
  color: #ff69b4;
}

.link-buttons .el-button:hover {
  color: #e856a7;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>