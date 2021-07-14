import http from './http';

//댓글 생성
export function comment_create(params) {
  return http.post('/comment/create', params);
}

//댓글 수정
// axios .post(url,{body},{params:{query}})
export function comment_modify(params) {
  const login_id = params.login_id;
  return http.put('/comment/modify', params.commentDto, { params: { login_id } });
}

//댓글 삭제
export function comment_delete(params) {
  return http.delete('/comment/delete', {
    params: {
      comment_id: params.comment_id,
      login_id: params.login_id,
    },
  });
}

export function get(params) {
  return http.post('/comment/create', { params: params });
}
