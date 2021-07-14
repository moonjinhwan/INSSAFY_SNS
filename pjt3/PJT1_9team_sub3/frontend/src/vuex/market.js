import * as marketApi from '@/api/market'
export default {
  namespaced: true,
  state: {
    flagmarket:false
  },

  mutations: {

    // response에서 받아오는 부분


    // user의 입력으로 인해 변하는 부분
    IS_WRITE_FLAG(state){
      console.log('market 작성 또는 변경 후 플래그 변화')
      state.flagmarket = !state.flagmarket
    },
  },

  actions: {
    // response에서 받아오면 처리할 부분.
    //component에서 유저에게 받아오면 처리할 부분
    isWriteFlag({commit}){
      commit('IS_WRITE_FLAG')
    },
    
    deletemarket({commit},params){
      marketApi.market_delete(params)
      .then(res=>{
        console.log(res)
      })
      .catch(err=>{
        console.log(err)
      })
      commit('IS_WRITE_FLAG')
    }
    
  },
};
