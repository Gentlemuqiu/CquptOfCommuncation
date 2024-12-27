<template>
  <div style="padding: 20px; background-color: #f9f9f9; border-radius: 8px;">
    <!-- 功能区域 -->
    <div style="margin: 10px 0;">
      <el-button type="primary" @click="add" style="font-weight: bold;">新增</el-button>
    </div>

    <!-- 搜索区域 -->
    <div style="margin: 10px 0;">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px; font-weight: bold;" @click="load">查询</el-button>
    </div>

    <!-- 表格显示 -->
    <el-table
      v-if="!loading || tableData.length > 0"
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); border-radius: 8px; overflow: hidden;">
      <el-table-column prop="name" label="题目"></el-table-column>
      <el-table-column prop="description" label="简介"></el-table-column>
      <el-table-column prop="date" label="发布日期"></el-table-column>
      <el-table-column prop="area" label="类型"></el-table-column>
      <el-table-column prop="zan" label="赞"></el-table-column>
      <el-table-column prop="cai" label="踩"></el-table-column>
      <el-table-column label="图片">
        <template #default="scope">
          <el-image style="width: 100px; height: 100px" :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态 -->
    <el-empty v-if="!loading && tableData.length === 0" description="暂无数据"></el-empty>

    <!-- 分页 -->
    <div style="margin: 10px 0;" v-if="tableData.length > 0">
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

    <!-- 弹窗表单 -->
    <el-dialog title="信息" v-model="dialogVisible" width="40%">
      <el-form :model="entity" :rules="rules" ref="entityForm" label-width="120px">
        <el-form-item label="题目" prop="name">
          <el-input v-model="entity.name" autocomplete="off" style="width: 100%"></el-input>
        </el-form-item>

        <!-- 简介 -->
        <el-form-item label="简介" prop="description">
          <el-input 
            type="textarea" 
            v-model="entity.description" 
            autocomplete="off" 
            placeholder="请输入简介..." 
            :rows="4" 
            style="width: 100%; resize: none; border-radius: 6px; box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1); padding: 8px;"></el-input>
        </el-form-item>

        <el-form-item label="发布日期" prop="date">
          <el-date-picker style="width: 100%" v-model="entity.date" type="date" value-format="YYYY-MM-DD" placeholder="选择日期"></el-date-picker>
        </el-form-item>

        <el-form-item label="类型" prop="area">
          <el-select clearable v-model="entity.area" placeholder="请选择内容所属类型" style="width: 100%">
            <el-option v-for="item in ['运动', '音乐', '剧情', '搞笑', '开胃']" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>

        <!-- 图片上传 -->
        <el-form-item label="图片" prop="img">
          <el-upload action="http://localhost:8080/files/upload" :on-success="fileSuccessUpload" :file-list="fileList" limit="1">
            <el-button size="small" type="primary">点击上传图片</el-button>
          </el-upload>
        </el-form-item>

        <!-- 视频上传 -->
        <el-form-item label="视频" prop="url">
          <el-upload action="http://localhost:8080/files/upload" :on-success="videoSuccessUpload" :file-list="videoList" limit="1" accept="video/*">
            <el-button size="small" type="primary">点击上传视频</el-button>
          </el-upload>
        </el-form-item>

      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
let url = "/movie";

export default {
  name: "Movie",
  data() {
    return {
      loading: true,
      entity: {}, // 表单数据
      dialogVisible: false,
      search: "",
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [], // 表格数据
      fileList: [], // 图片上传的文件列表
      videoList: [], // 视频上传的文件列表
      rules: { // 验证规则
        name: [{ required: true, message: "请输入题目", trigger: "blur" }],
        description: [{ required: true, message: "请输入简介", trigger: "blur" }],
        date: [{ required: true, message: "请选择发布日期", trigger: "change" }],
        area: [{ required: true, message: "请选择类型", trigger: "change" }],
        img: [{ required: true, message: "请上传图片", trigger: "change" }],
        url: [{ required: true, message: "请上传视频", trigger: "change" }]
      }
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
        })
        .catch(() => {
          this.loading = false;
        });
    },
    // 新增
    add() {
      this.dialogVisible = true;
      this.entity = { name: "", description: "", date: "", area: "", img: "", url: "" }; // 初始化字段
      this.fileList = [];
      this.videoList = [];
    },
    // 保存（新增或更新）
    save() {
      this.$refs.entityForm.validate(valid => {
        if (!valid) return; // 表单验证不通过则中止保存
        const method = this.entity.id ? "put" : "post";
        request[method](url, this.entity).then(() => {
          this.load();
          this.dialogVisible = false;
        });
      });
    },
    // 编辑
    handleEdit(row) {
      this.entity = { ...row };
      this.dialogVisible = true;
      this.fileList = [];
      this.videoList = [];
    },
    // 删除
    handleDelete(id) {
      request.delete(url + "/" + id).then(() => {
        this.load();
      });
    },
    // 图片上传成功
    fileSuccessUpload(res) {
      this.entity.img = res.data;
    },
    // 视频上传成功
    videoSuccessUpload(res) {
      this.entity.url = res.data;
    },
    // 分页处理
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    }
  }
};
</script>

<style scoped>
</style>
