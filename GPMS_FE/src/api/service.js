import axios from 'axios'
import Adapter from 'axios-mock-adapter'
import { get } from 'lodash'
import util from '@/libs/util'
import { errorLog, errorCreate } from './tools'
import store from '@/store'

async function refreshUnread () {
  let auc = 0
  try {
    const myservice = axios.create({ withCredentials: true })
    const res = await myservice({
      method: 'GET',
      url: process.env.VUE_APP_MESSAGE_API
    })
    auc = res.data.data.allUnreadCount
  } catch (e) {
    console.warn(e)
  }
  // console.log('=====' + auc + '=====')
  await store.dispatch('d2admin/message/set', auc)
}

/**
 * @description 创建请求实例
 */
function createService () {
  // 创建一个 axios 实例
  const service = axios.create({ withCredentials: true }) // 允许携带cookie
  // const service = axios.create() // 允许携带cookie
  // 请求拦截
  service.interceptors.request.use(
    config => config,
    error => {
      // 发送失败
      console.log(error)
      return Promise.reject(error)
    }
  )
  // 响应拦截
  service.interceptors.response.use(
    async response => {
      refreshUnread()
      // dataAxios 是 axios 返回数据中的 data
      const dataAxios = response.data
      // 这个状态码是和后端约定的
      const { code } = dataAxios
      // 根据 code 进行判断
      if (code === undefined) {
        // 如果没有 code 代表这不是项目后端开发的接口 比如可能是 D2Admin 请求最新版本
        return dataAxios
      } else {
        // 有 code 代表这是一个后端接口 可以进行进一步的判断
        switch (code) {
          case 0:
            // [ 示例 ] code === 0 代表没有错误
            return dataAxios
          case 200:
            // [ 示例 ] code === 200 代表没有错误
            return dataAxios
          case -1:
            // [ 示例 ] 其它和后台约定的 code
            errorCreate(`FATAL! [ code: -1 ] ${dataAxios.msg}: ${response.config.url}`)
            break
          default:
            var codeStr = code + ''
            if (codeStr[0] === '4') {
              if (dataAxios.data) {
                console.error(dataAxios.data)
                // errorCreate(`客户端错误 code:${dataAxios.code} ${dataAxios.msg}: ${JSON.stringify(dataAxios.data)}`)
                return dataAxios
              }
              errorCreate(`客户端错误 code:${dataAxios.code} ${dataAxios.msg}: ${response.config.url}`)
              break
            }
            // 不是正确的 code
            errorCreate(`[ 未知错误 code: ${dataAxios.code}  ]${dataAxios.msg}: ${response.config.url}`)
            break
        }
      }
    },
    error => {
      const status = get(error, 'response.status')
      const msg = get(error, 'response.data.msg')
      const errorMsg = get(error, 'response.data.data')
      switch (status) {
        case 400:
          var errorStr = ''
          if (msg != null) {
            errorStr = errorStr + msg + ' '
          }
          for (const errorMsgKey in errorMsg) {
            if (errorMsgKey != null && errorMsg[errorMsgKey] != null) {
              errorStr = errorStr + errorMsg[errorMsgKey] + ' '
            }
          }
          error.message = '请求错误：' + errorStr
          break
        case 401:
          error.message = '未授权，请登录'
          break
        case 403:
          error.message = '拒绝访问'
          break
        case 404:
          error.message = `请求地址出错: ${error.response.config.url}`
          break
        case 408:
          error.message = '请求超时'
          break
        case 500:
          error.message = '服务器内部错误'
          break
        case 501:
          error.message = '服务未实现'
          break
        case 502:
          error.message = '网关错误'
          break
        case 503:
          error.message = '服务不可用'
          break
        case 504:
          error.message = '网关超时'
          break
        case 505:
          error.message = 'HTTP版本不受支持'
          break
        default:
          break
      }
      errorLog(error)
      return Promise.reject(error)
    }
  )
  return service
}

/**
 * @description 创建请求方法
 * @param {Object} service axios 实例
 */
function createRequestFunction (service) {
  return async function (config) {
    const token = util.cookies.get('token')
    const configDefault = {
      headers: {
        Authorization: token,
        'Content-Type': get(config, 'headers.Content-Type', 'application/json')
      },
      timeout: 5000,
      baseURL: process.env.VUE_APP_API,
      data: {}
    }
    return service(Object.assign(configDefault, config))
  }
}

// 用于真实网络请求的实例和请求方法
export const service = createService()
export const request = createRequestFunction(service)

// 用于模拟网络请求的实例和请求方法
export const serviceForMock = createService()
export const requestForMock = createRequestFunction(serviceForMock)

// 网络请求数据模拟工具
export const mock = new Adapter(serviceForMock)
