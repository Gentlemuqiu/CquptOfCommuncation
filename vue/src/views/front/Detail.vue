<template>
  <div class="detail-page">

    <!-- ════════════ 帖子主信息区 ════════════ -->
    <div class="post-main-card">
      <div class="post-info">
        <!-- 分类徽章 -->
        <div class="info-badges">
          <span class="area-badge">
            <el-icon><Collection /></el-icon>
            {{ info.area }}
          </span>
          <span class="date-badge">
            <el-icon><Calendar /></el-icon>
            {{ info.date }}
          </span>
        </div>

        <!-- 标题 -->
        <h1 class="post-title">{{ info.name }}</h1>

        <!-- 简介 -->
        <div class="desc-wrap">
          <div class="desc-label">
            <span class="label-line"></span>
            简介
          </div>
          <p class="desc-text">{{ info.description }}</p>
        </div>

        <!-- 操作按钮 -->
        <div class="action-bar">
          <button
            class="action-btn collect-btn"
            :class="{ active: collectFlag }"
            @click="collectFlag ? cancelCollect() : collectMovie()"
            :title="collectFlag ? '取消收藏' : '收藏'"
          >
            <el-icon><Star /></el-icon>
            <span>{{ collectFlag ? '取消收藏' : '收藏' }}</span>
          </button>

          <button
            class="action-btn zan-btn"
            @click="zan"
          >
            <el-icon><Sunny /></el-icon>
            <span>赞 {{ info.zan || 0 }}</span>
          </button>

          <button
            class="action-btn cai-btn"
            @click="cai"
          >
            <el-icon><SemiSelect /></el-icon>
            <span>踩 {{ info.cai || 0 }}</span>
          </button>

        </div>
      </div>
    </div>

    <!-- ════════════ 发布者信息 ════════════ -->
    <div class="publisher-card" v-if="publisherUsername">
      <div class="section-hd">
        <div class="section-hd-left">
          <div class="hd-icon-wrap green">
            <el-icon><User /></el-icon>
          </div>
          <div>
            <h2 class="section-hd-title">发布者</h2>
            <p class="section-hd-sub">本条信息的发布者</p>
          </div>
        </div>
      </div>
      <div class="publisher-body">
        <el-avatar :size="52" class="publisher-avatar" :src="publisherAvatar">
          {{ publisherInitial }}
        </el-avatar>
        <div class="publisher-info">
          <h3 class="publisher-name">{{ publisherDisplayName }}</h3>
          <p class="publisher-username">@{{ publisherUsername }}</p>
        </div>
        <div class="publisher-actions">
          <el-button type="primary" size="small" class="view-works-btn" @click="viewAuthorWorks">
            <el-icon><Document /></el-icon>
            TA的作品
          </el-button>
          <template v-if="user.id && user.username !== publisherUsername">
            <el-button
              v-if="!followingPublisher"
              type="primary"
              size="small"
              plain
              class="follow-btn"
              @click="followPublisher"
            >
              <el-icon><Plus /></el-icon>
              关注
            </el-button>
            <el-button
              v-else
              size="small"
              class="following-btn"
              @click="unfollowPublisher"
            >
              <el-icon><Check /></el-icon>
              已关注
            </el-button>
          </template>
        </div>
      </div>
    </div>

    <!-- ════════════ 评论区 ════════════ -->
    <div class="comments-wrap">

      <!-- ─── 短评 ─── -->
      <div class="comment-section">
        <div class="section-hd">
          <div class="section-hd-left">
            <div class="hd-icon-wrap blue">
              <el-icon><ChatDotRound /></el-icon>
            </div>
            <div>
              <h2 class="section-hd-title">精彩短评</h2>
              <p class="section-hd-sub">{{ messages.length }} 条评论</p>
            </div>
          </div>
        </div>

        <!-- 短评输入 -->
        <div class="input-area">
          <el-avatar :size="40" class="input-avatar" :src="user.avatar">
            {{ user.username ? user.username[0].toUpperCase() : 'U' }}
          </el-avatar>
          <div class="input-box">
            <el-input
              type="textarea"
              :rows="3"
              v-model="entity.content"
              placeholder="写下你的想法，和大家一起交流..."
              class="comment-input"
              resize="none"
            />
            <div class="input-footer">
              <span class="input-hint">{{ (entity.content || '').length }} / 500</span>
              <el-button type="primary" class="send-btn" @click="save">
                <el-icon><Promotion /></el-icon>
                发表
              </el-button>
            </div>
          </div>
        </div>

        <!-- 短评列表 -->
        <div class="short-list" v-if="messages.length > 0">
          <div
            class="short-item"
            v-for="item in messages"
            :key="item.id"
          >
            <el-avatar :size="42" class="short-avatar" :src="item.avatar">
              {{ item.username ? item.username[0].toUpperCase() : 'U' }}
            </el-avatar>

            <div class="short-body">
              <div class="short-hd">
                <span class="short-name">{{ item.username }}</span>
                <span class="short-time">{{ item.time }}</span>
              </div>
              <p class="short-text">{{ item.content }}</p>

              <!-- 回复引用 -->
              <div class="reply-quote" v-if="item.parentMessage">
                <div class="quote-label">
                  <el-icon><ChatLineRound /></el-icon>
                  回复 {{ item.parentMessage.username }}：
                </div>
                <p class="quote-text">{{ item.parentMessage.content }}</p>
              </div>

              <div class="short-ft">
                <button class="text-action reply" @click="reReply(item.id)">
                  <el-icon><ChatLineRound /></el-icon> 回复
                </button>
                <button class="text-action warn" @click="jubao(item)">
                  <el-icon><Warning /></el-icon> 投诉
                </button>
                <button
                  class="text-action danger"
                  @click="del(item.id)"
                  v-if="item.username === user.username"
                >
                  <el-icon><Delete /></el-icon> 删除
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="empty-block">
          <el-icon class="empty-ico"><ChatDotRound /></el-icon>
          <p>暂无评论，快来发表第一条短评吧</p>
        </div>
      </div>
    </div>

    <!-- 回复弹窗 -->
    <el-dialog title="回复评论" v-model="dialogFormVisible" width="420px" class="reply-dialog">
      <el-input
        v-model="entity.reply"
        type="textarea"
        :rows="4"
        placeholder="写下你的回复..."
        class="reply-input"
      />
      <template #footer>
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="reply">确认回复</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import { getCurrentUser } from '@/utils/auth'
import { getMovieById, updateMovie } from '@/api/movie'
import { getUserById } from '@/api/user'
import { getCollectPage, addCollect, removeCollect } from '@/api/collectMovie'
import { getFollowListByUser, followAuthor, unfollowAuthor } from '@/api/lookAuthor'
import { getMessageByForeign, addMessage, deleteMessage } from '@/api/message'
import { submitJubao } from '@/api/jubao'
import {
  Collection, Calendar, Star, Sunny,
  SemiSelect, EditPen, Warning, Delete, ChatDotRound, ChatLineRound,
  Promotion, User, Document, Plus, Check
} from '@element-plus/icons-vue'

