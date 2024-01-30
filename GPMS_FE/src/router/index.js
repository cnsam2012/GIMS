import Vue from 'vue'
import VueRouter from 'vue-router'

// 进度条
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import store from '@/store/index'
import util from '@/libs/util.js'

// 路由数据
// import routes from './routes'

// --- rbac1 start ---
// 固定菜单与路由
import menuHeader from '@/menu/header'
import menuAside from '@/menu/aside'
import { frameInRoutes, routes } from '@/router/routes'
// 路由与组件映射关系
import routerMapComponents from '@/routerMapComponents'
// 模拟动态菜单与路由
// import { permissionMenu, permissionRouter } from '@/mock/permissionMenuAndRouter'
// 代码生成器生成的菜单与路由
import autoGenerateMenusAndRouters from '@/development'
import * as userService from '@/api/sys/user'
// --- rbac1 end ---

// fix vue-router NavigationDuplicated
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return VueRouterPush.call(this, location).catch(err => err)
}
const VueRouterReplace = VueRouter.prototype.replace
VueRouter.prototype.replace = function replace (location) {
  return VueRouterReplace.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

// 导出路由 在 main.js 里使用
const router = new VueRouter({
  routes
})

// --- rbac2 start ---
let permissionMenu = []
let permissionRouter = []

const permission = {
  functions: [],
  roles: [],
  isAdmin: false
}

// 标记是否已经拉取权限信息
let isFetchPermissionInfo = false

const fetchPermissionInfo = async () => {
  // 处理动态添加的路由
  const formatRoutes = function (routes) {
    routes.forEach(route => {
      route.component = routerMapComponents[route.component]
      if (route.children) {
        formatRoutes(route.children)
      }
    })
  }

  // const formatRoutesByComponentPath = function (routes) {
  //   routes.forEach(route => {
  //     route.component = function (resolve) {
  //       require([`../${route.componentPath}.vue`], resolve)
  //     }
  //     if (route.children) {
  //       formatRoutesByComponentPath(route.children)
  //     }
  //   })
  // }

  // const formatRoutesByComponentPath = function (routes) {
  //   routes.forEach(route => {
  //     route.component = () => import(`../${route.componentPath}.vue`)
  //     if (route.children) {
  //       formatRoutesByComponentPath(route.children)
  //     }
  //   })
  // }

  try {
    const userPermissionInfo = await userService.getUserPermissionInfo()
    permissionMenu = userPermissionInfo.accessMenus
    permissionRouter = userPermissionInfo.accessRoutes
    permission.functions = userPermissionInfo.userPermissions
    permission.roles = userPermissionInfo.userRoles
    permission.interfaces = util.formatInterfaces(userPermissionInfo.accessInterfaces)
    permission.isAdmin = (userPermissionInfo.isAdmin === 1)
  } catch (ex) {
    console.log(ex)
  }

  // 组合代码生成器生成的菜单和路由
  permissionMenu = [...permissionMenu, ...autoGenerateMenusAndRouters.menus]
  permissionRouter = [...permissionRouter, ...autoGenerateMenusAndRouters.routers]

  formatRoutes(permissionRouter)
  const allMenuAside = [...menuAside, ...permissionMenu]
  const allMenuHeader = [...menuHeader, ...permissionMenu]
  // 动态添加路由
  router.addRoutes(permissionRouter)
  // 处理路由 得到每一级的路由设置
  store.commit('d2admin/page/init', [...frameInRoutes, ...permissionRouter])
  // 设置顶栏菜单
  store.commit('d2admin/menu/headerSet', allMenuHeader)
  // 设置侧边栏菜单
  store.commit('d2admin/menu/fullAsideSet', allMenuAside)
  // 初始化菜单搜索功能
  store.commit('d2admin/search/init', allMenuHeader)
  // 设置权限信息
  store.commit('d2admin/permission/set', permission)
  // 加载上次退出时的多页列表
  store.dispatch('d2admin/page/openedLoad')
  await Promise.resolve()
}
// 免校验token白名单
const whiteList = ['/login']
// --- rbac2 end ---

/**
 * 路由拦截
 * 权限验证
 */
router.beforeEach(async (to, from, next) => {
  // 确认已经加载多标签页数据 https://github.com/d2-projects/d2-admin/issues/201
  await store.dispatch('d2admin/page/isLoaded')
  // 确认已经加载组件尺寸设置 https://github.com/d2-projects/d2-admin/issues/198
  await store.dispatch('d2admin/size/isLoaded')

  // 进度条
  NProgress.start()
  // 关闭搜索面板
  store.commit('d2admin/search/set', false)
  const token = util.cookies.get('token')
  // 验证当前路由所有的匹配中是否需要有登录验证的
  // if (to.matched.some(r => r.meta.auth)) {
  if (whiteList.indexOf(to.path) === -1) {
    // 这里暂时将cookie里是否存有token作为验证是否登录的条件
    // 请根据自身业务需要修改
    if (token && token !== 'undefined') {
      // next()
      // 拉取权限信息
      if (!isFetchPermissionInfo) {
        await fetchPermissionInfo()
        isFetchPermissionInfo = true
        next(to.path, true)
      } else {
        next()
      }
    } else {
      // 将当前预计打开的页面完整地址临时存储 登录后继续跳转
      // 这个 cookie(redirect) 会在登录后自动删除
      util.cookies.set('redirect', to.fullPath)
      // 没有登录的时候跳转到登录界面
      // 携带上登陆成功之后需要跳转的页面完整路径
      next({
        name: 'login',
        query: {
          redirect: to.fullPath
        }
      })
      // https://github.com/d2-projects/d2-admin/issues/138
      NProgress.done()
    }
  } else {
    // // 不需要身份校验 直接通过
    // next()
    if (to.name === 'login') {
      // 如果已经登录，则直接进入系统
      if (token && token !== undefined) {
        next(from.path, true)
        NProgress.done()
      } else {
        next()
      }
    } else {
      next()
    }
  }
})

router.afterEach(to => {
  // 进度条
  NProgress.done()
  // 多页控制 打开新的页面
  store.dispatch('d2admin/page/open', to)
  // TODO: 此处 store 或为 app.$store
  // 更改标题
  util.title(to.meta.title)
})

export default router
