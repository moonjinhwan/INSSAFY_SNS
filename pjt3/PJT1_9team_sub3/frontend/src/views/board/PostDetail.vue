<template>
  <v-app id="main-bg">
    <v-main>
      <v-container id="container"
        class="pt-8"
      >
        <v-row dense>
          <button class="c-btn newmorphism" @click="goBack">
            뒤로 가기
          </button>
        </v-row>        
        <v-row dense>
          <v-col class="pb-0">
            <Post :post="post" :nickname="nickname" :commentCount="commentCount" :image="image"/>
          </v-col>    
        </v-row>
        <v-row dense>
          <v-col class="pt-0">
            <v-card
              class="my-2
                d-flex
                flex-column"
              color="#F9F9F9"
            >
            <!-- 입력창 부분 -->
            <v-spacer></v-spacer>
            <div id="input">
              <input
                id="comment"
                v-model="comment"
                placeholder="한마디!"
                @keypress.enter="createComment"
              >
              <v-btn
                id="btn-comment"
                @click="createComment"
              ><v-icon>mdi-send</v-icon>
              </v-btn>
            </div>
              <CommentList :comments="comments" :postUserId="this.post.user_id"/>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import Post from '@/components/board/postdetail/PostForDetail.vue'
import CommentList from '@/components/board/postdetail/CommentList.vue'

import * as commentApi from '@/api/comment';
import * as postApi from '@/api/post'

export default {
  name:'PostDetail',
  data() {
    return {
      comment: '',
      post:{},
      nickname:'',
      comments:[],
      commentCount:0,
      image:'',
    }
  },
  components:{
    Post,
    CommentList,
  },
  computed:{
    flagComment(){
      return this.$store.state.comment.flagComment
    },
    flagModify(){
      return this.$store.state.post.flagModify
    },
    // 여기가 재사용 핵심 중 하나 입니다. 돌아가기 버튼의 포인트를 정하는 곳이에요.
    flagRouterBack(){
      const name = this.$route.name
      if(name==="MarketPost"){
        return {
          name: "Market"
        }
      }else if(name==="LearnSharePost"){
        return {
          name: "LearnShare"
        }
      }else if(name==="RecruitmentPost"){
        return {
          name: "Recruitment"
        }
      }else{
        return {
          name:'Board', 
          params: {board_id: Number(this.$route.params.board_id)}
        }
      }
    }
  },
  watch:{
    flagComment:'fetchData',
    flagModify : 'fetchData'
  },
  created(){
    this.fetchData()
  },
  methods:{
    fetchData(){
      console.log('댓글수정했는데 이게 되나?')
      this.loading=true
      postApi.getPost({
        login_id:String(localStorage.getItem('userId')),
        post_id:Number(this.$route.params.post_id)
      })
      .then(res=>{
        if(res.data.message==="NULL"){
          this.$router.push({ name: 'PageNotFound'})
        }else{
          this.post = res.data.postDto
          this.nickname = res.data.writer_nickname
          this.image = res.data.writer_image
          
          //좋아요와 스크립트 여부는 vuex에 저장해놔야함...
          this.$store.dispatch('post/isLiked',res.data.isLiked)
          this.$store.dispatch('post/isScrapped',res.data.isScrapped)
          //그리고 댓글 리스트를 여기서 가져오니까 이것도 vuex에 저장해야 할 듯?
          this.comments = res.data.commentList
          this.commentCount = this.comments.length
          // this.$store.dispatch('comment/getCommentList',res.data.commentList)
          // 포스트 라이크 카운트, 댓글 카운트 도 vuex에!
          this.$store.dispatch('post/isLikeCount',this.post.post_like)
        }
        
      })
      .catch(err=>{
        console.log(err)
      })
    },
    createComment(){
      if(this.comment.trim()!=""){
        const params = {
          user_id: String(localStorage.userId),
          post_id: Number(this.$route.params.post_id),
          comment_description : String(this.comment)
        }
        this.comment=''
        commentApi.comment_create(params)
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
      }else{
        alert('빈칸이에요..')
      }
        
      // this.$store.dispatch('createComment',params)
    },
    // 뒤로가기 구현
    goBack() {
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
    },
  }
  
}
</script>

<style scoped>
#input{
  margin-bottom: 1px;
}
#comment{
  width:95%;
  padding: 5px 10px 5px 10px;
  height: 40px;
}
#btn-comment{
  height: 40px;
}
/* 전체 메인 배경색 */
#main-bg {
  background-color: #ebebe9 !important;
}
/* 뒤로가기 버튼 */
.c-btn {
  width: 100px;
  height: 50px;
  font-weight: 700;
  margin-bottom: 5px;
  border: none;
  border-radius: 20px;
  transition: all 0.6s ease !important;
}
.c-btn:hover,
.c-btn:active {
  background: #ebebe9;
  background: linear-gradient(145deg, #d4d4d2, #fbfbf9);
  box-shadow: 10px 10px 20px #b3b3b1, -10px -10px 20px #ffffff;
}
/* 댓글 입력 부분 */
#input {
  display: flex;
  flex-direction: row;
  min-height: 10px;
}
.v-text-field__details{
  min-height: 0px;
}
</style>
