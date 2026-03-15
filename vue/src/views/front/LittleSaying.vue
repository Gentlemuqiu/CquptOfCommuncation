<template>
  <div class="page-bg">
    <div class="post-wrapper">

      <!-- ── 顶部 Hero 横幅 ── -->
      <div class="hero-banner">
        <div class="hero-icon-wrap">
          <el-icon class="hero-icon"><EditPen /></el-icon>
        </div>
        <div class="hero-text">
          <h1>发布就业信息</h1>
          <p>分享招聘资讯、求职经验，帮助更多同学找到理想工作</p>
        </div>
      </div>

      <!-- ── 主表单卡片 ── -->
      <div class="post-card">
        <el-form
          :model="entity"
          :rules="rules"
          ref="entityForm"
          label-position="top"
          class="post-form"
        >

          <!-- ─ 组：基本信息 ─ -->
          <div class="group-label">
            <span class="group-dot"></span>
            基本信息
          </div>

          <!-- 标题 -->
          <el-form-item label="信息标题" prop="name">
            <el-input
              v-model="entity.name"
              placeholder="请输入信息标题，简洁明了（不超过 50 字）"
              maxlength="50"
              show-word-limit
              clearable
              size="large"
              class="title-input"
            >
              <template #prefix>
                <el-icon><Document /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 分类 + 日期 两列 -->
          <div class="form-row">
            <el-form-item label="信息分类" prop="area" class="form-col">
              <div class="area-cards">
                <div
                  v-for="item in AREAS"
                  :key="item.key"
                  class="area-card"
                  :class="{ active: entity.area === item.key }"
                  @click="entity.area = item.key"
                >
                  <span class="area-emoji">{{ item.icon }}</span>
                  <span class="area-label">{{ item.label }}</span>
                </div>
              </div>
            </el-form-item>

            <el-form-item label="发布日期" prop="date" class="form-col date-col">
              <el-date-picker
                v-model="entity.date"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择发布日期"
                style="width: 100%"
                size="large"
              />
            </el-form-item>
          </div>

          <!-- ─ 组：详细内容 ─ -->
          <div class="group-label">
            <span class="group-dot"></span>
            详细内容
          </div>

          <!-- 简介 -->
          <el-form-item label="信息简介" prop="description">
            <el-input
              type="textarea"
              v-model="entity.description"
              placeholder="请简要描述该信息的主要内容，包括岗位要求、薪资待遇、申请方式等..."
              :rows="5"
              maxlength="300"
              show-word-limit
              class="desc-textarea"
            />
          </el-form-item>

          <!-- ─ 组：封面图片 ─ -->
          <div class="group-label">
            <span class="group-dot"></span>
            封面图片
            <span class="optional-badge">选填</span>
          </div>

          <el-form-item>
            <!-- 已上传预览 -->
            <div v-if="entity.img" class="img-preview-card">
              <img :src="entity.img" alt="封面预览" class="preview-img" />
              <div class="preview-info">
                <div class="preview-title">封面已上传</div>
                <div class="preview-sub">点击右侧按钮可替换或移除</div>
                <div class="preview-actions">
                  <el-upload
                    :action="uploadUrl"
                    :on-success="fileSuccessUpload"
                    :on-error="handleUploadError"
                    :file-list="fileList"
                    :limit="1"
                    :on-exceed="handleExceed"
                    accept="image/*"
                    :show-file-list="false"
                  >
                    <el-button size="small" type="primary" plain>重新上传</el-button>
                  </el-upload>
                  <el-button
                    size="small"
                    type="danger"
                    plain
                    @click="entity.img = ''; fileList = []"
                  >
                    移除图片
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 未上传时的上传区域 -->
            <el-upload
              v-else
              :action="uploadUrl"
              :on-success="fileSuccessUpload"
              :on-error="handleUploadError"
              :file-list="fileList"
              :limit="1"
              :on-exceed="handleExceed"
              accept="image/*"
              :show-file-list="false"
              drag
              class="upload-dragger"
            >
              <div class="upload-inner">
                <el-icon class="upload-icon"><Picture /></el-icon>
                <div class="upload-title">拖拽图片到此处，或 <span class="upload-link">点击上传</span></div>
                <div class="upload-hint">支持 JPG / PNG / GIF · 建议尺寸 800×450 · 最大 5MB</div>
              </div>
            </el-upload>
          </el-form-item>

          <!-- ─ 底部操作栏 ─ -->
          <div class="form-footer">
            <div class="footer-tip">
              <el-icon><InfoFilled /></el-icon>
              <span>发布后内容将经过审核，请确保信息真实有效</span>
            </div>
            <div class="form-actions">
              <el-button class="cancel-btn" @click="cancel">
                <el-icon><ArrowLeft /></el-icon>
                返回
              </el-button>
              <el-button
                type="primary"
                class="publish-btn"
                @click="save"
                :loading="saving"
              >
                <el-icon v-if="!saving"><Promotion /></el-icon>
                {{ saving ? '发布中...' : '立即发布' }}
              </el-button>
            </div>
          </div>

        </el-form>
      </div>

    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { INFO_AREAS } from "@/config/navigation";
