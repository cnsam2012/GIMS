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
    path: '/my',
    title: '控制台',
    icon: 'tachometer'
  },
  {
    path: '/addReports',
    title: '新建报告',
    icon: 'plus-square-o'
  }
])

export const menuHeaderTutorAndCom = supplementPath([
  {
    path: '/index',
    title: '首页',
    icon: 'home'
  },
  {
    path: '/my',
    title: '控制台',
    icon: 'tachometer'
  }
])

export const menuAside = supplementPath([
  {
    path: '/index',
    title: '首页',
    icon: 'home'
  },
  {
    path: '/myMessage',
    title: '我的消息',
    icon: 'bell-o'
  },
  {
    title: '我的',
    icon: 'user-circle-o',
    children: [
      {
        path: '/my',
        title: '控制台',
        icon: 'tachometer'
      },
      {
        path: '/alterUsername',
        title: '修改用户名',
        icon: 'pencil'
      },
      {
        path: '/alterPassword',
        title: '修改密码',
        icon: 'key'
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
    icon: 'user-plus'
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
    path: '/planscore_management',
    title: '实习评分',
    icon: 'sort-numeric-asc'
  },
  {
    path: '/addAnnouncement',
    title: '发布公告',
    icon: 'bullhorn'
  },
  {
    path: '/selectTutor',
    title: '选择导师',
    icon: 'address-book-o'
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
    path: '/myMessage',
    title: '我的消息',
    icon: 'bell-o'
  },
  {
    title: '我的',
    icon: 'user-circle-o',
    children: [
      {
        path: '/my',
        title: '控制台',
        icon: 'tachometer'
      },
      {
        path: '/alterUsername',
        title: '修改用户名',
        icon: 'pencil'
      },
      {
        path: '/alterPassword',
        title: '修改密码',
        icon: 'key'
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
    path: '/myMessage',
    title: '我的消息',
    icon: 'bell-o'
  },
  {
    title: '我的',
    icon: 'user-circle-o',
    children: [
      {
        path: '/my',
        title: '控制台',
        icon: 'tachometer'
      },
      {
        path: '/alterUsername',
        title: '修改用户名',
        icon: 'pencil'
      },
      {
        path: '/alterPassword',
        title: '修改密码',
        icon: 'key'
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
    icon: 'user-plus'
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
    path: '/planscore_management',
    title: '实习评分',
    icon: 'sort-numeric-asc'
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

export const menuSearchSuggest4Student = supplementPath([
  {
    path: '/index',
    title: '首页',
    icon: 'home'
  },
  {
    path: '/myMessage',
    title: '我的消息',
    icon: 'bell-o'
  },
  {
    title: '我的',
    icon: 'user-circle-o',
    children: [
      {
        path: '/my',
        title: '控制台',
        icon: 'tachometer'
      },
      {
        path: '/alterUsername',
        title: '修改用户名',
        icon: 'pencil'
      },
      {
        path: '/alterPassword',
        title: '修改密码',
        icon: 'key'
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
  },
  {
    path: '/announcement',
    title: '已发布公告',
    icon: 'exclamation'
  }
])
