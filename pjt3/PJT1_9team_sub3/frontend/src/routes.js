//main
import Main from './views/main/Main.vue';
import Intro from './views/main/Intro.vue';

//로그인 영역
import Login from './views/user/Login.vue';
import Join from './views/user/Join.vue';
import Mypage from '@/views/user/Mypage.vue';

// curation board
import BambooForest from '@/views/curation/bamboo/BambooForest';
import LearnShare from '@/views/curation/learningshare/LearnShare';
import StudyMain from '@/views/curation/study/StudyMain';
import Study from '@/views/curation/study/Study';
import StudyGroupForm from '@/views/curation/study/StudyGroupForm';
import Market from '@/views/curation/market/Market';
import Recruitment from '@/views/curation/recruitment/Recruitment';

//board
import Board from './views/board/Board.vue';
import PostDetail from './views/board/PostDetail.vue';
import BoardForm from './views/board/BoardForm.vue';

//search
import SearchPost from './views/search/SearchPost.vue';
import SearchBoard from './views/search/SearchBoard.vue';

// Chat
import ChatPage from './views/openchat/ChatPage.vue';

//redirect vue
import PageNotFound from './views/redirect/PageNotFound';
import Errors from './views/redirect/Errors';
import JoinSuccess from './views/redirect/JoinSuccess';

import Modify from './views/user/Modify';


//토큰 없이(비회원) About 외 페이지 접근 시 리다이렉트
// https://router.vuejs.org/kr/guide/advanced/navigation-guards.html
import store from './vuex/store';
const requireAuth = () => (to, from, next) => {
  const nextRoute = to.path;
  if (store.getters.getToken != null) {
    return next();
  } else {
    console.log(nextRoute);
    next('/login' + nextRoute);
  }
};
//로그인 중이면 로그인 페이지 방문시 이전 라우터로 이동
const redirectBefore = () => (to, from, next) => {
  //   const beforeRoute = from.path;
  if (store.getters.getToken != null) {
    return next(from);
  } else {
    return next();
  }
};

export default [
  //intro 영역
  {
    path: '/intro',
    name: 'Intro',
    component: Intro,
  },
  //main 영역
  {
    path: '/',
    name: 'Main',
    component: Main,
    beforeEnter: requireAuth(),
  },

  // 사용자 관련 영역
  {
    path: '/login',
    name: 'Login',
    component: Login,
    beforeEnter: redirectBefore(),
  },
  {
    //리다이렉트로 이동할 경우 로그인 환료 후 해당 페이지로 이동시키기 위해
    //보류중인 기능(로그인하지 않으면 about 페이지만 보여줄 예정이라 필요성 낮음)
    //Login.vue 내 computed 주석으로 구현 되어있음
    path: '/login/:nextRoute',
    name: 'LoginNextRoute',
    component: Login,
  },
  {
    path: '/join',
    name: 'Join',
    component: Join,
    beforeEnter: redirectBefore(),
  },
  {
    path: '/mypage',
    name: 'Mypage',
    component: Mypage,
    beforeEnter: requireAuth(), //토큰 정보 없을 때, 로그인 페이지로 redirect
  },

  // Curation board
  {
    path: '/bamboo',
    name: 'BambooForest',
    component: BambooForest,
  },
  {
    path: '/learningshare',
    name: 'LearnShare',
    component: LearnShare,
  },
  {
    path: '/learningshare/:post_id(\\d+)',
    name: 'LearnSharePost',
    component: PostDetail,
    props: true,
  },
  {
    path: '/study/main',
    name: 'StudyMain',
    component: StudyMain,
  },
  {
    path: '/study/main/:post_id(\\d+)',
    name: 'StudyMainPost',
    component: PostDetail,
    props: true,
  },
  {
    path: '/study/:board_id(\\d+)',
    name: 'Study',
    component: Study,
    props: true,
  },
  {
    path: '/study/:board_id(\\d+)/post/:post_id(\\d+)',
    name: 'StudyGroupPost',
    component: PostDetail,
    props: true,
  },
  {
    path: '/study/create',
    name: 'StudyGroupForm',
    component: StudyGroupForm,
  },
  {
    path: '/market',
    name: 'Market',
    component: Market,
  },
  {
    path: '/market/:post_id(\\d+)',
    name: 'MarketPost',
    component: PostDetail,
    props: true,
  },
  {
    path: '/recruitment',
    name: 'Recruitment',
    component: Recruitment,
  },
  {
    path: '/recruitment/:post_id(\\d+)',
    name: 'RecruitmentPost',
    component: PostDetail,
    props: true,
  },

  // board
  {
    path: '/board/:board_id(\\d+)',
    name: 'Board',
    component: Board,
    props: true,
  },
  {
    path: '/board/:board_id(\\d+)/post/:post_id(\\d+)',
    name: 'Post',
    component: PostDetail,
    props: true,
  },
  {
    path: '/board/create',
    name: 'BoardForm',
    component: BoardForm,
  },

  // 검색 관련 영역
  {
    path: '/post/search',
    name: 'SearchPost',
    component: SearchPost,
  },
  {
    path: '/board/search',
    name: 'SearchBoard',
    component: SearchBoard,
  },

  // 채팅 관련 영역
  {
    path: '/chat',
    name: 'ChatPage',
    component: ChatPage,
  },

  //redirect 영역
  {
    path: '*',
    redirect: '/404',
  },
  {
    path: '/404',
    name: 'PageNotFound',
    component: PageNotFound,
  },
  {
    path: '/errors',
    name: 'Errors',
    component: Errors,
  },
  {
    path: '/success/join',
    name: 'JoinSuccess',
    component: JoinSuccess,
  },

  {
    path: '/user/modify',
    name: 'Modify',
    component: Modify,
  },
];
