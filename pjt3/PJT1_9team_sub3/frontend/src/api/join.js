import http from '@/api/http';

export function confirmSolution(solution) {
  return http.get(`/account/confirm/quiz/${solution}`);
}
