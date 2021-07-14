<template>
  <v-app class="main-bg-color">
    <v-main>
      <v-container id="container" class="pt-8">
        <!-- PC에서 보여줄 curation이름과 검색 -->
        <v-row
          id="board-header"
          v-if="!ResponsiveSize.isMobile"
          no-gutters
          class="d-flex 
            flex-row 
            justify-space-between"
        >
          <!-- 페이지 이름 -->
          <div class="text-overline  text-weight-black" style="font-size: 20px !important;">Board</div>

          <!-- 검색버튼 -->
          <button id="search-btn" class="icon-btn" @click="clickSearchIcon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
              <path
                d="M23.809 21.646l-6.205-6.205c1.167-1.605 1.857-3.579 1.857-5.711 0-5.365-4.365-9.73-9.731-9.73-5.365 0-9.73 4.365-9.73 9.73 0 5.366 4.365 9.73 9.73 9.73 2.034 0 3.923-.627 5.487-1.698l6.238 6.238 2.354-2.354zm-20.955-11.916c0-3.792 3.085-6.877 6.877-6.877s6.877 3.085 6.877 6.877-3.085 6.877-6.877 6.877c-3.793 0-6.877-3.085-6.877-6.877z"
              />
            </svg>
          </button>
        </v-row>

        <v-row>
          <!-- 왼쪽 보드 설명 및 추가 기능 -->
          <v-col class="col-12 col-sm-4">
            <div id="description" class="rounded-bg container-description">
              <v-list color="transparent">
                <div
                  class="d-flex
                  flex-column
                  justify-space-between"
                  style="min-height: 25vh;"
                >
                  <!-- 보드 설명 -->
                  <BoardDescription :in-board="inBoard" :is-manager="isManager" @board-image="boardImage" />
                  <button class="btn-subscribe b-title" @click="onSubscribe" v-if="!inBoard">Subscribe</button>
                  <button class="btn-subscribing b-title" v-if="inBoard" v-b-modal.modal-unsubscribe>
                    Subscribing
                  </button>
                  <b-modal id="modal-unsubscribe" title="Info" size="sm" centered @ok="onSubscribe">
                    <p class="my-4">구독을 취소하시겠습니까?</p>
                    <template #modal-footer="{ok}">
                      <b-button variant="delete" @click="ok()">
                        취소하기
                      </b-button>
                    </template>
                  </b-modal>
                </div>
              </v-list>
            </div>
            <CheckList :is-manager="isManager" v-if="isCheck" />
            <VoteList :in-board="inBoard" :is-manager="isManager" v-if="isVote" />
            <UserRank v-if="isRank" />
          </v-col>
          <v-col class="col-12 col-sm-8">
            <!-- 달력 -->
            <CalendarSpan id="study-calendar" class="rounded-bg" :boardName="this.boardId" v-if="isCalendar" />
            <CalendarDialog :boardName="this.boardId" />
            <ModifyDialog :boardName="this.boardId" />
            <DetailDialog />

            <PostWrite :in-board="inBoard" style="margin:20px 10px" />
            <PostList />
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import BoardDescription from '@/components/board/BoardDescription.vue';

import PostWrite from '@/components/board/PostWrite.vue';
import PostList from '@/components/board/PostList.vue';

//board api
import * as boardApi from '@/api/board';

// 스타일 적용
import '@/assets/css/static/style.css';

// 체크리스트
import CheckList from '@/components/addfunc/CheckList';
// 투표
import VoteList from '@/components/addfunc/VoteList';
// 유저랭킹
import UserRank from '@/components/addfunc/UserRank';

import CalendarSpan from '@/components/calendar/CalendarSpan';

