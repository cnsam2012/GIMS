import { find, assign } from 'lodash'
import * as url from 'url'

const users = [
  {
    username: 'admin',
    password: 'admin',
    uuid: 'admin-uuid',
    name: 'Admin'
  },
  {
    username: 'editor',
    password: 'editor',
    uuid: 'editor-uuid',
    name: 'Editor'
  },
  {
    username: 'user1',
    password: 'user1',
    uuid: 'user1-uuid',
    name: 'User1'
  }
]

export default ({
  service,
  request,
  serviceForMock,
  requestForMock,
  mock,
  faker,
  tools
}) => ({
  /**
   * @description 模拟登录
   * @param {Object} data 登录携带的信息
   */
  FAKE_SYS_USER_LOGIN (data = {}) {
    // 模拟数据
    mock
      .onAny('/login')
      .reply(config => {
        const user = find(users, tools.parse(config.data))
        return user
          ? tools.responseSuccess(assign({}, user, { token: faker.random.uuid() }))
          : tools.responseError({}, '账号或密码不正确')
      })
    // 接口请求
    return requestForMock({
      url: '/login',
      method: 'post',
      data
    })
  },
  /**
   * 登录
   * @param data
   * @returns {*}
   * @constructor
   */
  SYS_USER_LOGIN (data = {}) {
    // 接口请求
    return request({
      url: '/login',
      method: 'post',
      data
    })
  },
  /**
   * 登录
   * @param data
   * @returns {*}
   * @constructor
   */
  SYS_USER_SIGNUP (data = {}) {
    // 接口请求
    return request({
      url: '/register',
      method: 'post',
      data
    })
  },
  /**
   * 注销
   * @returns {*}
   * @constructor
   */
  SYS_USER_LOGOUT () {
    // 接口请求
    return request({
      url: '/logout',
      method: 'GET'
    })
  },
  /**
   * 修改密码
   * @param data
   * @returns {*}
   * @constructor
   */
  SYS_USER_ALTER_PASSWORD (data = {}) {
    // 接口请求
    return request({
      url: '/user/password',
      method: 'PUT',
      data
    })
  },
  /**
   * 修改用户名
   * @param data
   * @returns {*}
   * @constructor
   */
  SYS_USER_ALTER_USERNAME (data = {}) {
    // 接口请求
    return request({
      url: '/user/username',
      method: 'PUT',
      data
    })
  },
  SYS_USER_GET_USERNAME (id) {
    return request(
      {
        url: '/user/profile/username/' + id,
        method: 'GET'
      }
    )
  }
})
