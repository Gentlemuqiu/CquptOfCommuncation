<template>
  <el-card>
    <el-row :gutter="10">
      <el-col :span="4" v-for="item in tableData" :key="item.id">
        <el-card style="margin-bottom: 10px; cursor: pointer" @click="$router.push('/front/detail?id=' + item.id)">
          <div><el-image :src="item.img" style="width: 100%; height: 150px"></el-image></div>
          <div style="text-align: center; font-size: 12px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">{{ item.name }}</div>
        </el-card>
      </el-col>
    </el-row>
    <div v-if="tableData.length === 0" style="text-align: center; padding: 20px;">
      <el-empty description="暂无数据"></el-empty>
    </div>
  </el-card>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Search",
  data() {
    return {
      tableData: []
    }
  },
  created() {
    this.fetchData();
  },
  watch: {
    // 监听路由参数变化
    '$route.query.name': {
      handler: 'fetchData',
      immediate: true
    }
  },
  methods: {
    fetchData() {
      const name = this.$route.query.name || '';
      request.get("/movie/search", {
        params: {
          name: name
        }
      }).then(res => {
        this.tableData = res.data || [];
      }).catch(err => {
        console.error('搜索失败：', err);
        this.tableData = [];
        this.$message.error('搜索失败，请稍后重试');
      });
    }
  }
}
</script>