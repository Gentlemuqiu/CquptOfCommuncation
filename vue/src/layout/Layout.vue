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
  background: var(--bg-secondary);
  background-image:
    radial-gradient(ellipse 80% 50% at 50% -20%, rgba(15, 118, 110, 0.08), transparent),
    radial-gradient(ellipse 60% 40% at 100% 100%, rgba(15, 118, 110, 0.05), transparent);
}

.admin-main {
  display: flex;
  align-items: stretch;
  gap: var(--spacing-xl);
  padding: var(--spacing-xl);
  max-width: 1600px;
  margin: 0 auto;
}

.admin-content {
  flex: 1;
  min-width: 0;
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-2xl);
  border: 1px solid var(--border-lighter);
  box-shadow: var(--shadow-sm);
}

@media (max-width: 900px) {
  .admin-main {
    flex-direction: column;
    padding: var(--spacing-md);
  }
  .admin-content {
    padding: var(--spacing-lg);
  }
}
</style>
