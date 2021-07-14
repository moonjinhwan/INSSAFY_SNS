<template>
  <v-card id="chatroom-bg">
    <!-- 위쪽 툴바 부분 -->
    <v-toolbar
      color="#5C5C64"
      dark
      id="room-title"
    >
      <!-- 채팅 상대방 닉네임 보여줄 부분 -->
      <v-toolbar-title text-color="white">{{ oppNickName }}</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn icon @click="exitRoom">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-toolbar>
    <div id="chat-message">
      <div id="contents">
        <div v-if="this.messages" class="db">
          <Message 
            v-for="(message, idx) in messages.slice().reverse()"
            :key="idx"
            :message="message"
          />
        </div>
        <!-- 채팅방 내에 있을 때 보여줄 부분 -->
        <v-container id="socket">
        </v-container>
      </div>
      <!-- 채팅 입력 부분 -->
      <div id="input">
          <v-text-field
            solo
            dense
            hide-details=""
            id="comment"
            v-model="sendContents"
            @keypress.enter="sendMessage"
            placeholder="메세지를 입력해주세요"
          >
          </v-text-field>
          <v-btn 
            :class="{'enableSend': isPossibleChat}"
            @click="sendMessage"
          >
            <v-icon>mdi-send</v-icon>
          </v-btn>
      </div>
    </div>
  </v-card>
</template>

<script>
import Message from '@/components/openchat/Message.vue'
//소켓 설정
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
// chat api
import * as chatApi from "@/api/chat"
// 날짜 지정
import timeForToday from '@/plugins/timeForToday'

export default {
  name: "ChatRoom",
  components: {
    Message,
  },
  mounted() {

  },
  created() {
    // this.connect()
  },
  watch: {
    // 실시간 메세지 전달
    receiveMsg: 'receiveMessage',
    // 방 이동 flag
    changeRoom: 'exitRoom',
  },
  computed: {
    // 현재 활성화 채팅방 정보
    oppNickName() {
      if (this.$store.state.chat.selectedNickname) { return this.$store.state.chat.selectedNickname + '님과의 대화'} else { return '대화를 선택해주세요'}
    },
    oppRoomId() {
      return this.$store.state.chat.selectedChatRoom
    },
    oppUserId() {
      return this.$store.state.chat.selectedId
    },
    // 상대방 닉네임
    oppUserNickname() {
      return this.$store.state.chat.selectedNickname
    },
    // 채팅방 보낼 메세지 있는지 확인
    isPossibleChat() {
      return Boolean(this.sendContents.trim())
    },
    isInRoom() {
      return this.$store.state.chat.isInChatRoom
    },
    messages() {
      return this.$store.state.chat.selectedMessages
    },
    // 소켓 연결에서 이용
    stompClient() {
      return this.$store.state.chat.stompClient
    },
    // 소켓연결을 통해 받은 메세지
    receiveMsg() {
      return this.$store.state.chat.socketMsg
    },
    // 채팅방 1=> 채팅방 2 이동
    changeRoom() {
      return this.$store.state.chat.isSwitch
    }
  },
  data() {
    return {
      // 보낼 내용
      sendContents: '',
      // 현재 접속한 유저
      userId: String(localStorage.userId),
    }
  },
  methods: {
    receiveMessage() {
      // this.receiveMsg

      // 뿌려줄 res.data 태그
      // 1 - 전체 감쌀 부분
      const divReceive = document.createElement("div");
      // 속성 주기
      divReceive.setAttribute("style", 
      "display: flex; flex-direction: row-reverse; justify-content: flex-end; align-items: flex-end; width: 100%; padding: 12px 0;"
      );
      // 2- 날짜 부분
      const divReceiveDate = document.createElement("div");
      divReceiveDate.setAttribute("style", "font-size: 0.75rem; font-weight: 400; margin: 0 4px;");
      // 텍스트 요소 꼭 필요
      divReceiveDate.textContent = timeForToday(this.receiveMsg.date)
      divReceive.appendChild(divReceiveDate)
      // 3- 내용 부분
      const divReceiveMsg = document.createElement("div");
      divReceiveMsg.textContent = this.receiveMsg.msg
      divReceiveMsg.setAttribute("style", "display: inline; float: right; padding: 0.4em 0.8em; border-radius: 0.7em 0.7em 0.7em 0; background-color: var(--basic-color-bg); color: var(--basic-color-fill); max-width: 70%; margin-left: 4px;")
      divReceive.appendChild(divReceiveMsg)
      // 이를 id로 검색해서 해당 지점에 달아준다
      const target = document.getElementById("socket")
      target.appendChild(divReceive)
    },
    // 메세지 보내기
    sendMessage() {
      if (this.stompClient && this.stompClient.connected && this.isPossibleChat) {
      // 한국시간으로 설정
      const date = new Date();
      
      // 소켓 통신
      const message = {
        room_id: this.oppRoomId,
        user_id:String(localStorage.userId),
        // 상대방 아이디 넣기
        opp_id: this.oppUserId,
        msg: this.sendContents,
        date: date,
        opp_nickName: this.oppUserNickname,
      };
      console.log(message, '파라미터 변경')
      console.log('연결중 보내기')
      this.stompClient.send('/app/receive', JSON.stringify(message), {});
      
      this.$store.dispatch('chat/sendMessages')

      // 초기화(input)
      this.sendContents = ''
      
      // 뿌려줄 res.data 태그
      // 1 - 전체 감쌀 부분
      const div = document.createElement("div");
      // 속성 주기
      div.setAttribute("style", 
      "display: flex; flex-direction: row; justify-content: flex-end; align-items: flex-end; width: 100%; padding: 12px 0;"
      );
      // 2- 날짜 부분
      const divDate = document.createElement("div");
      divDate.setAttribute("style", "font-size: 0.75rem; font-weight: 400; margin: 0 4px;");
      // 텍스트 요소 꼭 필요
      divDate.textContent = timeForToday(message.date)
      div.appendChild(divDate)
      // 3- 내용 부분
      const divMsg = document.createElement("div");
      divMsg.textContent = message.msg
      divMsg.setAttribute("style", "display: inline; float: right; padding: 0.4em 0.8em; border-radius: 0.7em 0.7em 0 0.7em; background-color: var(--basic-color-key); color: #fff; max-width: 70%;")
      div.appendChild(divMsg)
      // 이를 id로 검색해서 해당 지점에 달아준다
      const target = document.getElementById("socket")
      target.appendChild(div)

      // 받는 작업도 활성화시켜주자
      // this.receiveMessage()
      }  else if (this.isPossibleChat) {
          let you;
          if (this.oppUserNickname) {
            you = this.oppUserNickname + '님을'
          } else { you = "대화상대를" }
          this.$toast.open({
            message: `Oops! ${you} 다시 클릭해주세요`,
            type: 'error',
            duration: 3000,
          })
        }
      // 연결이 안되었음을 알려준다
      else {
        return
      }
    },
    exitRoom() {
      // 활성화 채팅방 없애고 메세지도 지우기
      this.$store.dispatch('chat/isNotSelected')
      // 입력창 비워주기
      this.sendContents = ''
      // 작성된 글 지워주기
      // 현재 작성된 애들 선택
      const parent = document.getElementById("socket")
      // 반복문으로 제거
      while (parent.hasChildNodes()) {
        parent.removeChild(parent.firstChild);
      }       
      // 소켓 연결 해제하기
      if (this.stompClient !== null) {
        // 끊어주기
        this.stompClient.disconnect()
      }
    },
  },
}
</script>

