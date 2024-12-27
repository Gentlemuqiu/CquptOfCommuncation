<template>
  <div class="bg-container">
    <div class="form-container">
      <div class="form-title">欢迎注册</div>
      <el-form ref="form" :model="form" :rules="rules" size="normal">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user-solid" v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input prefix-icon="el-icon-phone" v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="form.password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item prop="confirm">
          <el-input prefix-icon="el-icon-lock" v-model="form.confirm" placeholder="请再次输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" class="form-button" type="primary" @click="register">注册</el-button>
        </el-form-item>
        <el-form-item class="form-footer">
          <el-button type="text" @click="$router.push('/login')"> &lt;&lt; 前往登录 </el-button>
        </el-form-item>
      </el-form>
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
          { min: 3, max: 15, message: '用户名长度应在 3 到 15 个字符之间', trigger: 'blur' }
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
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          this.loading = true;
          try {
            const res = await request.post("/user/register", this.form);
            if (res.code === '0') {
              this.$message.success("注册成功");
              this.$router.push("/login");
            } else {
              this.$message.error(res.msg);
            }
          } catch (error) {
            this.$message.error("网络错误，请稍后再试");
          } finally {
            this.loading = false;
          }
        }
      });
    }
  }
};
</script>

<style scoped>
/* 背景容器样式 */
.bg-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background: url("../assets/image/flower.png") no-repeat center center; /* 背景图片 */
  background-size: cover; /* 背景自适应 */
}

/* 表单容器样式 */
.form-container {
  width: 400px;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
.form-title {
  font-size: 30px;
  text-align: center;
  padding: 20px 0;
  color: dodgerblue;
}

/* 按钮样式 */
.form-button {
  width: 100%;
}

/* 表单底部样式 */
.form-footer {
  text-align: center;
}
</style>
