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
  FETCH_ALL_MESSAGES (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'letter/list',
      method: 'POST',
      data
    })
  }
})
