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
   * @description demo接口
   * @param {Object} data 请求携带的信息
   */
  FETCH_ALL_PLANS (data = {}) {
    return request({
      url: 'plan/all',
      method: 'post',
      data
    })
  },
  FETCH_ALL_PLANS_C (data = {}) {
    return request({
      url: 'planc/all',
      method: 'post',
      data
    })
  },
  FETCH_SPEC_PLAN (data = {}) {
    return request({
      url: 'plan/detail',
      method: 'post',
      data
    })
  },
  FETCH_SPEC_PLAN_C (data = {}) {
    return request({
      url: 'planc/user',
      method: 'post',
      data
    })
  },
  FETCH_SPEC_PLAN_C_DETAIL (data = {}) {
    return request({
      url: 'planc/detail',
      method: 'post',
      data
    })
  },
  ADD_A_PLAN (data = {}) {
    return request({
      url: 'plan/add',
      method: 'post',
      data
    })
  },
  ADD_A_PLAN_C (data = {}) {
    return request({
      url: 'planc/add',
      method: 'post',
      data
    })
  },
  MARKING_A_PLAN_C (data = {}) {
    return request({
      url: 'planc/score',
      method: 'put',
      data
    })
  },
  DROP_A_PLAN (data = {}) {
    return request({
      url: 'plan/delete',
      method: 'delete',
      data
    })
  },
  DROP_A_PLAN_C (data = {}) {
    return request({
      url: 'planc/delete',
      method: 'delete',
      data
    })
  }
})
