<template>
  <v-card id="container">
    <div id="title">
    <!-- 체크리스트 제목 -->
    <div>투표</div>
    <!-- 수정버튼 -->
    <VoteDialog v-if="isManager"/>
    <!-- <v-icon id="edit-icon"
      large
      v-if="isManager"
    >
      mdi-plus-box
    </v-icon> -->
    </div>
    <VoteItem v-for="(voteId,idx) in votelist" :key="idx" :voteId="voteId" :isManager="isManager" :in-board="inBoard"/>
  </v-card>
</template>

<script>
import {get_voteList} from '@/api/addfunc'
export default {
  name: 'VoteList',
  components:{
    VoteDialog: () => import('@/components/addfunc/VoteDialog'),
    VoteItem: () => import('@/components/addfunc/VoteItem')
  },
  props:{
    inBoard:Boolean,
    isManager:Boolean,
  },
  data(){
    return {
      votelist:[],
    }
  },
  computed:{
    flagWrite(){
      return this.$store.state.addfunc.flagWrite
    }
  },
  watch:{
    flagWrite:'getVoteList'
  },
  created(){
    this.getVoteList()
  },
  methods:{
    getVoteList(){
      get_voteList(this.$route.params.board_id)
      .then(res=>{
        console.log(res)
        this.votelist = res.data.voteList
      })
      .catch(err=>{
        console.log(err)
      })
    }
  }
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
  padding: 5% 10%;
  min-height: 30vh;
}
/* 제목 */
#title {
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1em;
}
/* 편집 들어오는 버튼(아이콘) */
#edit-icon {
  cursor:pointer;
  color: #0B2945;
}
#edit-icon:hover {
  color: #AA2610;
}
</style>