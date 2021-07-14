<template>
  <v-card id="container">
    <div id="title">
    <!-- 체크리스트 제목 -->
    <div>Ranking</div>
    </div>
    <div v-if="!isEnough" class="empty">
      Oops! 데이터가 부족해요<br>보드를 더 활성화시켜볼까요?
    </div>
    <!-- 실제 랭킹 데이터 -->
    <v-container id="table" v-if="isEnough">
      <v-row dense id="th">
        <v-col>Rank</v-col>
        <v-col class="nickname">닉네임</v-col>
        <v-col>총 좋아요</v-col>
      </v-row>
      <v-row 
        id="tb"
        dense
        v-for="(rank, idx) in rankingList"
        :key="idx"
        class="ani-hover"
      >
        <!-- 메달 보여줄 부분 -->
        <v-col>
          <v-icon :color="rank.medal">{{ medal }}</v-icon>
        </v-col>
        <!-- 닉네임 -->
        <v-col class="nickname">{{rank.nickName}}</v-col>
        <!-- 총 좋아요 수 -->
        <v-col>{{rank.score}}</v-col>
      </v-row>
      <!-- 랭커 3명이 안됐을 때 보여줄 부분 -->
      <v-row 
        dense 
        v-if="count.length > 0"
        id="tf"
      >
        <div
          class="null ani-hover"
          v-for="(i) in count"
          :key="i"
        >당신이 다음 {{i}}등의 주인공!
        </div>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
// 추가기능 api
import * as addFuncApi from '@/api/addfunc';


export default {
  name: 'UserRank',
  data() {
    return {
      rankingList: [],
      // 랭킹 보여주기엔 데이터 부족할 때
      isEnough: true,
      // 메달 아이콘
      medal: 'mdi-medal',
      // 등 수 부족할 때 보여줄 부분
      count: [1, 2, 3],
    }
  },
  created() {
    this.getUserRankInBoard()
  },
  methods: {
    getUserRankInBoard() {
      // 보드 id
      const params = {
        board_id: Number(this.$route.params.board_id)
      }
      addFuncApi.getUserRank(params)
        .then(res => {
          console.log(res.status)
          // 조건 처리
          if (res.status === 204) {
            // 데이터가 부족해서 안가져와질 때
            this.isEnough = false;
          } else {
            this.rankingList= res.data.topUsers;
            // 메달 icon 생성
            const color = ['#ffd700', '#c0c0c0', '#8b4513'];
            
            // 반복문으로 배열 생성
            for (let i = 0; i < this.rankingList.length; i++) {
              // 메달색 넣기
              this.rankingList[i].medal = color[i]
              // 순위 넣기
              this.rankingList[i].rank = i + 1
            }
            // 빈 자리 채워주기
            this.count = (this.count.slice(this.rankingList.length))
          }
        })
        .catch(err => {
          console.error(err)
        })
    },
  },
}
</script>

<style scoped>
/* 전체 폰트 */
#container {
  font-family: 'Noto Sans KR', sans-serif !important;
  box-shadow: var(--basic-shadow-s) !important;
  border-radius: 15px;
  background-color: var(--basic-color-bg2) !important;
  margin: 5% 0;
  padding: 5% 0;
  /* min-height: 20vh; */
}
/* 제목 */
#title {
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1em;
  padding: 0 10%;
}
/* 랭킹 테이블 */
#table {
  height: 100%;
  margin: auto;
  padding: 0;
  text-align: center;
}
#th {
  font-weight: 600;
  font-size: 16px;
  margin: 25px 0 10px 0;
  color: var(--basic-color-key);
}
#tb {
  font-weight: 500;
  font-size: 14px;
  margin: 0;
}
/* hover animation */
.ani-hover {
  transition: transform 0.6s ease;
}
.ani-hover:hover {
  transform: scale(1.14);
}
/* 랭킹 인원 부족할 때 보여줄 부분 */
#tf {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
.null {
  margin: 10px 6px 5px 6px;
  font-size: 14px;
  font-weight: 300;
}
/* 데이터 부족해서 못 가져올 때 */
.empty {
  text-align: center;
  font-weight: 600;
  font-size: 16px;
  margin: 25px 0 10px 0;
  color: var(--basic-color-key);
  transition: transform 0.6s ease;
}

.empty:hover {
  transform: scale(1.04);
}
</style>