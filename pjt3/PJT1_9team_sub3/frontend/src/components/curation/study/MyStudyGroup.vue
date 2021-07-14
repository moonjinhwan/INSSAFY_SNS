<template>
  <v-card
    id="post-box"
    class="
      d-flex img-wrap justify-content-between"
    color="F9F9F9"
    @click="goThisStudy"
  >
    <div class="d-flex align-items-center">
      <div class="ml-2 mr-2">
        <GradientGenerator class="myinfo-list" style="height: 50px; width:50px" v-if="group.board_image == null" :radius="radius" />
        <v-img src="@/assets/images/slide.jpg" height="50px" width="50px" class="myinfo-list blur" v-if="group.board_image != null"> </v-img>
      </div>
      
      <div class="d-flex flex-column">
        <div id="post-title" class="font-weight-black">{{ group.board_name }}</div>
        <div id="post-contents" class="font-weight-bold py-0 pl-auto">{{ group.board_description }}</div>
      </div>
    </div>
    <!-- 포스트 제목 -->

    <!-- 포스트 글 내용 -->
    <v-card-actions>
      <v-list-item class="grow">
        <!-- 포스트 좋아요/댓글 수 -->
          <span style="float:right;">
            <v-icon>mdi-account-group</v-icon>
            {{ group.board_count }}
          </span>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<script>
import GradientGenerator from '@/components/etc/GradientGenerator';

export default {
  name: "MyStudyGroup",
  data() {
    return {
      radius: '15px',
    }
  },
  props:{
    group:Object,
  },
  components: {
    GradientGenerator,
  },
  methods: {
    goThisStudy() {
      this.$router.push({ name: 'Study',params:{
          board_id:this.group.board_id,
        } });
    }
  },
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