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
            :preview-src-list="[]"
          ></el-image>
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
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      user: JSON.parse(sessionStorage.getItem("user") || "{}"),
    };
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.loading = true;
      request
        .get(`${url}/page`, {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            userId: this.user.id,
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

    handleDelete(id) {
      request
        .delete(`${url}/${id}`)
        .then((res) => {
          if (res.code === "0") {
            this.$message.success("删除成功");
            this.load();
          } else {
            this.$message.error(res.msg || "删除失败");
          }
        })
        .catch(() => {
          this.$message.error("删除失败，请稍后重试");
        });
    },

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