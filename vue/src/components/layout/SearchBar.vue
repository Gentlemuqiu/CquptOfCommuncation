<template>
  <div class="search-section">
    <el-input 
      v-model="searchText" 
      clearable 
      placeholder="搜索就业信息..."
      class="search-input"
      @keyup.enter="handleSearch"
      @clear="handleClear"
      prefix-icon="el-icon-search"
    />
    <el-button 
      type="primary" 
      class="search-btn"
      @click="handleSearch"
      icon="el-icon-search"
    >
      搜索
    </el-button>
  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  data() {
    return {
      searchText: ''
    }
  },
  methods: {
    handleSearch() {
      if (!this.searchText.trim()) {
        this.$message.warning('请输入搜索内容');
        return;
      }
      this.$router.push({
        path: '/front/search',
        query: {
          name: this.searchText.trim()
        }
      });
    },
    handleClear() {
      this.searchText = '';
    }
  }
}
</script>

<style scoped>
.search-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  flex: 1;
  max-width: 500px;
  margin: 0 var(--spacing-lg);
}

.search-input {
  flex: 1;
}

.search-input :deep(.el-input__inner) {
  border-radius: var(--radius-full);
  height: 40px;
  border: 1px solid var(--border-base);
  transition: all var(--transition-base);
  padding-left: 40px;
}

.search-input :deep(.el-input__inner):focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(15, 118, 110, 0.12);
}

.search-input :deep(.el-input__prefix) {
  left: 12px;
}

.search-btn {
  border-radius: var(--radius-full);
  padding: 10px var(--spacing-lg);
  font-weight: var(--font-weight-medium);
  white-space: nowrap;
}

@media (max-width: 768px) {
  .search-section {
    max-width: 100%;
    margin: 0 var(--spacing-base);
  }
  
  .search-btn span {
    display: none;
  }
  
  .search-btn {
    padding: 10px;
    min-width: 40px;
  }
}
</style>