export default {
  name: 'Detail',
  components: {
    Collection, Calendar, Star, Sunny,
    SemiSelect, EditPen, Warning, Delete, ChatDotRound, ChatLineRound,
    Promotion, User, Document, Plus, Check
  },
  data() {
    return {
      info: {},
      messages: [],
      dialogFormVisible: false,
      entity: {},
      user: {},
      collectFlag: false,
      publisherUsername: '',
      publisherAvatar: '',
      publisherDisplayName: '',
      followingPublisher: false,
    }
  },
  computed: {
    publisherInitial() {
      return this.publisherUsername ? this.publisherUsername[0].toUpperCase() : 'U'
    }
  },
  created() {
    this.user = getCurrentUser()
    this.load()
    const id = location.search.split('=')[1]
    if (id) this.loadMessage(id)
  },
  methods: {
    refreshUser() {
      this.user = getCurrentUser()
      return this.user
    },
    checkLogin() {
      this.refreshUser()
      if (!this.user.id || !this.user.username) {
        this.$message({ message: '请先登录', type: 'warning' })
        return false
      }
      return true
    },

    collectMovie() {
      if (!this.checkLogin()) return
      addCollect({
        name: this.info.name,
        img: this.info.img,
        link: '/front/detail?id=' + this.info.id,
        userid: this.user.id,
        movieid: this.info.id
      }).then(res => {
        if (res.code === '0') {
          this.$message({ message: '收藏成功', type: 'success' })
          this.collectFlag = true
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    cancelCollect() {
      if (!this.checkLogin()) return
      removeCollect(this.user.id, this.info.id).then(res => {
        if (res.code === '0') {
          this.$message({ message: '已取消收藏', type: 'success' })
          this.collectFlag = false
        } else {
          this.$message.error(res.msg || '取消失败')
        }
      }).catch(() => this.$message.error('网络异常'))
    },

    load() {
      const id = location.search.split('=')[1]
      if (!id) return
      getMovieById(id).then(res => {
        if (res.code === '0') {
          this.info = res.data
          this.checkCollectStatus()
          this.loadPublisherInfo()
        }
      })
    },

    loadPublisherInfo() {
      const info = this.info
      const name = info.postUsername || info.postUserName
      if (name) {
        this.publisherUsername = name
        this.publisherDisplayName = info.postNickName || info.postNickname || name
        this.publisherAvatar = info.postAvatar || ''
        this.checkFollowStatus()
        return
      }
      const uid = info.postUserId != null ? info.postUserId : info.postUserid
      if (!uid) {
        this.publisherUsername = ''
        return
      }
      getUserById(uid).then(res => {
        if (res.code === '0' && res.data) {
          const u = res.data
          this.publisherUsername = u.username || ''
          this.publisherDisplayName = u.nickName || u.nickname || u.username || this.publisherUsername
          this.publisherAvatar = u.avatar || ''
          this.checkFollowStatus()
        }
      }).catch(() => {
        this.publisherUsername = ''
      })
    },

    checkFollowStatus() {
      this.refreshUser()
      if (!this.publisherUsername || !this.user.id || !this.user.username) {
        this.followingPublisher = false
        return
      }
      getFollowListByUser(this.user.username).then(res => {
        const list = res.data || []
        this.followingPublisher = list.some(row => row.commentUser === this.publisherUsername)
      }).catch(() => { this.followingPublisher = false })
    },

    viewAuthorWorks() {
      const authorId = this.info.postUserId != null ? this.info.postUserId : this.info.postUserid
      const query = { username: this.publisherUsername }
      if (authorId != null) query.userId = authorId
      this.$router.push({ path: '/front/authorWorks', query })
    },

    followPublisher() {
      if (!this.checkLogin() || !this.publisherUsername) return
      followAuthor({ user: this.user.username, commentUser: this.publisherUsername }).then(res => {
        if (res.code === '0') {
          this.$message.success('关注成功')
          this.followingPublisher = true
        } else {
          this.$message.error(res.msg || '关注失败')
        }
      }).catch(() => this.$message.error('网络异常'))
    },

    unfollowPublisher() {
      if (!this.checkLogin() || !this.publisherUsername) return
      unfollowAuthor(this.user.username, this.publisherUsername).then(res => {
        if (res.code === '0' || res.code === 0) {
          this.$message.success('已取消关注')
          this.followingPublisher = false
        } else {
          this.$message.error(res.msg || '取消失败')
        }
      }).catch(() => this.$message.error('网络异常'))
    },

    checkCollectStatus() {
      this.refreshUser()
      if (!this.user.id || !this.info.id) return
      getCollectPage({ userid: this.user.id, userId: this.user.id, pageNum: 1, pageSize: 500 }).then(res => {
        if (res.code === '0' && res.data.records) {
          const mid = Number(this.info.id)
          this.collectFlag = res.data.records.some(r =>
            Number(r.movieId != null ? r.movieId : r.movieid) === mid
          )
        }
      })
    },

    zan() {
      if (!this.checkLogin()) return
      const prevZan = this.info.zan || 0
      this.info.zan = prevZan + 1
      this.entity = JSON.parse(JSON.stringify(this.info))
      this.putInfo('zan', prevZan)
    },

    cai() {
      if (!this.checkLogin()) return
      const prevCai = this.info.cai || 0
      this.info.cai = prevCai + 1
      this.entity = JSON.parse(JSON.stringify(this.info))
      this.putInfo('cai', prevCai)
    },

    putInfo(revertKey, revertValue) {
      if (!this.checkLogin()) return
      updateMovie(this.entity).then(res => {
        if (res.code === '0') {
          this.$message({ message: '操作成功', type: 'success' })
        } else {
          this.$message({ message: res.msg || '操作失败', type: 'error' })
          if (revertKey === 'zan') this.info.zan = revertValue
          else if (revertKey === 'cai') this.info.cai = revertValue
        }
        this.entity = {}
        this.loadMessage(this.info.id)
      }).catch(() => {
        this.$message({ message: '网络异常', type: 'error' })
        if (revertKey === 'zan') this.info.zan = revertValue
        else if (revertKey === 'cai') this.info.cai = revertValue
        this.entity = {}
      })
    },

    jubao(info) {
      if (!this.checkLogin()) return
      submitJubao({
        commentId: info.id,
        commentUser: info.username,
        content: info.content,
        user: this.user.username
      }).then(res => {
        this.$message({ message: res.code === '0' ? '投诉成功' : '感谢你为社区做出贡献！', type: 'success' })
      })
    },

    loadMessage(id) {
      getMessageByForeign(id, 1).then(res => { this.messages = res.data || [] })
    },

    cancel() {
      this.dialogFormVisible = false
      this.entity.reply = ''
    },

    reReply(id) {
      if (!this.checkLogin()) return
      this.dialogFormVisible = true
      this.entity.parentId = id
    },

    reply() {
      if (!this.checkLogin()) return
      this.entity.content = this.entity.reply
      this.save()
    },

    save() {
      if (!this.checkLogin()) return
      if (!this.entity.content) {
        this.$message({ message: '请填写内容', type: 'warning' })
        return
      }
      this.entity.username = this.user.username
      this.entity.type = 1
      this.entity.foreignId = this.info.id
      addMessage(this.entity).then(res => {
        if (res.code === '0') {
          this.$message({ message: '评论成功', type: 'success' })
        } else {
          this.$message({ message: res.msg, type: 'error' })
        }
        this.entity = {}
        this.loadMessage(this.info.id)
        this.dialogFormVisible = false
      })
    },

    del(id) {
      if (!this.checkLogin()) return
      deleteMessage(id).then(() => {
        this.$message({ message: '删除成功', type: 'success' })
        this.loadMessage(this.info.id)
      })
    }
  }
}
</script>

<style scoped>
/* ════════════════════════════════════════
   页面容器
════════════════════════════════════════ */
.detail-page {
  padding-bottom: var(--spacing-2xl);
}

/* ════════════════════════════════════════
   帖子主信息卡片
════════════════════════════════════════ */
.post-main-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-lighter);
  margin-bottom: var(--spacing-xl);
}

.post-info {
  padding: var(--spacing-2xl);
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.info-badges {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
}

.area-badge {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 4px 12px;
  background: var(--primary-gradient-soft);
  border: 1px solid var(--border-primary);
  border-radius: var(--radius-full);
  color: var(--primary-dark);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
}

.date-badge {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 4px 12px;
  background: var(--bg-tertiary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-full);
  color: var(--text-tertiary);
  font-size: var(--font-size-sm);
}

.post-title {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-extrabold);
  color: var(--text-primary);
  margin: 0;
  line-height: var(--line-height-snug);
  letter-spacing: -0.5px;
}

/* 简介 */
.desc-wrap {
  flex: 1;
}

.desc-label {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--text-tertiary);
  margin-bottom: var(--spacing-sm);
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.label-line {
  width: 3px;
  height: 14px;
  border-radius: var(--radius-full);
  background: var(--primary-gradient);
  flex-shrink: 0;
}

.desc-text {
  font-size: var(--font-size-md);
  color: var(--text-secondary);
  line-height: var(--line-height-relaxed);
  margin: 0;
}

/* 操作按钮行 */
.action-bar {
  display: flex;
  gap: var(--spacing-sm);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-lighter);
  flex-wrap: wrap;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 9px 18px;
  border-radius: var(--radius-full);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  cursor: pointer;
  border: 1.5px solid var(--border-base);
  background: var(--bg-secondary);
  color: var(--text-secondary);
  transition: all var(--transition-cubic);
  font-family: var(--font-family);
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-sm);
}

