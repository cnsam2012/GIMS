// import request from '@/business/api/request.mock' 示例当中用的是模拟数据
import { request } from '@/api/service' // 改成这个请求真实后端
export function GetList (query) {
  return request({
    url: '/_page',
    method: 'get',
    params: query
  })
}

export function AddObj (obj) {
  return request({
    url: '/test/add',
    method: 'post',
    data: obj
  })
}

export function UpdateObj (obj) {
  return request({
    url: '/test/update',
    method: 'post',
    data: obj
  })
}

export function DelObj (id) {
  return request({
    url: '/test/delete',
    method: 'post',
    params: { id }
  })
}

// 以下为本案例示例数据
//
//   [
//   {date: '2016-05-02',status: '0', province: 'sz'},
//     {date: '2016-05-04',status: '1',province: 'sh,sz'},
//     {date: 2232433534511,status: '1', province: 'gz'},  //支持各种时间类型
//     {date: '2016-05-03',status: '2',province: 'wh,gz'}
//   ]
