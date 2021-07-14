
export default {
  namespaced: true,
  state: {
    writeFlag: false,
  },
  mutations: {
    IS_WRITE_FLAG(state) {
      console.log('study에서 데이터 변화로 인한 플래그 변동');
      state.writeFlag = !state.writeFlag;
    },
  },
  actions: {
    isWriteFlag({ commit }) {
      commit('IS_WRITE_FLAG');
    },
    
  },
};
