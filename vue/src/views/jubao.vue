<template>
  <div style="padding: 10px">
    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column prop="user" label="投诉人"></el-table-column>
      <el-table-column prop="content" label="投诉内容"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row)">
            <template #reference>
              <el-button size="mini" type="danger">已知悉并删除该评论</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div style="margin: 10px 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>
  </div>
</template>
<script>
import request from "@/utils/request";

export default {
  name: "Jubao",
  data() {
    return {
      loading: false, // 表格加载状态
      pageNum: 1, // 当前页码
      pageSize: 10, // 每页条数
      total: 0, // 数据总条数
      tableData: [], // 表格数据
    };
  },
  created() {
    this.load(); // 页面加载时获取数据
  },
  methods: {
    // 获取数据
    load() {
      this.loading = true;
      request
        .get("/jubao/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.loading = false;
          if (res.code === "0") {
            this.tableData = res.data.records;
            this.total = res.data.total;
          } else {
            this.$message.error(res.msg || "数据加载失败");
          }
        })
        .catch(() => {
          this.loading = false;
          this.$message.error("请求失败，请稍后重试");
        });
    },

    // 删除投诉和评论
    async handleDelete(row) {
      this.loading = true;
      try {
        // 删除评论
        await request.delete(`/message/${row.commentId}`);
        this.$message.success("删除评论成功");

        // 删除投诉
        await request.delete(`/jubao/${row.id}`);
        this.$message.success("删除投诉记录成功");

        // 刷新表格
        this.load();
      } catch (error) {
        this.$message.error("删除失败，请稍后重试");
      } finally {
        this.loading = false;
      }
    },

    // 分页事件
    handleSizeChange(newPageSize) {
      this.pageSize = newPageSize;
      this.debounceLoad(); // 防抖加载
    },
    handleCurrentChange(newPageNum) {
      this.pageNum = newPageNum;
      this.debounceLoad(); // 防抖加载
    },

    // 防抖加载
    debounceLoad: _.debounce(function () {
      this.load();
    }, 300),
  },
};
</script>
