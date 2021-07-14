<template>
  <div>
    <div>
      <StudyGroup
        v-for="(group,idx) in groups"
        :key="idx"
        :group="group"
        :flagComponent="flagComponent"
        class="pa-2 ani-hover"
      />
    </div>
    <infinite-loading @infinite="infiniteHandler" spinner="waveDots">
      <div slot="no-more" style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px;">목록의 끝입니다 :)</div>
    </infinite-loading>
  </div>
</template>

<script scoped>
import StudyGroup from '@/components/curation/study/StudyGroup.vue';
// import {mapState} from 'vuex'

//board api
import * as studyApi from '@/api/study';
import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'GroupList',
  data() {
    return {
      groups:[],
      page:0,
    }
  },
  components:{
    StudyGroup,
    InfiniteLoading
  },
  watch:{
    writeFlag:function(){
      this.page=0
      location.reload();
    }
  },
  computed:{
    writeFlag(){
      return this.$store.state.board.writeFlag
    },
    // 재사용의 핵심
    flagComponent(){
      
      let flag = {
        state:false,
        headerMarket:false,
        headerLearnShare:false,
      }
      const curationName = this.$route.name
      if(curationName==="Market"){
        flag.state = true
        flag.headerMarket = true
        return flag

      }else if(curationName==="LearnShare"){
        flag.state = false
        flag.headerLearnShare = true
      }
      return flag
    }
  },
  created() {
  },
  methods: {
    // 재사용의 핵심
    infiniteHandler($state){
      // const curationName = this.$route.name
      // let BOARD_ID
      // if(curationName!="Board"){
      //   BOARD_ID = this.$store.state.curationId[curationName]
      // }else{
      //   BOARD_ID = Number(this.$route.params.board_id)
      // }

      const EACH_LEN = 6
      
      studyApi.getAllGroupList(this.page,EACH_LEN)
      .then((res)=>{
        setTimeout(()=>{
          if(res.data.studyList){
            this.groups = this.groups.concat(res.data.studyList);
            this.page += 1;
            $state.loaded();
            if(res.data.studyList.length / EACH_LEN <1){
              $state.complete();
            }
          }else{
            $state.complete();
          }
        },1000);
      })
      .catch(err=>{console.log(err)})
  },
  
  }
}
</script>

<style scoped>
.ani-hover {
  transition: transform 0.5s ease;
}
.ani-hover:hover {
  transform: scale(1.01);
}
</style>