<template>
  <div style="padding: 10px">
    <!-- 搜索区域 -->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%">
      <el-table-column prop="title" label="标题（仅长评）"></el-table-column>
      <el-table-column label="内容">
        <template #default="scope">
          <div style="text-overflow:ellipsis; width:100%;">
            {{ scope.row.content.length > 100 ? scope.row.content.substring(0, 100) + '...' : scope.row.content }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="评论人"></el-table-column>
      <el-table-column prop="time" label="评论时间"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
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
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
let url = "/message";

export default {
  name: "Message",
  data() {
    return {
      loading: true,
      search: "", // 查询关键字
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [] // 表格数据
    };
  },
  created() {
    this.load();
  },
  methods: {
    // 加载数据
    load() {
      this.loading = true;
      request
        .get(url + "/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            search: this.search
          }
        })
        .then(res => {
          this.loading = false;
          this.tableData = res.data.records;
          this.total = res.data.total;
        });
    },
    // 删除数据
    handleDelete(id) {
      request.delete(`${url}/${id}`).then(res => {
        if (res.code === "0") {
          this.$message.success("删除成功");
          this.load(); // 刷新数据
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 分页：改变每页条数
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    // 分页：改变当前页
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    }
  }
};
</script>

<style scoped>
</style>
