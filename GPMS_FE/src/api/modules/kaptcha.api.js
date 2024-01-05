export default ({ service, request, serviceForMock, requestForMock, mock, faker, tools }) => ({
  /**
   * 获取验证码
   * @returns {*}
   * @constructor
   */
  GET_KAPTCHA () {
    return request({
      // url: 'http://localhost:8088/_demofetch',
      url: 'http://localhost:8088/api/kaptcha',
      method: 'get',
      responseType: 'blob'
    })
  }
})
