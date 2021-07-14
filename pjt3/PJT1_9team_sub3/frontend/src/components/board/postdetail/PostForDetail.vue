<template>
  <v-card id="container">
    <div id="post-detail">
      <!-- 포스트 디테일 헤더 부분 -->
      <div id="header">
        <!-- 클릭시 드롭다운 -->
        <v-menu bottom left>
          <template v-slot:activator="{ on, attrs }">
            <v-btn depressed text v-bind="attrs" v-on="on" class="px-0">
              <div id="header-user-info">
                <!-- 프로필 사진 연결하기 -->
                <div>
                  <Profile id="profile-image" :getUserImage="image"/>
                  
                </div>
                <!-- 작성자이름, 작성일자 -->
                <div id="header-info">
                  <div>{{ nickname }}</div>
                  <div>{{ date }}</div>
                </div>
              </div>
            </v-btn>
          </template>

          <v-list>
            <!-- 프로필 보기 -->
            <v-list-item-group>
              <!-- 메세지 보내기(나와 채팅 금지) -->
              <v-list-item @click="onChat" v-if="!flagWriter">
                <v-list-item-title>
                  메세지 보내기
                </v-list-item-title>
              </v-list-item>
              <!-- 관리자 권한 주기-->
              <v-list-item v-if="flagAuthority" @click="updateManager">
                <v-list-item-title>
                  권한 주기
                </v-list-item-title>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-menu>

        <!-- 수정 삭제 신고 버튼과 판매상태 정보 -->
        <div id="header-right">
          <!-- 판매정보 부분 -->
          <div v-if="flagComponent.state" @click="changeState">
            <v-chip v-if="this.post.post_state === 0" id="state-sale">
              판매중
            </v-chip>
            <v-chip v-else-if="this.post.post_state === 1" id="state-book">
              예약중
            </v-chip>
            <v-chip v-else id="state-complete">
              판매완료
            </v-chip>
          </div>
          <!-- 수정,삭제,신고 -->
          <div>
            <v-menu bottom left>
              <template v-slot:activator="{ on, attrs }">
                <v-btn icon x-small fab v-bind="attrs" v-on="on">
                  <v-icon dark>mdi-dots-vertical</v-icon>
                </v-btn>
              </template>

              <v-list>
                <!-- 수정 -->
                <v-list-item-group>
                  <v-list-item v-if="flagWriter">
                    <v-list-item-title>
                      <PostModify :post="post" />
                    </v-list-item-title>
                  </v-list-item>
                  <!-- 삭제 -->
                  <!-- <v-list-item v-if="flagWriter" @click="postDelete"> -->
                  <v-list-item v-if="flagWriter || isManager" v-b-modal.modal-delete>
                    <v-list-item-title>
                      삭제
                    </v-list-item-title>
                  </v-list-item>
                  <b-modal id="modal-delete" title="🗑" size="sm" centered @ok="postDelete">
                    <p class="my-4">포스트를 삭제하시겠어요?</p>
                    <template #modal-footer="{ok}">
                      <b-button variant="delete" @click="ok()">
                        삭제하기
                      </b-button>
                    </template>
                  </b-modal>
                  <!-- 신고 -->
                  <v-list-item v-if="!flagWriter" @click="report">
                    <v-list-item-title>
                      신고
                    </v-list-item-title>
                  </v-list-item>
                </v-list-item-group>
              </v-list>
            </v-menu>
          </div>
        </div>
      </div>

      <!-- 포스트 제목 -->
      <div id="body">
        <div id="title">
          <!-- 중고장터용(지역) -->
          <div v-if="flagComponent.headerMarket">
            <v-chip outlined pill color="#695C4C" class="mr-3">
              {{ this.post.post_header }}
            </v-chip>
          </div>
          <div>{{ post.post_title }}</div>
        </div>
        <!-- 게시글 내용 -->
        <div id="description">
          <p id="sentence" v-for="(d,idx) in descriptions" :key="idx">
            {{d}}
          </p>
          <!-- {{ post.post_description }} -->
        </div>
        <div>
          <img id="description-image" v-if="post.post_image" :src="post.post_image" alt="이미지 미리보기..." />
        </div>
      </div>

      <!-- 게시글 관련 이미지/댓글/좋아요 들어갈 부분 -->
      <div id="actions">
        <!-- 댓글 수 -->
        <div id="bottom-comment-like">
          <div id="bottom-comment">
            <v-icon middle class="mr-1">mdi-comment-outline</v-icon>
            <span>{{ commentCount }}</span>
          </div>
          <!-- 좋아요 -->
          <div id="bottom-like">
            <button @click="postLike">
              <!-- 좋아요 중 -->
              <v-icon middle v-if="flagLike" color="#FFC400" class="mr-1">mdi-emoticon-excited</v-icon>
              <!-- 좋아요 취소상태 -->
              <v-icon middle class="mr-1" v-else>mdi-emoticon-neutral-outline</v-icon>
            </button>
            <span>{{ countLike }}</span>
          </div>
        </div>
        <!-- 북마크 -->
        <div>
          <button @click="postScrap">
            <!-- 북마크 중 -->
            <v-icon middle v-if="flagScrap" color="#0B2945">mdi-bookmark</v-icon>
            <!-- 북마크 취소상태 -->
            <v-icon middle v-else>mdi-bookmark-outline</v-icon>
          </button>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script>
