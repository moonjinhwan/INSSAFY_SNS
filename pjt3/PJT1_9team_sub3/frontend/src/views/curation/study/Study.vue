<template>
  <v-app id="main-bg-color">
    <v-main style="background: #ebebe9;">
      <v-container class="pt-8">
        <!-- PC에서 보여줄 curation이름과 검색 -->
        <v-row
          v-if="!ResponsiveSize.isMobile"
          no-gutters
          dense
          class="d-flex 
            flex-row 
            justify-space-between"
        >
          <!-- 페이지 이름 -->
          <div
            class="text-overline  text-weight-black page-title"
            style="font-size: 20px !important;"
            @click="goMainStudy()"
          >
            Curation
          </div>
          <!-- 검색관련 부분 -->
          <div
            class="d-flex 
              flex-row 
              justify-flex-end"
          >
            <!-- 검색바 -->
            <!-- <v-text-field placeholder="검색" solo v-model="searchKeyword"></v-text-field> -->
          </div>
        </v-row>
        <v-row dense>
          <!-- 왼쪽 스터디 설명 부분 -->
          <v-col class="col-12 col-sm-4">
            <div id="description" class="rounded-bg container-description">
              <div id="container">
                <div class="board-name-detail">
                  <div class="name-setting">
                    <h4 class="b-desc">{{ groupDto.board_name }}</h4>
                    <!-- 수정버튼과 삭제버튼 토글-->
                    <v-icon id="edit-icon" v-if="isManager && !Edit" @click="btnModify">
                      mdi-cog
                    </v-icon>
                    <p class="r-desc delete-button" v-b-modal.modal-delete v-if="isManager && Edit">삭제</p>
                    <b-modal id="modal-delete" title="🗑" centered @ok="groupDelete">
                      <p class="my-4">보드를 삭제하시겠어요?</p>
                      <template #modal-footer="{ok}">
                        <b-button variant="delete" @click="ok()">
                          삭제하기
                        </b-button>
                      </template>
                    </b-modal>
                  </div>
                  <p class="l-desc board-detail" v-if="!Edit">
                    {{ groupDto.board_description }}
                  </p>
                  <!-- 수정버튼 클릭 시 생성-->
                  <div class="board-detail-form" v-if="Edit">
                    <v-textarea
                      solo
                      clearable
                      no-resize
                      clear-icon="mdi-close-circle"
                      label=""
                      placeholder="보드를 설명해주세요"
                      v-model="tempDescription"
                      color="grey-darken-4"
                    ></v-textarea>
                  </div>
                </div>

                <div class="edit-button-set" v-if="Edit">
                  <div>
                    <button class="p-button-cancel r-desc" @click="cancel">cancel</button>
                    <button class="p-button r-desc" @click="submit">Edit</button>
                  </div>
                </div>
              </div>

              <!-- 구독 버튼 -->
              <button class="btn-subscribe b-title" @click="studyRequest" v-if="!inBoard && flagRequest > 1">
                Subscribe
              </button>
              <button class="btn-Checking b-title" @click="studyWait" v-if="!inBoard && flagRequest === 0">
                Checking
              </button>
              <button class="btn-subscribing b-title" @click="studySecession" v-if="inBoard || flagRequest === 1">
                Subscribing
              </button>
              <v-divider class="my-2" v-if="isManager"></v-divider>
              <v-list color="transparent" v-if="isManager">
                <v-list-item class="mb-0"
                  ><a id="scrap-item" v-b-toggle href="#check-collapse" @click.prevent
                    >신청목록<b-icon icon="chevron-down" aria-hidden="true"></b-icon></a
                ></v-list-item>
                <b-collapse id="check-collapse">
                  <div class="p-1 mt-1" v-for="(user, idx) in requestList" :key="idx">
                    <CheckList :user="user" :isManager="isManager" />
                  </div>
                </b-collapse>
              </v-list>
              <v-divider class="my-2"></v-divider>
              <v-list color="transparent">
                <v-list-item
                  ><a id="scrap-item" v-b-toggle href="#item-collapse" @click.prevent
                    >내 스터디 목록 <b-icon icon="chevron-down" aria-hidden="true"></b-icon></a
                ></v-list-item>
                <b-collapse visible id="item-collapse">
                  <v-col v-for="(group, idx) in myStudyGroup" :key="idx">
                    <MyStudyGroup :group="group" />
                  </v-col>
                </b-collapse>
              </v-list>
            </div>
          </v-col>
          <!-- 오른쪽 스터디 본문 부분 -->
          <v-col class="col-12 col-sm-8">
            <!-- 달력 추가기능 선택시 들어갈 부분 -->
            <div id="center-post">
              <!-- 캘린더 들어가는 부분 -->
              <CalendarSpan
                id="study-calendar"
                class="rounded-bg"
                :boardName="this.$route.params.board_id"
                v-if="inBoard"
              />
            </div>
            <!-- 스터디 게시글쓰기 -->
            <PostWrite class="mt-3" :in-board="inBoard" />
            <GroupPostList />
          </v-col>
        </v-row>
      </v-container>
    </v-main>

    <CalendarDialog :boardName="this.$route.params.board_id" />
    <ModifyDialog :boardName="this.$route.params.board_id" />
    <DetailDialog />
  </v-app>
