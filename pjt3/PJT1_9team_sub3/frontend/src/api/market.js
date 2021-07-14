import http from './http';

// 중고장터는 board_id:34, login_id
export function getSaleList(params){
  return http.get('/post/getSalesList',{params:params})
}

export function create(params){
  return http.post('/bamboo/create',params)
}