// 게시글 수정
import PostModify from '@/components/board/postdetail/PostModify';
// 프로필 이미지
import Profile from '@/components/etc/OtherProfile';

import * as postApi from '@/api/post';
import { imageDelete } from '@/api/main';
import timeForToday from '@/plugins/timeForToday';

// 스타일 적용
import '@/assets/css/static/style.css';

// 채팅방 api
import * as chatApi from '@/api/chat';
import { async } from 'regenerator-runtime';

//보드 api
import * as boardApi from '@/api/board'

export default {
  name: 'PostForDetail',
  components: {
    PostModify,
    Profile,
  },
  props: {
    post: Object,
    nickname: String,
    commentCount: Number,
    image:String
  },
  data() {
    return {
      // nickname:'',
      viewImage: null,
      // post:{},
      tempComment: '',

      // 추후에 연결해줘야하는 부분 - 이미지(프로필 사진)
      profileImg: 'https://avatars0.githubusercontent.com/u/9064066?v=4&s=460',
      // 채팅방 id
      chatRoomId: '',

      inBoard: '',
      isManager: false,
    };
  },
  computed: {
    flagAuthority(){
      return this.post.user_id != localStorage.userId &&this.isManager 
    },
    flagLike() {
      return this.$store.state.post.flagLike;
    },
    flagScrap() {
      return this.$store.state.post.flagScrap;
    },
    countLike() {
      return this.$store.state.post.countLike;
    },
    // countComment(){
    //   const commentList = this.$store.state.comment.commentList
    //   return commentList.length
    // },
    date() {
      return timeForToday(this.post.post_date);
    },
    flagWriter() {
      return this.post.user_id === localStorage.userId;
    },

    // 재사용의 핵심
    flagComponent() {
      let flag = {
        state: false,
        headerMarket: false,
        headerLearnShare: false,
      };

      if (this.$route.name === 'MarketPost') {
        flag.state = true;
        flag.headerMarket = true;
        return flag;
      } else if (this.$route.name === 'LearnShare') {
        flag.state = false;
        flag.headerLearnShare = true;
      }
      return flag;
    },
    // 띄어쓰기 만들기
    descriptions(){
      const re1 = /\n/
      if(re1.test(this.post.post_description)){
        return this.post.post_description.split('\n')
      }else{
        return [this.post.post_description]
      }
      
      // return this.post.post_description.split('\n')
    }
  },
  watch: {
    // '$route':'fetchData'
  },
  created() {
    // 보드 구독했는 지 여부와 관리자 여부 확인하기 !!
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
    // ===================

    // 이미지 하나만 추출했어요.
    var input = this.post.post_image;
    if (input && input[0]) {
      var reader = new FileReader();
      reader.onload = (e) => {
        this.viewImage = e.target.result;
      };
      reader.readAsDataURL(input[0]);
    } else {
      this.viewImage = null;
    }
  },
  methods: {
    // 신고하기
    openToast: function(msg, type) {
      this.$toast.open({
        message: msg,
        type: type,
      });
    },
    report(){
      this.openToast(`비방/비하 등 유해하다고 판단되어 신고접수하였습니다.`, 'warning');
    },
    // 권한주기
    updateManager(){
      
      boardApi.board_updateManager(this.post.user_id,Number(this.$route.params.board_id))
      .then(res=>{
        this.isManager=true
      })
      .catch(err=>{
        console.log(err)
      })
    },
    // user가 좋아요 버튼 클릭 시 vuex에서 flag 변화 + 서버와 연결
    postLike(e) {
      postApi
        .likePost({ user_id: localStorage.getItem('userId'), post_id: this.post.post_id })
        .then((res) => {
          if (res.data.message === 'No Subscription') {
            alert('구독 후에 이용가능합니다.');
          } else {
            this.$store.dispatch('post/postLike', this.flagLike);
          }
          // console.log(res)
        })
        .catch((err) => {
          console.error(err);
        });
    },

    // user가 스크랩 버튼 클릭 시 vuex에서 flag 변화 + 서버와 연결
    postScrap(e) {
      postApi
        .scrapPost({ user_id: localStorage.getItem('userId'), post_id: this.post.post_id })
        .then((res) => {
          if (res.data.message === 'No Subscription') {
            alert('구독 후에 이용가능합니다.');
          } else {
            this.$store.dispatch('post/postScrap');
          }
          // console.log(res)
        })
        .catch((err) => {
          console.error(err);
        });
    },

    // 채팅으로 이동
    onChat() {
      // 중복 처리 해줘야 한다 - 있으면 기존껄로 없으면 만들기
      const params = {
        my_id: localStorage.getItem('userId'),
        opp_id: this.post.user_id,
      };
      chatApi.createChatRoom(params)
        .then(res => {
            console.log(res)
            if (res.data.message==="fail") {
              // 대화한 적 있다라는 메세지 보여주기
              this.$toast.open({
                message: `기존의 대화방 이동`,
                type: 'success',
                duration: 1000,
                position: 'top-right',
              }); 
            } else {
                this.$toast.open({
                  message: `새 대화방 생성`,
                  duration: 1000,
                  position: 'top-right',
                })
              }
            // 그리고 라우터 변환
            this.$router.push({ name: 'ChatPage' });
        })
        .catch(err => {
          console.error(err)
        })
    },
          // let existChatRoom;
          // // 1. 실패한다면 이미 채팅방 존재
          // if (res.data.message === 'fail') {
          //   // 현재 가지고 있는 채팅방을 가져오자
          //   chatApi
          //     .getChatList({ user_id: String(localStorage.userId) })
          //     .then((res) => {
          //       const chatLists = res.data.roomInfo;

          //       for (let i = 0; i < chatLists.length; i++) {
          //         if (chatLists[i].opp_id === this.post.user_id) {
          //           this.chatRoomId = chatLists[i].roomId;
          //           break;
          //         }
          //       }
          //       // break => 채팅방 존재(vuex 변화)
          //       existChatRoom = {
          //         roomId: this.chatRoomId,
          //         opp_nickName: this.nickname,
          //         opp_id: this.post.user_id,
          //       };
          //       // vuex state 변화
          //       this.$store.dispatch('chat/isSelected', existChatRoom);
              //   // 그리고 라우터 변환
              //   this.$router.push({ name: 'ChatPage' });
              // })
              // .catch((err) => {
              //   console.error(err);
              // });
          // } else {
          //   // 2. 처음이라면 대화방 만들어주고 보내주기
          //   const newChatRoom = {
          //     roomId: res.data.roomId,
          //     opp_nickName: this.nickname,
          //     opp_id: this.post.user_id,
          //   };
          //   // state 변환
          //   this.$store.dispatch('chat/isSelected', newChatRoom);
        //   }
        // })

    // 판매상태 변경
    changeState() {
      if (this.post.user_id != localStorage.userId) {
        return;
      }

      let sellState;
      if (this.post.post_state === 0) {
        sellState = 1;
      } else if (this.post.post_state === 1) {
        sellState = -1;
      } else {
        sellState = 0;
      }

      postApi
        .modifyState(this.post.post_id, sellState, localStorage.userId)
        .then((res) => {
          console.log(res);
          this.$store.dispatch('post/isModifyFlag');
        })
        .catch((err) => {
          console.log(err);
        });
    },

    // 포스트 삭제
    async postDelete() {
      if (this.post.user_id != localStorage.userId) {
        return;
      }
      // 이미지가 있다면 이미지 삭제후 삭제!
      // 이미지가 없다면 그냥 삭제!
      try {
        if (this.post.post_image != '') {
          await imageDelete(this.post.post_image)
            .then((res) => {
              console.log('이미지 삭제 완료!');
            })
            .catch((err) => {
              console.log(err);
            });
        }

        await postApi
          .post_delete(Number(this.post.post_id), localStorage.userId)
          .then((res) => {
            const nowName = this.$route.name
            let returnName
            if(nowName==='StudyMainPost'){
              this.$router.push({ name: 'StudyMain'});
            }
            else if(nowName==='MarketPost'){
              returnName = 'Market'
            }else if(nowName==='LearnSharePost'){
              returnName = 'LearnShare'
            }else if(nowName==='StudyGroupPost'){
              returnName = 'Study'
            }else if(nowName==='RecruitmentPost'){
              returnName = 'Recruitment'
            }else{
              returnName = 'Board'
            }
            
            this.$router.push({ name: returnName, params: { board_id: this.post.board_id } });
          })
          .catch((err) => {
            console.log(err);
          });
      } catch (err) {
        console.log('PostForDetail- 포스트 삭제 에러');
        console.log(err);
      }
    },
  },
};
</script>

