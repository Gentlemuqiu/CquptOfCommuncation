<template>
  <el-card style="margin-top: 10px">
    <el-form>
      <el-form-item label="标题" label-width="50px">
        <el-input v-model="entity.title" style="width: 50%"></el-input>
      </el-form-item>
    </el-form>
    <v-md-editor v-model="entity.content" height="400px"></v-md-editor>
    <div style="padding: 10px 0">
      <!-- 修复了取消按钮 -->
      <el-button @click="cancel">取消</el-button>
      <el-button type="primary" @click="save">发布</el-button>
    </div>
  </el-card>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "LongComment",
  data() {
    return {
      entity: { title: '' },
      user: {},
      id: null
    }
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    this.id = this.$route.query.id // 从路由参数中获取 `id`
  },
  methods: {
    cancel() {
      // 修复取消逻辑，正确传递 `id` 参数
      this.$router.push({ path: '/front/detail', query: { id: this.id } });
    },
    save() {
      if (!this.user.username) {
        this.$message({
          message: "请登录",
          type: "warning"
        });
        return;
      }
      if (!this.entity.content) {
        this.$message({
          message: "请填写内容",
          type: "warning"
        });
        return;
      }
      // 设置评论属性
      this.entity.username = this.user.username;
      this.entity.type = 2;
      this.entity.foreignId = this.id; // 将模块 ID 作为 `foreignId`
      request.post("/message", this.entity).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "评论成功",
            type: "success"
          });
          // 跳转回详情页面
          this.$router.push({ path: '/front/detail', query: { id: this.id } });
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
      });
    }
  }
}
</script>

<style scoped>
</style>
