import http from './http';

// page,size
export function getAllList(params){
  return http.get('/bamboo/getAllList',{params:params})
}

export function create(params){
  return http.post('/bamboo/create',params)
}


export function modify(params){
  return http.put('/bamboo/modfiy',{params:params})
}

//밤부아이디, 로그인아이디
export function bamboo_delete(params){
  return http.delete('/bamboo/delete',{params:params})
}