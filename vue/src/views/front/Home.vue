<template>
  <div class="home-container">
    <el-row gutter="20">
      <el-col :span="24">

        <!-- 轮播图 -->
        <el-carousel height="400px" indicator-position="inside" arrow="always" indicator-color="#ffffff">
          <el-carousel-item v-for="(item, index) in imgs" :key="item.img">
            <a :href="getLinkForItem(index)" target="_blank">
              <div class="carousel-img-container">
                <img :src="item.img" alt="" class="carousel-img">
              </div>
            </a>
          </el-carousel-item>
        </el-carousel>

        <!-- 分类按钮 -->
        <div class="category-buttons">
          <el-button
            v-for="item in areas" :key="item"
            :class="{'active': item === activeArea}"
            @click="loadArea(1, item)"
            class="category-btn"
          >
            {{ item }}
          </el-button>
        </div>

        <!-- 推荐列表 -->
        <div class="recommendation-list">
          <el-row :gutter="15">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in tableDataHot" :key="item.id">
              <el-card class="recommend-card" @click="$router.push('/front/detail?id=' + item.id)">
                <div class="card-image">
                  <el-image :src="item.img" fit="cover"></el-image>
                </div>
                <div class="card-title">
                  {{ item.name }}
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 根据分类加载列表 -->
        <div class="category-items">
          <el-row :gutter="15" v-if="tableDataArea.length > 0">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in tableDataArea" :key="item.id">
              <el-card class="category-card" @click="$router.push('/front/detail?id=' + item.id)">
                <div class="card-image">
                  <el-image :src="item.img" fit="cover"></el-image>
                </div>
                <div class="card-title">
                  {{ item.name }}
                </div>
              </el-card>
            </el-col>
          </el-row>

          <div v-else class="no-data">暂无数据...</div>

          <!-- 分页 -->
          <div class="pagination-container">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-sizes="[5, 10, 20]"
                :page-size="pageSize"
                layout="prev, pager, next"
                :total="total">
            </el-pagination>
          </div>
        </div>

      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Home",
  data() {
    return {
      imgs: [
        { img: require("@/assets/image/bg2.png"), link: 'https://www.cqupt.edu.cn/' },
        { img: require("@/assets/image/bg1.png"), link: 'https://software.cqupt.edu.cn/' },
        { img: require("@/assets/image/bg3.png"), link: 'https://zjc.cqupt.edu.cn/' }
      ],
      tableDataArea: [],
      tableDataHot: [],
      areas: ['运动', '音乐', '剧情', '搞笑', '开胃'],
      pageNum: 1,
      pageSize: 12,
      total: 0,
      activeArea: ''
    };
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {};
    this.loadData();
  },
  methods: {
    getLinkForItem(index) {
      return this.imgs[index].link;
    },
    loadArea(pageNum, area) {
      this.activeArea = area;
      request.get("/movie/page/area", {
        params: { pageNum, pageSize: this.pageSize, area }
      }).then(res => {
        this.tableDataArea = res.data.records;
        this.total = res.data.total;
      });
    },
    loadData() {
      this.loadArea(1, this.areas[0]); // 默认加载第一个区域
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.loadArea(1, this.activeArea);
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.loadArea(pageNum, this.activeArea);
    }
  }
};
</script>

<style scoped>
.home-container {
  padding: 20px;
  background-color: #f9f9f9;
}

.carousel-img-container {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.el-carousel .el-carousel-indicator {
  bottom: 15px;
  background-color: rgba(0, 0, 0, 0.6); /* 轮播图指示栏的背景颜色 */
  border-radius: 20px;
}

.el-carousel .el-carousel-indicator .el-carousel-indicator__item.is-active {
  background-color: #ff7e5f; /* 当前指示器的颜色 */
}

.category-buttons {
  margin-top: 20px; /* 增加轮播图和分类标签的间距 */
  margin-bottom: 20px;
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.category-btn {
  background: linear-gradient(135deg, #5f99f7, #36f0d7);
  border-radius: 30px;
  padding: 10px 20px;
  font-size: 16px;
  color: #fff;
  transition: all 0.3s ease;
}

.category-btn:hover {
  background: linear-gradient(135deg, #36f0d7, #5f99f7);
}

.active {
  background: linear-gradient(135deg, #4d9ce6, #2ac5b3);
  color: #fff;
}

.recommendation-list {
  margin-top: 30px;
}

.recommend-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.recommend-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.2);
}

.card-image {
  height: 180px;
}

.card-title {
  text-align: center;
  font-size: 14px;
  color: #333;
  padding: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.category-items {
  margin-top: 20px;
}

.no-data {
  text-align: center;
  padding: 30px;
  color: #777;
  font-size: 16px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.el-pagination {
  font-size: 14px;
}

.el-pagination .el-pager li {
  border-radius: 20px;
  padding: 6px 12px;
  transition: background-color 0.3s ease;
}

.el-pagination .el-pager li:hover {
  background-color: #f0f0f0;
}

.el-pagination .el-pager li.is-current {
  background-color: #409EFF;
  color: white;
}
</style>
