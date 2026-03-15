<template>
  <div class="admin-page">
    <!-- 页面标题 -->
    <div class="admin-page-header">
      <div class="page-header-left">
        <div class="page-header-icon orange">
          <el-icon><Bell /></el-icon>
        </div>
        <div>
          <h2 class="page-header-title">公告管理</h2>
          <p class="page-header-desc">管理系统公告，支持草稿保存与一键发布</p>
        </div>
      </div>
      <span class="page-header-badge" v-if="total > 0">
        共 {{ total }} 条公告
      </span>
    </div>

    <div class="admin-toolbar">
      <el-button type="primary" @click="add">
        <el-icon><Plus /></el-icon>新增公告
      </el-button>
    </div>

    <div class="admin-table-wrap">
      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="name"    label="公告名称" min-width="160" />
        <el-table-column prop="content" label="公告内容" min-width="280" show-overflow-tooltip />
        <el-table-column prop="time"    label="发布时间" width="160" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="scope">
            <span :class="['status-tag', scope.row.status === '已发布' ? 'published' : 'draft']">
              {{ scope.row.status || '草稿' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <el-button
              size="small"
              type="success"
              :disabled="scope.row.status === '已发布'"
              @click="publish(scope.row)"
            >
              <el-icon><Check /></el-icon>发布
            </el-button>
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除此公告吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-empty v-if="!loading && tableData.length === 0" description="暂无公告" />

    <div class="admin-pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next"
        :total="total"
      />
    </div>

    <!-- 弹框 -->
    <el-dialog :title="entity.id ? '编辑公告' : '新增公告'" v-model="dialogVisible" width="500px">
      <el-form :model="entity" :rules="rules" ref="form" label-width="80px" class="dialog-form">
        <el-form-item label="名称" prop="name">
          <el-input v-model="entity.name" placeholder="请输入公告名称" clearable />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            type="textarea"
            v-model="entity.content"
            placeholder="请输入公告内容"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import { Bell, Plus, Check } from '@element-plus/icons-vue'

export default {
  name: "Notice",
  components: { Bell, Plus, Check },
  data() {
    return {
      loading: false,
      entity: {},
      dialogVisible: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      rules: {
        name:    [{ required: true, message: "请输入公告名称", trigger: "blur" }],
        content: [{ required: true, message: "请输入公告内容", trigger: "blur" }],
      },
    };
  },
  created() { this.load(); },
  methods: {
    load() {
      this.loading = true;
      request.get("/notice/page", {
        params: { pageNum: this.pageNum, pageSize: this.pageSize }
      }).then(res => {
        this.loading = false;
        this.tableData = res.data.records;
        this.total = res.data.total;
      }).catch(() => { this.loading = false; });
    },
    add() { this.entity = {}; this.dialogVisible = true; },
    save() {
      this.$refs.form.validate(valid => {
        if (!valid) return;
        const method = this.entity.id ? "put" : "post";
        request[method]("/notice", this.entity).then(res => {
          if (res.code === '0') {
            this.$message.success(this.entity.id ? '更新成功' : '新增成功');
            this.load();
            this.dialogVisible = false;
          } else {
            this.$message.error(res.msg);
          }
        });
      });
    },
    handleEdit(row) { this.entity = { ...row }; this.dialogVisible = true; },
    handleDelete(id) {
      request.delete("/notice/" + id).then(res => {
        if (res.code === '0') { this.$message.success('删除成功'); this.load(); }
        else this.$message.error(res.msg);
      });
    },
    publish(row) {
      row.status = "已发布";
      request.put("/notice", row).then(res => {
        if (res.code === '0') this.$message.success('发布成功');
        else this.$message.error(res.msg);
      });
    },
    handleSizeChange(ps) { this.pageSize = ps; this.load(); },
    handleCurrentChange(pn) { this.pageNum = pn; this.load(); },
  }
};
</script>

<style scoped></style>
