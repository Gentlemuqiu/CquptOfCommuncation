import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import { registerElementPlus } from './plugins/element'
import { registerVMdEditor } from './plugins/vmd-editor'

import '@/styles/index.css'

const app = createApp(App)
registerVMdEditor(app)
registerElementPlus(app)
app.use(store).use(router).mount('#app')
