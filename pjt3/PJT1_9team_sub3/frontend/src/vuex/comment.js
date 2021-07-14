
export default {
  namespaced: true,
  state: {
    commentCount:0,
    flagComment:false
  },

  mutations: {

    // response에서 받아오는 부분
    GET_COMMENTLIST(state,commentList){
      state.commentList = commentList
    },
    GET_COMMENT_COUNT(state,count){
      state.commentCount = count
    },
    


    // user의 입력으로 인해 변하는 부분
    IS_WRITE_FLAG(state){
      console.log('comment 작성 또는 변경 후 플래그 변화')
      state.flagComment = !state.flagComment
    },
  },

  actions: {
    // response에서 받아오면 처리할 부분.
    getCommentList({commit},commentList){
      commit('GET_COMMENTLIST',commentList)
    },
    getCommentCount({commit},count){
      commit('GET_COMMENT_COUNT',count)
    },
    
    //component에서 유저에게 받아오면 처리할 부분
    isWriteFlag({commit}){
      commit('IS_WRITE_FLAG')
    },
    
  },
};
