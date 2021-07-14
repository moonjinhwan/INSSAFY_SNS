<template>
  <v-list id="list" three-line>
    <v-list-item 
      class="ma-auto"
    >
      <!-- 상대 프로필 이미지(임시 - 연결해줘야 함) -->
      <v-avatar>
        <Profile :getUserImage="chatList.opp_img"></Profile>
      </v-avatar>

      <v-list-item-content 
        class="ml-3 chat-partner"
        @click="selectThisChat(chatList)"
      >
        <!-- 상대방 닉네임 -->
        <v-list-item-title>{{ chatList.opp_nickName }}</v-list-item-title>
        <!-- 상대방 최근 메세지 -->
        <v-list-item-subtitle>{{ chatList.recentMsg }}</v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
    <v-divider></v-divider>
  </v-list>
</template>

<script>
// chat api
import * as chatApi from "@/api/chat" 

//소켓 설정
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';


export default {
  name:'ChatRoom',
  components:{
    Profile: () => import('@/components/etc/OtherProfile'),
  },
  props: {
    chatList: Object,
  },
  data() {
    return {
      // 소켓으로 받은 메세지
      receives: [],
    }
  },
  computed: {
    stompClient() {
      return this.$store.state.chat.stompClient
    },
    // 선택된 채팅방 정보
    selectChatRoom() {
      return this.$store.state.chat.selectedChatRoom
    },
    // 선택된 주인 정보
    selectOppId() {
      return this.$store.state.chat.selectedId
    },
  },
  methods: {
    // 현재 채팅방 메세지들 가져오기 - 초기 채팅방 가져올 때 한번만
    selectThisChat(chatList) {
      // 선택된 채팅방 없을 때는 채팅방 연결해주기
      if (!this.selectChatRoom) {
        console.log('들옴?')
        // vuex 정보를 갱신해서
        this.$store.dispatch('chat/isSelected', chatList)
        // 바로 연결을 해준다
        this.connect()
      }

      // 1. vuex !== 클릭한 채팅방: 연결 끊고 재 연결 시켜준다
      if (chatList.roomId !== this.selectChatRoom && this.selectChatRoom ) {
        // 채팅방 간 이동 구현
        this.$store.dispatch('chat/switchRoom')
        // 소켓 끊었다가 다시 연결
        this.reConnect()
        // vuex에 새로운 정보 갱신 - 지우고 => 다시 넣기
        this.$store.dispatch('chat/isNotSelected')
        this.$store.dispatch('chat/isSelected', chatList)
      }

      // 채팅했던 메세지 내용 불러오기
      const params = {
        endNUm: 1000,
        startNUm: 0,
        room_id: this.selectChatRoom,
        user_id: String(localStorage.userId),
        opp_id: this.selectOppId,
      }
      chatApi.getMessages(params)
        .then(res => {
          this.$store.dispatch('chat/getMessages', res.data.msgList)
        })
        .catch(err => {
          console.error(err)
        })

      // 채팅방 알림 초기화
      const payload = {
        my_id: String(localStorage.userId),
        opp_id: this.selectOppId,
      }
      
      chatApi.updateNotice(payload)
        .then(res => {
          console.log(res)
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 처음 채팅방 들어갈 때 확인
    connect() {
      const serverURL = 'http://i4c109.p.ssafy.io/api/ws';
      // const serverURL = 'http://localhost:8000/ws';

      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      this.$store.dispatch('chat/checkSocket', Stomp.over(socket))
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
      {},
      (frame) => {
        // 소켓 연결 성공
        this.connected = true;
        console.log('소켓 연결 성공', frame);
        // 서버의 메시지 전송 endpoint를 구독합니다.
        // 이런형태를 pub sub 구조라고 합니다.
        this.stompClient.subscribe('/message/' + this.selectChatRoom + '/' + String(localStorage.userId), (res) => {
          console.log('구독으로 받은 메시지 입니다.(연결)', res.body);
          const receive = JSON.parse(res.body);
          // vuex에 저장
          this.$store.dispatch('chat/pushMsg', receive)
          });
      },
      (error) => {
        // 소켓 연결 실패
        console.log('소켓 연결 실패', error);
        this.connected = false;
        alert('다시한번 클릭해주세요!')
        }
      );
    },
    // 소켓 재연결하기(끊었다가 다시 연결)
    reConnect() {
      const serverURL = 'http://i4c109.p.ssafy.io/api/ws';
      // const serverURL = 'http://localhost:8000/ws';
  
      let socket = new SockJS(serverURL);
      console.log(this.stompClient, '여기야여기')
      if (this.stompClient !== null) {
        // 끊었다가 재연결
        this.stompClient.disconnect();
        this.$store.dispatch('chat/checkSocket', null)
        // vuex 업데이트 후 재연결
        this.stompClient = Stomp.over(socket);
        this.$store.dispatch('chat/checkSocket', Stomp.over(socket))
        console.log(`소켓 연결을 재시도합니다. 서버 주소: ${serverURL}`);
        this.stompClient.connect(
          {},
          (frame) => {
            // 소켓 연결 성공
            this.connected = true;
            console.log('소켓 연결 성공', frame);
            // 서버의 메시지 전송 endpoint를 구독합니다.
            // 이런형태를 pub sub 구조라고 합니다.
            this.stompClient.subscribe('/message/' + this.oppRoomId + '/' + this.userId, (res) => {
              console.log('구독으로 받은 메시지 입니다.(재연결)', res.body);
              const received = JSON.parse(res.body);
              this.receives.push(received)
          });
        },
        (error) => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
          alert('다시한번 클릭해주세요!')
          }
        );
      }
    },
  },
}
</script>

<style scoped>
/* 전체 폰트 */
#list {
  font-family: 'Noto Sans KR', sans-serif !important;
  height: 100% !important;
  border: none;
  border-radius: 5px;
  color: var(--basic-color-key);
  text-shadow: 0 0px 1px var(--basic-color-fill3);
  background: #ebebe9 !important;
  box-shadow: var(--basic-shadow-s);
  border-radius: 15px !important;
  padding: 0px;
}
.chat-partner:hover {
  cursor: pointer;
}
</style>