<template>
  <div class="admin-page">
    <!-- 页面标题 -->
    <div class="admin-page-header">
      <div class="page-header-left">
        <div class="page-header-icon red">
          <el-icon><Warning /></el-icon>
        </div>
        <div>
          <h2 class="page-header-title">投诉管理</h2>
          <p class="page-header-desc">审核用户投诉，处理后将删除对应评论</p>
        </div>
      </div>
      <span class="page-header-badge" v-if="total > 0">
        待处理 {{ total }} 条
      </span>
    </div>

    <div class="admin-toolbar">
      <span class="toolbar-tip">
        <el-icon style="color: var(--info-color)"><InfoFilled /></el-icon>
        处理投诉后，相关评论将一并删除
      </span>
    </div>

    <div class="admin-table-wrap">
      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="user"          label="投诉人"   width="120" />
        <el-table-column prop="commentUser"   label="被投诉人" width="120" />
        <el-table-column prop="content"       label="投诉内容" min-width="280" show-overflow-tooltip />
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template #default="scope">
            <el-popconfirm
              title="确认已处理此投诉并删除该评论？"
              confirm-button-text="确认处理"
              cancel-button-text="取消"
              @confirm="handleDelete(scope.row)"
            >
              <template #reference>
                <el-button size="small" type="danger">
                  <el-icon><Check /></el-icon>处理并删除
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-empty v-if="!loading && tableData.length === 0" description="暂无投诉记录" />

    <div class="admin-pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next"
        :total="total"
      />
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { Warning, InfoFilled, Check } from '@element-plus/icons-vue'

export default {
  name: "Jubao",
  components: { Warning, InfoFilled, Check },
  data() {
    return {
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
    };
  },
  created() { this.load(); },
  methods: {
    load() {
      this.loading = true;
      request.get("/jubao/page", {
        params: { pageNum: this.pageNum, pageSize: this.pageSize }
      }).then(res => {
        this.loading = false;
        if (res.code === '0') {
          this.tableData = res.data.records;
          this.total = res.data.total;
        }
      }).catch(() => { this.loading = false; });
    },
    async handleDelete(row) {
      this.loading = true;
      try {
        await request.delete(`/message/${row.commentId}`);
        await request.delete(`/jubao/${row.id}`);
        this.$message.success('投诉已处理，评论已删除');
        this.load();
      } catch {
        this.$message.error('操作失败，请稍后重试');
        this.loading = false;
      }
    },
    handleSizeChange(ps) { this.pageSize = ps; this.load(); },
    handleCurrentChange(pn) { this.pageNum = pn; this.load(); },
  }
};
</script>

<style scoped></style>
