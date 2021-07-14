import * as userApi from '../api/user';

//initialized
const INIT_POSTS = () => {
  return [
    {
      post_id: -1,
      user_id: '',
      board_id: -1,
      post_date: 'YYYY-DD-MM HH:mm:ss',
      post_title: '',
      post_description: '',
      post_like: -1,
      post_image: '[]',
      post_iframe: '',
      post_header: '',
      post_state: -1,
    },
  ];
};
const INIT_COMMENTS = () => {
  return [
    {
      comment_id: -1,
      post_id: -1,
      user_id: '',
      comment_description: '',
      comment_date: 'YYYY-MM-DD HH:mm:ss',
    },
  ];
};
const INIT_SCRAPS = () => {
  return [
    {
      post_id: -1,
      user_id: '',
      board_id: -1,
      post_date: 'YYYY-MM-DD HH:mm:ss',
      post_title: '',
      post_description: '',
      post_like: -1,
      post_image: '',
      post_iframe: '',
      post_header: '',
      post_state: -1,
    },
  ];
};

export default {
  namespaced: true,
  state: {
    posts: INIT_POSTS(),
    comments: INIT_COMMENTS(),
    scraps: INIT_SCRAPS(),
  },

  mutations: {
    //게시글 set
    setPosts(state, payload) {
      state.posts = payload;
    },

    //댓글 set
    setComments(state, payload) {
      state.comments = payload;
    },

    //스크랩 set
    setScraps(state, payload) {
      state.scraps = payload;
    },
  },

  actions: {
    //즐겨찾기 토글
    async actPutFavorite({ commit }, payload) {
      try {
        const response = await userApi.putFavorite(payload);
        // console.log(response);
      } catch (error) {
        console.log(error);
        alert('즐겨찾기 수정 작업 중 문제가 발생했습니다.');
      }
    },
    //보드 구독 취소{board_id, user_id}
    async putDeleteSub({ commit }, payload) {
      try {
        const response = await userApi.putDeleteSub(payload);
        // console.log(response);
        if (response.data.message == 'SUCCESS') {
          return true;
        }
      } catch (error) {
        console.log(error);
        alert('구독 취소 작업 중 문제가 발생했습니다.');
      }
      return false;
    },

    //내정보>내 작성글
    //작성글 가져오기
    async getPosts(context) {
      try {
        const response = await userApi.getPosts(context.rootState.auth.user.userId);
        // console.log(response);
        context.commit('setPosts', response.data.posts);
      } catch (error) {
        console.log(error);
        alert('내 작성 글을 가져오는 도중 문제가 발생했습니다.');
      }
    },
    //작성글 삭제
    //No Permission:해당 글 없음 / fail:글삭제~레디스? /  success 모든 기능 성공
    async actDeletePost({ commit }, payload) {
      try {
        const response = await userApi.deletePost(payload);
        // console.log(response);
        if (response.data.message == 'success' || response.data.message == 'fail') {
          // return true;
        }
      } catch (error) {
        console.log(error);
        // alert('글 삭제 도중에 문제가 발생했습니다.');
      }
      //추후 문제 해결할 시 변경
      return true;
    },

    //작성 댓글
    //작성 댓글 가져오기
    async actGetComments(context) {
      try {
        const response = await userApi.getComment(context.rootState.auth.user.userId);
        // console.log(response);
        context.commit('setComments', response.data.comments);
      } catch (error) {
        console.log(error);
        alert('작성 댓글을 가져오는 도중 문제가 발생했습니다.');
      }
    },
    //작성 댓글 삭제
    async actDelComment(context, comment_id) {
      try {
        const response = await userApi.deleteComment({ comment_id: comment_id, login_id: context.rootState.auth.user.userId });
        console.log(response);
        if (response.data.message == 'success') {
          return true;
        }
      } catch (error) {
        console.log(error);
        alert('댓글을 삭제하는 도중 문제가 발생했습니다.');
      }
      return false;
    },

    //내 스크랩
    //rest api 스크랩 리스트 요청
    async actScraps(context) {
      try {
        const response = await userApi.getScraps(context.rootState.auth.user.userId);
        // console.log(response);
        if (response.data.message == 'SUCCESS') {
          context.commit('setScraps', response.data.scraps);
        }
      } catch (error) {
        console.log(error);
        alert('내 스크랩 목록을 가져오는 도중 문제가 발생했습니다.');
      }
    },
    //스크랩 삭제용 토글
    async actTogleScrap(context, post_id) {
      try {
        const response = await userApi.postScraps({
          user_id: context.rootState.auth.user.userId,
          post_id: post_id,
        });
        console.log(response);
        if (response.data.message == 'success') {
          return true;
        }
      } catch (error) {
        console.log(error);
        alert('스크랩 상태를 변경하는 도중 문제가 발생했습니다.');
      }
      return false;
    },
  },

  getters: {
    //작성글
    //작성글 리스트
    getsPosts(state) {
      return state.posts;
    },

    //작성댓글
    //작성댓글 리스트
    getGetComments(state) {
      return state.comments;
    },

    //내 스크랩
    //스크랩 리스트 반환
    getScraps(state) {
      return state.scraps;
    },
  },
};
