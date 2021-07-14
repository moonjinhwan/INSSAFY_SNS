export default {
  //comment
  createComment({ commit }, commentItem) {
    commit('CREATE_COMMENT', commentItem);
  },

  //post
  createPost({ commit }, postItem) {
    commit('CREATE_POST', postItem);
  },


}