export default {
  name: 'Board',
  components: {
    BoardDescription,
    PostList,
    PostWrite,
    CheckList,
    VoteList,
    UserRank,

    //달력
    CalendarSpan,
    CalendarDialog: () => import('@/components/calendar/CalendarDialog'),
    DetailDialog: () => import('@/components/calendar/DetailDialog'),
    ModifyDialog: () => import('@/components/calendar/ModifyDialog'),
  },
  // 뷰 인스턴스 제거될 때 resize 호출
  beforeDestroy() {
    if (typeof window === 'undefined') return;

    window.removeEventListener('resize', this.onResize, { passive: true });
  },
  mounted() {
    // resize 실시해서 현재 화면 크기 확인
    this.onResize();

    window.addEventListener('resize', this.onResize, { passive: true });
  },
  data() {
    return {
      // 모바일 화면 체크 mobile화면인지, 사이즈 이용할 값
      ResponsiveSize: {
        isMobile: false,
        viewSize: 0,
      },

      inBoard: '',
      isManager: false,
      boardimg: '',

      boardId: '',
    };
  },
  created() {
    this.$store.commit('search/CLEAR_SEARCH_LIST');
    this.boardId = this.$route.params.board_id;
    // this.$store.dispatch('board/IsInBoard',Number(this.$route.params.board_id))
    // 구독했는 지 파악하기 : inBoard
    const BOARD_ID = Number(this.$route.params.board_id);
    const boards = JSON.parse(localStorage.subBoard);
    const boardIds = boards.map((e) => {
      return e.board_id;
    });
    this.inBoard = boardIds.includes(BOARD_ID);

    // 구독했다면 관리자인가? 아님 그냥 유저인가? : isManager
    if (this.inBoard) {
      const idx = boards.findIndex((board) => board.board_id === BOARD_ID);
      if (boards[idx].user_role == 1) {
        this.isManager = true;
      }
    }
    if (this.boardimg != '') {
      console.log(this.boardimg);
      const header = document.querySelector('#board-header');
      header.style.background = `url(${this.boardimg})`;
    }
    // 추가기능 정보 vuex 저장
    this.fetchData();
  },
  computed: {
    // inBoard() {
    //   return this.$store.state.board.inBoard
    // }
    // 추가기능 체크
    isCheck() {
      return this.$store.state.addfunc.isCheckList;
    },
    isCalendar() {
      return this.$store.state.addfunc.isCalendar;
    },
    isVote() {
      return this.$store.state.addfunc.isVote;
    },
    isRank() {
      return this.$store.state.addfunc.isRank;
    },
  },
  methods: {
    boardImage(boardimg) {
      this.boardimg = boardimg;
      if (this.boardimg != '' && this.boardimg != null && this.boardimg != 'null') {
        const header = document.querySelector('#board-header');
        header.style.minHeight = '250px';
        header.style.background = `url(${this.boardimg})`;
      }
    },
    // 현재 활성화된 기기에 따라 flag 변경
    onResize() {
      this.ResponsiveSize.isMobile = window.innerWidth < 426;
      this.ResponsiveSize.viewSize = window.innerWidth;
    },
    onSubscribe() {
      const BOARD_ID = Number(this.$route.params.board_id);

      const boards = JSON.parse(localStorage.subBoard);
      const board = boards.filter((board) => board.board_id === Number(this.$route.params.board_id));

      const params = {
        user_id: String(localStorage.userId),
        board_id: BOARD_ID,
        user_role: 0,
      };
      boardApi
        .subscribe(params)
        .then((res) => {
          console.log(res);
          if (res.data.message === 'fail') {
            return;
          } else {
            this.inBoard = !this.inBoard;

            // localStorage 수정해주는 부분
            if (board.length > 0) {
              // 보드가 있네? 그럼 구독 해지!
              const idx = boards.findIndex((board) => board.board_id === Number(this.$route.params.board_id));
              boards.splice(idx, 1);
            } else {
              // 보드가 없었어. 그러면 바로 구독하면 돼!
              boards.push({
                board_id: Number(this.$route.params.board_id),
                user_id: localStorage.userId,
                favorite_flag: 0,
                write_post_count: 0,
                is_used: 0,
                user_role: 0,
              });
            }
            localStorage.subBoard = JSON.stringify(boards);
            this.$store.commit('auth/setSubBoardRefresh');
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 추가기능 flag 정보 가져오기
    fetchData() {
      boardApi
        .board_detail(this.$route.params.board_id)
        .then((res) => {
          const addfunc = res.data.board_function;
          // console.log(addfunc, '나옴?')
          // 추가기능 여부 갱신
          this.$store.dispatch('addfunc/isUsed', addfunc);
        })
        .catch((err) => {
          console.error(err);
        });
    },

    //검색 아이콘 클릭
    clickSearchIcon() {
      const epicenter = this.$router.currentRoute.path;
      let board_id = epicenter.substring(1);
      board_id = board_id.slice(board_id.indexOf('/') + 1);
      // console.log(board_id);
      this.$store.commit('search/SET_SEARCH_ACTIVE', { active: 'post', epicenter: board_id });
      this.$store.commit('setToastTogle');
      this.$store.commit('setToastType', 'search');
    },
  },
};
</script>

<style scoped>
#board-header {
  background-size: cover !important;
  background-position: center !important;
}
/* calendar 내용 */
#study-calendar {
  overflow: hidden;
  width: 100%;
  height: 650px;
  min-height: 650px;
  box-shadow: var(--basic-shadow-w);
}

.main-bg-color {
  background-color: #ebebe9;
}
/* 전체 폰트 */
#container {
  font-family: 'Noto Sans KR', sans-serif !important;
}
/* 왼쪽, 오른쪽 섹션 컨테이너 */
.custom-container {
  box-shadow: var(--basic-shadow-s) !important;
  border-radius: 15px !important;
  background-color: var(--basic-color-bg2) !important;
}

/* 구독버튼 */
.container-description {
  width: 100%;
  padding: 10px;
  box-shadow: var(--basic-shadow-w);
}
.btn-subscribe {
  text-align: center;
  margin: auto;
  height: 50px;
  width: 80%;
  border: none;
  border-radius: 5px;
  color: var(--basic-color-bg);
  text-shadow: 0 0px 1px var(--basic-color-fill3);
  background: var(--basic-color-key) !important;
  box-shadow: 10px 10px 20px #bcbcba, -10px -10px 20px #ffffff;
  transition: 0.3s all ease;
  display: flex;
  justify-content: center;
  align-items: center;
}
.btn-subscribe:hover,
.btn-subscribe:active {
  background-color: #ebebe9 !important;
  color: var(--basic-color-key);
}
.btn-subscribing {
  text-align: center;
  margin: auto;
  height: 50px;
  width: 80%;
  border: none;
  border-radius: 5px;
  color: var(--basic-color-key);
  text-shadow: 0 0px 1px var(--basic-color-fill3);
  background: var(--basic-color-bg2) !important;
  /* box-shadow: 10px 10px 20px #bcbcba, -10px -10px 20px #ffffff; */
  transition: 0.3s all ease;
  display: flex;
  justify-content: center;
  align-items: center;
}
.btn-subscribing:hover,
.btn-subscribing:active {
  background-color: var(--basic-color-bg) !important;
  color: var(--basic-color-key);
}
/* modal 에서 구독 취소 버튼 */
.btn-delete {
  margin-left: 5px;
  margin-top: 3px;
  font-size: 14px;
  padding: 4px 8px;
  border: 1px solid #aa2610 !important;
  border-radius: 30px;
  color: #fff;
  background-color: #f0725b !important;
  transition: background-color 0.3s, color 0.3s ease;
}
.btn-delete:hover,
.btn-delete:active {
  color: #fff;
  background-color: #aa2610 !important;
}
#custom-container {
  background-color: var(--basic-color-bg2) !important;
}

/* 검색 추가 */
#search-btn {
  width: 42px;
  height: 42px;
  border-radius: 25px;
  margin-top: 8px;
  background-color: var(--basic-color-bg) !important;
  transition: transform 0.5s ease;
}
#search-btn:hover {
  transform: scale(1.1);
}
.icon-btn {
  margin: 5px 10px;
  transition: transform 0.3s ease;
}
.icon-btn:hover,
.icon-btn:active {
  transform: scale(1.2);
}
</style>
