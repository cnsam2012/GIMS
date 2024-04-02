export default {
  namespaced: true,
  state: {
    unread: 0
  },
  getters: {
    unread (state) {
      return state.unread
    }
  },
  actions: {
    /**
     * 设置用户未读信息
     * @param state
     * @param dispatch
     * @param unreadCount
     * @returns {Promise<void>}
     */
    async set ({
      state,
      dispatch
    }, unreadCount) {
      state.unread = unreadCount
      // // 持久化
      // await dispatch('d2admin/db/set', {
      //   dbName: 'sys',
      //   path: 'user.unread',
      //   value: unreadCount,
      //   user: true
      // })
    },
    /**
     * @description 从数据库取用户未读
     * @param {Object} context
     */
    async load ({
      state,
      dispatch
    }) {
      // store 赋值
      // state.unread = await dispatch('d2admin/db/get', {
      //   dbName: 'sys',
      //   path: 'user.unread',
      //   defaultValue: {},
      //   user: true
      // })
    }
  }
}