/* 收藏 */
.collect-btn:hover { border-color: var(--warning-color); color: var(--warning-color); background: rgba(245,158,11,0.06); }
.collect-btn.active { border-color: var(--warning-color); color: var(--warning-color); background: rgba(245,158,11,0.1); }

/* 赞 */
.zan-btn:hover { border-color: var(--success-color); color: var(--success-color); background: var(--success-light); }
.zan-btn.active { border-color: var(--success-color); color: var(--success-color); background: var(--success-light); }

/* 踩 */
.cai-btn:hover { border-color: var(--text-disabled); color: var(--text-disabled); }
.cai-btn.active { border-color: var(--text-disabled); color: var(--text-disabled); background: var(--bg-tertiary); }

/* ════════════════════════════════════════
   发布者卡片
════════════════════════════════════════ */
.publisher-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  border: 1px solid var(--border-lighter);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-2xl);
  margin-bottom: var(--spacing-xl);
}

.publisher-body {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  flex-wrap: wrap;
}

.publisher-avatar {
  flex-shrink: 0;
  background: var(--primary-gradient-soft);
  color: var(--primary-color);
  font-weight: var(--font-weight-bold);
}

.publisher-info {
  flex: 1;
  min-width: 0;
}

.publisher-name {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin: 0 0 2px;
}

