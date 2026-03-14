<template>
  <div class="post-container">
    <div class="post-card">
      <div class="post-header">
        <h2 class="post-title">
          <i class="el-icon-edit-outline"></i>
          发布就业信息
        </h2>
        <p class="post-subtitle">分享招聘资讯、求职经验，帮助更多同学</p>
      </div>

      <el-form :model="entity" :rules="rules" ref="entityForm" label-width="100px" class="post-form">

        <!-- 标题 -->
        <el-form-item label="标题" prop="name">
          <el-input
            v-model="entity.name"
            placeholder="请输入信息标题（不超过 50 字）"
            maxlength="50"
            show-word-limit
            clearable
          />
        </el-form-item>

        <!-- 简介 -->
        <el-form-item label="简介" prop="description">
          <el-input
            type="textarea"
            v-model="entity.description"
            placeholder="请简要描述该信息的主要内容..."
            :rows="4"
            maxlength="300"
            show-word-limit
          />
        </el-form-item>

        <!-- 发布日期 -->
        <el-form-item label="发布日期" prop="date">
          <el-date-picker
            v-model="entity.date"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择发布日期"
            style="width: 100%"
          />
        </el-form-item>

        <!-- 分类 -->
        <el-form-item label="分类" prop="area">
          <el-select
            v-model="entity.area"
            placeholder="请选择信息分类"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in AREAS"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            >
              <span>{{ item.icon }} {{ item.label }}</span>
            </el-option>
          </el-select>
        </el-form-item>

        <!-- 封面图片（可选） -->
        <el-form-item label="封面图片">
          <el-upload
            action="http://localhost:8080/files/upload"
            :on-success="fileSuccessUpload"
            :on-error="handleUploadError"
            :file-list="fileList"
            :limit="1"
            :on-exceed="handleExceed"
            accept="image/*"
          >
            <el-button icon="el-icon-upload2" type="default">
              {{ entity.img ? '重新上传' : '点击上传封面图片（可选）' }}
            </el-button>
          </el-upload>
          <!-- 图片预览 -->
          <div v-if="entity.img" class="img-preview">
            <img :src="entity.img" alt="封面预览" />
            <el-button
              type="text"
              icon="el-icon-delete"
              class="img-remove"
              @click="entity.img = ''; fileList = []"
            >
              移除
            </el-button>
          </div>
          <div class="upload-tip">支持 jpg/png/gif，建议尺寸 800×450，最大 5MB</div>
        </el-form-item>

        <!-- 提交 -->
        <el-form-item>
          <div class="form-actions">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="save" :loading="saving">
              {{ saving ? '发布中...' : '立即发布' }}
            </el-button>
          </div>
        </el-form-item>

      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

const AREAS = [
  { key: '校园招聘', label: '校园招聘', icon: '🏫' },
  { key: '实习信息', label: '实习信息', icon: '💼' },
  { key: '社会招聘', label: '社会招聘', icon: '🏢' },
  { key: '求职技巧', label: '求职技巧', icon: '💡' },
  { key: '行业动态', label: '行业动态', icon: '📊' },
];

export default {
  name: "LittleSaying",
  data() {
    return {
      AREAS,
      entity: {
        name: "",
        description: "",
        date: "",
        area: "",
        img: "",
        postUserId: null   // 发帖人 ID，created 时自动赋值
      },
      fileList: [],
      saving: false,
      // img 不加入必填，upload 无法触发表单校验
      rules: {
        name: [
          { required: true, message: "请输入标题", trigger: "blur" },
          { max: 50, message: "标题不能超过 50 字", trigger: "blur" }
        ],
        description: [
          { required: true, message: "请输入简介", trigger: "blur" }
        ],
        date: [{ required: true, message: "请选择发布日期", trigger: "change" }],
        area: [{ required: true, message: "请选择分类", trigger: "change" }],
      }
    };
  },
  created() {
    // 自动填充发帖人 ID
    const user = JSON.parse(sessionStorage.getItem("user") || "{}");
    if (!user.id) {
      this.$message.warning("请先登录后再发布信息");
      this.$router.push("/login");
      return;
    }
    this.entity.postUserId = user.id;
  },
  methods: {
    fileSuccessUpload(res) {
      if (res.code === '0') {
        this.entity.img = res.data;
        this.$message.success('封面图片上传成功');
      } else {
        this.$message.error(res.msg || '图片上传失败');
      }
    },
    handleUploadError() {
      this.$message.error('上传失败，请检查网络或文件大小');
    },
    handleExceed() {
      this.$message.warning('已有文件，请先删除后再上传');
    },
    save() {
      this.$refs.entityForm.validate(async (valid) => {
        if (!valid) return;
        this.saving = true;
        try {
          const res = await request.post("/movie", this.entity);
          if (res.code === '0') {
            this.$message.success("发布成功！");
            this.$router.push('/front/home');
          } else {
            this.$message.error(res.msg || '发布失败，请稍后重试');
          }
        } catch {
          this.$message.error("网络错误，请稍后重试");
        } finally {
          this.saving = false;
        }
      });
    },
    cancel() {
      this.$router.back();
    }
  }
};
</script>

<style scoped>
.post-container {
  max-width: 760px;
  margin: 0 auto;
}

.post-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  padding: var(--spacing-2xl);
  box-shadow: var(--shadow-base);
}

.post-header {
  margin-bottom: var(--spacing-2xl);
  padding-bottom: var(--spacing-lg);
  border-bottom: 2px solid var(--border-lighter);
}

.post-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-sm);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.post-title i { color: var(--primary-color); }

.post-subtitle {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  margin: 0;
}

.post-form :deep(.el-form-item__label) {
  font-weight: var(--font-weight-semibold);
  color: var(--text-secondary);
}

/* 图片预览 */
.img-preview {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  margin-top: var(--spacing-base);
  padding: var(--spacing-base);
  background: var(--bg-secondary);
  border-radius: var(--radius-base);
  border: 1px solid var(--border-light);
}

.img-preview img {
  width: 120px;
  height: 68px;
  object-fit: cover;
  border-radius: var(--radius-sm);
}

.img-remove {
  color: var(--danger-color) !important;
  font-size: var(--font-size-sm) !important;
}

.upload-tip {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
  margin-top: var(--spacing-sm);
}


.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-base);
  padding-top: var(--spacing-md);
}

@media (max-width: 768px) {
  .post-card {
    padding: var(--spacing-lg) var(--spacing-md);
  }
}
</style>
