<template>
    <div class="new-post-container">
      <div class="form-header">
        <h2>新增帖子</h2>
      </div>
      <el-card class="form-card">
        <el-form :model="entity" :rules="rules" ref="entityForm" label-width="100px">
          <!-- 题目 -->
          <el-form-item label="题目" prop="name">
            <el-input
              v-model="entity.name"
              autocomplete="off"
              placeholder="请输入题目"
              class="input-field"
            ></el-input>
          </el-form-item>
  
          <!-- 简介 -->
          <el-form-item label="简介" prop="description">
            <el-input
              type="textarea"
              v-model="entity.description"
              autocomplete="off"
              placeholder="请输入简介"
              :rows="4"
              class="textarea-field"
            ></el-input>
          </el-form-item>
  
          <!-- 发布日期 -->
          <el-form-item label="发布日期" prop="date">
            <el-date-picker
              v-model="entity.date"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择发布日期"
              class="input-field"
            ></el-date-picker>
          </el-form-item>
  
          <!-- 类型 -->
          <el-form-item label="类型" prop="area">
            <el-select
              clearable
              v-model="entity.area"
              placeholder="请选择类型"
              class="input-field"
            >
              <el-option
                v-for="item in ['运动', '音乐', '剧情', '搞笑', '开胃']"
                :key="item"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
          </el-form-item>
  
          <!-- 图片上传 -->
          <el-form-item label="图片" prop="img">
            <el-upload
              action="http://localhost:8080/files/upload"
              :on-success="fileSuccessUpload"
              :file-list="fileList"
              limit="1"
              class="upload-field"
            >
              <el-button type="primary" size="small">点击上传图片</el-button>
            </el-upload>
          </el-form-item>
  
          <!-- 视频上传 -->
          <el-form-item label="视频" prop="url">
            <el-upload
              action="http://localhost:8080/files/upload"
              :on-success="videoSuccessUpload"
              :file-list="videoList"
              limit="1"
              accept="video/*"
              class="upload-field"
            >
              <el-button type="primary" size="small">点击上传视频</el-button>
            </el-upload>
          </el-form-item>
  
          <!-- 按钮组 -->
          <div class="form-footer">
            <el-button @click="cancel" size="large">取消</el-button>
            <el-button type="primary" @click="save" size="large">保存</el-button>
          </div>
        </el-form>
      </el-card>
    </div>
  </template>
  
  <script>
  export default {
    name: "NewPost",
    data() {
      return {
        entity: {
          name: "",
          description: "",
          date: "",
          area: "",
          img: "",
          url: ""
        }, // 表单数据
        fileList: [], // 图片上传列表
        videoList: [], // 视频上传列表
        rules: {
          name: [{ required: true, message: "请输入题目", trigger: "blur" }],
          description: [{ required: true, message: "请输入简介", trigger: "blur" }],
          date: [{ required: true, message: "请选择发布日期", trigger: "change" }],
          area: [{ required: true, message: "请选择类型", trigger: "change" }],
          img: [{ required: true, message: "请上传图片", trigger: "change" }],
          url: [{ required: true, message: "请上传视频", trigger: "change" }]
        }
      };
    },
    methods: {
      // 图片上传成功
      fileSuccessUpload(res) {
        this.entity.img = res.data;
      },
      // 视频上传成功
      videoSuccessUpload(res) {
        this.entity.url = res.data;
      },
      // 保存（验证表单后提交）
      save() {
        this.$refs.entityForm.validate(valid => {
          if (!valid) return;
          this.$message({
            type: "success",
            message: "保存成功！"
          });
          // 提交保存逻辑
        });
      },
      // 取消
      cancel() {
        this.entity = { name: "", description: "", date: "", area: "", img: "", url: "" }; // 清空表单
        this.fileList = [];
        this.videoList = [];
      }
    }
  };
  </script>
  
  <style scoped>
  .new-post-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    background-color: #f5f5f5;
    border-radius: 10px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  }
  
  .form-header {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .form-header h2 {
    font-size: 24px;
    font-weight: bold;
    color: #333;
  }
  
  .form-card {
    padding: 20px;
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .input-field {
    border-radius: 6px;
    padding: 8px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
  }
  
  .textarea-field {
    border-radius: 6px;
    padding: 8px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
    resize: none;
  }
  
  .upload-field {
    display: block;
  }
  
  .form-footer {
    text-align: right;
    margin-top: 20px;
  }
  
  .form-footer .el-button {
    margin-left: 10px;
    border-radius: 6px;
  }
  
  .el-form-item {
    margin-bottom: 20px;
  }
  
  .el-form-item label {
    font-weight: bold;
    font-size: 14px;
    color: #555;
  }
  </style>
  