</template>

<script>
import CalendarSpan from '@/components/calendar/CalendarSpan';

import * as studyApi from '@/api/study';
import * as boardApi from '@/api/board';
import { imageDelete } from '@/api/main';

import deepClone from '@/plugins/deepClone';

export default {
  name: 'StudyGroupMain',
  components: {
    MyStudyGroup: () => import('@/components/curation/study/MyStudyGroup.vue'),
    CheckList: () => import('@/components/curation/study/CheckList.vue'),
    PostWrite: () => import('@/components/board/PostWrite'),
    GroupPostList: () => import('@/components/board/PostList'),
    CalendarSpan,
    CalendarDialog: () => import('@/components/calendar/CalendarDialog'),
    DetailDialog: () => import('@/components/calendar/DetailDialog'),
    ModifyDialog: () => import('@/components/calendar/ModifyDialog'),
  },
  watch: {
    isMyStudyState: 'myStudyState',
    isRequestList: 'getRequestList',
  },
  created() {
    //왼쪽 스터디 디테일 가져오기
    this.getStudyDetail();

    // 왼쪽에 내 스터디 목록 가져오기
    this.getMyStudyGroup();

    // 구독했는 지 파악하기 : inBoard
    this.isInBoard();

    // 요청리스트 가져오기
    this.getRequestList();

    // 나의 현재 스터디 진행 상황
    this.myStudyState();
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
      // 검색 키워드
      searchKeyword: '',
      inBoard: '',
      myStudyGroup: {},
      groupDto: {},
      requestList: {},
      flagRequest: 2,
      Edit: false,

      //스터디 수정할 때 사용
      tempDescription: '',
    };
  },
  computed: {
    // 내가 스터디 그룹을 만들었는 지 확인하는 변수 : isManager
    isManager() {
      const boards = JSON.parse(localStorage.subBoard);
      const BOARD_ID = Number(this.$route.params.board_id);

      if (this.inBoard) {
        const idx = boards.findIndex((board) => board.board_id === BOARD_ID);
        if (boards[idx].user_role != undefined && boards[idx].user_role == 1) {
          return true;
        }
      }
      return false;
    },

    //스터디에서 어떤 변화가 발생했는 지 확인하는 것
    isMyStudyState() {
      return this.$store.state.study.writeFlag;
    },

    isRequestList() {
      return this.$store.state.study.writeFlag;
    },
  },
  methods: {
    // 현재 활성화된 기기에 따라 flag 변경
    onResize() {
      this.ResponsiveSize.isMobile = window.innerWidth < 426;
      this.ResponsiveSize.viewSize = window.innerWidth;
    },
    // study main으로 이동
    goMainStudy() {
      this.$router.push({ name: 'StudyMain' });
    },
    // 내가 가입된 스터디리스트 가져오기
    getMyStudyGroup() {
      studyApi
        .getMyGroupList(localStorage.userId)
        .then((res) => {
          const group = res.data.studyList.filter((group) => {
            if (group != null) {
              return group;
            }
          });
          this.myStudyGroup = group;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 스터디 내용 수정하기
    btnModify() {
      this.Edit = !this.Edit;
      alert(`Edit태그 불러오기!`);
    },
    cancel() {
      this.tempDescription = this.groupDto.board_description;
      this.Edit = !this.Edit;
    },
    submit() {
      // this.board.hashtags = [this.board.hashtags, ...this.tempHashtags]
      this.groupDto.description = this.tempDescription;
      this.groupDto.board_description = this.groupDto.description;
      boardApi
        .board_modify(this.groupDto, String(localStorage.userId))
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });

      this.cancel();
      alert(`수정!`);
    },
    //보드삭제
    async groupDelete() {
      if (!this.isManager) {
        alert('매니저만 사용할 수 있어요');
        return;
      }

      try {
        if (this.groupDto.image != '') {
          await imageDelete(this.groupDto.image)
            .then((res) => {
              console.log('이미지 삭제 완료!');
            })
            .catch((err) => {
              console.log(err);
            });
        }

        await boardApi
          .board_delete(Number(this.$route.params.board_id), localStorage.userId)
          .then((res) => {
            console.log('보드 삭제');
            this.$router.push({ name: 'StudyMain' });
          })
          .catch((err) => {
            console.log(err);
          });
      } catch (err) {
        console.log('PostForDetail- 보드 삭제 에러');
        console.log(err);
      }
    },
    // ==========================================

    // 유저의 현재 스터디 상황
    myStudyState() {
      studyApi
        .myStudyRequest(localStorage.userId, this.$route.params.board_id)
        .then((res) => {
          if (res.data.list.length >= 1) {
            console.log('현재 스터디 상황');
            console.log(res.data.list[0]['request_state']);
            console.log('--------------');
            this.flagRequest = res.data.list[0]['request_state'];
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 내가 가입되어 있는 스터디인가?
    isInBoard() {
      const BOARD_ID = Number(this.$route.params.board_id);
      const boards = JSON.parse(localStorage.subBoard);
      const boardIds = boards.map((e) => {
        return e.board_id;
      });
      this.inBoard = boardIds.includes(BOARD_ID);
    },
    // 스터디 정보 디테일한 거 가져오기!
    getStudyDetail() {
      boardApi
        .board_detail(this.$route.params.board_id)
        .then((res) => {
          this.groupDto = res.data.boardDto;
          this.tempDescription = res.data.boardDto.board_description;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 가입요청하기
    studyRequest() {
      studyApi
        .studyRequest(String(localStorage.userId), Number(this.$route.params.board_id))
        .then((res) => {
          console.log('스터디가입요청성공');
          console.log(res);
          console.log('---------');
          this.flagRequest = 0;
        })
        .catch((err) => {
          console.log('스터디가입요청실패');
          console.log(err);
          console.log('----------');
        });
    },
    // 기다리기
    studyWait() {
      alert('확인 중에 있습니다. 조금만 기다려주세요');
    },
    // 탈퇴하기
    studySecession() {
      if (this.isManager) {
        alert('그룹 생성자는 탈퇴할 수 없습니다.');
        return;
      }
      const BOARD_ID = Number(this.$route.params.board_id);

      const boards = JSON.parse(localStorage.subBoard);
      const board = boards.filter((board) => board.board_id === Number(this.$route.params.board_id));

      studyApi
        .studySecession(Number(this.$route.params.board_id), String(localStorage.userId))
        .then((res) => {
          console.log('스터디 탈퇴');
          console.log(res);
          console.log('---------');
          this.inBoard = !this.inBoard;
          this.flagRequest = 2;

          // localStorage 수정해주는 부분
          if (board.length > 0) {
            // 보드가 있네? 그럼 구독 해지!
            const idx = boards.findIndex((board) => board.board_id === Number(this.$route.params.board_id));
            boards.splice(idx, 1);
          }
          localStorage.subBoard = JSON.stringify(boards);
          this.$store.commit('auth/setSubBoardRefresh');
        })
        .catch((err) => {
          console.log('스터디 탈퇴 실패');
          console.log(err);
          console.log('---------');
        });
    },
    // 요청리스트 가져오기! ( 매니저만 볼 수 있음)
    getRequestList() {
      if (this.isManager) {
        studyApi
          .getRequestList(this.$route.params.board_id)
          .then((res) => {
            console.log('매니저만 보는 요청 리스트');
            console.log(res);
            console.log('--------------');
            this.requestList = res.data.list;
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    // 스터디보드 구독하기
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
      return;
    },
  },
};
</script>

<style scoped>
/* study main 화면 이동 pointer */
.page-title {
  cursor: pointer;
}

#main-bg-color {
  background-color: var(--basic-color-fill) !important;
}
#container {
  font-family: 'Noto Sans KR', sans-serif !important;
  display: flex;
  flex-direction: column;
  padding: 12px 15px;
  min-height: 20vh;
}
.name-setting {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1em;
}
.description {
  margin: 2%;
  padding: 10%;
  flex-basis: 20%;
}
.board-detail {
  margin: 8px 0;
  padding: 0 8px;
  min-height: 15vh;
}
.container-description {
  width: 100%;
  margin: 0px 0 20px;
  padding: 10px;
  box-shadow: var(--basic-shadow-w);
}
/* 버튼 */
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
.btn-Checking {
  text-align: center;
  margin: auto;
  height: 50px;
  width: 80%;
  border: none;
  border-radius: 5px;
  color: var(--basic-color-bg2);
  text-shadow: 0 0px 1px var(--basic-color-fill3);
  background: var(--basic-color-fill3) !important;
  /* box-shadow: 10px 10px 20px #bcbcba, -10px -10px 20px #ffffff; */
  transition: 0.3s all ease;
  display: flex;
  justify-content: center;
  align-items: center;
}
.btn-Checking :hover,
.btn-Checking :active {
  background-color: var(--basic-color-bg) !important;
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

#scrap-item {
  text-decoration: none;
  color: #000;
}
/* 수정할 때 사용하는 버튼 */
.edit-button-set {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.p-button {
  margin-left: 5px;
  margin-top: 3px;
  font-size: 14px;
  padding: 4px 8px;
  border: 1px solid #000;
  border-radius: 30px;
  transition: background-color 0.3s, color 0.3s ease;
}
.p-button:hover,
.p-button:active {
  color: #fff;
  background-color: #000 !important;
}
.p-button-cancel {
  margin-left: 5px;
  margin-top: 3px;
  font-size: 14px;
  padding: 4px 8px;
  border: 1px solid #000;
  border-radius: 30px;
  transition: background-color 0.3s, color 0.3s ease;
}
.p-button-cancel:hover,
.p-button-cancel:active {
  color: #fff;
  background-color: #aa2610 !important;
}

/* calendar 내용 */
#study-calendar {
  overflow: hidden;
  width: 100%;
  height: 650px;
  min-height: 650px;
  box-shadow: var(--basic-shadow-w);
}
/* 모바일 웹화면 */
@media (max-width: 426px) {
  #container-box {
    position: relative;
    flex-direction: column;
  }
  #left-info {
    width: 100%;
    margin: 0;
    overflow-y: inherit;
  }
  #type {
    margin-top: 0;
  }
  #center-post {
    width: 100%;
    height: auto;
    overflow-y: inherit;
  }
  #study-calendar {
    margin-top: 0;
  }
}
</style>
