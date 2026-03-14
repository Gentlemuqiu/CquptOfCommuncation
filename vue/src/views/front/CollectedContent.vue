<template>
  <div class="collected-page">
    <div class="page-header">
      <h2 class="page-title">
        <i class="el-icon-collection title-icon"></i>
        我收藏的内容
      </h2>
      <span class="item-count">共 {{ total }} 条</span>
    </div>

    <div v-if="tableData.length === 0 && !loading" class="empty-state">
      <i class="el-icon-collection empty-icon"></i>
      <p class="empty-text">暂无收藏内容</p>
      <p class="empty-hint">去浏览信息，点击「收藏」即可保存到这里</p>
    </div>

    <div v-else class="content-grid">
      <div
        v-for="item in tableData"
        :key="item.id"
        class="content-card"
        @click="$router.push(item.link)"
      >
        <div class="card-img-wrap">
          <el-image :src="item.img" fit="cover" class="card-img" />
        </div>
        <div class="card-info">
          <h3 class="card-name">{{ item.name }}</h3>
          <el-popconfirm title="确定取消收藏吗？" @confirm.stop="handleDelete(item.id)">
            <template #reference>
              <el-button
                size="small"
                type="danger"
                plain
                icon="el-icon-delete"
                @click.stop
              >
                取消收藏
              </el-button>
            </template>
          </el-popconfirm>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrap" v-if="total > pageSize">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[8, 12, 24]"
        :page-size="pageSize"
        layout="prev, pager, next, sizes"
        :total="total"
      />
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "CollectedContent",
  data() {
    return {
      loading: false,
      pageNum: 1,
      pageSize: 12,
      total: 0,
      tableData: [],
      user: JSON.parse(sessionStorage.getItem("user") || "{}")
    };
  },
  created() { this.load(); },
  methods: {
    load() {
      this.loading = true;
      request.get("/collectMovie/page", {
        params: { pageNum: this.pageNum, pageSize: this.pageSize, userId: this.user.id }
      }).then(res => {
        this.loading = false;
        if (res.code === '0') {
          this.tableData = res.data.records || [];
          this.total = res.data.total || 0;
        }
      }).catch(() => { this.loading = false; });
    },
    handleDelete(id) {
      request.delete(`/collectMovie/${id}`).then(res => {
        if (res.code === '0') { this.$message.success('已取消收藏'); this.load(); }
        else this.$message.error(res.msg);
      });
    },
    handleSizeChange(ps) { this.pageSize = ps; this.load(); },
    handleCurrentChange(pn) { this.pageNum = pn; this.load(); },
  }
};
</script>

<style scoped>
.collected-page {
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

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: var(--spacing-lg);
}

.content-card {
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-md);
  overflow: hidden;
  cursor: pointer;
  transition: all var(--transition-cubic);
  background: var(--bg-primary);
}

.content-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-lg);
  border-color: var(--border-primary);
}

.card-img-wrap {
  height: 140px;
  overflow: hidden;
  background: var(--bg-tertiary);
}

.card-img {
  width: 100%;
  height: 100%;
  transition: transform var(--transition-slow);
}

.content-card:hover .card-img {
  transform: scale(1.08);
}

.card-info {
  padding: var(--spacing-base) var(--spacing-md);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--spacing-sm);
}

.card-name {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
  min-width: 0;
  transition: color var(--transition-base);
}

.content-card:hover .card-name { color: var(--primary-color); }

.pagination-wrap {
  display: flex;
  justify-content: center;
  padding-top: var(--spacing-xl);
  margin-top: var(--spacing-xl);
  border-top: 1px solid var(--border-lighter);
}
</style>
