<template>
  <div class="admin-page">
    <!-- 工具栏 -->
    <div class="admin-toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="add">新增信息</el-button>
      <el-input
        v-model="search"
        placeholder="搜索标题/简介..."
        clearable
        class="search-input"
        @keyup.enter="load"
      >
        <template #append>
          <el-button icon="el-icon-search" @click="load" />
        </template>
      </el-input>
    </div>

    <!-- 数据表格 -->
    <div class="admin-table-wrap">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="name" label="标题" min-width="160" show-overflow-tooltip />
        <el-table-column prop="description" label="简介" min-width="200" show-overflow-tooltip />
        <el-table-column prop="date" label="发布日期" width="120" />
        <el-table-column prop="area" label="分类" width="110">
          <template #default="scope">
            <el-tag size="small" type="success" effect="plain">{{ scope.row.area }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="赞/踩" width="100" align="center">
          <template #default="scope">
            <span style="color: var(--success-color)">{{ scope.row.zan || 0 }}</span>
            /
            <span style="color: var(--danger-color)">{{ scope.row.cai || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="封面" width="90" align="center">
          <template #default="scope">
            <el-image
              style="width: 60px; height: 60px; border-radius: 6px"
              :src="scope.row.img"
              fit="cover"
              :preview-src-list="[scope.row.img]"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除此条信息吗？" @confirm="handleDelete(scope.row.id)">
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

    <!-- 编辑弹框 -->
    <el-dialog :title="entity.id ? '编辑信息' : '新增信息'" v-model="dialogVisible" width="560px">
      <el-form :model="entity" :rules="rules" ref="entityForm" label-width="90px" class="dialog-form">
        <el-form-item label="标题" prop="name">
          <el-input v-model="entity.name" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input
            type="textarea"
            v-model="entity.description"
            placeholder="请输入简介"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="发布日期" prop="date">
          <el-date-picker
            style="width: 100%"
            v-model="entity.date"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择日期"
          />
        </el-form-item>
        <el-form-item label="分类" prop="area">
          <el-select v-model="entity.area" placeholder="请选择分类" clearable style="width: 100%">
            <el-option
              v-for="item in AREAS"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="封面图片">
          <el-upload
            action="http://localhost:8080/files/upload"
            :on-success="fileSuccessUpload"
            :file-list="fileList"
            :limit="1"
            list-type="picture"
          >
            <el-button type="default" icon="el-icon-upload2">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="视频">
          <el-upload
            action="http://localhost:8080/files/upload"
            :on-success="videoSuccessUpload"
            :file-list="videoList"
            :limit="1"
            accept="video/*"
          >
            <el-button type="default" icon="el-icon-upload2">上传视频</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import { INFO_AREAS } from "@/config/navigation";

const URL = "/movie";

export default {
  name: "Sayings",
  data() {
    return {
      AREAS: INFO_AREAS.map(a => a.key),
      loading: false,
      entity: {},
      dialogVisible: false,
      search: "",
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      fileList: [],
      videoList: [],
      rules: {
        name:        [{ required: true, message: "请输入标题", trigger: "blur" }],
        description: [{ required: true, message: "请输入简介", trigger: "blur" }],
        date:        [{ required: true, message: "请选择发布日期", trigger: "change" }],
        area:        [{ required: true, message: "请选择分类", trigger: "change" }],
      }
    };
  },
  created() { this.load(); },
  methods: {
    load() {
      this.loading = true;
      request.get(URL + "/page", {
        params: { pageNum: this.pageNum, pageSize: this.pageSize, search: this.search }
      }).then(res => {
        this.loading = false;
        this.tableData = res.data.records;
        this.total = res.data.total;
      }).catch(() => { this.loading = false; });
    },
    add() {
      this.entity = { name: "", description: "", date: "", area: "", img: "", url: "" };
      this.fileList = [];
      this.videoList = [];
      this.dialogVisible = true;
    },
    save() {
      this.$refs.entityForm.validate(valid => {
        if (!valid) return;
        const method = this.entity.id ? "put" : "post";
        request[method](URL, this.entity).then(res => {
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
    handleEdit(row) {
      this.entity = { ...row };
      this.fileList = [];
      this.videoList = [];
      this.dialogVisible = true;
    },
    handleDelete(id) {
      request.delete(URL + "/" + id).then(() => {
        this.$message.success('删除成功');
        this.load();
      });
    },
    fileSuccessUpload(res) { this.entity.img = res.data; },
    videoSuccessUpload(res) { this.entity.url = res.data; },
    handleSizeChange(pageSize) { this.pageSize = pageSize; this.load(); },
    handleCurrentChange(pageNum) { this.pageNum = pageNum; this.load(); },
  }
};
</script>

<style scoped></style>
