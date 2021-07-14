<template>
  <v-card
    id="post-box"
    class="mx-2
      mt-4
      mb-2
      pb-0
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
          {{ post.board_name }}
        </div>
      </div>
      <GradientGenerator class="myinfo-list" style="height: 100px" v-if="post.board_image == null" :radius="radius" />
      <v-img
        height="100px"
        class="myinfo-list blur"
        v-if="post.board_image != null"
        :style="{ backgroundImage: `url(${post.board_image})` }"
      >
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
      <v-col cols="9" id="post-title" class="font-weight-black">{{ post.post_title }}</v-col>
      <v-col
        cols="3"
        id="post-date"
        class="text-overline 
          pl-0
          text-end"
        >{{ post.post_date | moment('YY.MM.DD.') }}</v-col
      >
    </v-card-title>

    <!-- 포스트 글 내용 -->
    <v-card-text
      id="post-contents"
      class="font-weight-bold
        py-0 pl-auto"
    >
      {{ post.post_description }}
    </v-card-text>
    <v-card-actions>
      <v-list-item class="grow">
        <!-- <v-list-item-content>
          <v-list-item-title>
            <v-icon>
              mdi-account-group
            </v-icon>
            {{ `?` }}
          </v-list-item-title>
        </v-list-item-content> -->

        <!-- 포스트 좋아요/댓글 수 -->
        <v-row align="center" justify="end">
          <span style="float:right;">
            <v-icon small> mdi-thumb-up </v-icon>
            {{ post.post_like }}
            <!-- <v-icon small> mdi-comment-processing </v-icon>
            {{ `?` }} -->
          </span>
        </v-row>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<script>
import GradientGenerator from '@/components/etc/GradientGenerator';
export default {
  name: 'MyPost',
  props: {
    post: Object,
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
  mounted() {
    console.log(this.post);
  },
  methods: {
    // 작성글 삭제
    removeBoard: function() {
      // 보드 구독목록에서 삭제
      this.$toast.open({
        position: 'top-right',
        duration: 1800,
        message: `클릭하여 ${this.board_name} 글 삭제`,
        type: 'error',
        //보드 구독 취소 후 리스트에서 애니메이션으로 제거
        onClick: () => {
          this.$emit('delPost', this.post.post_id);
        },
        queue: true,
      });
    },
    // 해당 post로 이동(상세 주소 넘겨주기)
    moveToPost() {
      this.$router.push(`/board/${this.post.board_id}/post/${this.post.post_id}`);
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
  background-size: cover;
  background-position: center;
  background-color: #000;
  box-shadow: var(--basic-shadow-m);
}
</style>
