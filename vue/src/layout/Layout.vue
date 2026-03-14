<template>
  <div class="admin-layout">
    <Header />
    <div class="admin-main">
      <Aside />
      <main class="admin-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
import Header from "@/components/admin/Header";
import Aside from "@/components/admin/Aside";

export default {
  name: "Layout",
  components: {
    Header,
    Aside
  }
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  background: linear-gradient(180deg, var(--bg-secondary) 0%, var(--bg-tertiary) 100%);
}

.admin-main {
  display: flex;
  align-items: stretch;
  gap: var(--spacing-lg);
  padding: var(--spacing-lg);
}

.admin-content {
  flex: 1;
  min-width: 0;
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-base);
}

@media (max-width: 900px) {
  .admin-main {
    flex-direction: column;
  }
}
</style>
