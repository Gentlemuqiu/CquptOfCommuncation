/**
 * v-md-editor  markdown 编辑器/预览 全局注册
 */
import VMdEditor from '@kangc/v-md-editor'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'

import VMdPreview from '@kangc/v-md-editor/lib/preview'
import '@kangc/v-md-editor/lib/style/preview.css'

VMdEditor.use(githubTheme)
VMdPreview.use(githubTheme)

export function registerVMdEditor(app) {
  app.use(VMdEditor).use(VMdPreview)
}
