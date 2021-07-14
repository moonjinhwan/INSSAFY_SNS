<template>
  <v-container>
    <v-row id="container">
      <v-col cols="12">
        <!-- 날짜 입력 -->
        <div :class="{ textYou: !isMine, textMine: isMine}">
          <div class="date">{{ sendDate }}</div>
          <div
          :class="{ you: !isMine, mine: isMine}">
            {{ message.msg }}
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
// 날짜 지정
import timeForToday from '@/plugins/timeForToday'


export default {
  name: 'Message',
  props: {
    message:Object,
  },
  computed: {
    isMine() {
      if (this.message.user_id === String(localStorage.getItem('userId'))) { return true } else { return false }
    },
    sendDate() {
      return timeForToday(this.message.date)
    },
  },
  data() {
    return {
    }
  },
  methods: {
  },
}
</script>

<style scoped>
/* 전체 폰트 */
#container {
  font-family: 'Noto Sans KR', sans-serif !important;
}
/* message 속성(상대방) */
.you {
  display: inline;
  padding: 0.4em 0.8em;
  border-radius: 0.7em 0.7em 0.7em 0;
  background-color: var(--basic-color-bg);
  color: var(--basic-color-fill);
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