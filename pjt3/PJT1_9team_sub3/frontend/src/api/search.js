import http from './http';

//전체 보드를 대상으로 검색
export function getSearchAllBoard(payload) {
  return http.get(
    `/board/searchBoard?keyword=${payload.keyword}&page=${payload.page}&size=${payload.size}&sort=${payload.sort}&type=${payload.type}`
  );
}

//전체 포스트를 대상으로 검색
export function getSearchAllPost(payload) {
  return http.get(
    `/post/searchPost?keyword=${payload.keyword}&page=${payload.page}&size=${payload.size}&sort=${payload.sort}&type=${payload.type}`
  );
}

//보드 내 포스트 검색
export function getSearchPost(payload) {
  return http.get(
    `/post/board/searchPost?board_id=${payload.board_id}&keyword=${payload.keyword}&page=${payload.page}&size=${payload.size}&sort=${payload.sort}&type=${payload.type}&user_id=${payload.user_id}`
  );
}
