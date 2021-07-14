<template>
  <div class="board">
    <!-- 대나무숲 해더 -->
    <div class="bamboo-hd">
      <div class="bamboo-hd-title">Curation</div>
      <BambooWrite class="bamboo-hd-write"/>
      <div class="bamboo-hd-empty"></div>
    </div>
    
    <BambooList/>
  </div>
</template>

<script>
// import BambooPost from '@/views/curation/bamboo/BambooPost.vue'
import BambooWrite from '@/components/curation/bamboo/BambooWrite.vue'
import BambooList from '@/components/curation/bamboo/BambooList.vue'

export default {
  name: "BambooForest",
  components: {
    // BambooPost,
    BambooWrite,
    BambooList,
  }
}
</script>

<style scoped>
.board{
  max-width: 1200px !important;
  margin: 0 auto;
  margin-top:30px;
  /* width:80%;
  margin-left:10%;
  margin-right:10%; */
}


/* 해더부분 */
.bamboo-hd {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  /* border: 1px solid; */
}

.bamboo-hd-title {
  font-size: 1.5rem;
  flex:1;
  padding-left:1rem;
}
.bamboo-hd-write{
  flex-basis: 50%;
  padding:0 15px;
}
.bamboo-hd-empty{
  flex:1;
}

/* 모바일 웹화면 */
@media (max-width: 576px) {
  .bamboo-post-list {
    flex-direction: column;
  }
  .bamboo-hd-title{
    display:none;
  }
  .bamboo-hd-write{
    flex-basis: 100%;
  }
  .bamboo-hd-empty{
    display: none;
  }
  /* 입력하는 창 고정시키고 밑으로 나오게하기 - 구현하기 */
}
</style>