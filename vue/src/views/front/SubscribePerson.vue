<template>
  <div class="subscribe-page">
    <div class="page-header">
      <h2 class="page-title">
        <i class="el-icon-coordinate title-icon"></i>
        我关注的人
      </h2>
      <span class="item-count">共 {{ table.length }} 人</span>
    </div>

    <!-- 空状态 -->
    <div v-if="table.length === 0" class="empty-state">
      <i class="el-icon-user empty-icon"></i>
      <p class="empty-text">还没有关注任何人</p>
      <p class="empty-hint">在帖子详情页点击「关注」即可关注作者</p>
    </div>

    <!-- 关注列表 -->
    <div v-else class="follow-list">
      <div v-for="row in table" :key="row.id" class="follow-item">
        <div class="follow-avatar">
          <el-avatar :size="44" class="avatar">
            {{ row.commentUser ? row.commentUser[0].toUpperCase() : 'U' }}
          </el-avatar>
        </div>
        <div class="follow-info">
          <span class="follow-name">{{ row.commentUser }}</span>
          <span class="follow-label">长评作者</span>
        </div>
        <div class="follow-actions">
          <el-button
            type="primary"
            size="small"
            plain
            icon="el-icon-document"
            @click="look(row.commentUser)"
          >
            查看长评
          </el-button>
          <el-popconfirm
            title="确定取消关注吗？"
            @confirm="cancel(row)"
          >
            <template #reference>
              <el-button size="small" icon="el-icon-minus">取消关注</el-button>
            </template>
          </el-popconfirm>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "SubscribePerson",
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem("user") || "{}"),
      table: []
    };
  },
  created() { this.load(); },
  methods: {
    load() {
      request.get("/lookAuthor/user/" + this.user.username).then(res => {
        this.table = res.data || [];
      });
    },
    look(username) {
      this.$router.push({ path: "/front/longCommentTable", query: { username } });
    },
    cancel(row) {
      request.delete(`/lookAuthor/${row.user}/${row.commentUser}`).then(() => {
        this.$message.success("已取消关注");
        this.load();
      });
    }
  }
};
</script>

<style scoped>
.subscribe-page {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-base);
}

.page-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
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

.item-count {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  background: var(--bg-tertiary);
  padding: 3px 10px;
  border-radius: var(--radius-full);
  margin-left: auto;
}

.empty-state {
  text-align: center;
  padding: var(--spacing-4xl);
}

.empty-icon {
  font-size: 52px;
  color: var(--text-disabled);
  display: block;
  margin-bottom: var(--spacing-md);
}

.empty-text {
  font-size: var(--font-size-lg);
  color: var(--text-tertiary);
  margin: 0 0 var(--spacing-sm);
  font-weight: var(--font-weight-medium);
}

.empty-hint {
  font-size: var(--font-size-sm);
  color: var(--text-disabled);
  margin: 0;
}

.follow-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.follow-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-md) var(--spacing-lg);
  background: var(--bg-secondary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-md);
  transition: all var(--transition-base);
}

.follow-item:hover {
  background: var(--bg-hover);
  border-color: var(--border-primary);
  box-shadow: var(--shadow-xs);
}

.follow-avatar .avatar {
  background: var(--primary-gradient);
  color: #fff;
  font-weight: var(--font-weight-bold);
  font-size: var(--font-size-lg);
}

.follow-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.follow-name {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
}

.follow-label {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

.follow-actions {
  display: flex;
  gap: var(--spacing-sm);
}
</style>
