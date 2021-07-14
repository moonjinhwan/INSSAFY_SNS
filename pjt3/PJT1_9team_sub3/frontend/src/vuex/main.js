import * as mainApi from '@/api/main';

const INIT_FAVORITES = () => {
  return {
    board_id: -1,
    board_name: '',
    board_description: '',
    board_image: null,
    board_igmyeong: 0,
    board_location: '',
    board_hash: '',
    board_date: 'YYYY-MM-DD HH:mm:ss',
    user_id: '',
    board_state: 0,
  };
};

const INIT_POPURAL_RANK = () => {
  return [
    {
      board_id: '',
      board_image: {},
      board_name: '',
      board_posts: [
        {
          board_id: -1,
          post_date: '',
          post_description: '',
          post_header: '',
          post_id: -1,
          post_iframe: '',
          post_image: '',
          post_like: -1,
          post_state: -1,
          post_title: '',
          user_id: '',
        },
      ],
    },
  ];
};

const INIT_POPURAL_RANK2 = () => {
  return [
    {
      post_id: -1,
      user_id: '',
      board_id: -1,
      board_date: 'YYYY-MM-DD HH:mm:ss',
      post_title: '',
      post_description: '',
      post_like: -1,
      post_image: '',
      post_iframe: '',
      post_header: '',
      post_state: 0,
    },
    {
      post_id: -1,
      user_id: '',
      board_id: -1,
      board_date: 'YYYY-MM-DD HH:mm:ss',
      post_title: '',
      post_description: '',
      post_like: -1,
      post_image: '',
      post_iframe: '',
      post_header: '',
      post_state: 0,
    },
    {
      post_id: -1,
      user_id: '',
      board_id: -1,
      board_date: 'YYYY-MM-DD HH:mm:ss',
      post_title: '',
      post_description: '',
      post_like: -1,
      post_image: '',
      post_iframe: '',
      post_header: '',
      post_state: 0,
    },
  ];
};
const INIT_HEART_BTNS = () => {
  return {
    board_id1: false,
    baord_id2: false,
    board_id3: false,
  };
};

