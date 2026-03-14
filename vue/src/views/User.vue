<template>
  <div class="admin-page">
    <!-- 工具栏 -->
    <div class="admin-toolbar">
      <el-input
        v-model="search"
        placeholder="搜索用户名 / 昵称..."
        clearable
        class="search-input"
        @keyup.enter="load"
      >
        <template #append>
          <el-button icon="el-icon-search" @click="load" />
        </template>
      </el-input>
    </div>

    <!-- 表格 -->
    <div class="admin-table-wrap">
      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="username"  label="用户名" min-width="110" />
        <el-table-column prop="nickName"  label="昵称"   min-width="110" />
        <el-table-column prop="phone"     label="手机号" width="130" />
        <el-table-column prop="age"       label="年龄"   width="70" align="center" />
        <el-table-column prop="sex"       label="性别"   width="70" align="center" />
        <el-table-column prop="address"   label="地址"   min-width="140" show-overflow-tooltip />
        <el-table-column label="禁止评论" width="100" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.forbidComment"
              active-color="var(--danger-color)"
              inactive-color="#ddd"
              @change="change(scope.row, 'forbidComment')"
            />
          </template>
        </el-table-column>
        <el-table-column label="禁止登录" width="100" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.forbidLogin"
              active-color="var(--danger-color)"
              inactive-color="#ddd"
              @change="change(scope.row, 'forbidLogin')"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="90" align="center" fixed="right">
          <template #default="scope">
            <el-popconfirm title="确定删除该用户吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-empty v-if="!loading && tableData.length === 0" description="暂无数据" />

    <!-- 分页 -->
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

export default {
  name: "User",
  data() {
    return {
      loading: false,
      search: "",
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
      request.get("/user/page", {
        params: { pageNum: this.pageNum, pageSize: this.pageSize, search: this.search }
      }).then(res => {
        this.loading = false;
        this.tableData = res.data.records.filter(item => item.role !== 1);
        this.total = this.tableData.length;
      }).catch(() => { this.loading = false; });
    },
    handleDelete(id) {
      request.delete(`/user/${id}`).then(res => {
        if (res.code === '0') { this.$message.success('删除成功'); this.load(); }
        else this.$message.error(res.msg);
      });
    },
    change(row, field) {
      request.put('/user', row).then(res => {
        if (res.code === '0') {
          this.$message.success(field === 'forbidComment' ? '评论权限已更新' : '登录权限已更新');
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    handleSizeChange(ps) { this.pageSize = ps; this.load(); },
    handleCurrentChange(pn) { this.pageNum = pn; this.load(); },
  }
};
</script>

<style scoped></style>