<style scoped>
#chatroom-bg {
  background-color: #F9F9F9;
  font-family: 'Noto Sans KR', sans-serif !important;
  box-shadow: var(--basic-shadow-s) !important;
  border-radius: 15px 15px 5px 5px;
  padding: 0px 0px 0px 0px !important;
  height: 100%;
}

/* 채팅방 부분 */
#chat-message {
  height: calc(90vh - 64px);
  display: flex;
  flex-direction: column-reverse;
  overflow-y: scroll !important;
}
/* 채팅메세지 부분 */
#contents {
  margin-bottom: 25px;
}
/* DB에서 가져오는 부분 */
.db {
  order: 1;
}
/* 댓글 입력 부분 */
#input {
  display: flex;
  flex-direction: row;
  order: -1;
  width: 100%;
  position: absolute;
  background: #F9F9F9;
}
/* 활성화 버튼 */
.enableSend {
  background-color: #695c4c !important;
  color: #fff !important;
}
/* message 속성(상대방) */
.you {
  display: inline;
  padding: 0.4em 0.8em;
  border-radius: 0.7em 0.7em 0.7em 0;
  background-color: var(--basic-color-bg);
  color: var(--basic-color-fill);
  margin-left: 4px;
  max-width: 70%;
}
/* message 속성(나) */
.mine {
  display: inline;
  float: right;
  padding: 0.4em 0.8em;
  border-radius: 0.7em 0.7em 0 0.7em;
  background-color: var(--basic-color-key);
  color: #FFF;
  max-width: 70%;
}
/* 날짜 포함 전체 메세지 */
.textMine {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: flex-end;
  width: 100%;
}
.textYou {
  display: flex;
  flex-direction: row-reverse;
  justify-content: flex-end;
  align-items: flex-end;
  width: 100%;
}
/* 날짜 스타일 */
.date {
  font-size: 0.75rem;
  font-weight: 400;
  margin: 0 4px;
}
</style>