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
  },
  SEND_MESSAGE (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'letter/send',
      method: 'POST',
      data
    })
  },
  SEND_ANNOUNCEMENT (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'notice/send',
      method: 'POST',
      data
    })
  },
  EMPTY_ANNOUNCEMENT (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'notice/empty',
      method: 'POST',
      data
    })
  },
  CONVERSATION_DETAIL (data = {}) {
    return request({
      contentType: 'application/json',
      url: 'letter/detail',
      method: 'POST',
      data
    })
  }
})
