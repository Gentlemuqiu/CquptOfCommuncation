import axios from 'axios'
import { getCurrentUser } from '@/utils/auth'

const baseUrl = process.env.VUE_APP_BASEURL || '/api'

const request = axios.create({
  baseURL: baseUrl,
  timeout: 10000
})

// 请求拦截器：仅在有登录用户时注入 userid，避免未登录时传 0 导致后端返回全部数据
request.interceptors.request.use(config => {
  config.headers['Content-Type'] = 'application/json;charset=utf-8'
  const u = getCurrentUser()
  if (u && u.id != null) config.headers['userid'] = u.id
  return config
}, error => Promise.reject(error))

// 响应拦截器：统一解包数据
request.interceptors.response.use(
  response => {
    const res = response.data
    if (response.config.responseType === 'blob') return res
    return typeof res === 'string' ? (res ? JSON.parse(res) : res) : res
  },
  error => {
    console.error('request error:', error)
    return Promise.reject(error)
  }
)

export default request
