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
   * @description 获取所有部门
   * @param page
   */
  FETCH_ALL_REPORTS (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'report/detail/all',
      method: 'POST',
      data
    })
  },
  FETCH_SPEC_USER_REPORTS (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'user/reports',
      method: 'POST',
      data
    })
  },
  // /**
  //  * 获取相关部门
  //  * @param data
  //  * @returns {*}
  //  * @constructor
  //  */
  FETCH_FUZZY_REPORTS (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'search',
      method: 'POST',
      data
    })
  },
  ADD_REPORTS (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'report/add',
      method: 'POST',
      data
    })
  },
  // UPDATE_DEPARTMENTS (data = {}) {
  //   return request({
  //     contentType: 'application/json',
  //     url: 'updateDepartment',
  //     method: 'PUT',
  //     data
  //   })
  // },
  DROP_REPORTS (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'report/delete',
      method: 'delete',
      data
    })
  }
})
