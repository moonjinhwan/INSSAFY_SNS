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
    <div class="user-info  container-shadow">
      <div class="u-profile">
        <Profile :getUserImage="post.user_image" />
      </div>
      <div class="d-info">
        <p class="text-ellipsis m-desc">{{ post.user_nickname }}</p>
        <p class="text-ellipsis l-desc">{{ post.post_date | moment('YY.MM.DD. hh:mm A') }}</p>
      </div>
    </div>
    <!-- 포스트 제목 -->
    <div class="post-content">
      <p class="p-title b-desc text-ellipsis">{{ post.post_title }}</p>
      <p class="p-desc m-desc">{{ post.post_description }}</p>
      <div class="p-like">
        <v-icon small> mdi-thumb-up </v-icon>
        {{ post.post_like }}
      </div>
    </div>

    <div
      class="img-wrap
      d-flex
      flex-column"
    >
      <img class="p-image" v-if="image" :src="image" />
    </div>
  </v-card>
</template>

<script>
import Profile from '@/components/etc/OtherProfile';

export default {
  name: 'MyPost',
  props: {
    post: Object,
  },
  components: {
    Profile,
  },
  data() {
    return {
      board_image: null,
      radius: '15px',
      image: false,
    };
  },
  created() {
    if (this.post.post_image != '' && this.post.post_image != 'null' && this.post.post_image != null)
      this.image = this.post.post_image;
  },

  methods: {
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
  background-color: var(--basic-color-bg2);
  box-shadow: var(--basic-shadow-c);
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
.p-image {
  margin: 10px;
  border-radius: 15px;
  background-size: cover;
  background-position: center;
  background-color: #000;
  box-shadow: var(--basic-shadow-m);
}

/* 보드 검색 핀터레스트 형태로 개편하며 작성 */
.user-info {
  display: flex;
  flex-direction: row;
  justify-content: start;
  justify-items: center;
  margin: 10px;
  padding: 6px;
  background-color: #fff;
  border-radius: 13px !important;
}
.u-profile {
  width: 42px;
  height: 42px;
}
.d-info {
  margin: 0 10px;
}
.d-info p {
  margin: 0;
}
.d-info p:nth-child(1) {
  font-size: 1rem;
}
.d-info p:nth-child(2) {
  font-size: 0.8rem;
}

/* 타이틀과 내용 */
.post-content {
  margin: 5px 10px;
}
.p-title {
  font-size: 1.2rem;
  margin: 0 0 5px;
  color: rgba(0, 0, 0, 0.747);
}
.p-desc {
  margin: 0;
  padding: 5px;
  font-size: 0.8rem;
}
.p-like {
  float: right;
  margin: 0 5px 5px 0;
}
</style>
