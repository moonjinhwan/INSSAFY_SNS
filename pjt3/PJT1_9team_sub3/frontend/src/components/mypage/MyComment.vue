<template>
  <v-card
    id="post-box"
    class="mx-2
      mt-4
      mb-2
      px-2
      py-0
      d-flex
      flex-column"
    color="F9F9F9"
    @click="moveToPost"
  >
    <!-- 삭제 버튼 -->
    <div class="align-self-end mt-1" style="position:absolute;">
      <v-btn icon color="#AA2610" @click.stop="removeComment">
        <v-icon dark class="ml-1">
          mdi-close-thick
        </v-icon>
      </v-btn>
    </div>
    <!-- 댓글 내용 -->
    <v-card-text
      id="post-contents"
      class="font-weight-bold
        py-0 px-2 mt-5"
    >
      {{ comment.comment_description }}
    </v-card-text>
    <!-- <v-card-actions>
      <v-list-item>
        <v-row align="center" justify="end">
          <span style="float:right;">
            <v-icon small> mdi-thumb-up </v-icon>
            {{ post.likeCount }}
            <v-icon small> mdi-comment-processing </v-icon>
            {{ post.commentCount }}
          </span>
        </v-row>
      </v-list-item>
    </v-card-actions> -->

    <!-- 포스트 제목 -->
    <v-card-title
      class="
        d-flex
        flex-row
        space-between
        pa-0
        mt-5"
    >
      <v-col
        cols="9"
        id="post-title"
        class="text-overline 
          pt-0"
        >{{ comment.post_title }}</v-col
      >
      <v-col
        cols="3"
        id="post-date"
        class="text-overline 
          text-end
          pl-0
          pt-0"
        >{{ comment.comment_date | moment('YY.MM.DD.') }}
      </v-col>
    </v-card-title>
  </v-card>
</template>

<script>
export default {
  name: 'MyComment',
  props: {
    comment: Object,
  },
  data() {
    return {
      post: {},
    };
  },
  methods: {
    // 작성글 삭제
    removeComment() {
      this.$toast.open({
        position: 'top-right',
        duration: 1800,
        message: `클릭하여 ${this.board_name}에 작성한 댓글 삭제`,
        type: 'error',
        //보드 구독 취소 후 리스트에서 애니메이션으로 제거
        onClick: () => {
          this.$emit('delComment', this.comment.comment_id);
        },
        queue: true,
      });
    },
    // 해당 post로 이동(상세 주소 넘겨주기)
    moveToPost() {
      this.$router.push(`/board/${this.comment.board_id}/post/${this.comment.post_id}`);
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
  width: 100%;
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
  font-size: 14px;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
/* 게시글 제목 넘치는 부분 처리 */
#post-title {
  width: 100%;
  font-size: 14px;
  text-align: end;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
/* 날짜 넘치는 부분 처리 */
#post-date {
  width: 100%;
  font-size: 100px im !important;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
@media (max-width: 426px) {
  #post-date,
  #post-title {
    font-size: 10px !important;
  }
}
/* 댓글 내용 넘치는 부분 처리 */
#post-contents {
  font-size: 18px;
  line-height: 20px;
  max-height: 40px;
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
