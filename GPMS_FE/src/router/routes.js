import layoutHeaderAside from '@/layout/header-aside'

// 由于懒加载页面太多的话会造成webpack热更新太慢，所以开发环境不使用懒加载，只有生产环境使用懒加载
const _import = require('@/libs/util.import.' + process.env.NODE_ENV)

/**
 * 在主框架内显示
 */
const frameIn = [
  {
    path: '/',
    redirect: { name: 'index' },
    component: layoutHeaderAside,
    children: [
      // 首页
      {
        path: 'index',
        name: 'index',
        meta: {
          auth: true
        },
        component: _import('system/index')
      },
      // 演示页面
      {
        path: 'page1',
        name: 'page1',
        meta: {
          title: '页面 1',
          auth: true
        },
        component: _import('demo/page1')
      },
      {
        path: 'page2',
        name: 'page2',
        meta: {
          title: '页面 2',
          auth: true
        },
        component: _import('demo/page2')
      },
      {
        path: 'page3',
        name: 'page3',
        meta: {
          title: '页面 3',
          auth: true
        },
        component: _import('demo/page3')
      },
      {
        path: 'my',
        name: 'my',
        meta: {
          title: '控制台',
          auth: true
        },
        component: _import('my/my')
      },
      {
        path: 'TEST_LOGIN',
        name: 'TEST_LOGIN',
        meta: {
          title: '测试登录',
          auth: true
        },
        component: _import('TEST_LOGIN/tel')
      },
      {
        path: 'departments_management',
        name: 'departments_management',
        meta: {
          title: '部门管理',
          auth: true
        },
        component: _import('admin/departments_management')
      },
      {
        path: 'users_management',
        name: 'users_management',
        meta: {
          title: '用户管理',
          auth: true
        },
        component: _import('admin/users_management')
      },
      {
        path: 'tutorial_management',
        name: 'tutorial_management',
        meta: {
          title: '指导学生管理',
          auth: true
        },
        component: _import('admin/tutorial_management')
      },
      {
        path: 'reports_management',
        name: 'reports_management',
        meta: {
          title: '报告管理',
          auth: true
        },
        component: _import('admin/reports_management')
      },
      {
        path: 'plans_management',
        name: 'plans_management',
        meta: {
          title: '实习管理',
          auth: true
        },
        component: _import('admin/plans_management')
      },
      {
        path: 'planchoose_management',
        name: 'planchoose_management',
        meta: {
          title: '已选实习',
          auth: true
        },
        component: _import('admin/planchoose_management')
      },
      {
        path: 'planscore_management',
        name: 'planscore_management',
        meta: {
          title: '实习评分',
          auth: true
        },
        component: _import('admin/planscore_management')
      },
      {
        path: 'planScoreDetail/:planid',
        name: 'planScoreDetail',
        meta: {
          title: '评分中',
          auth: true
        },
        component: _import('planScoreDetail')
      },
      {
        path: 'planDetail/:planid',
        name: 'planDetail',
        meta: {
          title: '实习详情',
          auth: true
        },
        component: _import('planDetail')
      },
      {
        path: 'alterPassword',
        name: 'alterPassword',
        meta: {
          title: '修改密码',
          auth: true
        },
        component: _import('userSettings/password')
      },
      {
        path: 'alterUsername',
        name: 'alterUsername',
        meta: {
          title: '修改用户名',
          auth: true
        },
        component: _import('userSettings/username')
      },
      {
        path: 'addReports',
        name: 'addReports',
        meta: {
          title: '新建报告',
          auth: true
        },
        component: _import('addReports')
      },
      {
        path: 'markReports/:rid',
        name: 'markReports',
        meta: {
          title: '报告评分',
          auth: true
        },
        component: _import('markReports')
      },
      {
        path: 'addAnnouncement',
        name: 'addAnnouncement',
        meta: {
          title: '新建公告',
          auth: true
        },
        component: _import('addAnnouncement')
      },
      {
        path: 'selectTutor',
        name: 'selectTutor',
        meta: {
          title: '选择导师',
          auth: true
        },
        component: _import('selectTutor')
      },
      {
        path: 'announcement',
        name: 'announcement',
        meta: {
          title: '已发布公告',
          auth: true
        },
        component: _import('announcementDetail')
      },
      {
        path: 'myMessage',
        name: 'myMessage',
        meta: {
          title: '我的信息',
          auth: true
        },
        component: _import('message')
      },
      {
        path: 'myMessageDetail/:cid/:name1/:name2',
        name: 'myMessageDetail',
        meta: {
          title: '信息详情',
          auth: true
        },
        component: _import('messageDetail')
      },
      // 系统 前端日志
      {
        path: 'log',
        name: 'log',
        meta: {
          title: '前端日志',
          auth: true
        },
        component: _import('system/log')
      },
      // 刷新页面 必须保留
      {
        path: 'refresh',
        name: 'refresh',
        hidden: true,
        component: _import('system/function/refresh')
      },
      // 页面重定向 必须保留
      {
        path: 'redirect/:route*',
        name: 'redirect',
        hidden: true,
        component: _import('system/function/redirect')
      }
    ]
  }
]

/**
 * 在主框架之外显示
 */
const frameOut = [
  // 登录
  {
    path: '/login',
    name: 'login',
    component: _import('system/login')
  },
  // 注册
  {
    path: '/signup',
    name: 'signup',
    component: _import('system/signup')
  }
]

/**
 * 错误页面
 */
const errorPage = [
  {
    path: '*',
    name: '404',
    component: _import('system/error/404')
  }
]

// 导出需要显示菜单的
export const frameInRoutes = frameIn

// 重新组织后导出
export default [
  ...frameIn,
  ...frameOut,
  ...errorPage
]
