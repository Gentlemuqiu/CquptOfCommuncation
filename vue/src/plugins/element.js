/**
 * Element Plus 全局注册
 */
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/index.css'
import 'dayjs/locale/zh-cn'
import locale from 'element-plus/lib/locale/lang/zh-cn'

export function registerElementPlus(app) {
  app.use(ElementPlus, { locale, size: 'small' })
}
