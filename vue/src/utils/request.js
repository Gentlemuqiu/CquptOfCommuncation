import axios from 'axios'

const baseUrl = process.env.VUE_APP_BASEURL || '/api'

const request = axios.create({
  baseURL: baseUrl,
  timeout: 10000
})

// 请求拦截器：统一注入 userid
request.interceptors.request.use(config => {
  config.headers['Content-Type'] = 'application/json;charset=utf-8'
  const user = sessionStorage.getItem('user')
  config.headers['userid'] = user ? JSON.parse(user).id : 0
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
