<template>
  <div class="detail-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="image-card" shadow="hover">
          <el-image 
            :src="info.img"
            @click="showVideo" 
            class="detail-image"
            fit="cover"
          />
          <div class="play-overlay" @click="showVideo" v-if="info.url">
            <i class="el-icon-video-play"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card class="info-card" shadow="hover">
          <div class="detail-header">
            <h1 class="detail-title">{{ info.name }}</h1>
            <div class="detail-meta">
              <span class="meta-item">
                <i class="el-icon-time"></i>
                发布时间：{{ info.date }}
              </span>
              <span class="meta-item">
                <i class="el-icon-collection-tag"></i>
                类型：<span class="tag-text">{{ info.area }}</span>
              </span>
            </div>
          </div>
          
          <div class="detail-content">
            <div class="description-section">
              <h3 class="section-title">简介</h3>
              <p class="description-text">{{ info.description }}</p>
            </div>
            
            <div class="action-buttons">
              <el-button 
                type="primary" 
                icon="el-icon-star-off"
                @click="collectMovie"
                class="action-btn"
              >
                收藏
              </el-button>
              <el-button 
                type="success" 
                icon="el-icon-thumb"
                @click="zan"
                class="action-btn"
              >
                赞 {{ info.zan || 0 }}
              </el-button>
              <el-button 
                type="info" 
                icon="el-icon-remove-outline"
                @click="cai"
                class="action-btn"
              >
                踩 {{ info.cai || 0 }}
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-dialog 
      :title="info.name || '视频播放'" 
        v-model="videoDialogVisible" 
        width="70%"
        :before-close="handleCloseVideo"
      >
        <video 
          v-if="info.url" 
          :src="info.url" 
          controls 
          autoplay
          style="width: 100%"
        >
          您的浏览器不支持视频播放
        </video>
        <span v-else>暂无视频资源</span>
      </el-dialog>
    </el-row>

    <div class="comments-section">
      <!-- 长评区域 -->
      <el-card class="comment-card" shadow="hover">
        <div class="comment-header">
          <h2 class="section-title-large">
            <i class="el-icon-edit-outline"></i>
            精彩长评
          </h2>
          <el-button 
            type="primary" 
            icon="el-icon-edit"
            @click="$router.push({path: '/front/longComment', query: {id: info.id}})"
            class="publish-btn"
          >
            发表长评
          </el-button>
        </div>
        
        <div class="long-comments-list">
          <div 
            class="long-comment-item" 
            v-for="item in longMessages" 
            :key="item.id"
          >
            <div class="comment-main">
              <h3 
                class="long-comment-title" 
                @click="showLongComment(item.content)"
              >
                {{ item.title }}
              </h3>
              <div class="comment-meta">
                <span class="comment-author">
                  <i class="el-icon-user"></i>
                  {{ item.username }}
                </span>
                <span class="comment-time">
                  <i class="el-icon-time"></i>
                  {{ item.time }}
                </span>
              </div>
            </div>
            <div class="comment-actions">
              <el-button 
                type="text" 
                icon="el-icon-user"
                @click="look(item.username)"
                class="action-link"
              >
                关注
              </el-button>
              <el-button 
                type="text" 
                icon="el-icon-warning-outline"
                @click="jubao(item)"
                class="action-link danger"
              >
                投诉
              </el-button>
              <el-button 
                type="text" 
                icon="el-icon-delete"
                @click="del(item.id)"
                v-if="item.username === user.username"
                class="action-link danger"
              >
                删除
              </el-button>
            </div>
          </div>
          <div v-if="longMessages.length === 0" class="empty-comments">
            暂无长评，快来发表第一条吧！
          </div>
        </div>
      </el-card>

      <!-- 短评区域 -->
      <el-card class="comment-card" shadow="hover">
        <h2 class="section-title-large">
          <i class="el-icon-chat-line-round"></i>
          精彩短评
        </h2>
        
        <div class="comment-input-section">
          <el-input 
            type="textarea" 
            :rows="4" 
            v-model="entity.content"
            placeholder="写下你的想法..."
            class="comment-textarea"
          />
          <div class="comment-submit">
            <el-button 
              type="primary" 
              icon="el-icon-s-promotion"
              @click="save"
              class="submit-btn"
            >
              发表短评
            </el-button>
          </div>
        </div>

        <div class="short-comments-list">
          <div 
            class="short-comment-item" 
            v-for="item in messages" 
            :key="item.id"
          >
            <div class="comment-avatar">
              <el-avatar :src="item.avatar" :size="50">
                {{ item.username ? item.username[0].toUpperCase() : 'U' }}
              </el-avatar>
            </div>
            <div class="comment-content">
              <div class="comment-header-info">
                <span class="comment-author-name">{{ item.username }}</span>
                <span class="comment-time-text">{{ item.time }}</span>
              </div>
              <div class="comment-text">{{ item.content }}</div>
              
              <!-- 回复内容 -->
              <div class="reply-content" v-if="item.parentMessage">
                <div class="reply-header">
                  <i class="el-icon-chat-line-round"></i>
                  回复 {{ item.parentMessage.username }}：
                </div>
                <div class="reply-text">{{ item.parentMessage.content }}</div>
              </div>
              
              <div class="comment-footer">
                <el-button 
                  type="text" 
                  size="small"
                  icon="el-icon-chat-line-round"
                  @click="reReply(item.id)"
                  class="reply-btn"
                >
                  回复
                </el-button>
                <el-button 
                  type="text" 
                  size="small"
                  icon="el-icon-warning-outline"
                  @click="jubao(item)"
                  class="report-btn"
                >
                  投诉
                </el-button>
                <el-button 
                  type="text" 
                  size="small"
                  icon="el-icon-delete"
                  @click="del(item.id)"
                  v-if="item.username === user.username"
                  class="delete-btn"
                >
                  删除
                </el-button>
              </div>
            </div>
          </div>
          <div v-if="messages.length === 0" class="empty-comments">
            暂无短评，快来发表第一条吧！
          </div>
        </div>
      </el-card>

      <el-dialog title="回复信息" v-model="dialogFormVisible" width="30%">
        <el-form :model="entity" label-width="80px">
          <el-form-item label="内容">
            <el-input v-model="entity.reply" autocomplete="off" type="textarea" :rows="3"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="reply">确 定</el-button>
        </template>
      </el-dialog>

      <el-dialog title="长评" v-model="vis" width="40%">
        <el-card>
          <v-md-preview :text="longComment"></v-md-preview>
        </el-card>
      </el-dialog>
    </div>
  </div>
