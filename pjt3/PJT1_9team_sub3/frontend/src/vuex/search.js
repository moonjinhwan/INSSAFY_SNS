import * as searchApi from '@/api/search';
import { faSleigh } from '@fortawesome/free-solid-svg-icons';
import store from './store';
//api로 넘길 payload 형태 객체 초기화
const INIT_PAYLOAD = () => {
  return {
    keyword: '',
    page: 0,
    size: 10,
    sort: 'new',
    type: '',
  };
};

//target(검색 주체) 초기화
const INIT_HEADER = () => {
  return {
    allBoard: '보드 검색',
    allPost: '포스트 검색',
    board: '서브보드 검색',
    post: '보드 내 포스트 검색',
  };
};
//type(이름, 내용, 지역, 해시 등) 초기화
const INIT_BOARD_TYPE = () => {
  return {
    allBoard: {
      key: ['보드명', '보드설명', '지역', '해시태그'],
      value: ['name', 'description', 'location', 'hash'],
    },
    allPost: {
      key: ['글 제목', '글 내용', '머리말'],
      value: ['title', 'desc', 'header'],
    },
    post: {
      key: ['글 제목', '글 내용', '머리말'],
      value: ['title', 'desc', 'header'],
    },
  };
};

const INIT_SORT = () => {
  return {
    key: ['최신순', '인기순'],
    value: ['new', 'hot'],
  };
};

export default {
  namespaced: true,
  //--------------------------------
  //------------ state -------------
  //--------------------------------
  state: {
    active: 'allBoard',

    //검색 request 매개변수
    payload: INIT_PAYLOAD(),

    //검색바 상태
    header: INIT_HEADER(),
    //검색 type
    type: INIT_BOARD_TYPE(),
    //검색 정렬
    sort: INIT_SORT(),

    //검색 결과
    searchList: [],

    //검색 버튼 클릭 이벤트 발생 지점
    epicenter: '',
  },

  //--------------------------------
  //---------- mutations -----------
  //--------------------------------
  mutations: {
    //검색 토스트 열릴 때 상태 관리
    SET_SEARCH_ACTIVE(state, payload) {
      state.active = payload.active;
      state.epicenter = payload.epicenter;
    },

    //검색 토스트 내에서 sort 변경
    SET_PAYLOAD_SORT(state, sortValue) {
      state.payload.sort = sortValue;
    },

    //payload 적용
    SET_PAYLOAD_RESET(state) {
      state.payload = INIT_PAYLOAD();
    },
    SET_PAYLOAD(state, payload) {
      state.payload.keyword = payload.keyword;
      state.payload.page = payload.page;
      state.payload.type = payload.type;
    },
    SET_PAYLOAD_ID(state, payload) {
      state.payload.type = payload.type;
      state.payload.board_id = payload.board_id;
      state.payload.user_id = payload.user_id;
    },
    //다음 페이지
    SET_NEXT_PAGE(state) {
      state.payload.page = state.payload.page + 1;
    },
    //size 기본값 = 12
    SET_SIZE(state, size) {
      state.payload.size = size;
    },

    //result 적용
    SET_SEARCH_LIST(state, payload) {
      state.searchList = state.searchList.concat(payload);
    },
    CLEAR_SEARCH_LIST(state) {
      state.searchList = [];
    },
  },
  //--------------------------------
  //---------- actions -------------
  //--------------------------------
  actions: {
    //모든 보드 검색
    async actSearchAllBoard({ commit, state }) {
      try {
        const response = await searchApi.getSearchAllBoard(state.payload);
        if (response.data.message === 'success') {
          commit('SET_SEARCH_LIST', response.data.boardList);
          // console.log(state.searchList);
          // console.log(response.data.isLastPage);
          if (response.data.isLastPage === 'No data') {
            return false;
          }
          return true;
        }
      } catch (error) {
        console.log(error);
        alert('검색 도중에 문제가 발생했습니다.');
      }
      return false;
    },

    //모든 포스트 검색
    async actSearchAllPost({ commit, state }) {
      try {
        const response = await searchApi.getSearchAllPost(state.payload);
        if (response.data.message === 'success') {
          commit('SET_SEARCH_LIST', response.data.postList);
          if (response.data.isLastPage === 'No data') {
            return false;
          }
          return true;
        }
      } catch (error) {
        console.log(error);
        alert('검색 도중에 문제가 발생했습니다.');
      }
      return false;
    },

    //보드 내에서 포스트 검색
    async actSearchPost({ commit, state }) {
      try {
        const response = await searchApi.getSearchPost(state.payload);
        // console.log(response);
        if (response.data.message === 'success') {
          commit('SET_SEARCH_LIST', response.data.postList);
          // console.log(state.searchList);
          if (response.data.isLastPage === 'No data') {
            return false;
          }
          return true;
        }
      } catch (error) {
        console.log(error);
      }
      return false;
    },
  },

  //--------------------------------
  //---------- getters -------------
  //--------------------------------
  getters: {
    //활성화된 target 정보 반환
    getHeader(state) {
      return state.header[state.active];
    },
    getType(state) {
      return state.type[state.active];
    },
    getSort(state) {
      return state.payload.sort;
    },
  },
};
