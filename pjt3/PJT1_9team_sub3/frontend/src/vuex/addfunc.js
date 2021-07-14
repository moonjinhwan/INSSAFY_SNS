export default {
  namespaced: true,
  state: {
    // 추가기능 체크 flag
    isCheckList: false,
    isCalendar: false,
    isVote: false,
    isRank: false,

    // 체크리스트 편집중 flag
    isEditNow: false,
    flagWrite: false,
  },
  getters: {},
  mutations: {
    // response에서 받아오는 부분
    IS_USED(state, data) {
      state.isCheckList = data.checklist_flag;
      state.isCalendar = data.calendar_flag;
      state.isVote = data.vote_flag;
      // userRank response가 다르다
      state.isRank = data.user_rank_flag;
    },

    // 체크리스트 편집중 상태 저장
    IS_EDIT_CHECK_LIST(state, boolean) {
      console.log(boolean)
      state.isEditNow = Boolean(boolean)
    },
    FLAG_WRITE(state){
      state.flagWrite = !state.flagWrite
    },
  },
  actions: {
    isUsed({ commit }, addfunc) {
      commit('IS_USED', addfunc);
    },
    isEditCheckList({ commit }, bool) {
      commit('IS_EDIT_CHECK_LIST', bool);
    },
    flagWrite({ commit }) {
      commit('FLAG_WRITE');
    },
  },
};