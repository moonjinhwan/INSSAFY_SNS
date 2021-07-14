import axios from 'axios';
import * as boardApi from '@/api/board';

export default {
  namespaced: true,
  state: {
    writeFlag: false,
    boardList: [],
  },
  mutations: {
    IS_WRITE_FLAG(state) {
      console.log('post 작성후 플래그 변화');
      state.writeFlag = !state.writeFlag;
    },
    GET_BOARD_LIST(state, boardList) {
      state.boardList = boardList;
    },
  },
  actions: {
    isWriteFlag({ commit }) {
      commit('IS_WRITE_FLAG');
    },
    getBoardList({ commit }, boardList) {
      commit('GET_BOARD_LIST', boardList);
    },

    //보드 구독하기
    async actPostSubscribe(context, board_id) {
      try {
        const response = await boardApi.subscribe({
          user_id: context.rootState.auth.user.userId,
          board_id: board_id,
          user_role: 0,
        });
        // console.log(response);
        if (response.data.message == 'success') {
          // console.log(context);
          context.dispatch('auth/getSubBoard', null, { root: true });
          return true;
        }
      } catch (error) {
        console.log(error);
        alert('구독 처리 중 문제가 발생하였습니다.');
      }
      return false;
    },
  },
};
