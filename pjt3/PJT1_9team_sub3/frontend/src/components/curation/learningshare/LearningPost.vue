<template>
  <v-card
    class="mx-auto
      my-0
      d-flex
      flex-column"
    color="F9F9F9"
  >
    <!-- 포스트 작성자 부분 -->
    <v-card-title class="d-flex flex-row align-center ma-0 py-2 pb-0">
      <v-list-item-avatar class="rounded-circle">
        <v-img
          class="elevation-6"
          alt=""
          :src="post.profileImg"
        ></v-img>
      </v-list-item-avatar>
      <!-- 작성자이름, 작성일자 -->
      <div class="text-caption">
        <div>{{ post.writer }}</div>
        <div>{{ post.date }}</div>
      </div>
    </v-card-title>
    <!-- 포스트 제목 -->

    <v-col
      class="font-weight-black"
    >{{ post.title }}</v-col>


    <!-- 게시글 내용 -->
    <v-col>
      {{ post.content }}
    </v-col>
    <!-- 게시글 관련 이미지/댓글/좋아요 들어갈 부분 -->
    <v-card-actions>
      <!-- 이미지 -->
      <v-spacer></v-spacer>
      <div>
        <v-icon middle>mdi-camera-burst</v-icon>
        <span>{{ post.ImgCount }}</span>
        <v-icon small>mdi-comment-processing</v-icon>
        <span>{{ post.commentCount }}</span>
        <v-icon small>mdi-thumb-up</v-icon>
        <span>{{ post.likeCount }}</span>
      </div>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: 'ScrapPost',
  data() {
    return {
      post:
        {
          writer: 'pjtcoder',
          title: '(게시물제목)Vue 이제 이겨보자',
          profileImg: 'https://avatars0.githubusercontent.com/u/9064066?v=4&s=460',
          content: "For sale, cellphone, never used For sale, cellphone, never used For sale, cellphone, never used For sale, cellphone, never used For sale, cellphone, never used For sale, cellphone, never used For sale, cellphone, never used For sale, cellphone, never used",
          // 게시글에 포함된 이미지의 개수
          ImgCount: 4,
          date: `21.02.02`,
          likeCount: 4,
          commentCount: 4,
        },
    }
  },
  methods: {
    // 작성글 삭제
    removeBoard() {
      console.log('작성글 삭제')
    },
    // 해당 보드로 이동(상세 주소 넘겨주기)
    moveToBoard() {
      this.$router.push({ name: 'Board' });
    },
  },
}
</script>

<style scoped>
</style>