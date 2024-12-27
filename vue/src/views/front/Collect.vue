<template>
  <div style="padding: 10px">
    <!-- 数据表格 -->
    <el-table :data="tableData" border stripe>
      <el-table-column prop="name" label="贴子"></el-table-column>
      <el-table-column label="评论内容" width="120" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="show(scope.row.content)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="收藏时间"></el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template #default="scope">
          <el-popconfirm
            title="确定要取消收藏这个帖子吗？"
            @confirm="handleDelete(scope.row.id)"
          >
            <template #reference>
              <el-button type="danger" size="small">取消收藏</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="长评" v-model="vis" width="40%">
      <el-card>
        <v-md-preview :text="longComment"></v-md-preview>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Collect",
  data() {
    return {
      tableData: [], // 收藏数据
      longComment: "", // 长评论内容
      vis: false, // 弹窗可见性
      user: JSON.parse(sessionStorage.getItem("user") || "{}"), // 当前用户信息
    };
  },
  created() {
    this.load(); // 初始化加载数据
  },
  methods: {
    // 加载收藏数据
    load() {
      request.get(`/collect/user/${this.user.id}`).then((res) => {
        if (res.code === "0") {
          this.tableData = res.data; // 加载表格数据
        } else {
          this.$message.error(res.msg || "加载失败");
        }
      });
    },
    // 显示长评论内容
    show(content) {
      this.longComment = content;
      this.vis = true;
    },
    // 删除收藏
    handleDelete(id) {
      request
        .delete(`/collect/${id}`) // 调用后端的删除接口
        .then((res) => {
          if (res.code === "0") {
            this.$message({
              type: "success",
              message: "取消收藏成功",
            });
            this.load(); // 刷新表格数据
          } else {
            this.$message.error(res.msg || "取消收藏失败");
          }
        })
        .catch(() => {
          this.$message.error("取消收藏失败，请稍后重试");
        });
    },
  },
};
</script>

<style scoped>
.el-table {
  margin-top: 10px;
}
</style>