<style scoped>
/* 전체 폰트 */
#container {
  font-family: 'Noto Sans KR', sans-serif !important;
  background-color: #fcfcfc !important;
}
/* 전체 detail 담겨진 부분 */
#post-detail {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 0;
  padding: 1% 2%;
}
/* 프로필 이미지 */
#profile-image {
  width: 40px;
  height: 40px;
}
/* 전체 최상단(유저 & 판매상태 & 신고) */
#header {
  margin: 1%;
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
/* 사용자 정보 클릭시 드롭다운 연결 */
.v-menu__content {
  transform: translate(5px, 40px);
}
.v-list {
  padding: 0;
}

/* 프로필, 닉네임, 작성일  */
#header-user-info {
  display: flex;
  flex-direction: row;
  align-items: center;
}
/* 작성자 닉네임, 작성일 */
#header-info {
  height: 70%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-left: 0.6em;
}
/* 닉네임 */
#header-info > div:first-child {
  line-height: 110%;
  font-size: 14px;
  font-weight: 400;
}
/* 작성일자 */
#header-info > div:last-of-type {
  line-height: 110%;
  font-size: 12px;
  font-weight: 300;
}
/* 헤더의 오른쪽 - 판매정보, 신고버튼 */
#header-right {
  display: flex;
  flex-direction: row;
}
/* 삭제버튼 */
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
/* 판매정보 */
#sell-state {
  background-color: #0b2945;
  color: #fff;
  border-radius: 10%;
}
/* 게시글 제목 */
#title {
  margin: 0 0 1% 1%;
}
#state-sale {
  background-color: #0b2945;
  color: #fff;
  border-radius: 10%;
}
#state-book {
  background-color: #aa2610;
  color: #fff;
  border-radius: 10%;
}
#state-complete {
  background-color: #f9f9f9;
  color: #000;
  border-radius: 10%;
}
/* 게시글 바디*/
#body {
  width: 100%;
  min-height: 200px;
}
/* 게시글 제목 */
#title {
  margin: 2% 0 2% 1%;
  display: flex;
  flex-direction: row;
  font-size: 18px;
  font-weight: 600;
}
/* 게시글 내용 */
#description {
  margin: 0 0 1% 1%;
  font-size: 16px;
}
#description-image {
  margin: 0 auto 1% auto;
  font-size: 16px;
  max-width: 80%;
}
#sentence{
  margin:0px;
}
/* 댓글, 좋아요, 북마크 부분 */
#actions {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
/* 댓글 좋아요 부분 */
#bottom-comment-like {
  display: flex;
  flex-direction: row;
  align-items: center;
}
#bottom-comment {
  /* display: flex; */
  /* align-items: center; */
  margin-right: 5px;
}
#bottom-like {
  display: flex;
  align-items: center;
}
</style>