</template>

<script>import request from "@/utils/request";

export default {
  name: "Detail",
  data() {
    return {
      videoDialogVisible: false,
      complaintText: '',
      info: {},
      dialogVisible: false,
      messages: [],
      longMessages: [],
      dialogFormVisible: false,
      entity: {},
      longComment: '',
      vis: false,
      user: {},
      zanFlag: false,
      caiFlag: false,
    }
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    this.load()

    let id = location.search.split("=")[1]
    this.loadMessage(id);
  },
  methods: {
    // 添加统一的登录检查方法
    checkLogin() {
      if (!this.user.id || !this.user.username) {
        this.$message({
          message: "请先登录",
          type: "warning"
        });
        return false;
      }
      return true;
    },

    showVideo() {
      if (this.info.url) {
        this.videoDialogVisible = true;
      } else {
        this.$message({
          message: "暂无视频资源",
          type: "warning"
        });
      }
    },
    
    handleCloseVideo(done) {
      const videoElement = document.querySelector('video');
      if (videoElement) {
        videoElement.pause();
      }
      done();
    },

    collectMovie() {
      if (!this.checkLogin()) return;
      
      request.post("/collectMovie/", { 
        name: this.info.name, 
        img: this.info.img,
        link: '/front/detail?id=' + this.info.id, 
        userid: this.user.id, 
        movieid: this.info.id 
      }).then(res => {
        if(res.code === '0') {
          this.$message({
            message: "收藏成功",
            type: "success"
          });
        } else{
          this.$message.error(res.msg)
        }
      })
    },

    load() {
      let id = location.search.split("=")[1]
      request.get("/movie/" + id).then(res => {
        if(res.code === '0') {
          this.info = res.data
        }
      })
    },

    zan() {
      if (!this.checkLogin()) return;
      
      if (this.zanFlag) {
        this.$message({
          message: "请勿重复点赞",
          type: "warning"
        });
        return
      }
      this.info.zan += 1
      this.entity = JSON.parse(JSON.stringify(this.info))
      this.zanFlag = true
      this.putInfo()
    },

    cai() {
      if (!this.checkLogin()) return;
      
      if (this.caiFlag) {
        this.$message({
          message: "请勿重复踩",
          type: "warning"
        });
        return
      }
      this.info.cai += 1
      this.entity = JSON.parse(JSON.stringify(this.info))
      this.caiFlag = true
      this.putInfo()
    },

    putInfo() {
      if (!this.checkLogin()) return;

      request.put("/movie", this.entity).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "操作成功",
            type: "success"
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
        this.entity = {}
        this.loadMessage(this.info.id);
      })
    },

    look(username) {
      if (!this.checkLogin()) return;
      
      if (username === this.user.username) {
        this.$message({
          message: "自己不能关注自己",
          type: "error"
        });
        return
      }
      request.post("/lookAuthor/", {commentUser: username, user: this.user.username}).then(res => {
        if(res.code === '0') {
          this.$message({
            message: "关注成功",
            type: "success"
          });
        }
      })
    },

    jubao(info) {
      if (!this.checkLogin()) return;
      
      request.post('/jubao', {
        commentId: info.id,
        commentUser: info.username,
        content: info.content,
        user: this.user.username
      }).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "投诉成功",
            type: "success"
          });
        } else {
          this.$message({
            message: "感谢你为社区做出贡献！",
            type: "success"
          });
        }
      })
    },

    showLongComment(content) {
      this.longComment = content
      this.vis = true
    },

    loadMessage(id) {
      request.get("/message/foreign/" + id + "/1").then(res => {
        this.messages = res.data;
      })

      request.get("/message/foreign/" + id + "/2").then(res => {
        this.longMessages = res.data;
      })
    },

    cancel() {
      this.dialogFormVisible = false;
      this.entity.reply = '';
    },

    reReply(id) {
      if (!this.checkLogin()) return;
      
      this.dialogFormVisible = true;
      this.entity.parentId = id;
    },

    reply() {
      if (!this.checkLogin()) return;
      
      this.entity.content = this.entity.reply;
      this.save();
    },

    save() {
      if (!this.checkLogin()) return;

      if (!this.entity.content) {
        this.$message({
          message: "请填写内容",
          type: "warning"
        });
        return;
      }
      
      this.entity.username = this.user.username
      this.entity.type = 1
      this.entity.foreignId = this.info.id

      request.post("/message", this.entity).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "评论成功",
            type: "success"
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
        this.entity = {}
        this.loadMessage(this.info.id);
        this.dialogFormVisible = false;
      })
    },

    del(id) {
      if (!this.checkLogin()) return;
      
      request.delete("/message/" + id).then(res => {
        this.$message({
          message: "删除成功",
          type: "success"
        });
        this.loadMessage(this.info.id)
      })
    }
  }
}
</script>

