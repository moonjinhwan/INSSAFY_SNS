<template>
  <!-- <v-app class="main-bg-color"> -->
    <!-- <v-main class="grey lighten-3"> -->
      <v-container
        class="chat-page"
      > 
        <v-row>
          <!-- 왼쪽 채팅방 리스트 -->
          <v-col 
            class="col-12 col-sm-5"
            v-show="!ResponsiveSize.isMobile || !isInRoom"
            id="container" 
          >
            <!-- 채팅방 리스트 타이틀 -->
            <div id="title">채팅방 목록</div>
            <!-- 채팅중인 대화상대 목록 -->
            <v-col class="my-list">
              <ChatRoomList
                v-for="(chatList, idx) in chatLists"
                :key="idx"
                :chatList="chatList"
                class="ani-hover"
              />
            </v-col>
          </v-col>
          <!-- 채팅방 부분 -->
          <v-col
            id="right"
            class="col-12 col-sm-7"
            v-show="!ResponsiveSize.isMobile || isInRoom"  
          >
            <ChatRoom />
          </v-col>
        </v-row>
      </v-container>
    <!-- </v-main> -->
  <!-- </v-app> -->
</template>

<script>
// 왼쪽 채팅 목록
import ChatRoomList from "@/components/openchat/ChatRoomList.vue"
// 오른쪽 채팅방 부분
import ChatRoom from "@/components/openchat/ChatRoom.vue"
// style 적용
import '@/assets/css/static/style.css';
// chat api
import * as chatApi from "@/api/chat"

export default {
  
  // // 알람이 안오게끔 만들기
  // beforeRouteLeave (to, from, next) {
  //   // 소켓을 제거해준다
  //   const serverURL = 'http://i4c109.p.ssafy.io/api/ws';
  //   // const serverURL = 'http://localhost:8000/ws';
  //   this.stompClient.unsubscribe(`/notice/` + String(localStorage.userId), (res) => {
  //     console.log('구독취소', res.body)
  //   })
  // },
  // // 알람이 안오게끔 만들기
  // beforeRouteUpdate (to, from, next) {
  //   // 소켓을 제거해준다
  //   const serverURL = 'http://i4c109.p.ssafy.io/api/ws';
  //   // const serverURL = 'http://localhost:8000/ws';
  //   this.stompClient.unsubscribe(`/notice/` + String(localStorage.userId), (res) => {
  //     console.log('구독취소', res.body)
  //   })
  // },
  name:'ChatPage',
  components: {
    ChatRoomList,
    ChatRoom,
  },
  // 뷰 인스턴스 제거될 때 resize 호출
  beforeDestroy () {
      if (typeof window === 'undefined') return

      window.removeEventListener('resize', this.onResize, { passive: true })
  },
  created() {
    this.getChatList()
  },
  mounted () {
    // resize 실시해서 현재 화면 크기 확인
    this.onResize()

    window.addEventListener('resize', this.onResize, { passive: true })
  },
  watch: {
    // 받고, 주면 갱신
    // 소켓 통신으로 받은 메세지들
    socketMsg: 'getChatList',
    isSend: 'getChatList',
  },
  computed: {
    // 채팅방 들어갔는지 확인
    socketMsg() {
      return this.$store.state.chat.socketMsg
    },
    isInRoom() {
      return this.$store.state.chat.isInChatRoom
    },
    isSend() {
      return this.$store.state.chat.isSend
    },
    // 소켓 연결 끊기 사용 - notice
    stompClient() {
      return this.$store.state.chat.stompClient
    },
  },
  data() {
    return {
      // 모바일 화면 체크 mobile화면인지, 사이즈 이용할 값
      ResponsiveSize: {
        isMobile: false, 
        viewSize: 0,
      },
      // 채팅 중인 상대 목록 정보
      chatLists: [],
    }
  },
  methods: {
    // 현재 활성화된 기기에 따라 flag 변경
    onResize() {
      this.ResponsiveSize.isMobile = window.innerWidth < 426;
      this.ResponsiveSize.viewSize = window.innerWidth;
    },
    // 채팅 목록 가져오기
    getChatList() {
      chatApi.getChatList({user_id:String(localStorage.userId)})
        .then(res => {
          // console.log(res)
          this.chatLists = res.data.roomInfo
          console.log(this.chatLists)
        })
        .catch(err => {
          console.error(err)
        })
    },
    // // Notice 지우기
    // deleteNotice() {
    //   console.log(this.$router.currentRoute.name === "ChatPage", 'here')
    //   // 현재페이지 오면 꺼주자
    //   if (this.$router.currentRoute.name === "ChatPage") 
    //   {
    //     this.stompClient.unsubscribe(`/notice/` + String(localStorage.userId), (res) => {
    //     console.log('구독취소', res.body)
    //   }) 
    //   }
    // },
  },
}
</script>

<style scoped>
.chat-page {
  background-color: #ebebe9;
  max-height: 90vh;
  margin: 20px auto;
}
/* 전체 폰트 */
#container {
  font-family: 'Noto Sans KR', sans-serif !important;
  box-shadow: var(--basic-shadow-s) !important;
  border-radius: 15px;
  background-color: var(--basic-color-bg2) !important;
  min-height: calc(100vh - 82px);
}
/* 제목 */
#title {
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1em;
}
/* 내 대화상대 목록 리스트 */ 
.my-list {
  overflow-y: scroll;
}
/* 오른쪽 채팅방 부분 */
#right {
  padding: 0px 0px 0px 8px;
  min-height: calc(100vh - 82px);
}
/* z-index 조절 */
.front {
  z-index: 1;
  position: absolute;
}
.back {
  z-index: -1;
  position: absolute;
  margin: auto;
}
.ani-hover {
  transition: transform 0.5s ease;
}
.ani-hover:hover {
  transform: scale(1.04);
}
/* toast 알림 */
.v-toast__item {
  display: none !important;
  z-index: -10 !important;
}
</style>
