<template>
  <div class="longcomment-page">
    <div class="page-header">
      <h2 class="page-title">
        <i class="el-icon-edit-outline title-icon"></i>
        {{ username }} 的长评列表
      </h2>
      <el-button size="small" @click="$router.back()" icon="el-icon-arrow-left">返回</el-button>
    </div>

    <div v-if="longMessages.length === 0" class="empty-state">
      <el-empty description="该用户暂无长评" image-size="100" />
    </div>

    <div v-else class="comment-list">
      <div
        v-for="item in longMessages"
        :key="item.id"
        class="comment-item"
      >
        <div class="comment-meta">
          <span class="post-name">
            <i class="el-icon-s-order"></i>
            {{ item.movie && item.movie.name }}
          </span>
          <span class="comment-time">
            <i class="el-icon-time"></i>
            {{ item.time }}
          </span>
        </div>
        <h3 class="comment-title">{{ item.title }}</h3>
        <div class="comment-actions">
          <el-button
            type="primary"
            plain
            size="small"
            icon="el-icon-view"
            @click="showLongComment(item.content)"
          >
            阅读全文
          </el-button>
        </div>
      </div>
    </div>

    <!-- 预览弹框 -->
    <el-dialog title="长评内容" v-model="vis" width="600px">
      <v-md-preview :text="longComment" />
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "LongCommentTable",
  data() {
    return {
      username: '',
      longMessages: [],
      vis: false,
      longComment: ''
    };
  },
  created() {
    this.username = this.$route.query.username || '';
    if (this.username) {
      request.get(`/message/user/${this.username}/2`).then(res => {
        this.longMessages = res.data || [];
      });
    }
  },
  methods: {
    showLongComment(content) {
      if (!content) { this.$message.warning('内容为空'); return; }
      this.longComment = content;
      this.vis = true;
    }
  }
};
</script>

<style scoped>
.longcomment-page {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-base);
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--border-lighter);
}

.page-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.title-icon { color: var(--primary-color); }

.empty-state { padding: var(--spacing-3xl); }

.comment-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.comment-item {
  padding: var(--spacing-md) var(--spacing-lg);
  background: var(--bg-secondary);
  border: 1px solid var(--border-lighter);
  border-left: 3px solid var(--primary-color);
  border-radius: var(--radius-md);
  transition: all var(--transition-base);
}

.comment-item:hover {
  background: var(--bg-hover);
  border-color: var(--border-primary);
  transform: translateX(2px);
}

.comment-meta {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-sm);
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

.post-name, .comment-time {
  display: flex;
  align-items: center;
  gap: 4px;
}

.post-name i { color: var(--primary-color); }

.comment-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-base);
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
}
</style>
