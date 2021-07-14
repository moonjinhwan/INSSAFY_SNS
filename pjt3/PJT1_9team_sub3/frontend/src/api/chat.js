import http from './http';

// 채팅방 만들기
export function createChatRoom(params) {
  return http.post('/chat/checkRoomDupl', {}, {params})
}
// 채팅리스트 가져오기(대화상대 목록)
export function getChatList(params) {
  return http.post('/chat/getRoomList', {}, {params})
}
// 채팅방 내 메세지 가져오기
export function getMessages(params) {
  return http.post('/chat/enterRoom', {}, {params})
}

// 로그인 후에 알림 받기 API
export function getNotice(userId) {
  return http.get(`/chat/notice/${userId}`)
}

// 채팅방 들어갔을 때 받게 되는 알림의 메세지 개수 초기화
export function updateNotice(params) {
  return http.post('/chat/updateNotice', {}, {params})
} 