.publisher-username {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  margin: 0;
}

.publisher-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  flex-shrink: 0;
}

.view-works-btn,
.follow-btn,
.following-btn {
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.following-btn {
  color: var(--success-color);
  border-color: var(--success-color);
}

.following-btn:hover {
  background: var(--success-light);
  border-color: var(--success-color);
  color: var(--success-color);
}

/* ════════════════════════════════════════
   评论区整体
════════════════════════════════════════ */
.comments-wrap {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xl);
}

.comment-section {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  border: 1px solid var(--border-lighter);
  box-shadow: var(--shadow-base);
  padding: var(--spacing-2xl);
  transition: box-shadow var(--transition-base);
}

.comment-section:hover {
  box-shadow: var(--shadow-md);
}

/* 区块标题 */
.section-hd {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-lg);
  border-bottom: 1px solid var(--border-lighter);
}

.section-hd-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
}

.hd-icon-wrap {
  width: 44px; height: 44px;
  border-radius: var(--radius-base);
  display: flex; align-items: center; justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.hd-icon-wrap.green { background: var(--primary-gradient-soft); color: var(--primary-color); }
.hd-icon-wrap.blue  { background: rgba(14,165,233,0.1); color: #0ea5e9; }

.section-hd-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0 0 2px;
  line-height: 1;
}

.section-hd-sub {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
  margin: 0;
}

/* 文字操作按钮 */
.text-action {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  padding: 4px 10px;
  border: none;
  background: none;
  border-radius: var(--radius-full);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  cursor: pointer;
  transition: all var(--transition-fast);
  font-family: var(--font-family);
  color: var(--text-disabled);
}

.text-action:hover { background: var(--bg-tertiary); }
.text-action.follow:hover { color: var(--primary-color); background: var(--primary-gradient-soft); }
.text-action.reply:hover  { color: var(--info-color);   background: var(--info-light); }
.text-action.warn:hover   { color: var(--warning-color); background: var(--warning-light); }
.text-action.danger:hover { color: var(--danger-color);  background: var(--danger-light); }

/* ════════════════════════════════════════
   短评输入区
════════════════════════════════════════ */
.input-area {
  display: flex;
  gap: var(--spacing-base);
  margin-bottom: var(--spacing-xl);
  align-items: flex-start;
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-lighter);
}