import {
  EditPen, Document, Picture, InfoFilled, ArrowLeft, Promotion
} from '@element-plus/icons-vue'

const UPLOAD_URL = (process.env.VUE_APP_BASEURL || '/api') + '/files/upload';

export default {
  name: "LittleSaying",
  components: { EditPen, Document, Picture, InfoFilled, ArrowLeft, Promotion },
  data() {
    return {
      AREAS: INFO_AREAS,
      uploadUrl: UPLOAD_URL,
      entity: {
        name: "",
        description: "",
        date: "",
        area: "",
        img: "",
        postUserId: null
      },
      fileList: [],
      saving: false,
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
/* ════════════════════════════════════════
   页面背景
════════════════════════════════════════ */
.page-bg {
  min-height: calc(100vh - 64px);
  background: var(--bg-page);
  padding: var(--spacing-xl) var(--spacing-xl) var(--spacing-3xl);
}

.post-wrapper {
  max-width: 800px;
  margin: 0 auto;
}

/* ════════════════════════════════════════
   顶部 Hero 横幅
════════════════════════════════════════ */
.hero-banner {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  background: var(--primary-gradient);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl) var(--spacing-2xl);
  margin-bottom: var(--spacing-xl);
  box-shadow: var(--shadow-primary);
  position: relative;
  overflow: hidden;
}

.hero-banner::before {
  content: '';
  position: absolute;
  top: -40px;
  right: -40px;
  width: 160px;
  height: 160px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.06);
}

.hero-banner::after {
  content: '';
  position: absolute;
  bottom: -60px;
  right: 80px;
  width: 220px;
  height: 220px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.04);
}

.hero-icon-wrap {
  width: 60px;
  height: 60px;
  border-radius: var(--radius-lg);
  background: rgba(255, 255, 255, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.hero-icon {
  font-size: 28px;
  color: #ffffff;
}

.hero-text h1 {
  margin: 0 0 6px;
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: #ffffff;
  letter-spacing: 0.02em;
}

.hero-text p {
  margin: 0;
  font-size: var(--font-size-sm);
  color: rgba(255, 255, 255, 0.78);
  line-height: var(--line-height-snug);
}

/* ════════════════════════════════════════
   主卡片
════════════════════════════════════════ */
.post-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-lighter);
  border-radius: var(--radius-xl);
  padding: var(--spacing-2xl);
  box-shadow: var(--shadow-base);
  transition: box-shadow var(--transition-base);
}

.post-card:hover {
  box-shadow: var(--shadow-md);
}

/* ════════════════════════════════════════
   分组标题
════════════════════════════════════════ */
.group-label {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  color: var(--primary-color);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin: var(--spacing-xl) 0 var(--spacing-base);
}

.group-label:first-child {
  margin-top: 0;
}

.group-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--primary-gradient);
  flex-shrink: 0;
}

.optional-badge {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
  background: var(--bg-tertiary);
  padding: 1px 8px;
  border-radius: var(--radius-full);
  font-weight: var(--font-weight-normal);
  letter-spacing: normal;
  text-transform: none;
}

/* ════════════════════════════════════════
   表单样式
════════════════════════════════════════ */
.post-form :deep(.el-form-item__label) {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--text-secondary);
  margin-bottom: 6px;
  padding-bottom: 0;
  line-height: 1.4;
}

.post-form :deep(.el-input__wrapper) {
  border-radius: var(--radius-base);
  transition: box-shadow var(--transition-fast);
}

.post-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--primary-lighter) inset;
}

.post-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--primary-color) inset !important;
}

.post-form :deep(.el-textarea__inner) {
  border-radius: var(--radius-base);
  font-size: var(--font-size-base);
  line-height: var(--line-height-relaxed);
  resize: vertical;
}

.title-input :deep(.el-input__inner) {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-medium);
}

/* ════════════════════════════════════════
   两列布局
════════════════════════════════════════ */
.form-row {
  display: grid;
  grid-template-columns: 1fr 240px;
  gap: var(--spacing-lg);
  align-items: start;
}

