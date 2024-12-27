<template>
  <div style="padding: 10px">

    <!-- 搜索区域 -->
    <div style="margin: 10px 0">
      <el-input
        v-model="search"
        placeholder="请输入关键字"
        style="width: 20%"
        clearable
      ></el-input>
      <el-button
        type="primary"
        style="margin-left: 5px"
        @click="load"
      >查询</el-button>
    </div>

    <!-- 表格显示 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%; border-radius: 8px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);"
    >
      <el-table-column prop="id" label="ID" sortable></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="nickName" label="昵称"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="age" label="年龄"></el-table-column>
      <el-table-column prop="sex" label="性别"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column label="禁止评论">
        <template #default="scope">
          <el-switch
            @change="change(scope.row, 'forbidComment')"
            v-model="scope.row.forbidComment"
            active-color="#f00"
            inactive-color="#ddd"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="禁止登录">
        <template #default="scope">
          <el-switch
            @change="change(scope.row, 'forbidLogin')"
            v-model="scope.row.forbidLogin"
            active-color="#f00"
            inactive-color="#ddd"
          >
          </el-switch>
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
    <div style="margin: 10px 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>

    <!-- 弹窗表单 -->
    <el-dialog title="信息" v-model="dialogVisible" width="40%">
      <el-form :model="entity" label-width="120px">
        <el-form-item label="用户名">
          <el-input v-model="entity.username" style="width: 80%" clearable></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="entity.nickName" style="width: 80%" clearable></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="entity.phone" type="number" style="width: 80%" clearable></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="entity.age" type="number" style="width: 80%" clearable></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio v-model="entity.sex" label="男">男</el-radio>
          <el-radio v-model="entity.sex" label="女">女</el-radio>
          <el-radio v-model="entity.sex" label="未知">未知</el-radio>
        </el-form-item>
        <el-form-item label="地址">
          <el-input
            type="textarea"
            v-model="entity.address"
            style="width: 80%"
            clearable
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

const url = "/user";

export default {
  name: "User",
  data() {
    return {
      loading: false,
      entity: {},
      dialogVisible: false,
      search: "",
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
    };
  },
  created() {
    this.load();
  },
  methods: {
    showMessage(type, message) {
      this.$message({ type, message });
    },
    load() {
      this.loading = true;
      request
        .get(`${url}/page`, {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            search: this.search,
          },
        })
        .then((res) => {
          this.loading = false;
          // 过滤掉管理员
          this.tableData = res.data.records.filter(item => item.role !== 1); 
          this.total = this.tableData.length; // 更新总条数
        })
        .catch(() => {
          this.loading = false;
          this.showMessage("error", "加载失败，请稍后重试");
        });
    },
    save() {
      const method = this.entity.id ? "put" : "post";
      request[method](url, this.entity)
        .then((res) => {
          if (res.code === "0") {
            this.showMessage("success", this.entity.id ? "更新成功" : "新增成功");
            this.load();
            this.dialogVisible = false;
          } else {
            this.showMessage("error", res.msg);
          }
        })
        .catch(() => {
          this.showMessage("error", "操作失败，请稍后重试");
        });
    },
    handleDelete(id) {
      request
        .delete(`${url}/${id}`)
        .then((res) => {
          if (res.code === "0") {
            this.showMessage("success", "删除成功");
            this.load();
          } else {
            this.showMessage("error", res.msg);
          }
        })
        .catch(() => {
          this.showMessage("error", "删除失败，请稍后重试");
        });
    },
    change(row, field) {
      request
        .put(url, row)
        .then((res) => {
          if (res.code === "0") {
            this.showMessage("success", `${field === "forbidComment" ? "评论权限" : "登录权限"}修改成功`);
          } else {
            this.showMessage("error", res.msg);
          }
        })
        .catch(() => {
          this.showMessage("error", "权限修改失败，请稍后重试");
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
