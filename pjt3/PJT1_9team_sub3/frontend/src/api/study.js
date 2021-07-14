import http from './http';
import { getPost } from './post';

// 전체 그룹 리스트 가져오기
export function getAllGroupList(page,size){
  return http.get('/study/getAllList',{
    params:{
      page:page,
      size:size
    }
  })
}

// 나의 스터디 그룹 리스트 가져오기
export function getMyGroupList(login_id){
  return http.get('/study/getStudyList',{
    params:{
      login_id
    }
  })
}

// 스터디 가입 요청
export function studyRequest(user_id,board_id){
  return http.post('/study/request',{user_id,board_id})
}

// 스터디 가입 요청 리스트
export function getRequestList(board_id){
  return http.get('/study/requestList',{
    params:{
      board_id
    }
  })
}

// 스터디 가입 수락/거절 (-1이면 거절, 1이면 수락)
export function studyRequestProcess(user_id,board_id,option){
  return http.post('/study/requestProcess',{
    user_id,board_id,option
  })
}

// 학습 공유 내 wordcloud 가져오기
export function getWordCloud(){
  return http.get('/study/wordCloud')
}

//스터디그룹 탈퇴
export function studySecession(board_id,user_id){
  return http.delete('/study/secession',{
    params:{board_id,user_id}
  })
}

// 특정 스터디에 대한 나의 가입 요청 현황
export function myStudyRequest(user_id,board_id){
  return http.get('/study/myStudyRequest',{
    params:{
      user_id,board_id
    }
  })
}
