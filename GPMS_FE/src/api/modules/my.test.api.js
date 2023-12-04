export default ({ service, request, serviceForMock, requestForMock, mock, faker, tools }) => ({
  /**
   * @description demo接口
   * @param {Object} data 请求携带的信息
   */
  DEMO_FETCH (data = {}) {
    return request({
      // url: 'http://localhost:8088/_demofetch',
      url: 'http://localhost:8088/_df',
      method: 'post',
      data
    })
  },
  DEF_BLANK () {
    return request({
      // url: 'http://localhost:8088/_demofetch',
      url: 'http://localhost:8088/_df',
      method: 'post'
    })
  }
})
