<template>
  <div id="app">
    <Spinner />
    <Nav id="navigation" />
    <router-view></router-view>
  </div>
</template>
<script>
import './assets/css/static/style.css';

import Nav from './components/header/Nav.vue';
import Spinner from './components/etc/Spinner.vue';

// 채팅 알림 관련
//소켓 설정
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
// chat api
import * as chatApi from '@/api/chat';

export default {
  name: 'app',
  components: {
    Nav,
    Spinner,
  },
  created() {
    this.connect();
  },
  data() {
    return {
      // socket 재연결 막기
      stompClient: null,
      // 알림 메세지
      received: '',
    };
  },
  methods: {
    // 소켓 연결하기
    connect() {
      if (this.$router.currentRoute.name === "ChatPage") {
        return
      } else {
      console.log(this.BASEURL)
      const serverURL = 'http://i4c109.p.ssafy.io/api/ws';
      // const serverURL = 'http://localhost:8000/ws';
      let socket = new SockJS(serverURL);
      if (this.stompClient != null) {
        this.stompClient.disconnect();
        this.stompClient = Stomp.over(socket);
      } else {
        this.stompClient = Stomp.over(socket);
      }
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          console.log('소켓 연결 성공', frame);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          this.stompClient.subscribe('/notice/' + String(localStorage.userId), (res) => {
            console.log('구독으로 받은 메시지 입니다.', res.body);
            this.received = JSON.parse(res.body);

            // 알림이 들어오면 토스트 띄워주기
            this.$toast.open({
              message: `${this.received.opp_nickName}님의 메세지`,
              type: 'info',
              duration: 3000,
              position: 'top-right',
            });
          });
        },
        (error) => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
        }
      );
    }
    }
  },
};
</script>

<style scoped>
#navigation {
  position: relative;
  z-index: 50;
}
</style>
