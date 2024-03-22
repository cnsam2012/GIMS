// Vue
import Vue from 'vue'
import i18n from './i18n'
import App from './App'
// 核心插件
import d2Admin from '@/plugin/d2admin'
// store
import store from '@/store/index'

// 菜单和路由设置
import router from './router'
import { menuHeader, menuAside, menuSearchSuggest } from '@/menu'
import { frameInRoutes } from '@/router/routes'
// import axios from 'axios'
// axios.defaults.withCredentials = true
import { d2CrudPlus } from 'd2-crud-plus'
// import d2Crud from '@d2-projects/d2-crud' 【d2-crud官方已停止维护，推荐使用增强版d2-crud-x】
// 推荐将d2-crud替换为d2-crud-x【使用方式基本与d2-crud一致】
import d2CrudX from 'd2-crud-x'
import { request } from '@/api/service'

import VMdEditor from '@kangc/v-md-editor'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'
// highlightjs
import hljs from 'highlight.js'
VMdEditor.use(githubTheme, {
  Hljs: hljs
})
Vue.use(VMdEditor) // 你项目http请求用的什么就引入什么

// 核心插件
Vue.use(d2Admin)
Vue.use(d2CrudX, { name: 'd2-crud-x' }) // 注册名称为d2-crud-x ，不传name则使用d2-crud作为标签名称
Vue.use(d2CrudPlus, {
  // 获取数据字典的请求方法
  // 可在dict中配置getData方法覆盖此全局方法
  getRemoteDictFunc (url, dict) {
    return request({ // 用你项目中的http请求
      url: url,
      method: 'get'
    }).then(ret => {
      return ret.data // 返回字典数组
    })
  },
  commonOption () { // d2-crud option 全局配置，每个页面的crudOptions会以全局配置为基础进行覆盖
    return {
      format: {
        page: { // page接口返回的数据结构配置，
          request: { // 请求参数
            current: 'current', // 当前
            size: 'size'
          },
          response: { // 返回结果
            current: 'current', // 当前页码 ret.data.current
            size: 'size', // 每页条数，ret.data.size,
            // size: (data) => { return data.size }, //你也可以配置一个方法，自定义返回
            total: 'total', // 总记录数 ret.data.total
            records: 'records' // 列表数组 ret.data.records
          }
          // response (ret) {
          //   // 这里默认配置是  return ret.data
          //   return ret
          // }
        }
      },
      formOptions: {
        defaultSpan: 12 // 默认的表单 span
      },
      options: {
        height: '100%' // 表格高度100%，此时d2-crud-x外部容器必须有高度, 使用toolbar必须设置
      },
      pageOptions: {
        compact: false // 是否紧凑型页面
      },
      viewOptions: {
        disabled: false // 开启查看按钮
      }
    }
  }
})

new Vue({
  router,
  store,
  i18n,
  render: h => h(App),
  created () {
    // 处理路由 得到每一级的路由设置
    this.$store.commit('d2admin/page/init', frameInRoutes)
    // 设置顶栏菜单
    this.$store.commit('d2admin/menu/headerSet', menuHeader)
    // 设置侧边栏菜单
    this.$store.commit('d2admin/menu/asideSet', menuAside)
    // 初始化菜单搜索功能
    this.$store.commit('d2admin/search/init', menuSearchSuggest)
  },
  mounted () {
    // 展示系统信息
    this.$store.commit('d2admin/releases/versionShow')
    // 用户登录后从数据库加载一系列的设置
    this.$store.dispatch('d2admin/account/load')
    // 获取并记录用户 UA
    this.$store.commit('d2admin/ua/get')
    // 初始化全屏监听
    this.$store.dispatch('d2admin/fullscreen/listen')
  }
}).$mount('#app')
