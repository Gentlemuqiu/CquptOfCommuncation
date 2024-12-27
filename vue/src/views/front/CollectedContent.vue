<template>
  <div style="padding: 10px">
    <!-- 表格区域 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column label="图片">
        <template #default="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.img"
            :preview-src-list="[scope.row.img]"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="link" label="链接">
        <template #default="scope">
          <a :href="scope.row.link" target="_blank">{{ scope.row.link }}</a>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <el-popconfirm
            title="确定删除吗？"
            @confirm="handleDelete(scope.row.id)"
          >
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div style="margin: 10px 0; background-color: white">
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

const url = "/collectMovie";

export default {
  name: "CollectMovie",
  data() {
    return {
      loading: false, // 表格加载状态
      pageNum: 1, // 当前页码
      pageSize: 10, // 每页条数
      total: 0, // 数据总量
      tableData: [], // 表格数据
      user: JSON.parse(sessionStorage.getItem("user") || "{}"), // 当前用户信息
    };
  },
  created() {
    this.load(); // 初始化加载数据
  },
  methods: {
    // 加载表格数据
    load() {
      this.loading = true;
      request
        .get(`${url}/page`, {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            userId: this.user.id, // 传递当前用户的 ID
          },
        })
        .then((res) => {
          this.loading = false;
          if (res.code === "0") {
            this.tableData = res.data.records || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "加载数据失败");
          }
        })
        .catch(() => {
          this.loading = false;
          this.$message.error("加载数据失败，请稍后重试");
        });
    },

    // 删除数据
    handleDelete(id) {
      request
        .delete(`${url}/${id}`)
        .then((res) => {
          if (res.code === "0") {
            this.$message.success("删除成功");
            this.load(); // 重新加载数据
          } else {
            this.$message.error(res.msg || "删除失败");
          }
        })
        .catch(() => {
          this.$message.error("删除失败，请稍后重试");
        });
    },

    // 分页相关方法
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
  },
};
</script>

<style scoped>
.el-table {
  margin-top: 10px;
}
</style>
