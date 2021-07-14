<template>
  <v-card
    class="mx-auto
      my-0
      d-flex
      flex-column"
    color="F9F9F9"
  >
    <!-- 포스트 작성자 부분 -->
    <v-card-title class="d-flex flex-row align-center ma-0 py-2 pb-0">
      <v-list-item-avatar class="rounded-circle">
        <v-img
          class="elevation-6"
          alt="이미지"
          :src="post.boardImg"
        ></v-img>
      </v-list-item-avatar>
      <!-- 작성자이름, 작성일자 -->
      <div class="text-caption">
        <div>{{ post.user_nickname }}</div>
        <div>{{ date }}</div>
      </div>
      <!-- 판매상태 들어갈 부분 -->
       <v-btn-toggle
        group
        class="align-self-end 
          ma-2 ml-auto"
      >
        <v-chip
          style="background-color: #0B2945 !important;
          color: #fff;
          border-radius: 10%;"
        >
          {{ isState }}
        </v-chip>
      </v-btn-toggle>
    </v-card-title>
    <!-- 포스트 제목 -->
    <div class="card-body-custom" @click="goToDetail">
      <v-col
      class="font-weight-black"
    ><v-chip
      class="ma-2"
      color="primary"
      v-if="1"
    >
     머리말
      <!-- {{post.post_header}} -->
    </v-chip>{{ post.post_title }}</v-col>
    <v-col>
      {{ post.post_description }}
    </v-col>
    </div>
    
    <div id="card-footer">
      <div>좋아요수 : {{post.post_like}}</div>
      <div>댓글수 :</div>
    </div>
  </v-card>
</template>

<script>
import timeForToday from "@/plugins/timeForToday"
export default {
  name: 'ScrapPost',
  props:{
    post:Object
  },
  data() {
    return {
      isState: ['판매중', '거래중', '판매완료'],
    }
  },
  computed:{
    date(){
      return timeForToday(this.post.post_date)
    }
  },
  created(){
    console.log(this.post)
  },
  mounted(){
    this.showState()
  },
  methods: {
    // 작성글 삭제
    removeBoard() {
      console.log('작성글 삭제')
    },
    // 해당 보드로 이동(상세 주소 넘겨주기)
    moveToBoard() {
      this.$router.push({ name: 'Board' });
    },
    showState() {
      this.isState = this.isState[this.post.post_state]
    },
    goToDetail() {
      // params를 이용해서 데이터를 넘겨줄 수 있다.
      this.$router.push({ name: 'MarketPost', params: { post_id: this.post.post_id }})
      // this.$router.push({ name: 'Post', params: {post:this.post} });
    },
  },
}
</script>

<style scoped>
/* 이미지 흐리게 하기 */
.blur {
  filter: brightness(50%);
  z-index: 1;
}
.text {
  position: absolute;
  width: 100%;
  height: 100px;
  color: #FFFFFF;
  display: flex;
  flex-direction: column;
  text-align: center;
  justify-content: flex-start;
  align-items: center;
  z-index: 2;
}
/* 보드로 이동 */
.board-title {
  flex-grow: 100%;
  cursor: pointer;
}
/* 게시글 제목 넘치는 부분 처리 */
#post-title {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
/* 날짜 넘치는 부분 처리 */
#post-date {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
/* 댓글 내용 넘치는 부분 처리 */
#post-contents {
  font-size: 14px;
  line-height: 20px;
  max-height: 40px;
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
/* stepper 가로정렬로 */
.stepper {
}
#card-footer{
  display:flex;
  padding:0 15px 8px;
  justify-content: flex-end;
  
}
.card-body-custom{
  cursor:pointer;
}
</style>