.date-col :deep(.el-date-editor) {
  border-radius: var(--radius-base);
}

/* ════════════════════════════════════════
   分类卡片选择器
════════════════════════════════════════ */
.area-cards {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-sm);
}

.area-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: var(--spacing-sm) var(--spacing-base);
  min-width: 72px;
  border: 1.5px solid var(--border-light);
  border-radius: var(--radius-base);
  cursor: pointer;
  background: var(--bg-secondary);
  transition: all var(--transition-fast);
  user-select: none;
}

.area-card:hover {
  border-color: var(--primary-lighter);
  background: var(--bg-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-sm);
}

.area-card.active {
  border-color: var(--primary-color);
  background: var(--primary-gradient-soft);
  box-shadow: 0 0 0 3px rgba(15, 118, 110, 0.12);
}

.area-emoji {
  font-size: 20px;
  line-height: 1;
}

.area-label {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  color: var(--text-secondary);
  white-space: nowrap;
}

.area-card.active .area-label {
  color: var(--primary-dark);
  font-weight: var(--font-weight-semibold);
}

/* ════════════════════════════════════════
   上传区域
════════════════════════════════════════ */
.upload-dragger {
  width: 100%;
}

.upload-dragger :deep(.el-upload) {
  width: 100%;
}

.upload-dragger :deep(.el-upload-dragger) {
  width: 100%;
  height: 140px;
  border-radius: var(--radius-lg);
  border: 2px dashed var(--border-base);
  background: var(--bg-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-base);
}

.upload-dragger :deep(.el-upload-dragger:hover) {
  border-color: var(--primary-lighter);
  background: var(--bg-hover);
}

.upload-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-sm);
}

.upload-icon {
  font-size: 36px;
  color: var(--text-disabled);
}

.upload-title {
  font-size: var(--font-size-base);
  color: var(--text-secondary);
  font-weight: var(--font-weight-medium);
}

.upload-link {
  color: var(--primary-color);
  font-weight: var(--font-weight-semibold);
}

.upload-hint {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

/* ════════════════════════════════════════
   图片预览卡片
════════════════════════════════════════ */
.img-preview-card {
  display: flex;
  gap: var(--spacing-lg);
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-light);
  align-items: center;
}

.preview-img {
  width: 160px;
  height: 90px;
  object-fit: cover;
  border-radius: var(--radius-base);
  flex-shrink: 0;
  box-shadow: var(--shadow-sm);
}

.preview-info {
  flex: 1;
}

.preview-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin-bottom: 4px;
}

.preview-sub {
  font-size: var(--font-size-sm);
  color: var(--text-disabled);
  margin-bottom: var(--spacing-base);
}

.preview-actions {
  display: flex;
  gap: var(--spacing-sm);
}

/* ════════════════════════════════════════
   底部操作栏
════════════════════════════════════════ */
.form-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: var(--spacing-2xl);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-lighter);
  gap: var(--spacing-md);
  flex-wrap: wrap;
}

.footer-tip {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

.footer-tip .el-icon {
  color: var(--warning-color);
  font-size: 14px;
}

.form-actions {
  display: flex;
  gap: var(--spacing-base);
}

.cancel-btn {
  min-width: 100px;
  height: 42px;
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-medium);
  border-radius: var(--radius-base);
  color: var(--text-tertiary);
  border-color: var(--border-base);
  transition: all var(--transition-base);
}

.cancel-btn:hover {
  color: var(--text-secondary);
  border-color: var(--border-dark);
  background: var(--bg-tertiary);
}

.publish-btn {
  min-width: 130px;
  height: 42px;
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  border-radius: var(--radius-base);
  background: var(--primary-gradient);
  border: none;
  box-shadow: var(--shadow-primary);
  letter-spacing: 0.5px;
  transition: all var(--transition-base);
}

.publish-btn:hover {
  box-shadow: var(--shadow-primary-lg);
  transform: translateY(-1px);
}

/* ════════════════════════════════════════
   响应式
════════════════════════════════════════ */
@media (max-width: 768px) {
  .page-bg {
    padding: var(--spacing-md);
  }

  .hero-banner {
    padding: var(--spacing-lg);
    gap: var(--spacing-base);
  }

  .hero-text h1 {
    font-size: var(--font-size-xl);
  }

  .post-card {
    padding: var(--spacing-lg);
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .form-footer {
    flex-direction: column;
    align-items: flex-start;
  }

  .form-actions {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>
