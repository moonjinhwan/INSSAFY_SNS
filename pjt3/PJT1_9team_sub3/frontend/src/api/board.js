import http from './http';

//보드생성
export function board_create(board) {
  return http.post('/board/create', board);
}

//보드리스트 무한스크롤
export function get_boards(params){
  const sort = params.sort
  const page = params.page
  const size = params.size
  return http.get('/board/getBoards',{
    params:{
      sort:sort,
      page:page,
      size:size
    }
  })
}

// 보드 디테일 정보
export function board_detail(board_id) {
  return http.get('/board/detail', {
    params: {
      board_id: board_id,
    },
  });
}

//구독 토글
export function subscribe(payload) {
  return http.post('/board/subscribe', payload);
}

//보드 수정
export function board_modify(body,login_id) {
  return http.put('/board/modify',body,{params:{login_id:login_id}})
}

//보드 삭제
export function board_delete(board_id,login_id) {
  return http.delete(`/board/delete/${board_id}`,{params:{login_id:login_id}})
}
// ==============

//보드 권한 주기
export function board_updateManager(user_id,board_id){
  return http.post('/board/updateManager',{
    user_id,board_id
  })
}