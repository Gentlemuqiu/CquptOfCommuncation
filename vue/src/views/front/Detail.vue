<template>
  <div style="padding: 5px 0">
    <el-row :gutter="5">
      <el-col :span="6">
        <el-card>
          <el-image :src="info.img"
           @click="showVideo" 
            style="cursor: pointer"
          />
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card>
          <div style="padding: 10px 0; font-size: 24px">{{ info.name }}</div>
          <div style="color: #888; font-size: 14px">发布时间：{{ info.date }}</div>
          <div style="padding: 10px 0">
            <div style="padding: 5px 0">类型：<span style="color: #888">{{ info.area }}</span></div>
            <!-- 禁止交互，仅用于显示 -->
            <div style="padding: 5px 0">简介：</div>
            <div style="padding: 5px 0; color: #666; text-indent: 2em">{{ info.description }}</div>
            <div style="text-align: right; padding: 5px 0">
              <el-button style="background-color: dodgerblue; color: white" @click="collectMovie">收藏</el-button>
              <el-button style="background-color: #008cff; color: white" @click="zan">赞 {{ info.zan }}</el-button>
              <el-button type="info" @click="cai">踩 {{ info.cai }}</el-button>
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

    <div style="margin-top: 10px; margin-bottom: 80px">
      <el-card>
        <div style="padding: 20px;">
          <el-button type="primary" style="margin-bottom: 20px" @click="$router.push({path: '/front/longComment', query: {id: info.id}})">发表长评</el-button>
          <div style="padding: 10px 0; font-size: 20px; border-bottom: 1px solid #ccc; margin-bottom: 20px">精彩长评列表</div>
          <div style="border-bottom: 1px dashed #ccc; padding: 10px 20px; color: #666" v-for="item in longMessages">
            <span class="longComment" style="font-size: 20px; cursor: pointer" @click="showLongComment(item.content)">{{ item.title }}</span>
            <span style="font-size: 12px; margin-left: 20px; ">{{ item.username }}</span>
            <span style="font-size: 12px; margin-left: 20px;">{{ item.time }}</span>
            <el-button type="major" style="background-color: orangered; color: white; margin-left: 10px" @click="jubao(item)">投诉</el-button>
            <el-button type="text" style="float: right; margin-left: 10px" @click="del(item.id)" v-if="item.username === user.username">删除</el-button>
            <el-button type="text" style="float: right;margin-left: 10px " @click="collect(item.content)">收藏</el-button>
            <el-button type="text" style="float: right" @click="look(item.username)">关注他</el-button>
          </div>
        </div>
        <div style="padding: 20px; color: #888">
          <div>
            <div style="padding: 10px 0; font-size: 20px; border-bottom: 1px solid #ccc; margin-bottom: 20px; color: black">精彩短评列表</div>
            <el-input type="textarea" :rows="3" v-model="entity.content"></el-input>
            <div style="text-align: right; padding: 10px"><el-button type="primary" @click="save">发表短评</el-button></div>
          </div>
        </div>

        <div style="display: flex; padding: 20px" v-for="item in messages">
          <div style="text-align: center; flex: 1">
            <el-image :src="item.avatar" style="width: 60px; height: 60px; border-radius: 50%"></el-image>
          </div>
          <div style="padding: 0 10px; flex: 5">
            <div><b style="font-size: 14px">{{ item.username }}</b></div>
            <div style="padding: 10px 0; color: #888">
              {{ item.content }}
              <el-button type="text" size="mini" @click="del(item.id)" v-if="item.username === user.username">删除</el-button>
              <el-button type="major" style="background-color: orangered; color: white; margin-left: 10px" @click="jubao(item)">投诉</el-button>
            </div>
            <div style="background-color: #eee; padding: 10px" v-if="item.parentMessage">{{ item.parentMessage.username }}：{{ item.parentMessage.content }}</div>
            <div style="color: #888; font-size: 12px">
              <span>{{ item.time  }}</span>
              <el-button type="text" style="margin-left: 20px" @click="reReply(item.id)">回复</el-button>
            </div>
          </div>
        </div>

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
      </el-card>




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

    collect(content) {
      if (!this.checkLogin()) return;
      
      request.post("/collect/", {
        userId: this.user.id, 
        name: this.info.name, 
        content: content
      }).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "收藏成功",
            type: "success"
          });
          }else {
          this.$message({
            message: "您已收藏过了哟！",
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

<style>
.longComment:hover {
  color: dodgerblue;
}


/* Add new styles */
.el-image {
  transition: opacity 0.3s;
}

.el-image:hover {
  opacity: 0.8;
}
</style>
