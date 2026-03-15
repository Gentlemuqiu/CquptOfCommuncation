<template>
  <div class="search-wrap">
    <div class="search-inner">
      <el-icon class="search-prefix-icon"><Search /></el-icon>
      <input
        v-model="searchText"
        class="search-input"
        placeholder="搜索就业信息..."
        @keyup.enter="handleSearch"
      />
      <button
        v-if="searchText"
        class="clear-btn"
        @click="handleClear"
        title="清空"
      >
        <el-icon><CircleClose /></el-icon>
      </button>
      <button class="search-btn" @click="handleSearch">搜索</button>
    </div>
  </div>
</template>

<script>
import { Search, CircleClose } from '@element-plus/icons-vue'

export default {
  name: 'SearchBar',
  components: { Search, CircleClose },
  data() {
    return { searchText: '' }
  },
  methods: {
    handleSearch() {
      if (!this.searchText.trim()) {
        this.$message.warning('请输入搜索内容');
        return;
      }
      this.$router.push({ path: '/front/search', query: { name: this.searchText.trim() } });
    },
    handleClear() {
      this.searchText = '';
    }
  }
}
</script>

<style scoped>
.search-wrap {
  flex: 1;
  max-width: 400px;
}

.search-inner {
  display: flex;
  align-items: center;
  height: 38px;
  background: var(--bg-secondary);
  border: 1.5px solid var(--border-light);
  border-radius: var(--radius-full);
  padding: 0 6px 0 14px;
  gap: 6px;
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
}

.search-inner:focus-within {
  border-color: var(--primary-color);
  background: #fff;
  box-shadow: 0 0 0 3px rgba(15, 118, 110, 0.1);
}

.search-prefix-icon {
  color: var(--text-disabled);
  font-size: 15px;
  flex-shrink: 0;
  transition: color var(--transition-fast);
}

.search-inner:focus-within .search-prefix-icon {
  color: var(--primary-color);
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  background: transparent;
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  font-family: var(--font-family);
}

.search-input::placeholder {
  color: var(--text-placeholder);
}

.clear-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  color: var(--text-disabled);
  font-size: 15px;
  line-height: 1;
  transition: color var(--transition-fast);
}

.clear-btn:hover {
  color: var(--text-tertiary);
}

.search-btn {
  height: 28px;
  padding: 0 var(--spacing-md);
  background: var(--primary-gradient);
  border: none;
  border-radius: var(--radius-full);
  color: #fff;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  cursor: pointer;
  white-space: nowrap;
  flex-shrink: 0;
  transition: all var(--transition-fast);
  font-family: var(--font-family);
  box-shadow: 0 2px 6px rgba(15, 118, 110, 0.28);
}

.search-btn:hover {
  box-shadow: 0 3px 10px rgba(15, 118, 110, 0.38);
  transform: translateY(-1px);
}

.search-btn:active {
  transform: translateY(0);
}

@media (max-width: 768px) {
  .search-wrap {
    max-width: 100%;
  }
}
</style>
