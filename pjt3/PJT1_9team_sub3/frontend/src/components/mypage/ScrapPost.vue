<template>
  <v-card
    id="post-box"
    class="mx-2
      my-4
      mb-2
      pa-2
      pb-4
      d-flex
      flex-column"
    color="F9F9F9"
    @click="moveToPost"
  >
    <div
      class="img-wrap
      d-flex
      flex-column"
    >
      <!-- 보드 이미지 위로 나오는 부분 -->
      <div class="text">
        <!-- 삭제 버튼 -->
        <div class="align-self-end">
          <v-btn icon color="#AA2610" @click.stop="removeBoard">
            <v-icon dark>
              mdi-close-thick
            </v-icon>
          </v-btn>
        </div>
        <div class="board-title">
          {{ scrap.board_name }}
        </div>
      </div>
      <GradientGenerator class="myinfo-list" style="height: 100px" v-if="scrap.board_image == null" :radius="radius" />
      <v-img height="100px" class="myinfo-list blur" v-if="scrap.board_image != null" :style="{ backgroundImage: `url(${scrap.board_image})` }">
      </v-img>
    </div>
    <!-- 포스트 제목 -->
    <v-card-title
      class="
        d-flex
        flex-row
        space-between
        pa-0"
    >
      <v-col cols="9" id="post-title" class="font-weight-black">{{ scrap.post_title }}</v-col>
      <v-col
        cols="3"
        id="post-date"
        class="text-overline 
        pl-0
        text-end"
        >{{ scrap.post_date | moment('YY.MM.DD.') }}</v-col
      >
    </v-card-title>

    <!-- 댓글 내용 -->
    <v-card-text
      id="post-contents"
      class="font-weight-bold
        py-0 pl-auto"
    >
      {{ scrap.post_description }}
    </v-card-text>

    <!-- <v-card-actions>
      <v-list-item class="grow">
        <v-list-item-avatar class="rounded-circle">
          <v-img class="elevation-6" alt="" :src="post.boardImg"></v-img>
        </v-list-item-avatar>
        <div>{{ `작성자 닉네임?` }}</div>

        <v-row align="center" justify="end">
          <span style="float:right;">
            <v-icon small> mdi-thumb-up </v-icon>
            {{ scrap.post_like }}
            <v-icon small> mdi-comment-processing </v-icon>
            {{ `댓글수?` }}
          </span>
        </v-row>
      </v-list-item>
    </v-card-actions> -->
  </v-card>
</template>

<script>
import GradientGenerator from '@/components/etc/GradientGenerator';
export default {
  name: 'ScrapPost',
  props: {
    scrap: Object,
  },
  components: {
    GradientGenerator,
  },
  data() {
    return {
      board_image: null,
      radius: '15px',
    };
  },
  created() {
    // if (scraps.board_image != '' && scraps.board_image != null && scraps.board_image != 'null') {
    //   this.board_image = scraps.board_image;
    // }
  },
  methods: {
    // 작성글 삭제
    removeBoard: function() {
      this.$toast.open({
        position: 'top-right',
        duration: 1800,
        message: `클릭하여 ${this.scrap.board_name} 스크랩 취소`,
        type: 'error',
        //보드 구독 취소 후 리스트에서 애니메이션으로 제거
        onClick: () => {
          this.$emit('delScrap', this.scrap.post_id);
        },
        queue: true,
      });
    },
    // 해당 보드로 이동(상세 주소 넘겨주기)
    moveToPost: function() {
      this.$router.push(`/board/${this.scrap.board_id}/post/${this.scrap.post_id}`);
    },
  },
};
</script>

<style scoped>
/* 하나의 게시글 전체 부분 */
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
  background-size: cover;
  background-position: center;
  box-shadow: var(--basic-shadow-m);
}
</style>
