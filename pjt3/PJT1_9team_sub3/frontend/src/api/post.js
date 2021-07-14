import http from './http';

// 보드내에서 포스트 리스트 가져오기
export function getPostList(params){
  return http.get('/post/getPostList',{params:params})
}

// 포스트 쓰기
export function create(params){
  return http.post('/post/create',params)
}

// 포스트 수정하기
export function modify(params){
  const body = params.postItem
  const login_id = params.login_id
  return http.put('/post/modify',body,{params:{login_id}})
}

// 포스트 삭제하기
export function post_delete(post_id,login_id){
  return http.delete(`/post/delete/${post_id}/${login_id}`)
}

// 디테일 포스트 가져오기
export function getPost(params){
  return http.get('/post/getPostById', {
    params:params
  })
}

// 좋아요 토글
export function likePost(params){
  return http.post('/post/like',params)
}

// 스크랩 토글
export function scrapPost(params){
  return http.post('/post/scrap',params)
}

// 중고장터 state 수정
export function modifyState(post_id,post_state,login_id){
  return http.put('/post/modifyState',{},{params:{
    post_id,
    post_state,
    login_id
  }})

}