.input-avatar {
  background: var(--primary-gradient) !important;
  color: #fff !important;
  font-weight: var(--font-weight-bold) !important;
  flex-shrink: 0;
  margin-top: 2px;
}

.input-box { flex: 1; min-width: 0; }

.comment-input :deep(.el-textarea__inner) {
  border-radius: var(--radius-base);
  border: 1.5px solid var(--border-light);
  background: var(--bg-primary);
  font-family: var(--font-family);
  font-size: var(--font-size-base);
  line-height: var(--line-height-relaxed);
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
  resize: none;
}

.comment-input :deep(.el-textarea__inner:focus) {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(15,118,110,0.1);
}

.input-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: var(--spacing-sm);
}

.input-hint {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

.send-btn {
  background: var(--primary-gradient);
  border: none;
  border-radius: var(--radius-full);
  padding: 7px 20px;
  font-weight: var(--font-weight-semibold);
  box-shadow: 0 2px 8px rgba(15,118,110,0.25);
  gap: 5px;
  transition: all var(--transition-base);
}

.send-btn:hover {
  box-shadow: var(--shadow-primary);
  transform: translateY(-1px);
}

/* ════════════════════════════════════════
   短评列表
════════════════════════════════════════ */
.short-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.short-item {
  display: flex;
  gap: var(--spacing-md);
  padding: var(--spacing-md) var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-lighter);
  transition: all var(--transition-base);
}

