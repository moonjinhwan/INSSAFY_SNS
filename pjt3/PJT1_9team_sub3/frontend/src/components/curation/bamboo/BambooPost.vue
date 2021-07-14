<template>
  <div class="post">
    <div class="post-header">
      <div class="username-date-option">
        <div class="user-name-date">
          <div>
            {{post.writer_nickname}}
          </div>
          <div class="post-date">{{date}}</div>
        </div>
      </div>
      <b-dropdown id="dropdown-left" class="user-name" variant="link" toggle-class="text-decoration-none" no-caret>
        <template #button-content>
          <v-btn icon x-small fab>
            <v-icon dark>
              mdi-dots-vertical
            </v-icon>
          </v-btn>
        </template>
        <!-- <b-dropdown-item ><PostModify :post="post"/></b-dropdown-item> -->
        <!-- <b-dropdown-item v-if="flagWriter" ><bambooModify :post="post"/></b-dropdown-item> -->
        <b-dropdown-item v-if="flagWriter" @click="bamboo_delete">삭제</b-dropdown-item>
        <b-dropdown-item id="declare" v-if="!flagWriter"  @click="report">신고</b-dropdown-item>
      </b-dropdown>
    </div>
    <div class="post-body">
      <div class="title f-text b-desc">{{post.bamboo_title}}</div>
      <div class="description r-desc">{{post.bamboo_description}}</div>
      <div>
        <img id="description-image" v-if="post.bamboo_image" :src="post.bamboo_image" alt="이미지 미리보기...">
      </div>
    </div>
    <!-- <div class="post-footer">
      <div v-if="post.post_like>=10">
        <div class="post-like" @click="postLike" v-if="flagLike"  style="z-index: 1; position:relative; left:37.64px"><b-icon icon="emoji-smile" aria-hidden="true"></b-icon> {{post.post_like}}</div>
      </div>
      <div v-else>
        <div class="post-like" @click="postLike" v-if="flagLike"  style="z-index: 1; position:relative; left:28.64px"><b-icon icon="emoji-smile" aria-hidden="true"></b-icon> {{post.post_like}}</div>
      </div>
      <div class="post-like" @click="postLike" v-if="flagLike"><b-icon icon="emoji-smile-fill" aria-hidden="true" color="#AA2610"></b-icon> {{post.post_like}}</div>
      <div class="post-like" @click="postLike" v-if="!flagLike"><b-icon icon="emoji-smile" aria-hidden="true"></b-icon> {{post.post_like}}</div>
      <div class="post-comment"><b-icon icon="chat" aria-hidden="true"></b-icon> {{post.comment_count}}</div>
      <div class="post-bookmark" @click="postBookmark" v-if="flagBookmark"><b-icon icon="bookmark-fill" aria-hidden="true"></b-icon></div>
      <div class="post-bookmark" @click="postBookmark" v-if="!flagBookmark"><b-icon icon="bookmark" aria-hidden="true"></b-icon></div>
    </div> -->
  </div>
</template>

<script>
// import bambooModify from '@/components/curation/bamboo/bambooModify'
import timeForToday from '@/plugins/timeForToday'

export default {
  name:"Post",
  components:{
    // bambooModify
  },
  props:{
    post:Object
  },
  data() {
    return {
    }
  },
  computed:{
    date(){
      return timeForToday(this.post.bamboo_date)
    },
    flagWriter(){
      return this.post.user_id===localStorage.userId
    }
  },
  
  methods:{
    openToast: function(msg, type) {
      this.$toast.open({
        message: msg,
        type: type,
      });
    },
    report(){
      this.openToast(`비방/비하 등 유해하다고 판단되어 신고접수하였습니다.`, 'warning');
    },
    bamboo_delete(){
      const params = {
        bamboo_id:this.post.bamboo_id,
        login_id:localStorage.userId
      }
      this.$store.dispatch('bamboo/deleteBamboo',params)
    },
    bamboo_modify(){

    }
  }
}
</script>

<style>
.post{
  display: inline-block;
  margin-top:1rem !important;
  margin-bottom: 0rem !important;
  background-color: #fff;
  padding: 0.8rem;
  border-radius:10px;
  height:auto;
  width:100%;
  box-shadow: var(--basic-shadow-w);
}
.post .post-header{
  margin-bottom: 0.5rem;
  display: flex;
  flex-direction: row;
}
.post .post-body{
  /* height:auto; */
}
#description-image{
  margin: 0 auto 1% auto;
  max-width: 100%;
}
.post .post-footer{
  display:flex;
  flex-direction: row;
  justify-content: flex-end;
  margin-top:1rem;
}
.post-comment{
  margin-left: 0.5rem;
}
.post-bookmark{
  margin-left:0.5rem;
}
.post .title{
  font-weight: bold;
  font-size:1.5rem;
  margin-top: 0.5rem;
  margin-bottom: 1rem;
}
.username-date-option{
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  width:100%;
}
.user-profile-img{
  margin-right: 0.5rem;
}
.user-name button{
  padding:0 !important;
  color:black;
  transform:translate(3px,-6px)
}
.user-name button:hover{
  color:black;
  text-decoration: none;
}
.dropdown-item{
  padding:0 0.3rem !important;
}
.dropdown-menu {
   min-width: 3.1rem !important;
   transform:translate(3px,-6px);
}
#declare{
  color: var(--basic-color-new)
}
.user-name-date{
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin: auto 0;
}
@media screen and (max-width: 576px) {
  .post{
  width:100%;
}
}
</style>