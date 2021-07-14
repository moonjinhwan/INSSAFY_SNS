<template>
  <v-card
    id="post-box"
    class="
      d-flex img-wrap justify-content-between"
    color="F9F9F9"
  >
    <div class="d-flex align-items-center ml-3">
      <!-- <div class="ml-2 mr-2">
        <GradientGenerator class="myinfo-list" style="height: 50px; width:50px" v-if="user.user_image == null" :radius="radius" />
        <v-img src="user.user_image" height="50px" width="50px" class="myinfo-list blur" v-if="user.user_image != null"> </v-img>
      </div> -->
      <Profile :getUserImage="image" style="width:40px; height:40px;" class="mr-1"/>
      <div class="d-flex flex-column">
        <div id="post-title" class="font-weight-black">{{user.user_nickname}}</div>
      </div>
    </div>
    <!-- 포스트 제목 -->

    <!-- 포스트 글 내용 -->
    <v-card-actions id="btn-check">
      <v-list-item>
        <button @click="permit">승인</button>
        <button @click="reject">거절</button>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<script>
// import GradientGenerator from '@/components/etc/GradientGenerator';
import * as studyApi from '@/api/study'
import Profile from '@/components/etc/OtherProfile'

export default {
  name: "MyStudyGroup",
  data() {
    return {
      radius: '15px',
    }
  },
  props:{
    user:Object,
    isManager:Boolean
  },
  components: {
    // GradientGenerator,
    Profile,
  },
  computed:{
    image(){
      return this.user.user_image
    }
  },
  methods: {
    permit(){
      if(this.isManager){
        studyApi.studyRequestProcess( this.user.user_id,Number(this.$route.params.board_id),1)
        .then(res=>{
          this.$store.dispatch('study/isWriteFlag')
        })
        .catch(err=>{
          console.log(err)
        })
      }else{
        alert('매니저만 할 수 있어요.')
      }
    },
    reject(){
      if(this.isManager){
        studyApi.studyRequestProcess( this.user.user_id,Number(this.$route.params.board_id),-1)
        .then(res=>{
          this.$store.dispatch('study/isWriteFlag')
        })
        .catch(err=>{
          console.log(err)
        })
      }else{
        alert('매니저만 할 수 있어요.')
      }
    }
  },
  created(){
  
  }
}
</script>

<style scoped>
/* 해당 스터디로 이동시키는 부분 pointer */
#post-box {
  cursor: pointer;
  border-radius: 15px;
  box-shadow: var(--basic-shadow-c);
}
/* 이미지 흐리게 하기 */
.blur {
  filter: brightness(50%);
  z-index: 1;
}
.text {
  position: absolute;
  width: calc(100% - 20px);
  margin: 0 !important;
  height: 100px;
  color: #ffffff;
  display: flex;
  flex-direction: column;
  text-align: center;
  justify-content: flex-start;
  align-items: center;
  z-index: 2;
}

.v-list-item{
  display: flex;
  flex-direction: column;
  min-height:0px;
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
@media (max-width: 426px) {
  #post-date {
    font-size: 10px !important;
  }
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
.myinfo-list {
  border-radius: 15px;
  box-shadow: var(--basic-shadow-m);
}
</style>