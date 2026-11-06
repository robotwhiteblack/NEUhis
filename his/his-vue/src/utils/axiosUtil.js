import axios from 'axios'
import { Loading, Message, MessageBox } from 'element-ui'
import router from '@/router'
axios.defaults.baseURL = 'http://localhost:8088'

// AI助手专用axios实例
export const aiAxios = axios.create({
  baseURL: 'http://localhost:8000', // RAG后端地址
  timeout: 20000
})

// 拦截请求，添加请求头
axios.interceptors.request.use(config => {
  if (config.url !== '/login') {
    const token = window.sessionStorage.getItem('token')
    if (!token) {
      MessageBox.alert('登录信息已过期，请重新登录', '登录过期', {
        confirmButtonText: '跳转到登录页',
        callback: action => { router.push('/') }
      })
      return
    }
    config.headers.Authorization = token
  }
  // 在最后必须 return config
  return config
})

axios.interceptors.response.use(config => config,
  err => {
    // console.dir(err)
    if (err.response.status === 404) {
      Message.error('无法请求到' + err.response.config.baseURL + err.response.data.path)
    }
  }
)

// aiAxios请求拦截器，自动加conversation_id
aiAxios.interceptors.request.use(config => {
  // 只对/api/路径下的AI助手接口加conversation_id
  if (config.url && config.url.startsWith('/api/')) {
    // 检查URL是否已带conversation_id
    const urlHasCid = /[?&]conversation_id=/.test(config.url)
    if (!urlHasCid) {
      const cid = sessionStorage.getItem('medicalAssistantConversationId')
      if (cid) {
        // 拼接到URL
        if (config.url.indexOf('?') === -1) {
          config.url += `?conversation_id=${cid}`
        } else {
          config.url += `&conversation_id=${cid}`
        }
      }
    }
  }
  return config
})

aiAxios.interceptors.response.use(
  response => response,
  error => {
    console.error('AI助手接口异常:', error)
    return Promise.reject(error)
  }
)

const loadMsgGet = {
  lock: true,
  text: '数据加载中',
  spinner: 'el-icon-loading',
  background: 'rgba(255, 255, 255, 0.7)'
}

const loadMsgPut = {
  lock: true,
  text: '数据提交中',
  spinner: 'el-icon-loading',
  background: 'rgba(255, 255, 255, 0.7)'
}

const callbackhandler = (promise, callback, loading) => {
  promise.then(
    response => {
    // console.log(response)
      if (response.data.status === 200) {
        callback(response.data)
      } else {
        Message.error(response.data.message)
      }
    }
  ).catch(() => {
    Message.error('服务器发生错误，正在联系程序员小哥哥排查中.....')
  }).finally(() => {
    loading.close()
  })
}

export default {
  get (url, callback, params = {}) {
    const loading = Loading.service(loadMsgGet)
    callbackhandler(axios.get(url, { params: params }), callback, loading)
  },

  post (url, callback, data = {}) {
    const loading = Loading.service(loadMsgPut)
    setTimeout(() => {
      loading.close()
    }, 10000)
    callbackhandler(axios.post(url, data), callback, loading)
  },

  put (url, callback, data = {}) {
    const loading = Loading.service(loadMsgPut)
    setTimeout(() => {
      loading.close()
    }, 10000)
    callbackhandler(axios.put(url, data), callback, loading)
  },

  del (url, callback, params = {}) {
    MessageBox.confirm('确定要删除数据吗?', '删除操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      const loading = Loading.service(loadMsgPut)
      setTimeout(() => {
        loading.close()
      }, 10000)
      callbackhandler(axios.delete(url, { params: params }), callback, loading)
    }).catch(err => err)
  },
  // 讲原生的axios 暴露 ，自定义使用
  http: axios
}
