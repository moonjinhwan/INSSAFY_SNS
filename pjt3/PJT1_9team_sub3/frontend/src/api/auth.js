import http from './http';

export function login(email, password) {
  return http.post('/account/confirm/login', {
    user_email: email,
    user_password: password,
  });
}

//member 객체로 매개변수 받음
export function join(member) {
  return http.post('/account/confirm/join', member);
}

// export function duplicate(email) {
//   return http.post('/account/confirm/emailCheck/',{
//     user_email: email,
//   });
// }

export function duplicate(email) {
  return http.get(`/account/confirm/emailCheck/${email}`);
}

export function modify(email, password) {
  return http.post('/account/modify', {
    email: email,
    password: password,
  });
}

//비밀번호 이중체크
export function postPassword(payload) {
  return http.post('/account/user/password', payload);
}
//비밀번호 수정
export function putPassword(payloard) {
  return http.put('/account/user/password', payloard);
}
//회원탈퇴
export function deleteUser(payload) {
  return http.delete(`/account/user/delete?user_id=${payload.user_id}&user_password=${payload.password}`);
}
