<template>
  <div class="admin-page">
    <!-- 页面标题 -->
    <div class="admin-page-header">
      <div class="page-header-left">
        <div class="page-header-icon blue">
          <el-icon><ChatDotRound /></el-icon>
        </div>
        <div>
          <h2 class="page-header-title">评论管理</h2>
          <p class="page-header-desc">查看并管理所有评论内容</p>
        </div>
      </div>
      <span class="page-header-badge" v-if="total > 0">
        共 {{ total }} 条评论
      </span>
    </div>

    <div class="admin-toolbar">
      <el-input
        v-model="search"
        placeholder="搜索评论内容 / 评论人..."
        clearable
        class="search-input"
        @keyup.enter="load"
      >
        <template #append>
          <el-button @click="load"><el-icon><Search /></el-icon></el-button>
        </template>
      </el-input>
    </div>

    <div class="admin-table-wrap">
      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="title"    label="标题" min-width="140" show-overflow-tooltip />
        <el-table-column label="内容"   min-width="240">
          <template #default="scope">
            <span class="text-clamp">{{ scope.row.content }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="评论人" width="110" />
        <el-table-column prop="time"     label="评论时间" width="160" />
        <el-table-column label="操作" width="90" align="center" fixed="right">
          <template #default="scope">
            <el-popconfirm title="确定删除该评论吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-empty v-if="!loading && tableData.length === 0" description="暂无评论" />

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
import { getMessagePage, deleteMessage } from '@/api/message'
import { ChatDotRound, Search } from '@element-plus/icons-vue'

export default {
  name: 'Comment',
  components: { ChatDotRound, Search },
  data() {
    return {
      loading: false,
      search: '',
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.loading = true
      getMessagePage({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        search: this.search
      })
        .then(res => {
          this.tableData = res.data.records || []
          this.total = res.data.total || 0
        })
        .catch(() => {})
        .finally(() => { this.loading = false })
    },
    handleDelete(id) {
      deleteMessage(id).then(res => {
        if (res.code === '0') {
          this.$message.success('删除成功')
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSizeChange(ps) {
      this.pageSize = ps
      this.load()
    },
    handleCurrentChange(pn) {
      this.pageNum = pn
      this.load()
    },
  }
}
</script>

<style scoped>
.text-clamp {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  line-height: var(--line-height-snug);
}
</style>
