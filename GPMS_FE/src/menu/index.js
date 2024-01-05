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
    title: '页面',
    icon: 'folder-o',
    children: [
      {
        path: '/page1',
        title: '页面 1'
      },
      {
        path: '/page2',
        title: '页面 2'
      },
      {
        path: '/page3',
        title: '页面 3'
      }
    ]
  },
  {
    title: '我的',
    icon: 'folder-o',
    children: [
      {
        path: '/my',
        title: '我的 缺省'
      }
    ]
  },
  {
    path: '/TEST_LOGIN',
    title: '测试登录',
    icon: 'address-card-o'
  }
])

export const menuAside = supplementPath([
  {
    path: '/index',
    title: '首页',
    icon: 'home'
  },
  {
    title: '页面',
    icon: 'folder-o',
    children: [
      {
        path: '/page1',
        title: '页面 1'
      },
      {
        path: '/page2',
        title: '页面 2'
      },
      {
        path: '/page3',
        title: '页面 3'
      }
    ]
  },
  {
    title: '我的',
    icon: 'folder-o',
    children: [
      {
        path: '/my',
        title: '我的 缺省'
      }
    ]
  },
  {
    path: '/TEST_LOGIN',
    title: '测试登录',
    icon: 'address-card-o'
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
  }
])