<style scoped>
.detail-container {
  padding: var(--spacing-lg) 0;
}

/* ── 图片卡片 ── */
.image-card {
  position: relative;
  overflow: hidden;
  border-radius: var(--radius-md);
}

.detail-image {
  width: 100%;
  height: 380px;
  cursor: pointer;
  transition: transform var(--transition-slow);
  border-radius: var(--radius-base);
  display: block;
}

.detail-image:hover {
  transform: scale(1.04);
}

.play-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 72px;
  height: 72px;
  background: var(--primary-gradient);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--transition-cubic);
  z-index: 10;
  box-shadow: var(--shadow-primary);
}

.play-overlay:hover {
  transform: translate(-50%, -50%) scale(1.12);
  box-shadow: var(--shadow-primary-lg);
}

.play-overlay i {
  font-size: 32px;
  color: #fff;
  margin-left: 3px;
}

/* ── 信息卡片 ── */
.info-card {
  border-radius: var(--radius-md);
  height: 100%;
}

.detail-header {
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--border-lighter);
}

.detail-title {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-md) 0;
  line-height: var(--line-height-snug);
}

.detail-meta {
  display: flex;
  gap: var(--spacing-lg);
  flex-wrap: wrap;
}

.meta-item {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  display: flex;
  align-items: center;
  gap: 5px;
}

.meta-item i {
  color: var(--primary-color);
  font-size: 14px;
}

.tag-text {
  color: var(--primary-color);
  font-weight: var(--font-weight-semibold);
  background: var(--primary-gradient-soft);
  padding: 2px var(--spacing-sm);
  border-radius: var(--radius-full);
  font-size: var(--font-size-sm);
}

.detail-content {
  padding-top: var(--spacing-lg);
}

.description-section {
  margin-bottom: var(--spacing-lg);
}

.section-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-base);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.section-title::before {
  content: '';
  display: inline-block;
  width: 3px;
  height: 16px;
  background: var(--primary-gradient);
  border-radius: var(--radius-full);
}

.description-text {
  font-size: var(--font-size-md);
  color: var(--text-secondary);
  line-height: var(--line-height-relaxed);
  text-indent: 2em;
  margin: 0;
}

.action-buttons {
  display: flex;
  gap: var(--spacing-base);
  justify-content: flex-end;
  padding-top: var(--spacing-md);
  border-top: 1px solid var(--border-lighter);
  flex-wrap: wrap;
}

.action-btn {
  padding: 10px var(--spacing-lg);
  font-weight: var(--font-weight-medium);
  border-radius: var(--radius-base);
  transition: all var(--transition-cubic);
}

