<template>
  <v-card id="container">
    <div id="post-detail">
      <!-- 포스트 디테일 헤더 부분 -->
      <div id="contents">
        <div id="header">
          <!-- 클릭시 드롭다운 -->
          <v-menu
            bottom
            left
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                depressed
                text
                v-bind="attrs"
                v-on="on"
                class="px-0"
              >
                <div id="header-user-info">
                  <!-- 프로필 사진 연결하기 -->
                  <v-avatar size="40">
                    <Profile id="profile-image" :getUserImage="image"/>
                  </v-avatar>
                  <!-- 작성자이름, 작성일자 -->
                  <div id="header-info">
                    <div>
                    {{ comment.user_nickname }}
                    </div>
                    <div>{{ date }}</div>
                  </div>
                </div>
              </v-btn>
            </template>
            
            <v-list>
              <!-- 프로필 보기 -->
              <v-list-item-group>
                <v-list-item>
                  <v-list-item-title>
                    Profile 보기
                  </v-list-item-title>
                </v-list-item>
                <!-- 메세지 보내기 -->
                <v-list-item @click="onChat" v-if="!flagWriter">
                  <v-list-item-title>
                    메세지 보내기
                  </v-list-item-title>
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </v-menu>
        </div>
        <div class="comment-description" @mouseover="showBtn" v-if="!Edit">
          {{commentDescription}}
        </div>
        <!-- 내 댓글 수정시 나오는 input -->
        <div
          class="comment-description"
          v-if="Edit"
        >
          <v-text-field
            dense
            label=""
            v-model="tempComment"
          ></v-text-field>
        </div>
      </div>
      <div>
        <!-- 내 댓글 수정, 삭제 버튼 -->
        <div 
          id="btnComment" 
          v-if="!Edit"
          class="modify-btn"
        >
          <div @click="btnCommentModify">수정</div>
          <div @click="btnCommentDelete">삭제</div>
        </div>
        <!-- 수정 중 완료, 취소 버튼 -->
        <div 
          v-if="Edit"
          class="modify-btn"
        >
          <button @click="submit">Edit</button>
          <button @click="cancel">cancel</button>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script>
import * as commentApi from '@/api/comment'

import timeForToday from '@/plugins/timeForToday'
import deepClone from '@/plugins/deepClone'

// 채팅방 api
import * as chatApi from '@/api/chat';

// 프로필 이미지
import Profile from '@/components/etc/OtherProfile';
// 스타일 적용
// import '@/assets/css/static/style.css';

export default {
  name:"Comment",
  components: {
    Profile,
  },
  props:{
    comment:Object,
    postUserId:String,
  },
  data(){
    return {
      Edit:false,
      tempComment:'',
    }
  },
  computed:{
    date(){
      return timeForToday(this.comment.comment_date)
    },
    commentDescription(){
      return this.comment.comment_description
    },
    // 채팅을 위한 flag(나 아닌 사람만 대화 가능)
    flagWriter() {
      return this.comment.user_id === String(localStorage.userId);
    },
    image(){
      return this.comment.user_image
    }
  },
  mounted(){
    
  },
  methods:{
    showBtn(e) {
      // 댓글쓴 사람이랑 로그인한 사람이랑 같아야, 다음 버튼이 보인다.
      function sleep(ms) {
        return new Promise((r) => setTimeout(r, ms))
      }
      if(this.comment.user_id===localStorage.userId){
        e.path[2].querySelector('#btnComment').style.visibility="visible"
        sleep(3000).then(() => {

          if(typeof e === Object || !Object.keys(e.path[2].querySelector('#btnComment')).includes('style'))
            e.path[2].querySelector('#btnComment').style.visibility="hidden"
        })
      }
      
    },
    btnCommentDelete() {
      commentApi.comment_delete({login_id:localStorage.userId, comment_id:this.comment.comment_id})
        .then(res=>{
          if(res.data.message==='No Permission'){
            alert('구독 후에 이용가능합니다.')
          }else{
            this.$store.dispatch('comment/isWriteFlag')
          }
        })
        .catch(err=>{
          console.log(err)
        })
    },
    btnCommentModify() {
      this.tempComment = this.commentDescription
      this.Edit = !this.Edit;
    },
    cancel(){
      this.tempComment = this.commentDescription
      this.Edit = !this.Edit
    },
    submit(){
      
      // 아직 수정부분은 완료되지 않았습니다
      let params = {}
      params.commentDto = deepClone(this.comment)
      params.commentDto.comment_description = this.tempComment
      params.login_id = String(localStorage.userId)
      delete params.commentDto.user_nickname
      commentApi.comment_modify(params)
        .then(res=>{
          // console.log(res.data.message)
          if(res.data.message==='No Permission'){
            alert('구독 후에 이용가능합니다.')
          }else{
            this.$store.dispatch('comment/isWriteFlag')
            this.cancel()
          }
        })
        .catch(err=>{
          console.log(err)
        })
    },
    // 채팅으로 이동
    onChat() {
      // 중복 처리 해줘야 한다 - 있으면 기존껄로 없으면 만들기
      const params = {
        my_id: localStorage.getItem('userId'),
        opp_id: this.comment.user_id,
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
  }
}
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
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  margin: 0;
  padding: 1% 2%;
}
/* 프로필 이미지 */
#profile-image {
  width: 100%;
  height: 100%;
}
/* 댓글 작성자 정보, 댓글 내용 */
#contents {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  width: 90%;
}
/* 사용자 정보 */
#header {
  margin: 1%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
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

/* 게시글 내용 */
#description {
  margin: 0 0 0 1%;
  font-size: 14px;
}
/* 보여주는 부분에서 이용 */
#btnComment{
  visibility:hidden;
}
/* 댓글 내용 부분 margin */
.comment-description {
  overflow: auto;
  width: 100%;
  height: 40px;
  align-self: center;
  margin-left:10px;
  margin-top:10px;
}
/* 댓글 조작 버튼 */
.modify-btn {
  display: flex;
  flex-direction: column;
  cursor: pointer;
}
</style>
