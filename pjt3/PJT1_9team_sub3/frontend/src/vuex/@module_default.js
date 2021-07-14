import * as searchApi from '@/api/search';

//state 초기화
const INIT_STATE = () => {
  return {
    allBoard: false,
    allPost: false,
    board: false,
    post: false,
  };
};

export default {
  namespaced: true,

  //--------------------------------
  //------------ state -------------
  //--------------------------------
  state: {
    keyword: '',
    state: INIT_STATE(),
  },

  //--------------------------------
  //---------- mutations -----------
  //--------------------------------
  mutations: {},

  //--------------------------------
  //---------- actions -------------
  //--------------------------------
  actions: {},

  //--------------------------------
  //---------- getters -------------
  //--------------------------------
  getters: {},
};