export default {
  namespaced: true,
  state: {
    favorites: INIT_FAVORITES(),
    followRank: INIT_POPURAL_RANK(),
    postRank: INIT_POPURAL_RANK(),
    likeRank: INIT_POPURAL_RANK2(),
    commentRank: INIT_POPURAL_RANK2(),
    subHeartBtns: INIT_HEART_BTNS(),
    postHeartBtns: INIT_HEART_BTNS(),
  },

  mutations: {
    //즐겨찾기
    setFavorites(state, payload) {
      state.favorites = payload;
    },

    //인기
    setFollowRank(state, payload) {
      state.followRank = payload;
    },
    setPostRank(state, payload) {
      state.postRank = payload;
    },
    SET_LIKE_RANK(state, payload) {
      state.likeRank = payload;
    },
    SET_COMMENT_RANK(state, payload) {
      state.commentRank = payload;
    },

    //하트 버튼 클릭(같은 보드가 중복 노출될 경우 함께동작 로직 구현)
    SET_SUB_HEART_BTNS(state, payload) {
      state.subHeartBtns = payload;
    },
    SET_POST_HEART_BTNS(state, payload) {
      state.postHeartBtns = payload;
    },
    CLICK_SUB_HEART_BTNS(state, payload) {
      state.subHeartBtns[payload.board_id] = !payload.state;
      if (state.postHeartBtns.hasOwnProperty(payload.board_id)) {
        state.postHeartBtns[payload.board_id] = !payload.state;
      }
    },
    CLICK_POST_HEART_BTNS(state, payload) {
      state.postHeartBtns[payload.board_id] = !payload.state;
      if (state.subHeartBtns.hasOwnProperty(payload.board_id)) {
        state.subHeartBtns[payload.board_id] = !payload.state;
      }
    },
  },

  actions: {
    //즐겨찾기 목록 요청
    async actFavorites(context, userId) {
      try {
        const res = await mainApi.getFavorites(userId);
        // console.log(res);
        if (res.data.status == 'SUCCESS') {
          context.commit('setFavorites', res.data.favorite);
        }
      } catch (e) {
        // console.log(e);
        // alert('즐겨찾기 목록을 가져오는 중 문제가 발생했습니다.');
      }
    },

    //인기보드
    //구독자 순 목록 요청
    async actFollowRank(context) {
      try {
        const res = await mainApi.getFollowRank();
        // console.log(res);
        const boards = makeBoard(res.data);
        context.commit('setFollowRank', boards);
        // console.log(context);
        //하트 버튼 세팅
        let payload = {};
        boards.forEach((el) => {
          payload[el.board_id] = context.rootGetters['auth/getSubscribed'](el.board_id);
        });
        context.commit('SET_SUB_HEART_BTNS', payload);
      } catch (e) {
        // console.log(e);
        // alert('인기보드(구독자 순) 목록 요청 중 문제가 발생했습니다.');
      }
    },
    //게시글 순 목록 요청
    async actPostRank(context) {
      try {
        const response = await mainApi.getPostRank();
        // console.log(response);
        const boards = makeBoard(response.data);
        context.commit('setPostRank', boards);
        //하트 버튼 세팅
        let payload = {};
        boards.forEach((el) => {
          payload[el.board_id] = context.rootGetters['auth/getSubscribed'](el.board_id);
        });
        context.commit('SET_POST_HEART_BTNS', payload);
      } catch (error) {
        // console.log(error);
        // alert('인기보드(게시글 순) 목록 요청 중 문제가 발생했습니다.');
      }
    },

    //좋아요 순 목록 요청
    async actLikeRank({ commit }) {
      try {
        const response = await mainApi.getLikeRank();
        // console.log(response);
        commit('SET_LIKE_RANK', response.data.like);
      } catch (error) {
        // console.log(error);
        // alert('인기글(좋아요 순) 목록 요청 중 문제가 발생했습니다.');
      }
    },
    //댓글 순 목록 요청
    async actCommentRank({ commit }) {
      try {
        const response = await mainApi.getCommentRank();
        // console.log(response);
        commit('SET_COMMENT_RANK', response.data.postComment);
      } catch (error) {
        // console.log(error);
        // alert('인기글(댓글 순) 목록 요청 중 문제가 발생했습니다.');
      }
    },

    async actNewBoards() {
      try {
        const response = await mainApi.getNewBoard();
        // console.log(response.data.boardList);
        return response.data.boardList;
      } catch (error) {
        console.log(error);
      }
    },
  },

  getters: {
    //즐겨찾기
    getFavorites(state) {
      return state.favorites;
    },

    //인기-보드
    //구독자 순(팔로우 순)
    getFollowRank(state) {
      return state.followRank;
    },
    //게시글 순
    getPostsRank(state) {
      return state.postRank;
    },

    //인기-글
    //좋아요 순
    getLikeRank(state) {
      return state.likeRank;
    },
    getCommentRank(state) {
      return state.commentRank;
    },

    //heart 버튼
    getSubHeartBtns: (state) => (board_id) => {
      return state.subHeartBtns[board_id];
    },
    getPostHeartBtns: (state) => (board_id) => {
      return state.postHeartBtns[board_id];
    },
  },
};

//키값에 value로 사용될 값이 오는 request를 board객체 배열로 가공하는 함수
function makeBoard(data) {
  //키값(보드 정보) 추출
  let keys = Object.keys(data);
  for (let i = 0; i < keys.length; i++) {
    keys[i] = keys[i].replace('{', '');
    keys[i] = keys[i].replace('}', '');
  }
  //키값(보드 정보) 객체로  가공
  let boards = [];
  let values = Object.values(data);
  for (let i = 0; i < keys.length; i++) {
    const obj = keys[i].split(', ');
    //객체 저장 용
    let id, name, image;
    for (let j = 0; j < obj.length; j++) {
      const arr = obj[j].split('=');
      switch (arr[0]) {
        case 'board_id':
          id = arr[1];
          break;
        case 'board_name':
          name = arr[1];
          break;
        case 'board_image':
          if (arr[1] != null && arr[1] != 'null' && arr[1] != '') image = arr[1];
          break;
        default:
          console.log('popular 팔로워 부분, reponse 데이터 가공 실패');
          break;
      }
    }
    //가공된 객체를 board 배열에 담기
    boards.push({
      board_id: id,
      board_name: name,
      board_image: image,
      board_posts: values[i],
    });
  }
  return boards;
}
