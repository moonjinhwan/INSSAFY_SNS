<template>
  <div>
    
    <p @click="show" v-if="!isShow" style="cursor:pointer">{{this.voteDto.vote_name}}</p>
    <div id="vote-container">
      <vue-poll :showResults="showResult" v-bind="options" @addvote="addVote" v-if="isShow"/>
      <div class="btn-set">
        <button class="p-button-cancel r-desc" @click="voteDelete" v-if="isShow && isManager">삭제</button>
        <div class="binkan"></div>
        <button class="p-button r-desc" @click="show" v-if="isShow">close</button>
      </div>

    </div>
  </div>
</template>

<script>
import {get_voteDetail,vote_select,vote_delete} from '@/api/addfunc'
import VuePoll from 'vue-poll'

export default {
  name:'VoteItem',
  props:{
    voteId:Number,
    isManager:Boolean,
    inBoard:Boolean,
  },
  components:{
    VuePoll
  },
  data(){
    return {
      voteDto:{},
      voteItemList:[],
      dialog: false,
      isShow:false,
      options: {
        question: '',
        answers: [
        ]
      },
      isVoted:0,
    }
  },
  computed:{
    showResult(){
      if(this.isVoted>=1){
        return true
      }else{
        return false
      }
    },
    flag(){
      return this.$store.state.addfunc.flagWrite
    }
  },
  watch:{

  },
  created(){
    this.getVoteDetail()
  },
  methods:{
    getVoteDetail(){
      get_voteDetail(this.voteId,localStorage.userId)
      .then(res=>{
        this.voteDto = res.data.voteDto
        this.voteItemList = res.data.voteItemList
        this.options.question = this.voteDto.vote_name
        this.voteItemList.forEach((item,idx)=>{
          this.options.answers.push({
            value:Number(idx),
            text:item.vote_item_name,
            votes:Number(item.vote_item_count),
            id:Number(item.vote_item_id)
          })
        })
        this.isVoted = res.data.isVoted
        console.log(this.isVoted)
      })
    },
    addVote(obj){
      vote_select(localStorage.userId,this.options.answers[obj.value]['id'])
      .then(res=>{
        // console.log(res)
        this.isVoted = 1 
        this.$store.dispatch('addfunc/flagWrite')
      })
      .catch(err=>{
        console.log(err)
      })
      console.log('You voted ' + obj.value + '!');
    },
    //투표 보여주기
    show(){
      if(!this.inBoard){
        alert('구독후 이용할 수 있어요 :)')
      }else{
        this.isShow = !this.isShow
      }
    },
    // 투표삭제
    voteDelete(){
      console.log(this.voteId)
      vote_delete(Number(this.voteId))
      .then(res=>{
        console.log(res)
        this.isShow = !this.isShow
        this.$store.dispatch('addfunc/flagWrite')
      })
    }
  }

}
</script>

<style scoped>
.btn-secondary{
  color:#000 !important;
  border: 0px;
}
#vote-container {
  font-family: 'Noto Sans KR', sans-serif !important;
  border-radius: 15px !important;
  /* background-color: var(--basic-color-bg2) !important; */
  width: 110%;
  transform: translate(-13px,0);
  padding: 0 10px;
}
.qst{
  font-size: 1.5rem !important;
}

/* 투표 내 */
.btn-set{
  display: flex;
  justify-content: space-between;
}
.binkan{
  width:0.5px;
}
.p-button {
  margin-left: 5px;
  margin-top: 3px;
  font-size: 14px;
  padding: 4px 8px;
  border: 1px solid #000;
  border-radius: 30px;
  transition: background-color 0.3s, color 0.3s ease;
}
.p-button:hover,
.p-button:active {
  color: #fff;
  background-color: #0b2945 !important;
}
.p-button-cancel {
  margin-left: 5px;
  margin-top: 3px;
  font-size: 14px;
  padding: 4px 8px;
  border: 1px solid #000;
  border-radius: 30px;
  transition: background-color 0.3s, color 0.3s ease;
}
.p-button-cancel:hover,
.p-button-cancel:active {
  color: #fff;
  background-color: #aa2610 !important;
}
</style>