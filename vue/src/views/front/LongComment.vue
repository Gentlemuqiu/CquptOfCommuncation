<template>
  <div class="longcomment-write">
    <div class="write-header">
      <h2 class="write-title">
        <i class="el-icon-edit-outline"></i>
        发表长评
      </h2>
      <p class="write-subtitle">支持 Markdown 格式，畅快表达你的想法</p>
    </div>

    <el-form class="write-form">
      <el-form-item label="评论标题" label-width="80px">
        <el-input
          v-model="entity.title"
          placeholder="请输入一个简短的标题（选填）"
          clearable
          maxlength="60"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <div class="editor-wrap">
      <v-md-editor v-model="entity.content" height="420px" placeholder="开始写你的长评..." />
    </div>

    <div class="write-actions">
      <el-button @click="cancel" icon="el-icon-arrow-left">返 回</el-button>
      <el-button type="primary" @click="save" icon="el-icon-s-promotion" :loading="saving">
        {{ saving ? '发布中...' : '发布长评' }}
      </el-button>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "LongComment",
  data() {
    return {
      entity: { title: '', content: '' },
      user: {},
      id: null,
      saving: false
    };
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user") || "{}");
    this.id = this.$route.query.id;
  },
  methods: {
    cancel() {
      this.$router.push({ path: '/front/detail', query: { id: this.id } });
    },
    async save() {
      if (!this.user.username) {
        this.$message.warning("请先登录");
        return;
      }
      if (!this.entity.content || !this.entity.content.trim()) {
        this.$message.warning("请填写评论内容");
        return;
      }
      this.saving = true;
      try {
        const res = await request.post("/message", {
          ...this.entity,
          username: this.user.username,
          type: 2,
          foreignId: this.id
        });
        if (res.code === '0') {
          this.$message.success("发布成功！");
          this.$router.push({ path: '/front/detail', query: { id: this.id } });
        } else {
          this.$message.error(res.msg);
        }
      } catch {
        this.$message.error("网络错误，请稍后重试");
      } finally {
        this.saving = false;
      }
    }
  }
};
</script>

<style scoped>
.longcomment-write {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-base);
}

.write-header {
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--border-lighter);
}

.write-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-sm);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.write-title i { color: var(--primary-color); }

.write-subtitle {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  margin: 0;
}

.write-form {
  margin-bottom: var(--spacing-md);
}

.editor-wrap {
  border-radius: var(--radius-base);
  overflow: hidden;
  border: 1px solid var(--border-light);
}

.write-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-base);
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-lighter);
}
</style>
