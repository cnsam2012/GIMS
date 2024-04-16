import { uniqueId } from 'lodash'

/**
 * @description 给菜单数据补充上 path 字段
 * @description https://github.com/d2-projects/d2-admin/issues/209
 * @param {Array} menu 原始的菜单数据
 */
function supplementPath (menu) {
  return menu.map(e => ({
    ...e,
    path: e.path || uniqueId('d2-menu-empty-'),
    ...e.children ? {
      children: supplementPath(e.children)
    } : {}
  }))
}

export const menuHeader = supplementPath([
  {
    path: '/index',
    title: '首页',
    icon: 'home'
  },
  {
    path: '/addReports',
    title: '新建报告',
    icon: 'plus-square-o'
  }
])

export const menuHeaderAdmin = supplementPath([
  {
    path: '/index',
    title: '首页',
    icon: 'home'
  }
])

export const menuAside = supplementPath([
  {
    path: '/index',
    title: '首页',
    icon: 'home'
  },
  {
    title: '我的',
    icon: 'folder-o',
    children: [
      {
        path: '/my',
        title: '我的 缺省',
        icon: 'user'
      }
    ]
  },
  {
    path: '/departments_management',
    title: '部门管理',
    icon: 'building-o'
  },
  {
    path: '/reports_management',
    title: '报告管理',
    icon: 'file-text-o'
  },
  {
    path: '/users_management',
    title: '用户管理',
    icon: 'user-circle-o'
  },
  {
    path: '/plans_management',
    title: '实习管理',
    icon: 'briefcase'
  },
  {
    path: '/planchoose_management',
    title: '已选实习',
    icon: 'check-square-o'
  },
  {
    path: '/addAnnouncement',
    title: '发布公告',
    icon: 'bullhorn'
  },
  {
    path: '/announcement',
    title: '已发布公告',
    icon: 'exclamation'
  }
])

export const menuAsideStudent = supplementPath([
  {
    path: '/index',
    title: '首页',
    icon: 'home'
  },
  {
    title: '我的',
    icon: 'folder-o',
    children: [
      {
        path: '/my',
        title: '我的 缺省',
        icon: 'user'
      }
    ]
  },
  {
    path: '/reports_management',
    title: '报告管理',
    icon: 'file-text-o'
  },
  {
    path: '/planchoose_management',
    title: '已选实习',
    icon: 'check-square-o'
  }
])

export const menuSearchSuggest = supplementPath([
  {
    path: '/index',
    title: '首页',
    icon: 'home'
  },
  {
    path: '/TEST_LOGIN',
    title: '测试登录',
    icon: 'address-card-o'
  },
  {
    path: '/departments_management',
    title: '部门管理',
    icon: 'building-o'
  }
])