.short-item:hover {
  background: var(--bg-hover);
  border-color: var(--border-primary);
  box-shadow: var(--shadow-xs);
}

.short-avatar {
  background: var(--primary-gradient-soft) !important;
  color: var(--primary-dark) !important;
  font-weight: var(--font-weight-bold) !important;
  flex-shrink: 0;
}

.short-body { flex: 1; min-width: 0; }

.short-hd {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  margin-bottom: 6px;
}

.short-name {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
}

.short-time {
  font-size: var(--font-size-xs);
  color: var(--text-disabled);
}

.short-text {
  color: var(--text-secondary);
  line-height: var(--line-height-relaxed);
  margin: 0 0 var(--spacing-sm);
  font-size: var(--font-size-base);
  word-break: break-word;
}

/* 回复引用 */
.reply-quote {
  background: var(--bg-primary);
  border-left: 3px solid var(--border-base);
  border-radius: 0 var(--radius-sm) var(--radius-sm) 0;
  padding: var(--spacing-sm) var(--spacing-base);
  margin-bottom: var(--spacing-sm);
}

.quote-label {
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
  display: flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 4px;
}

.quote-text {
  color: var(--text-tertiary);
  font-size: var(--font-size-sm);
  margin: 0;
  line-height: var(--line-height-snug);
}

.short-ft {
  display: flex;
  gap: var(--spacing-xs);
  margin-top: var(--spacing-xs);
}

/* ════════════════════════════════════════
   空状态
════════════════════════════════════════ */
.empty-block {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-3xl);
  text-align: center;
  color: var(--text-disabled);
}

.empty-ico {
  font-size: 40px;
  margin-bottom: var(--spacing-base);
  opacity: 0.4;
}

.empty-block p {
  font-size: var(--font-size-md);
  margin: 0;
}

/* ════════════════════════════════════════
   弹窗
════════════════════════════════════════ */
.long-preview {
  max-height: 60vh;
  overflow-y: auto;
}

.reply-input :deep(.el-textarea__inner) {
  border-radius: var(--radius-base);
  font-family: var(--font-family);
}

/* ════════════════════════════════════════
   响应式
════════════════════════════════════════ */
@media (max-width: 860px) {
  .post-info { padding: var(--spacing-lg); }

  .post-title { font-size: var(--font-size-2xl); }

  .comment-section { padding: var(--spacing-lg); }
}

@media (max-width: 480px) {
  .action-bar {
    gap: var(--spacing-xs);
  }

  .action-btn {
    padding: 7px 12px;
    font-size: var(--font-size-xs);
  }
}
</style>
