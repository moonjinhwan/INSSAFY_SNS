import http from './http';
import { getPost } from './post';

// 보드에 달린 추가기능 수정 및 삭제
export function modifyAddFunction(params) {
  return http.put('/board/modifyFunction', {}, { params });
}
// 체크리스트 생성
export function checkListCreate(params) {
  return http.post('/CheckList/create', params);
}
// 체크리스트 목록 조회
export function getCheckList(board_id) {
  return http.get(`/CheckList/read/${board_id}`);
}
// 체크리스트 삭제 - put은 1, 2, 3(위치인자) 필수
export function deleteCheckList(params) {
  return http.put('/CheckList/delete', {}, { params });
}
// 체크리스트 수정
export function modifyCheckList(params) {
  return http.put('/CheckList/update', params);
}

// 보드내 유저 랭킹 가져오기
export function getUserRank(params) {
  // axios get 요청 방법 1
  // return http.get(`/addFunc/getUserRank?board_id=${params}`)
  // 방법 2
  return http.get('/addFunc/getUserRank', {
    params: {
      board_id: params.board_id,
    },
  });
}

//투표생성
export function vote_create(params) {
  return http.post('/vote/create', params);
}

//투표항목생성
export function vote_item_create(vote_id, vote_item_name) {
  return http.post('/vote/item/create', {
    vote_id,
    vote_item_name,
  });
}

// 투표선택
export function vote_select(user_id, vote_item_id) {
  return http.post('/vote/select', {
    user_id,
    vote_item_id,
  });
}

// 투표 디테일한 정보
export function get_voteDetail(vote_id, user_id) {
  return http.get('/vote/getVoteById', {
    params: {
      vote_id,
      user_id,
    },
  });
}

// 보드 내 투표리스트
export function get_voteList(board_id) {
  return http.get('/vote/getBoardVote', {
    params: {
      board_id,
    },
  });
}

//투표삭제
export function vote_delete(vote_id) {
  return http.delete(`/vote/delete/${vote_id}`);
}