.action-btn:hover {
  transform: translateY(-2px);
}

/* ── 评论区域 ── */
.comments-section {
  margin-top: var(--spacing-xl);
  margin-bottom: var(--spacing-2xl);
}

.comment-card {
  margin-bottom: var(--spacing-xl);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--border-lighter);
}

.section-title-large {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.section-title-large i {
  color: var(--primary-color);
}

.publish-btn {
  border-radius: var(--radius-base);
  font-weight: var(--font-weight-medium);
}

/* ── 长评列表 ── */
.long-comments-list {
  margin-top: var(--spacing-md);
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.long-comment-item {
  padding: var(--spacing-md) var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  border-left: 3px solid var(--primary-color);
  transition: all var(--transition-base);
}

.long-comment-item:hover {
  background: var(--bg-hover);
  box-shadow: var(--shadow-sm);
  transform: translateX(2px);
}

.comment-main {
  margin-bottom: var(--spacing-sm);
}

.long-comment-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-sm) 0;
  cursor: pointer;
  transition: color var(--transition-base);
}

.long-comment-title:hover {
  color: var(--primary-color);
}

.comment-meta {
  display: flex;
  gap: var(--spacing-md);
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

.comment-author,
.comment-time {
  display: flex;
  align-items: center;
  gap: 4px;
}

.comment-actions {
  display: flex;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
  margin-top: var(--spacing-sm);
}

.action-link {
  padding: 3px var(--spacing-sm);
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  transition: color var(--transition-base);
}

.action-link.danger,
.action-link.danger:hover {
  color: var(--danger-color);
}

/* ── 短评输入框 ── */
.comment-input-section {
  margin: var(--spacing-lg) 0;
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  border: 1px solid var(--border-lighter);
}

.comment-textarea :deep(.el-textarea__inner) {
  border-radius: var(--radius-base) !important;
  border: 1.5px solid var(--border-light) !important;
  background: var(--bg-primary) !important;
  resize: none !important;
  font-family: var(--font-family) !important;
}

.comment-textarea :deep(.el-textarea__inner:focus) {
  border-color: var(--primary-color) !important;
  box-shadow: 0 0 0 3px rgba(15, 118, 110, 0.1) !important;
}

.comment-submit {
  text-align: right;
  margin-top: var(--spacing-base);
}

.submit-btn {
  border-radius: var(--radius-base);
  padding: 10px var(--spacing-xl);
  font-weight: var(--font-weight-semibold);
}

/* ── 短评列表 ── */
.short-comments-list {
  margin-top: var(--spacing-lg);
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.short-comment-item {
  display: flex;
  gap: var(--spacing-md);
  padding: var(--spacing-md) var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  border: 1px solid var(--border-lighter);
  transition: all var(--transition-base);
}

.short-comment-item:hover {
  background: var(--bg-hover);
  border-color: var(--border-primary);
  box-shadow: var(--shadow-xs);
}

.comment-avatar { flex-shrink: 0; }

.comment-content { flex: 1; min-width: 0; }

.comment-header-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  margin-bottom: var(--spacing-sm);
}

.comment-author-name {
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  font-size: var(--font-size-md);
}

.comment-time-text {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

.comment-text {
  color: var(--text-secondary);
  line-height: var(--line-height-relaxed);
  margin-bottom: var(--spacing-base);
  font-size: var(--font-size-base);
  word-break: break-word;
}

.reply-content {
  background: var(--bg-primary);
  border-left: 3px solid var(--primary-lighter);
  padding: var(--spacing-base);
  margin: var(--spacing-base) 0;
  border-radius: 0 var(--radius-sm) var(--radius-sm) 0;
}

.reply-header {
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
  margin-bottom: 5px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.reply-text {
  color: var(--text-secondary);
  font-size: var(--font-size-sm);
  line-height: var(--line-height-snug);
}

.comment-footer {
  display: flex;
  gap: var(--spacing-sm);
  margin-top: var(--spacing-sm);
}

.reply-btn { font-size: var(--font-size-sm); color: var(--text-tertiary); }
.reply-btn:hover { color: var(--primary-color); }

.report-btn,
.delete-btn {
  font-size: var(--font-size-sm);
  color: var(--text-disabled);
}

.report-btn:hover,
.delete-btn:hover { color: var(--danger-color); }

/* ── 空状态 ── */
.empty-comments {
  text-align: center;
  padding: var(--spacing-3xl) var(--spacing-lg);
  color: var(--text-disabled);
  font-size: var(--font-size-md);
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  border: 1px dashed var(--border-light);
}
</style>
