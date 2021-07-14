//기본 셋팅
import Vue from 'vue';
import Vuex from 'vuex';
//vuex 분할
import getters from './getters';
import actions from './actions';
import mutations from './mutations';

//api 작업 구현부
import auth from './auth';
import error from './error';
import board from './board';
import post from './post';
import comment from './comment';
import main from './main';
import user from './user';
import bamboo from './bamboo';
import market from './market';
import addfunc from './addfunc';
import chat from './chat';
import calendar from './calendar';
import search from './search';
import study from './study';

Vue.use(Vuex);

const PROBLEMS = () => [
  '시스템 기반 OO:OO까지 설문 완료(숫자만 입력)',
  '공가 내 규정으로 OOOOO 취득시 3일/월 허용',
  '6.복리후생(안전사고 대비 OOOOO 운영)',
  '2.생활수칙(OOO 운영)',
  '4.정보보안 규정 내에서 마지막 단어 OO',
];
const POPULAR = () => {};

const state = {
  //spinner(로디용)
  spinnerActive: false,
  spinnerMessage: '',
  //toast
  toastActive: false,
  toastType: {
    search: false,
    myinfo: false,
    email: false,
  },

  //가입하기 인증 문제
  problems: PROBLEMS(),

  //curation Id
  curationId: {
    Market: 122,
    LearnShare: 121,
    StudyMain: 120,
    Recruitment: 123,
  },
};

export default new Vuex.Store({
  state,
  mutations,
  getters,
  actions,
  modules: {
    auth,
    error,
    board,
    post,
    comment,
    main,
    user,
    bamboo,
    market,
    addfunc,
    chat,
    calendar,
    search,
    study,
  },
});
