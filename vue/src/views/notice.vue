<template>
  <div style="padding: 10px">
    <!-- 功能区域 -->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
    </div>

    <!-- 表格显示 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="content" label="内容"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column prop="time" label="发布时间"></el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            type="primary"
            size="mini"
            @click="publish(scope.row)"
            :disabled="scope.row.status === '已发布'"
          >发布</el-button>
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
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
      ></el-pagination>
    </div>

    <!-- 弹窗表单 -->
    <el-dialog title="信息" v-model="dialogVisible" width="40%">
      <el-form :model="entity" label-width="120px" :rules="rules" ref="form">
        <el-form-item label="名称" prop="name">
          <el-input v-model="entity.name" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            type="textarea"
            v-model="entity.content"
            autocomplete="off"
            style="width: 80%"
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

const url = "/notice";

export default {
  name: "Notice",
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
        name: [{ required: true, message: "请输入名称", trigger: "blur" }],
        content: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },
    };
  },
  created() {
    this.load();
  },
  methods: {
    // 消息提示方法
    showMessage(type, message) {
      this.$message({ type, message });
    },

    // 加载表格数据
    load() {
      this.loading = true;
      request
        .get(url + "/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.loading = false;
          this.tableData = res.data.records;
          this.total = res.data.total;
        })
        .catch(() => {
          this.loading = false;
          this.showMessage("error", "数据加载失败，请稍后重试");
        });
    },

    // 新增
    add() {
      this.dialogVisible = true;
      this.entity = {};
    },

    // 保存（新增或更新）
    save() {
      this.$refs.form.validate((valid) => {
        if (!valid) return;

        const method = this.entity.id ? "put" : "post";
        request[method](url, this.entity)
          .then((res) => {
            if (res.code === "0") {
              this.showMessage(
                "success",
                this.entity.id ? "更新成功" : "新增成功"
              );
              this.load();
              this.dialogVisible = false;
            } else {
              this.showMessage("error", res.msg);
            }
          })
          .catch(() => {
            this.showMessage("error", "操作失败，请稍后重试");
          });
      });
    },

    // 编辑
    handleEdit(row) {
      this.entity = { ...row };
      this.dialogVisible = true;
    },

    // 删除
    handleDelete(id) {
      request
        .delete(url + "/" + id)
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

    // 发布
    publish(row) {
      row.status = "已发布";
      request
        .put(url, row)
        .then((res) => {
          if (res.code === "0") {
            this.showMessage("success", "发布成功");
          } else {
            this.showMessage("error", res.msg);
          }
        })
        .catch(() => {
          this.showMessage("error", "发布失败，请稍后重试");
        });
    },

    // 分页功能
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
