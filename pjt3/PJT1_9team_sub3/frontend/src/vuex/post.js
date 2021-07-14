

export default {
  namespaced: true,
  state: {
    flagLike:false,
    flagScrap:false,
    flagModify:false,

    countLike:0,
    commnetList:[]
  },

  mutations: {

    // response에서 받아오는 부분
    IS_LIKED(state,data){
      state.flagLike = data
    },
    IS_SCRAPPED(state,data){
      state.flagScrap = data
    },
    IS_LIKE_COUNT(state,likeCount){
      state.countLike = likeCount
    },
    GET_COMMENTLIST(state,commentList){
      state.commentList = commentList
    },


    // user의 입력으로 인해 변하는 부분
    POST_LIKE(state,data){
      state.flagLike = !state.flagLike
      state.countLike += data
    },
    POST_SCRAP(state){
      state.flagScrap = !state.flagScrap
    },
    IS_MODIFY_FLAG(state){
      state.flagModify = !state.flagModify
    }
  },

  actions: {
    // response에서 받아오면 처리할 부분.
    isLiked({commit},isLiked){
      if(isLiked===0){
        commit('IS_LIKED',false)
      }else{
        commit('IS_LIKED',true)
      }
    },
    isScrapped({commit},isScrapped){
      if(isScrapped===0){
        commit('IS_SCRAPPED',false)
      }else{
        commit('IS_SCRAPPED',true)
      }
    },
    isLikeCount({commit},likeCount){
      commit('IS_LIKE_COUNT',likeCount)
    },
    getCommentList({commit},commentList){
      commit('GET_COMMENTLIST',commentList)

    },

    //component에서 유저에게 받아오면 처리할 부분
    postLike({commit},flagLike){
      if(flagLike){
        commit('POST_LIKE',-1)
      }else{
        commit('POST_LIKE',1)
      }
    },
    postScrap({commit}){
      commit('POST_SCRAP')
    },
    isModifyFlag({commit}){
      commit('IS_MODIFY_FLAG')
    }

    
    
  },
};
