export default ({ service, request, serviceForMock, requestForMock, mock, faker, tools }) => ({
  /**
   * @description demo接口
   * @param {Object} data 请求携带的信息
   */
  FETCH_ALL_DEPARTMENTS (page = {}) {
    return request({
      // url: 'http://localhost:8088/_demofetch',
      url: '/department',
      method: 'get',
      page
    })
  }
})
